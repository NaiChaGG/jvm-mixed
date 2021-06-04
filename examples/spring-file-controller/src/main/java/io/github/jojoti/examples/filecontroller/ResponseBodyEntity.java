package io.github.jojoti.examples.filecontroller;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class ResponseBodyEntity<Body> {

    public static final ResponseBodyEntity<Void> VOID = ok();
    /**
     * 约定 ok ok.code
     */
    private final int code;
    private final Body body;

    private ResponseBodyEntity(int code, Body body) {
        this.code = code;
        this.body = body;
    }

    public static <Body> ResponseBodyEntity<Body> ok() {
        return ok(null);
    }

    public static <Body> ResponseBodyEntity<Body> ok(Body body) {
        return new ResponseBodyEntity<>(0, body);
    }

    public static <Body> ResponseBodyEntity<Body> fail(int code) {
        return new ResponseBodyEntity<>(code, null);
    }

    public int getCode() {
        return code;
    }

    public Body getBody() {
        return body;
    }

}