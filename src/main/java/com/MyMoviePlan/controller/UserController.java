package com.MyMoviePlan.controller;

import com.MyMoviePlan.entity.UserEntity;
import com.MyMoviePlan.exception.UnAuthorizedException;
import com.MyMoviePlan.exception.UserNotFoundException;
import com.MyMoviePlan.model.Credentials;
import com.MyMoviePlan.model.Token;
import com.MyMoviePlan.security.ApplicationUserDetailsService;
import com.MyMoviePlan.service.UserService;
import com.MyMoviePlan.util.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.MyMoviePlan.model.UserRole.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final JWTUtil jwtUtil;
    private final UserService service;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ApplicationUserDetailsService userDetailsService;

    @GetMapping("/")
    public String index() {
        return "Welcome " + getUserName();
    }

    @PostMapping("authenticate")
    public Token authenticate(@RequestBody Credentials credentials) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getUsername());

        final String token = jwtUtil.generateToken(userDetails);

        return new Token(token);
    }

    @GetMapping("{username}")
    @PreAuthorize("hasAuthority('READ')")
    public UserEntity user(@PathVariable final String username) {

        final UserEntity user = service.getUser(username)
                .orElseThrow(() -> new UserNotFoundException("User: '" + username + "' not found"));
        if (authorizeUser(user, getUserName()))
            return user;
        else
            throw new UnAuthorizedException("You are not authorized access this account");
    }

    @GetMapping("all")
    @PreAuthorize("hasAuthority('WRITE')")
    public List<UserEntity> allUsers() {
        return service.findAll();
    }


    @GetMapping("edit/{username}")
    @PreAuthorize("hasAuthority('READ')")
    public UserEntity editUser(@PathVariable final String username) {
        final UserEntity user = service.getUser(username)
                .orElseThrow(() -> new UserNotFoundException("User: '" + username + "' not found"));
        if (authorizeUser(user, getUserName()))
            return user;
        else
            throw new UnAuthorizedException("You are not authorized access this account");
    }

    @PutMapping("update/{username}")
    @PreAuthorize("hasAuthority('READ')")
    public UserEntity updateUser(@RequestBody final UserEntity userEntity,
                                 @PathVariable final String username) {

        final UserEntity user = service.getUser(username)
                .orElseThrow(() -> new UserNotFoundException("User: '" + username + "' not found"));

        if (!authorizeUser(user, getUserName()))
            throw new UnAuthorizedException("You are not authorized access this account");
        return service.save(userEntity);
    }

    @PostMapping("sign-up")
    public String signUp(@RequestBody final UserEntity userEntity) {

        if (userEntity.getEmail().contains("super"))
            userEntity.setUserRole(ROLE_SUPER_ADMIN);
        else if (userEntity.getEmail().contains("admin"))
            userEntity.setUserRole(ROLE_ADMIN);
        else
            userEntity.setUserRole(ROLE_USER);

        userEntity.setAccountNonExpired(true)
                .setAccountNonLocked(true)
                .setCredentialsNonExpired(true)
                .setEnabled(true)
                .setPassword(passwordEncoder.encode(userEntity.getPassword()));
        try {
            service.save(userEntity);
        } catch (Exception e) {
            throw new RuntimeException("Unique key contains volition");
        }
        return "Your account is created";
    }

    @PutMapping("change-password")
    @PreAuthorize("hasAuthority('READ')")
    public String changePassword(@RequestParam final String email, @RequestParam final String password) {

        final UserEntity user = service.getUser(email)
                .orElseThrow(() -> new UserNotFoundException("User: '" + email + "' not found."));

        if (!authorizeUser(user, getUserName()))
            throw new UnAuthorizedException("You are not authorized access this account");

        user.setPassword(passwordEncoder.encode(password));
        service.save(user);
        return "password changed successfully";
    }

    @PutMapping("forgot-password")
    public String forgotPassword(@RequestParam final String email, @RequestParam final String password) {
        final UserEntity user = service.getUser(email)
                .orElseThrow(() -> new UserNotFoundException("User: '" + email + "' not found."));

        user.setPassword(passwordEncoder.encode(password));
        service.save(user);
        return "Password reset successfully";
    }

    @DeleteMapping("delete/{username}")
    @PreAuthorize("hasAuthority('DELETE')")
    public String delete(@PathVariable final String username) {
        final UserEntity user = service.getUser(username)
                .orElseThrow(() -> new UserNotFoundException("User: '" + username + "' not found."));
        service.deleteById(user.getId());
        return "User is deleted";
    }

    private String getUserName() {
        final String token = request.getHeader("Authorization");
        return jwtUtil.getUserName(token);
    }

    private boolean authorizeUser(UserEntity user, String username) {
        if (user.getEmail().equals(username) || user.getId().equals(username) || user.getMobile().equals(username))
            return true;
        else {
            final UserEntity userEntity = service.getUser(username)
                    .get();

            if (userEntity.getUserRole().equals(ROLE_ADMIN) || userEntity.getUserRole().equals(ROLE_SUPER_ADMIN))
                return true;
        }
        return false;
    }
}
