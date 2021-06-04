package io.github.jojoti.util.shareidv1;

import com.google.inject.assistedinject.Assisted;

/**
 * @author JoJo Wang
 */
public interface SharedIdSignHashFactory {

    /**
     * 创建 shared id 对象，线程 安全
     *
     * @param signStr
     * @param hashIdSlat
     * @return
     */
    SharedId<Long, SharedIdSignDecodeValue> create(
            @Assisted("signStr") String signStr,
            @Assisted("hashIdSlat") String hashIdSlat);

}
