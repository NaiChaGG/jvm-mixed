package io.github.jojoti.util.shareidv1;

public class SharedIdExpireEncodeValue {
    private final Long id;
    private final Integer ttl;

    public SharedIdExpireEncodeValue(Long id, Integer millsTtl) {
        this.id = id;
        this.ttl = millsTtl;
    }

    public Long getId() {
        return id;
    }

    public Integer getTtl() {
        return ttl;
    }
}