package io.github.trapspring.datajdbc;

/**
 * @author Wang Yue
 */
public
enum JSON_TYPE {
    JSON("json"), JSONB("jsonb");

    public final String type;

    JSON_TYPE(String type) {
        this.type = type;
    }

}
