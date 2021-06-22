package io.github.jojoti.grpcstartersbexamples;

import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Repository
public interface AccountGuestRepository extends CrudRepository<AccountGuestEntity, Long> {

    @Modifying
    @Transactional
    int deleteByUid(long uid);

//    /**
//     * 根据账号查询
//     *
//     * @param cliIdSha256
//     * @return
//     */
//    @Query
//    MemberLogin findMemberLoginByCliIdSha256AndAppId(String cliIdSha256, long appId);
//
//    final class MemberLogin {
//        private final long uid;
//        private final long blockExpireTime;
//
//        public MemberLogin(long uid, long blockExpireTime) {
//            this.uid = uid;
//            this.blockExpireTime = blockExpireTime;
//        }
//
//        public boolean isBlock() {
//            return blockExpireTime > System.currentTimeMillis();
//        }
//
//        public long getUid() {
//            return uid;
//        }
//
//        public long getBlockExpireTime() {
//            return blockExpireTime;
//        }
//    }

}