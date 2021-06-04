package io.github.jojoti.examples.rxjava3.operators;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.functions.Function;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class OperatorsMaybe {

    public static void main(String[] args) throws InterruptedException {
        get().<String>flatMap(c -> Single.just("xxx" + c)).subscribe(System.out::println);
        Thread.sleep(2000);
    }

    static Single<String> get() {
        return Maybe.empty()
                .flatMapSingle(integer -> {
                    System.out.println("=======");
                    return Single.just(integer + "integer");
                })
//                .map(c-> "c")
                .switchIfEmpty(Single.fromCallable(OperatorsMaybe::emptyJust));
    }

    static String emptyJust() {
        System.out.println("ssss");
        return "\"xxx----";
    }

}