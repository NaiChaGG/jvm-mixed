package io.github.trapspring.datajdbcpostgres;

import java.util.List;

/**
 * use pgsql json
 * rf https://www.postgresql.org/docs/9.5/functions-json.html
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public final class JsonArray {

    public final List<?> arrays;

    private final JSON_TYPE type;

    JsonArray(List<?> arrays, JSON_TYPE type) {
        this.arrays = arrays;
        this.type = type;
    }

    public static JsonArray fromJsonList(List<?> array) {
        return new JsonArray(array, JSON_TYPE.JSON);
    }

    public static JsonArray fromJsonbList(List<?> array) {
        return new JsonArray(array, JSON_TYPE.JSONB);
    }

    public JSON_TYPE getJsonType() {
        return type;
    }

}