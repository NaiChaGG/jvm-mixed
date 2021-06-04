package io.github.jojoti.util.shareidv1;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.io.BaseEncoding;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.hashids.Hashids;

import java.util.List;

/**
 * @author JoJo Wang
 */
public class SharedIdExpireSignHash implements SharedId<SharedIdExpireEncodeValue, SharedIdExpireDecodeValue> {

    private final String signStr;

    private final Hashids hashids;

    private final SignatureArg signatureArg;

    @Inject
    public SharedIdExpireSignHash(SignatureArg signatureArg,
                                  @Assisted("hashIdSlat") String hashIdSlat,
                                  @Assisted("signStr") String signStr) {
        this.signatureArg = signatureArg;

        this.signStr = signStr;
        this.hashids = new Hashids(hashIdSlat);
    }

    @Override
    public String encode(SharedIdExpireEncodeValue encodeValue) {
        long ttl = System.currentTimeMillis() + encodeValue.getTtl();

        String sign = signatureArg.argsSign(signStr, encodeValue.getId(), ttl).sha256();

        return BaseEncoding.base64Url().encode(String.format("%d|%s|%s", ttl,
                this.hashids.encode(encodeValue.getId()), sign).getBytes());
    }

    @Override
    public SharedIdExpireDecodeValue decode(String encodeId) {
        String sharedId = new String(BaseEncoding.base64Url().decode(encodeId));
        List<String> splitList = Splitter.on('|').splitToList(sharedId);
        Preconditions.checkArgument(splitList.size() == 3);

        long[] ids = this.hashids.decode(splitList.get(1));
        Preconditions.checkArgument(ids != null && ids.length == 1);

        final long expireTime = Long.parseLong(splitList.get(0));

        String sign = signatureArg.argsSign(signStr, ids[0], expireTime).sha256();

        // 获取签名
        if (!sign.equals(splitList.get(2))) {
            return new SharedIdExpireDecodeValue(null, expireTime, null, ids[0]);
        }

        if (System.currentTimeMillis() - expireTime > 0) {
            return new SharedIdExpireDecodeValue(null, expireTime, ids[0], null);
        }

        return new SharedIdExpireDecodeValue(ids[0], expireTime, null, null);
    }


}

