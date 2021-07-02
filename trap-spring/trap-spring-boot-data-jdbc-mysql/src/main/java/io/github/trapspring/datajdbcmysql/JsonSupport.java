package io.github.trapspring.datajdbcmysql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mysql.cj.xdevapi.JsonString;
import io.github.trapspring.datajdbc.JsonArray;
import io.github.trapspring.datajdbc.JsonMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

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
                new JsonObjectReadingConverter(objectMapper),
                new JsonListWritingConverter(objectMapper),
                new JsonListReadingConverter(objectMapper)
        ));
    }

}

@WritingConverter
class JsonObjectWritingConverter implements Converter<JsonMap, JsonString> {

    private final ObjectMapper objectMapper;

    JsonObjectWritingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonString convert(JsonMap jsonField) {
//        var jsonObject = new PGobject();
//        jsonObject.setType(jsonField.getJsonType().type);
//        try {
//            jsonObject.setValue(objectMapper.writeValueAsString(jsonField.maps));
//        } catch (SQLException | JsonProcessingException throwables) {
//            throw new RuntimeException(throwables);
//        }
//        return jsonObject;
        return null;
    }

}

@ReadingConverter
class JsonObjectReadingConverter implements Converter<JsonString, JsonMap> {
    private final ObjectMapper objectMapper;

    JsonObjectReadingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonMap convert(JsonString source) {
//        try {
//            if (JSON_TYPE.JSON.type.equals(source.getType())) {
//                return JsonMap.fromJsonList(objectMapper.readValue(source.getValue(), Map.class));
//            } else {
//                return JsonMap.fromJsonbList(objectMapper.readValue(source.getValue(), Map.class));
//            }
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
        return null;
    }

}

@WritingConverter
class JsonListWritingConverter implements Converter<JsonArray, JsonString> {

    private final ObjectMapper objectMapper;

    JsonListWritingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonString convert(JsonArray jsonField) {
//        var jsonObject = new PGobject();
//        jsonObject.setType(jsonField.getJsonType().type);
//        try {
//            jsonObject.setValue(objectMapper.writeValueAsString(jsonField.arrays));
//        } catch (SQLException | JsonProcessingException throwables) {
//            throw new RuntimeException(throwables);
//        }
//        return jsonObject;
        return new JsonString().setValue("{\"data\":1}");
    }

}

@ReadingConverter
class JsonListReadingConverter implements Converter<JsonString, JsonArray> {
    private final ObjectMapper objectMapper;

    JsonListReadingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonArray convert(JsonString source) {
//        try {
//            if (JSON_TYPE.JSON.type.equals(source.getType())) {
//                return JsonArray.fromJsonList(objectMapper.readValue(source.getValue(), List.class));
//            } else {
//                return JsonArray.fromJsonbList(objectMapper.readValue(source.getValue(), List.class));
//            }
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
        return null;
    }

}