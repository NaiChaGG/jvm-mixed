/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/experiment-jvm.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
public class SharedIdSignHash implements SharedId<Long, SharedIdSignDecodeValue> {

    private final String signStr;

    private final Hashids hashids;

    private final SignatureArg signatureArg;

    @Inject
    public SharedIdSignHash(SignatureArg signatureArg,
                            @Assisted("signStr") String signStr,
                            @Assisted("hashIdSlat") String hashIdSlat) {
        this.signatureArg = signatureArg;

        this.signStr = signStr;
        this.hashids = new Hashids(hashIdSlat);
    }

    @Override
    public String encode(Long id) {
        String encodeId = this.hashids.encode(id);
        long timestamp = System.currentTimeMillis();
        String sign = signatureArg.argsSign(signStr, encodeId, timestamp).sha256();
        String sharedId = String.format("%s|%d|%s", encodeId, timestamp, sign);

        return BaseEncoding.base64Url().encode(sharedId.getBytes());
    }

    @Override
    public SharedIdSignDecodeValue decode(String encodeId) {
        String sharedId = new String(BaseEncoding.base64Url().decode(encodeId));
        List<String> splitList = Splitter.on('|').splitToList(sharedId);
        Preconditions.checkArgument(splitList.size() == 3);
        String shortId = String.valueOf(splitList.get(0));
        long timestamp = Long.valueOf(splitList.get(1));

        String sign = signatureArg.argsSign(signStr, shortId, timestamp).sha256();

        long[] ids = this.hashids.decode(shortId);
        Preconditions.checkArgument(ids != null && ids.length == 1);

        // 获取签名
        if (sign.equals(splitList.get(2))) {
            return new SharedIdSignDecodeValue(ids[0], timestamp, null);
        }

        return new SharedIdSignDecodeValue(null, timestamp, ids[0]);
    }

}
