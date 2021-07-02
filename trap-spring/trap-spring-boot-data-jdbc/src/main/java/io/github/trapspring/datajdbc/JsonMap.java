package io.github.trapspring.datajdbc;

import java.util.Map;

/**
 * use pgsql json
 * rf https://www.postgresql.org/docs/9.5/functions-json.html
 *
 * @author Wang Yue
 */
public final class JsonMap {

    public final Map<?, ?> maps;

    private final JSON_TYPE type;

    JsonMap(Map<?, ?> maps, JSON_TYPE type) {
        this.maps = maps;
        this.type = type;
    }

    public static JsonMap fromJsonList(Map<?, ?>  array) {
        return new JsonMap(array, JSON_TYPE.JSON);
    }

    public static JsonMap fromJsonbList(Map<?, ?>  array) {
        return new JsonMap(array, JSON_TYPE.JSONB);
    }

    public JSON_TYPE getJsonType() {
        return type;
    }

}