import reactor.core.publisher.Mono;

public class MonoTest {

    public void main(String[] args) {
        Mono<String> x = Mono.just(1)
                .flatMap(f -> {
                    throw new RuntimeException("and exception.");
                })
                .flatMap(f -> Mono.error(new RuntimeException("Runtime exception xxxxxxxxxxxxx.")));

        Mono<String> e = x.doOnError(RuntimeException.class, t -> {
            System.out.println("error handler:" + t.getMessage());
        }).doOnSuccessOrError((t, u) -> {
            System.out.println("catch handler:" + u.getMessage());

        }).onErrorReturn("出错了");

//        e.subscribe(System.out::println);
        e.subscribe(System.out::println, Throwable::printStackTrace)
        ;

    }


}
