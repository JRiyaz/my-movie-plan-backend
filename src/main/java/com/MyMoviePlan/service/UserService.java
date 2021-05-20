package com.MyMoviePlan.service;

import com.MyMoviePlan.entity.UserEntity;
import com.MyMoviePlan.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;

    public Optional<UserEntity> findByEmail(final String email) {
        return repository.findByEmail(email);
    }

    public Optional<UserEntity> findByMobile(final String mobile) {
        return repository.findByMobile(mobile);
    }

    public Optional<UserEntity> findById(final String id) {
        return repository.findById(id);
    }

    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    public UserEntity save(final UserEntity user) {
        return repository.save(user);
    }

    public void deleteById(final String id) {
        repository.deleteById(id);
    }

    public Optional<UserEntity> getUser(final String idOrEmailOrMobile) {
        final String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Optional<UserEntity> user = null;
        if (Pattern.matches(regex, idOrEmailOrMobile))
            user = findByEmail(idOrEmailOrMobile);
        else if (idOrEmailOrMobile.contains("-") && idOrEmailOrMobile.length() > 10)
            user = findById(idOrEmailOrMobile);
        else
            user = findByMobile(idOrEmailOrMobile);
        return user;
    }
}
