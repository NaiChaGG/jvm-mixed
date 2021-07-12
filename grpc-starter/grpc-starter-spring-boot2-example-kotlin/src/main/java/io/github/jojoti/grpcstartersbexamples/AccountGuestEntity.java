//package io.github.jojoti.grpcstartersbexamples;
//
//import org.springframework.data.annotation.Id;
//import org.springframework.data.relational.core.mapping.Table;
//
///**
// * @author JoJo Wang
// * @link github.com/jojoti
// */
//@Table("account_guest")
//public class AccountGuestEntity {
//
//    @Id
//    private long uid;
//
//    private long appId;
//
//    private long channelId;
//
//    private String cliIdSha256;
//
//    private String cliIdRaw;
//
//    /**
//     * 封号被禁言的到期时间
//     */
//    private long blockExpireTime;
//
//    public long getUid() {
//        return uid;
//    }
//
//    public void setUid(long uid) {
//        this.uid = uid;
//    }
//
//    public long getAppId() {
//        return appId;
//    }
//
//    public void setAppId(long appId) {
//        this.appId = appId;
//    }
//
//    public long getChannelId() {
//        return channelId;
//    }
//
//    public void setChannelId(long channelId) {
//        this.channelId = channelId;
//    }
//
//    public String getCliIdSha256() {
//        return cliIdSha256;
//    }
//
//    public void setCliIdSha256(String cliIdSha256) {
//        this.cliIdSha256 = cliIdSha256;
//    }
//
//    public String getCliIdRaw() {
//        return cliIdRaw;
//    }
//
//    public void setCliIdRaw(String cliIdRaw) {
//        this.cliIdRaw = cliIdRaw;
//    }
//
//    public long getBlockExpireTime() {
//        return blockExpireTime;
//    }
//
//    public void setBlockExpireTime(long blockExpireTime) {
//        this.blockExpireTime = blockExpireTime;
//    }
//}