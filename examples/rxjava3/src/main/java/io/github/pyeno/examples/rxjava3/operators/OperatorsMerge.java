package io.github.jojoti.examples.rxjava3.operators;

import com.google.common.collect.Lists;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Supplier;
import lombok.val;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class OperatorsMerge {

    public static void main(String[] args) {
//        val flow = Flowable.create(emitter -> {
//            emitter.onNext("111");
//        }, BackpressureStrategy.BUFFER);
//
//        flow.concatWith(Flowable.just("1", "2"));
//
//        flow.subscribe(System.out::println);

//        Flowable.just("3", "4", "5", "6").scan(new BiFunction<String, String, String>() {
//            @Override
//            public String apply(String s, String s2) throws Throwable {
//                return s + ""+ s2;
//            }
//        }).subscribe(System.out::println);


        Flowable.just("3", "4", "5", "6").mergeWith(Flowable.just("7", "8", "9")).collect(Lists::newArrayList, List::add).subscribe(System.out::println);


//        val empty = Flowable.<String>empty();

//        val list = Flowable.<List<String>>empty().scan()

//        empty.mergeWith(Flowable.fromPublisher(Flowable.just("3", "4")))
//                .mergeWith(Flowable.fromPublisher(Flowable.just("5", "6")))
//                .subscribe();

//        Flowable.just("1", "2").concatWith(Flowable.just("3", "4")).subscribe(System.out::println);

    }

}