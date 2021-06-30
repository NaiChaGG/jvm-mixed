package io.github.trapspring.datajdbcpostgres;

/**
 * use pgsql json
 * rf https://www.postgresql.org/docs/9.5/functions-json.html
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface JsonObject {

    enum JSON_TYPE {
        JSON("json"), JSONB("jsonb");

        public final String type;

        JSON_TYPE(String type) {
            this.type = type;
        }

    }

    default JSON_TYPE getJsonType() {
        return JSON_TYPE.JSON;
    }


}
