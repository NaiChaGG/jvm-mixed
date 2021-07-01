package io.github.jojoti.grpcstartersbexamples;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Wang Yue
 */
@Component
public class InitData implements CommandLineRunner {

    private final AccountGuestRepository accountGuestRepository;

    public InitData(AccountGuestRepository accountGuestRepository) {
        this.accountGuestRepository = accountGuestRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        var rs = this.accountGuestRepository.findMemberLoginByAppId(1);

//        System.out.println(rs);
    }

}
