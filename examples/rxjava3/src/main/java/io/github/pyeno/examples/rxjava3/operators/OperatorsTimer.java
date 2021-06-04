package io.github.jojoti.examples.rxjava3.operators;

import com.google.common.collect.Lists;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Action;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class OperatorsTimer {

    public static void main(String[] args) throws InterruptedException {


        Observable.interval(10, TimeUnit.MICROSECONDS)
                .subscribe(c -> {
                    System.out.println(c);
                }, t -> {
                    System.out.println("finish");
                }, () -> {
                    System.out.println("finish");
                });

        Thread.sleep(88888);

    }

}