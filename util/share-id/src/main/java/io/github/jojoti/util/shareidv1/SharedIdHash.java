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
import com.google.common.io.BaseEncoding;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.hashids.Hashids;

/**
 * @author JoJo Wang
 */
public class SharedIdHash implements SharedId<Long, Long> {

    private final Hashids hashids;

    @Inject
    public SharedIdHash(@Assisted("hashIdSlat") String hashIdSlat) {
        this.hashids = new Hashids(hashIdSlat);
    }

    @Override
    public String encode(Long id) {
        return BaseEncoding.base64Url().encode(this.hashids.encode(id).getBytes());
    }

    @Override
    public Long decode(String encodeId) {
        String sharedId = new String(BaseEncoding.base64Url().decode(encodeId));
        long[] ids = this.hashids.decode(sharedId);
        Preconditions.checkArgument(ids != null && ids.length == 1);
        return ids[0];
    }

}
