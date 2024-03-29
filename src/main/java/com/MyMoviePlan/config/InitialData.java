package com.MyMoviePlan.config;

import com.MyMoviePlan.model.UserRole;
import com.MyMoviePlan.entity.UserEntity;
import com.MyMoviePlan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialData implements CommandLineRunner {

    @Autowired
    private UserService service;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        try {
                final UserEntity super_admin = new UserEntity("Riyaz J", "j.riyazu@gmail.com",
                        "8099531318", "Male", passwordEncoder.encode("super"), true,
                        true, true, true, true,
                        UserRole.ROLE_SUPER_ADMIN);

                final UserEntity admin = new UserEntity("Fayaz J", "j.fayaz@gmail.com",
                        "9019168638", "Male", passwordEncoder.encode("admin"), true,
                        true, true, true, true,
                        UserRole.ROLE_ADMIN);

                final UserEntity user = new UserEntity("Inthiyaz J", "j.inthiyaz@gmail.com",
                        "8985462507", "Male", passwordEncoder.encode("user"), true,
                        true, true, true, true,
                        UserRole.ROLE_USER);

                service.save(super_admin);
                service.save(admin);
                service.save(user);
        } catch(Exception e) {
                System.out.println("Data already exists");
        }

    }
}
