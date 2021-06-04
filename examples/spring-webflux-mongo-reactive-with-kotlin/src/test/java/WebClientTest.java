import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Level;

public class WebClientTest {


    public void main(String[] args) {
//        Flux<Integer> ints = Flux.range(1, 4);
//        ints.subscribe(System.out::println,
//                error -> System.err.println("Error " + error),
//                () -> {System.out.println("Done");});
        WebClient client = WebClient.create("http://192.168.9.147:8094");

        Mono<Map> reslut = client.get().uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(35))
                .log("category-", Level.ALL, SignalType.ON_ERROR, SignalType.ON_COMPLETE, SignalType.CANCEL, SignalType.REQUEST);

        reslut.subscribe(s -> System.out.println(s), Throwable::printStackTrace)
                .dispose();
//
//
////        MemberBiz memberInterface = null;
//
////        memberInterface.signInByTelephoneAccountNoPass("", "")
////                .filter(ServiceMessage::isSuccess).map(memberOrNewDtoServiceMessage -> MyResponseEntity.okEmptyMapBody());
////                .filter(ServiceMessage::isSuccess).map(memberOrNewDtoServiceMessage -> MyResponseEntity.okEmptyMapBody());
//
//
    }

}
