package io.github.jojoti.utilhashidtoken;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.BaseEncoding;
import org.hashids.Hashids;

import java.util.UUID;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface HashIdToken {

    static DecodeToken parseToken(String tokenBase64) {
        var tokenDecode = new String(BaseEncoding.base64Url().decode(tokenBase64), Charsets.UTF_8);
        var list = DecodeToken.SPLITTER.splitToList(tokenDecode);
        if (list.size() != DecodeToken.TOKEN_SIZE) {
            throw new IllegalArgumentException("Token error, aa|bb..");
        }
        final var slat = list.get(0);
        final var hashToken = list.get(1);
        var ids = new Hashids("session" + slat).decode(hashToken);
        if (ids == null || ids.length < 1) {
            throw new IllegalArgumentException("Token format error.");
        }
        return new DecodeToken(slat, ids);
    }

    static NewToken createToken(long uid, long scopeId) {
        final var slat = UUID.randomUUID().toString().replace("-", "");
        var hashToken = new Hashids("session" + slat).encode(uid, scopeId);
        var token = slat + "|" + hashToken;
        return new NewToken(slat, BaseEncoding.base64Url().encode(token.getBytes()));
    }

    final class DecodeToken {
        static final Splitter SPLITTER = Splitter.on('|').limit(2);
        /**
         * 至少要包含一个id
         */
        private static final int TOKEN_SIZE = 2;
        public final String salt;
        private final long[] ids;

        DecodeToken(String salt, long[] ids) {
            this.salt = salt;
            this.ids = ids;
        }

        public long getUid() {
            return ids[0];
        }

        public int getScopeId() {
            return (int) ids[1];
        }

    }

    final class NewToken {
        private final String slat;
        private final String tokenBase64;

        NewToken(String slat, String tokenBase64) {
            this.slat = slat;
            this.tokenBase64 = tokenBase64;
        }

        public String getSlat() {
            return slat;
        }

        public String getTokenBase64() {
            return tokenBase64;
        }
    }

}
