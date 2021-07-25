package io.github.jojoti.utilguavaext;

import com.google.common.net.InetAddresses;
import org.junit.jupiter.api.Test;

/**
 * @author Wan Steve
 */
class GetAddressTest {

    @Test
    void getSocketAddress() {
        var rs = InetAddresses.isUriInetAddress("dns:///foo.googleapis.com:8080");
        System.out.println(rs);
        var rs1 = InetAddresses.isUriInetAddress("dns:///foo.googleapis.com:8080");
        System.out.println(rs1);
        var rs2 = InetAddresses.isUriInetAddress("zookeeper://zk.example.com:9900/example_service");
        System.out.println(rs2);


        var sout = GetAddress.getSocketAddress("doamin:9000");
    }

}