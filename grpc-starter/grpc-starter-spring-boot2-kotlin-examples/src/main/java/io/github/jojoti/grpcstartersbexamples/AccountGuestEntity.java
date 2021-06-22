package io.github.jojoti.grpcstartersbexamples;

import io.github.trapspring.datajdbc.TrapBaseTimeEntity;

import javax.persistence.*;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Entity(name = "account_guest")
@Table(indexes = {
        @Index(columnList = "cliIdSha256,appId", unique = true),
})
public class AccountGuestEntity extends TrapBaseTimeEntity {

    @Id
    private long uid;

    @Column
    private long appId;

    @Column
    private long channelId;

    @Column
    private String cliIdSha256;

    @Column
    private String cliIdRaw;

    /**
     * 封号被禁言的到期时间
     */
    @Column
    private long blockExpireTime;

    @Override
    public Long getId() {
        return this.uid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getCliIdSha256() {
        return cliIdSha256;
    }

    public void setCliIdSha256(String cliIdSha256) {
        this.cliIdSha256 = cliIdSha256;
    }

    public String getCliIdRaw() {
        return cliIdRaw;
    }

    public void setCliIdRaw(String cliIdRaw) {
        this.cliIdRaw = cliIdRaw;
    }

    public long getBlockExpireTime() {
        return blockExpireTime;
    }

    public void setBlockExpireTime(long blockExpireTime) {
        this.blockExpireTime = blockExpireTime;
    }
}