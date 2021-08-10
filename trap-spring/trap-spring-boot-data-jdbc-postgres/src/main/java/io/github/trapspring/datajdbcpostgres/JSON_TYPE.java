package io.github.trapspring.datajdbcpostgres;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public enum JSON_TYPE {
    JSON("json"), JSONB("jsonb");

    public final String type;

    JSON_TYPE(String type) {
        this.type = type;
    }

}
