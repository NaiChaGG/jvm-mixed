package io.github.jojoti.examples.tests;

import com.google.common.collect.Sets;
import lombok.val;

import java.util.Comparator;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class JavaTests {

    public static void main(String[] args) {
        val sets = Sets.newHashSet("172.168.1.2:9110", "2:91002", "4", "3");
        sets.stream().sorted(Comparator.comparingInt(String::hashCode)).forEach(System.out::println);
        System.out.println("-------");
        sets.stream().sorted().forEachOrdered(System.out::println);

//        val values = Lists.newArrayList(1, 2, 3);
//
//        values.remove(1);
    }

}