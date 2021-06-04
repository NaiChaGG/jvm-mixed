import com.mongodb.reactivestreams.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import java.util.concurrent.CountDownLatch;

public class MongoTest {

    private static final Logger log = LoggerFactory.getLogger(MongoTest.class);

    public void main(String[] args) throws Exception {

        CountDownLatch latch = new CountDownLatch(1);

        ReactiveMongoTemplate mongoOps = new ReactiveMongoTemplate(MongoClients.create(), "database");
//
//        mongoOps.insert(new Person("Joe", 34))
//                .doOnNext(person -> log.info("Insert: " + person))
//                .flatMap(person -> mongoOps.findById(person.getId(), Person.class))
//                .doOnNext(person -> log.info("Found: " + person))
////                .zipWith(person -> mongoOps.updateFirst(query(where("name").is("Joe")), update("age", 35), Person.class))
//                .flatMap(tuple -> mongoOps.remove(tuple.getT1()))
////                .flatMap(deleteResult -> mongoOps.findAll(Person.class))
//                .count().doOnSuccess(count -> {
//            log.info("Number of people: " + count);
//            latch.countDown();
//        }).subscribe();
//
//        latch.await();
    }

}

class Person {

    private String id;
    private String name;
    private int age;
    private String T1;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getT1() {
        return T1;
    }

    public void setT1(String t1) {
        T1 = t1;
    }
}