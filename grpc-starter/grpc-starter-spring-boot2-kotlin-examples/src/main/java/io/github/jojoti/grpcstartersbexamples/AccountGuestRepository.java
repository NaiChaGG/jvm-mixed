package io.github.jojoti.grpcstartersbexamples;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface AccountGuestRepository extends CrudRepository<AccountGuestEntity, Long> {

    /**
     * 根据账号查询
     *
     * @return
     */
    @Query
    List<MemberLogin> findMemberLoginByAppId(long appId);

    class MemberLogin {
        private long uid;
        private long blockExpireTime;

        public void setUid(long uid) {
            this.uid = uid;
        }

        public void setBlockExpireTime(long blockExpireTime) {
            this.blockExpireTime = blockExpireTime;
        }

        public boolean isBlock() {
            return blockExpireTime > System.currentTimeMillis();
        }

        public long getUid() {
            return uid;
        }

        public long getBlockExpireTime() {
            return blockExpireTime;
        }
    }

}