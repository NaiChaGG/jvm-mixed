package io.github.jojoti.examples.tests;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Map;

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class GuavaTable {
    public static void main(String[] args) {
        final Table<String, String, String> groupWorkers = HashBasedTable.create();
        groupWorkers.put("world", "1", "1");
        groupWorkers.put("world", "1", "2");
        groupWorkers.put("world", "1", "3");

        for (Map.Entry<String, Map<String, String>> stringMapEntry : groupWorkers.columnMap().entrySet()) {
            for (Map.Entry<String, String> stringStringEntry : stringMapEntry.getValue().entrySet()) {
                System.out.println(stringMapEntry.getKey() + stringMapEntry.getValue());
            }
            break;
        }

    }
}
