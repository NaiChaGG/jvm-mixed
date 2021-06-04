package io.github.jojoti.sserviceaccount.biz

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class AccountWxOAuth2ImplTest {

    @Autowired
    private lateinit var accountWxOAuth2: AccountWxOAuth2

    @Test
    fun wxRegisterOrCreate() {
        accountWxOAuth2.wxRegisterOrCreate(
            "wx28f0fe53b735ce7c",
            "op0gp0hlyixn4iHz7Xv_nswx0DR4",
            "1"
        )
    }

    @Test
    fun registerOrCreate() {
    }

}