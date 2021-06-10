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

/**
 * @author JoJo Wang
 * <p>
 * example:
 * 1 -> ax|111111|sign
 */
public interface SharedId<EncodeValue, DecodeValue> {

    /**
     * 1 -> azxf|time|sign
     * <p>
     * 把 数字类型 转化为字符串
     *
     * @param id
     * @return
     */
    String encode(EncodeValue id);

    /**
     * 将字符串转化为已知 数据类型
     *
     * @param encodeId
     * @return
     */
    DecodeValue decode(String encodeId);

}