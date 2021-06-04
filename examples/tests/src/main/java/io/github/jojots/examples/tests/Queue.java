package io.github.jojoti.examples.tests;

import com.google.common.collect.Queues;

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class Queue {

    public static void main(String[] args) {
        var queue = Queues.<String>newArrayDeque();

        queue.add("1");
        queue.add("2");
        queue.add("3");
        queue.add("4");

        queue.remove("2");

        System.out.println(queue);
    }

}