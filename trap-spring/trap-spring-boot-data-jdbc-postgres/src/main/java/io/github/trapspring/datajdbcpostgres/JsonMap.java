package io.github.trapspring.datajdbcpostgres;

import java.util.List;
import java.util.Map;

/**
 * use pgsql json
 * rf https://www.postgresql.org/docs/9.5/functions-json.html
 *
 * @author Wang Yue
 */
public final class JsonMap {

    final Map<?, ?> maps;

    private final JsonObject.JSON_TYPE type;

    JsonMap(Map<?, ?> maps, JsonObject.JSON_TYPE type) {
        this.maps = maps;
        this.type = type;
    }

    public static JsonMap fromJsonList(Map<?, ?>  array) {
        return new JsonMap(array, JsonObject.JSON_TYPE.JSON);
    }

    public static JsonMap fromJsonbList(Map<?, ?>  array) {
        return new JsonMap(array, JsonObject.JSON_TYPE.JSONB);
    }

    public JsonObject.JSON_TYPE getJsonType() {
        return type;
    }

}