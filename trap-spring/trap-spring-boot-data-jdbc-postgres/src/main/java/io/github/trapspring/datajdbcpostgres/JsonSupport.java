package io.github.trapspring.datajdbcpostgres;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.postgresql.util.PGobject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.*;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * https://stackoverflow.com/questions/64521056/how-to-read-write-postgres-jsonb-type-with-spring-data-jdbc
 * https://stackoverflow.com/questions/25738569/how-to-map-a-map-json-column-to-java-object-with-jpa
 * https://github.com/nkonev/jdbc-repository-jsonb/blob/repro/src/main/kotlin/name/nkonev/jdbc/repository/JsonJdbcConfig.kt
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Configuration(proxyBeanMethods = false)
public class JsonSupport extends AbstractJdbcConfiguration {

    //    private ObjectMapper objectMapper;
    private final ObjectMapper objectMapper;

    JsonSupport(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // 支持 别名
    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

//    @Bean
//    @Override
//    public JdbcConverter jdbcConverter(JdbcMappingContext mappingContext, NamedParameterJdbcOperations operations,
//                                       @Lazy RelationResolver relationResolver, JdbcCustomConversions conversions, Dialect dialect) {
//
//        DefaultJdbcTypeFactory jdbcTypeFactory = new DefaultJdbcTypeFactory(operations.getJdbcOperations());
//        return new BasicJdbcConverter(mappingContext, relationResolver, conversions, jdbcTypeFactory,
//                dialect.getIdentifierProcessing());
//    }

    @Bean
    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(Lists.newArrayList(
                new JsonObjectWritingConverter(objectMapper),
//                new JsonObjectReadingConverter(objectMapper),
                new JsonListWritingConverter(objectMapper),
                new JsonListReadingConverter(objectMapper)
        ));
    }

}

@WritingConverter
class JsonObjectWritingConverter implements Converter<JsonMap, PGobject> {

    private final ObjectMapper objectMapper;

    JsonObjectWritingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public PGobject convert(JsonMap jsonField) {
        var jsonObject = new PGobject();
        jsonObject.setType(jsonField.getJsonType().type);
        try {
            jsonObject.setValue(objectMapper.writeValueAsString(jsonField.maps));
        } catch (SQLException | JsonProcessingException throwables) {
            throw new RuntimeException(throwables);
        }
        return jsonObject;
    }

}

@ReadingConverter
class JsonObjectReadingConverter implements Converter<PGobject, JsonMap> {
    private final ObjectMapper objectMapper;

    JsonObjectReadingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonMap convert(PGobject source) {
        try {
            if (JsonObject.JSON_TYPE.JSON.type.equals(source.getType())) {
                return JsonMap.fromJsonList(objectMapper.readValue(source.getValue(), Map.class));
            } else {
                return JsonMap.fromJsonbList(objectMapper.readValue(source.getValue(), Map.class));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

@WritingConverter
class JsonListWritingConverter implements Converter<JsonArray, PGobject> {

    private final ObjectMapper objectMapper;

    JsonListWritingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public PGobject convert(JsonArray jsonField) {
        var jsonObject = new PGobject();
        jsonObject.setType(jsonField.getJsonType().type);
        try {
            jsonObject.setValue(objectMapper.writeValueAsString(jsonField.getArray()));
        } catch (SQLException | JsonProcessingException throwables) {
            throw new RuntimeException(throwables);
        }
        return jsonObject;
    }

}

@ReadingConverter
class JsonListReadingConverter implements Converter<PGobject, JsonArray> {
    private final ObjectMapper objectMapper;

    JsonListReadingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonArray convert(PGobject source) {
        try {
            if (JsonObject.JSON_TYPE.JSON.type.equals(source.getType())) {
                return JsonArray.fromJsonList(objectMapper.readValue(source.getValue(), List.class));
            } else {
                return JsonArray.fromJsonbList(objectMapper.readValue(source.getValue(), List.class));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}