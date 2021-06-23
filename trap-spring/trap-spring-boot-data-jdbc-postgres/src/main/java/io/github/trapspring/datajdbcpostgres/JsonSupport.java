package io.github.trapspring.datajdbcpostgres;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.postgresql.util.PGobject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.sql.SQLException;

/**
 * https://stackoverflow.com/questions/64521056/how-to-read-write-postgres-jsonb-type-with-spring-data-jdbc
 * https://stackoverflow.com/questions/25738569/how-to-map-a-map-json-column-to-java-object-with-jpa
 * https://github.com/nkonev/jdbc-repository-jsonb/blob/repro/src/main/kotlin/name/nkonev/jdbc/repository/JsonJdbcConfig.kt
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Configuration
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

    @Bean
    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(Lists.newArrayList(
                new JsonFieldWritingConverter(objectMapper),
                new JsonFieldReadingConverter(objectMapper),

                new JsonbFieldWritingConverter(objectMapper),
                new JsonbFieldReadingConverter(objectMapper)
        ));
    }
}

@WritingConverter
class JsonFieldWritingConverter implements Converter<JsonField, PGobject> {

    private final ObjectMapper objectMapper;

    JsonFieldWritingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public PGobject convert(JsonField jsonField) {
        var jsonObject = new PGobject();
        jsonObject.setType("json");
        try {
            jsonObject.setValue(objectMapper.writeValueAsString(jsonField));
        } catch (SQLException | JsonProcessingException throwables) {
            throw new RuntimeException(throwables);
        }
        return jsonObject;
    }

}

@ReadingConverter
class JsonFieldReadingConverter implements Converter<PGobject, JsonField> {
    private final ObjectMapper objectMapper;

    JsonFieldReadingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonField convert(PGobject source) {
        try {
            return objectMapper.readValue(source.getValue(), JsonField.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

@WritingConverter
class JsonbFieldWritingConverter implements Converter<JsonbField, PGobject> {

    private final ObjectMapper objectMapper;

    JsonbFieldWritingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public PGobject convert(JsonbField jsonField) {
        var jsonObject = new PGobject();
        jsonObject.setType("jsonb");
        try {
            jsonObject.setValue(objectMapper.writeValueAsString(jsonField));
        } catch (SQLException | JsonProcessingException throwables) {
            throw new RuntimeException(throwables);
        }
        return jsonObject;
    }

}

@ReadingConverter
class JsonbFieldReadingConverter implements Converter<PGobject, JsonbField> {
    private final ObjectMapper objectMapper;

    JsonbFieldReadingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonbField convert(PGobject source) {
        try {
            return objectMapper.readValue(source.getValue(), JsonbField.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
