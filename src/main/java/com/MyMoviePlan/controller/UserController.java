package com.MyMoviePlan.controller;

import com.MyMoviePlan.entity.UserEntity;
import com.MyMoviePlan.model.Credentials;
import com.MyMoviePlan.model.HttpResponse;
import com.MyMoviePlan.model.Token;
import com.MyMoviePlan.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService service;
    private final HttpServletRequest request;

    @GetMapping("/")
    public String index() {
        return "Welcome " + service.getUserName();
    }

    @PostMapping("authenticate")
    public Token authenticate(@RequestBody final Credentials credentials) {

        return service.authenticate(credentials);
    }

    @GetMapping("check/{username}")
    public Token checkUniqueness(@PathVariable final String username) {
        return service.checkUniqueness(username);
    }

    @GetMapping("get-user")
    @PreAuthorize("hasAuthority('READ')")
    public UserEntity user() {
        return service.getLoggedInUser()
                .setPassword(null);
    }

    @GetMapping("all")
    @PreAuthorize("hasAuthority('WRITE')")
    public List<UserEntity> allUsers() {
        return service.findAll();
    }

    @PutMapping("update/{username}")
    @PreAuthorize("hasAuthority('READ')")
    public UserEntity updateUser(@RequestBody final UserEntity userEntity,
                                 @PathVariable final String username) {

        return service.update(userEntity, username);
    }

    @PostMapping("sign-up")
    public HttpResponse signUp(@RequestBody final UserEntity userEntity) {

        return service.register(userEntity);
    }

    @PutMapping("change-password")
    @PreAuthorize("hasAuthority('READ')")
    public HttpResponse changePassword(@RequestBody final Credentials credentials) {

        return service.changePassword(credentials);
    }

    @PutMapping("forgot-password")
    public HttpResponse forgotPassword(@RequestBody final Credentials credentials) {
        return service.forgotPassword(credentials);
    }

    @DeleteMapping("delete/{username}")
    @PreAuthorize("hasAuthority('DELETE')")
    public HttpResponse delete(@PathVariable final String username) {
        return service.deleteById(username);
    }
}
