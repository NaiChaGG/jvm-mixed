package io.github.jojoti.grpcstartersbcli;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface ServiceName<T extends Enum<T>> {

    /**
     * 获取 service name
     *
     * @return
     */
    String getServiceName();

    T getEnumValue();

}