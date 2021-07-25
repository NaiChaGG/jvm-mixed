package io.github.jojoti.utilguavaext;

import org.junit.jupiter.api.Test;

/**
 * @author Wan Steve
 */
class GetAddressTest {

    @Test
    void getSocketAddress() {
        var sout = GetAddress.getSocketAddress("doamin:9000");
    }

}