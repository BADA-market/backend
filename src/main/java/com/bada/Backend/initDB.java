package com.bada.Backend;

import com.bada.Backend.domain.User.dto.UserSignUpDto;
import com.bada.Backend.domain.User.entity.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class initDB {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbinit();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    //commit test
    //commit test2
    static class InitService{
        private final EntityManager em;

        public void dbinit() {

            User doncham = new User();
            doncham.dummyUser("돈참","1234");
            em.persist(doncham);
        }
    }
}
