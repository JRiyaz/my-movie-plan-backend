package com.MyMoviePlan.service;

import com.MyMoviePlan.entity.UserEntity;
import com.MyMoviePlan.exception.UnAuthorizedException;
import com.MyMoviePlan.exception.UserNotFoundException;
import com.MyMoviePlan.model.Credentials;
import com.MyMoviePlan.model.HttpResponse;
import com.MyMoviePlan.model.Token;
import com.MyMoviePlan.repository.UserRepository;
import com.MyMoviePlan.util.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.MyMoviePlan.model.UserRole.*;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final JWTUtil jwtUtil;
    private final UserRepository repository;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public Optional<UserEntity> findByEmail(final String email) {
        return repository.findByEmail(email);
    }

    public Optional<UserEntity> findByMobile(final String mobile) {
        return repository.findByMobile(mobile);
    }

    public Optional<UserEntity> findById(final String id) {
        return repository.findById(id);
    }

    public UserEntity findByUserName(final String username) {

        final UserEntity user = this.getUser(username);
        if (authorizeUser(user, getUserName()))
            return user;
        else
            throw new UnAuthorizedException("You are not authorized access this account");
    }

    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    public UserEntity save(final UserEntity user) {
        return repository.save(user);
    }

    public UserEntity update(final UserEntity userEntity, final String username) {
        final UserEntity user = this.getUser(username);

        if (!authorizeUser(user, getUserName()))
            throw new UnAuthorizedException("You are not authorized access this account");

        userEntity.setName(isNullOrEmpty(userEntity.getName()) ? user.getName() : userEntity.getName())
                .setEmail(isNullOrEmpty(userEntity.getEmail()) ? user.getEmail() : userEntity.getEmail())
                .setMobile(isNullOrEmpty(userEntity.getMobile()) ? user.getMobile() : userEntity.getMobile())
                .setTerms(user.getTerms())
                .setPassword(user.getPassword())
                .setUserRole(user.getUserRole())
                .setAccountNonExpired(user.getIsAccountNonExpired())
                .setAccountNonLocked(user.getIsAccountNonLocked())
                .setCredentialsNonExpired(user.getIsCredentialsNonExpired())
                .setEnabled(user.getIsEnabled());

        return repository.save(userEntity);
    }

    public HttpResponse register(final UserEntity user) {

        if (user.getEmail().contains("super"))
            user.setUserRole(ROLE_SUPER_ADMIN);
        else if (user.getEmail().contains("admin"))
            user.setUserRole(ROLE_ADMIN);
        else
            user.setUserRole(ROLE_USER);

        user.setAccountNonExpired(true)
                .setAccountNonLocked(true)
                .setCredentialsNonExpired(true)
                .setEnabled(true)
                .setPassword(this.passwordEncoder.encode(user.getPassword()));
        try {
            this.save(user);
        } catch (Exception e) {
            throw new RuntimeException("SQL Unique key constrains volition");
        }
        return new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Your account is created");
    }

    public HttpResponse deleteById(final String username) {
        final UserEntity user = this.getUser(username);
        repository.deleteById(user.getId());
        return new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Your account is deleted");
    }

    public HttpResponse forgotPassword(final Credentials credentials) {
        final UserEntity user = this.getUser(credentials.getUsername());

        user.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        this.save(user);
        return new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Your password reset successfully");
    }

    public HttpResponse changePassword(final Credentials credentials) {

        final UserEntity user = this.getUser(credentials.getUsername());

        if (!this.authorizeUser(user, this.getUserName()))
            throw new UnAuthorizedException("You are not authorized access this account");

        user.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        this.save(user);
        return new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Your password changed successfully");
    }

    public Token authenticate(final Credentials credentials) {
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

        final String token = jwtUtil.generateToken(credentials.getUsername());
        return new Token(token);
    }

    public Token checkUniqueness(final String username) {
        final String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Optional<UserEntity> user = null;
        if (Pattern.matches(regex, username))
            user = findByEmail(username);
        else if (username.contains("-") && username.length() > 10)
            user = findById(username);
        else
            user = findByMobile(username);
        return user.isPresent() ? new Token(username) : new Token(null);
    }

    public UserEntity getUser(final String idOrEmailOrMobile) {
        final String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Optional<UserEntity> user = null;
        if (Pattern.matches(regex, idOrEmailOrMobile))
            user = findByEmail(idOrEmailOrMobile);
        else if (idOrEmailOrMobile.contains("-") && idOrEmailOrMobile.length() > 10)
            user = findById(idOrEmailOrMobile);
        else
            user = findByMobile(idOrEmailOrMobile);
        return user
                .orElseThrow(() -> new UserNotFoundException("User: '" + idOrEmailOrMobile + "' not found"));
    }

    public String getUserName() {
        final String token = request.getHeader("Authorization");
        return jwtUtil.getUserName(token);
    }

    public UserEntity getLoggedInUser() {
        return getUser(getUserName());
    }

    private boolean authorizeUser(final UserEntity user, final String username) {
        if (user.getEmail().equals(username) || user.getId().equals(username) || user.getMobile().equals(username))
            return true;
        else
            return user.getUserRole().equals(ROLE_ADMIN) || user.getUserRole().equals(ROLE_SUPER_ADMIN);

    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.equals(" ") || value.equals(null) ? true : false;
    }
}
