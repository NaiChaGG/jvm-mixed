package io.github.jojoti.util.shareidv1;

/**
 * @author JoJo Wang
 * <p>
 * example:
 * [1,2] -> ax|111111|sign
 */
public interface SharedIds<EncodeValue, DecodeValue> {

    /**
     * 1 -> azxf|time|sign
     * <p>
     * 把 数字类型 转化为字符串
     *
     * @param ids
     * @return
     */
    String encode(EncodeValue... ids);

    /**
     * 将字符串转化为已知 数据类型
     *
     * @param encodeId
     * @return
     */
    DecodeValue[] decode(String encodeId);

}
