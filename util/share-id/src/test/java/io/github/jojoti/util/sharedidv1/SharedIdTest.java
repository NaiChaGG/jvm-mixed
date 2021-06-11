///*
// * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/experiment-jvm.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package io.github.jojoti.util.sharedidv1;
//
//import io.github.jojoti.util.shareidv1.*;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class SharedIdTest {
//
//    private final SharedIdSignHashFactory sharedIdSignHashFactory = GuiceApp.sharedIdSignHash();
//    private final SharedIdHashFacotry sharedIdHashFacotry = GuiceApp.sharedIdHash();
//    private final SharedIdExpireSignFactory sharedIdExpireSignFactory = GuiceApp.sharedIdExpireHash();
//
//    @Test
//    public void testSharedIdSignHash() {
//        SharedId<Long, SharedIdSignDecodeValue> sharedId = sharedIdSignHashFactory.create("dd", "gggggggggg");
//
//        String encodeStr = sharedId.encode(1L);
//
//        SharedIdSignDecodeValue decodeId = sharedId.decode(encodeStr);
//
//        assertEquals(1L, decodeId.getId().longValue());
//    }
//
//    @Test
//    public void testShareIdsSignHash() {
//        SharedId<Long, SharedIdSignDecodeValue> sharedId = sharedIdSignHashFactory.create("qqqqq", "xxxx");
//
//        String encodeStr = sharedId.encode(1L);
//
//        SharedIdSignDecodeValue decodeId = sharedId.decode(encodeStr);
//
//        assertEquals(1, decodeId.getId().longValue());
//    }
//
//    @Test
//    public void testSharedIdHash() {
//        SharedId<Long, Long> sharedId = sharedIdHashFacotry.create("dd");
//
//        String encodeStr = sharedId.encode(1L);
//
//        long decodeId = sharedId.decode(encodeStr);
//
//        assertEquals(1, decodeId);
//    }
//
//    @Test
//    public void testSharedIdExpireHash() {
//        SharedId<SharedIdExpireEncodeValue, SharedIdExpireDecodeValue> sharedId = sharedIdExpireSignFactory.create("dd", "iii");
//
//        String encodeStr = sharedId.encode(new SharedIdExpireEncodeValue(1L, 1000));
//
//        SharedIdExpireDecodeValue decodeId = sharedId.decode(encodeStr);
//
//        assertEquals(1L, decodeId.getId().longValue());
//    }
//
//    @Test
//    public void testSharedIdExpireHashExpire() throws Exception {
//
//        SharedId<SharedIdExpireEncodeValue, SharedIdExpireDecodeValue> sharedId = sharedIdExpireSignFactory.create("dd", "dd");
//
//        String encodeStr = sharedId.encode(new SharedIdExpireEncodeValue(1L, 1000));
//
//        Thread.sleep(1001);
//
//        SharedIdExpireDecodeValue decodeId = sharedId.decode(encodeStr);
//
//        assertEquals(1L, decodeId.getExpireId().longValue());
//        assertTrue(decodeId.isExpired());
//    }
//
//}