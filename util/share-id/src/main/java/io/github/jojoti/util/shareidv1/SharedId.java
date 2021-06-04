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