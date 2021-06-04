package io.github.jojoti.versioncode;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author JoJo Wang
 * @date 2017/11/23
 */
public class VersionCode {

    private final int versionLength;
    private final int versionCodeLength;

    public VersionCode(int versionLength, int versionCodeLength) {
        this.versionLength = versionLength;
        this.versionCodeLength = versionCodeLength;
    }

    public static VersionCode getDefault() {
        return Holder.DEFAULT;
    }

    /**
     * 1.0.0    -> 1 00 00
     * 2.3.4    -> 2 03 04
     * 2.3.40   -> 2 03 40
     * 2.4.40   -> 2 04 40
     * 20.3.40  -> 20 03 40
     *
     * @param version
     * @return
     */
    public int encode(String version) {
        List<String> items = Splitter.on('.').trimResults().splitToList(version);
        if (items.size() != this.versionLength) {
            throw new RuntimeException("Version pattern error.");
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).length() > this.versionCodeLength) {
                throw new IllegalArgumentException(String.format("Version code do not allow more than %d digits.", this.versionCodeLength));
            }
            if (i > 0 && items.get(i).length() == 1) {
                buffer.append('0');
            }
            buffer.append(items.get(i));
        }
        return Integer.parseInt(buffer.toString());
    }

    /**
     * 比较 两个 版本号的大小
     *
     * @param left
     * @param right
     * @return
     */
    public boolean compare(String left, String right) {
        return encode(left) > encode(right);
    }

    /**
     * 1 00 00  -> 1.0.0
     * 2 03 04  -> 2.3.4
     * 2 03 40  -> 2.3.40
     * 2 04 40  -> 2.4.40
     * 20 03 40 -> 20.3.40
     *
     * @param code
     * @return
     */
    public String decode(int code) {
        List<String> items = Splitter.fixedLength(1).trimResults().splitToList(String.valueOf(code));

        int length = this.versionLength;

        // 0.0
        List<String> versionList = Lists.newArrayList();

        int offsetOne = items.size() - 1;

        StringBuilder temp = new StringBuilder();

        while (length-- > 1) {
            for (int i = 0; i < this.versionCodeLength; i++) {
                temp.append(items.get(offsetOne -= 1));
            }
            versionList.add(Integer.valueOf(temp.reverse().toString()).toString());
            // 清空 builder
            temp.setLength(0);
        }

        // 追加前面几位
        for (int i = 0; i < offsetOne; i++) {
            temp.append(Integer.valueOf(items.get(i)).toString());
        }

        temp.append('.');

        for (int i = versionList.size() - 1; i >= 0; i--) {
            temp.append(versionList.get(i));
            temp.append('.');
        }

        return temp.substring(0, temp.length() - 1);
    }

    private static final class Holder {
        /**
         * 999.99.99
         */
        static final VersionCode DEFAULT = new VersionCode(3, 2);
    }

}