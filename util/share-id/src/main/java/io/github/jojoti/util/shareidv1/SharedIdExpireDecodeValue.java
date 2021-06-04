package io.github.jojoti.util.shareidv1;

public class SharedIdExpireDecodeValue {

    private final Long id;
    private final Long expireTime;
    private final Long expireId;
    private final Long signErrorId;

    public SharedIdExpireDecodeValue(Long id, Long expireTime, Long expireId, Long signErrorId) {
        this.id = id;
        this.expireTime = expireTime;
        this.expireId = expireId;
        this.signErrorId = signErrorId;
    }

    public Long getId() {
        return id;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public Long getExpireId() {
        return expireId;
    }

    public boolean isOk() {
        return this.id != null;
    }

    public boolean isExpired() {
        return this.expireId != null;
    }

    public boolean isSignError() {
        return this.signErrorId != null;
    }

    public Long getSignErrorId() {
        return signErrorId;
    }
}