package io.github.jojoti.versioncode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
class VersionCodeTest {

    @org.junit.jupiter.api.Test
    void encode() {
        assertEquals(VersionCode.getDefault().encode("1.1.1"), 10101);
    }

    @org.junit.jupiter.api.Test
    void compare() {
        assertTrue(VersionCode.getDefault().compare("2.1.1", "1.9.99"));
    }

    @org.junit.jupiter.api.Test
    void decode() {
//        assertEquals("1.1.1", VersionCode.getDefault().decode(10101));
    }
}