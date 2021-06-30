package io.github.trapspring.datajdbcpostgres;

import java.util.List;

/**
 * use pgsql json
 * rf https://www.postgresql.org/docs/9.5/functions-json.html
 *
 * @author Wang Yue
 */
public final class JsonArray {

    final List<?> arrays;

    private final JsonObject.JSON_TYPE type;

    JsonArray(List<?> arrays, JsonObject.JSON_TYPE type) {
        this.arrays = arrays;
        this.type = type;
    }

    public static JsonArray fromJsonList(List<?> array) {
        return new JsonArray(array, JsonObject.JSON_TYPE.JSON);
    }

    public static JsonArray fromJsonbList(List<?> array) {
        return new JsonArray(array, JsonObject.JSON_TYPE.JSONB);
    }

    public List<?> getArray() {
        return arrays;
    }

    public JsonObject.JSON_TYPE getJsonType() {
        return type;
    }

}