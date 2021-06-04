package io.github.jojoti.util.shareidv1;

public class SharedIdSignDecodeValue {

    private final Long id;
    private final Long createTime;
    private final Long signErrorId;

    public SharedIdSignDecodeValue(Long id, Long createTime, Long signErrorId) {
        this.id = id;
        this.createTime = createTime;
        this.signErrorId = signErrorId;
    }

    public Long getId() {
        return id;
    }

    public Long getSignErrorId() {
        return signErrorId;
    }

    public boolean isSignError() {
        return signErrorId != null;
    }

    public boolean isOk() {
        return this.id != null;
    }

    public Long getCreateTime() {
        return createTime;
    }
}
