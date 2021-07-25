package io.github.jojoti.utilguavaext;

import com.google.common.net.InetAddresses;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.URL;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface GetAddress {

    static SocketAddress getSocketAddress(String address) {
        // fixme 蛋疼
        URL url;
        try {
            url = new URL("http://" + address);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        // ip地址
        if (InetAddresses.isInetAddress(url.getHost())) {
            // 使用 guava api 可以监听 127.0.0.1 0.0.0.0 等端口
            return new InetSocketAddress(InetAddresses.forString(url.getHost()), url.getPort());
        }
        // 正常的 domain 地址
        throw new IllegalArgumentException("ip " + url.getHost() + " address valid error");
    }

}
