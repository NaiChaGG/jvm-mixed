package io.github.jojoti.util.shareidv1;

import com.google.inject.assistedinject.Assisted;

/**
 * @author JoJo Wang
 */
public interface SharedIdExpireSignFactory {

    SharedId<SharedIdExpireEncodeValue, SharedIdExpireDecodeValue> create(
            @Assisted("hashIdSlat") String hashIdSlat, @Assisted("signStr") String signStr);

}
