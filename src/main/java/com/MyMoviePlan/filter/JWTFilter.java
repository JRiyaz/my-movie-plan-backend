package com.MyMoviePlan.filter;

import com.MyMoviePlan.model.HttpResponse;
import com.MyMoviePlan.security.ApplicationUserDetailsService;
import com.MyMoviePlan.util.JWTUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component()
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private ApplicationUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String authorization = request.getHeader("Authorization");
            String token = null;
            String userName = null;

            if (authorization != null && authorization.startsWith("Bearer ")) {
                token = authorization.substring(7);
                userName = jwtUtil.getUsernameFromToken(token);
            }

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails
                        = userDetailsService.loadUserByUsername(userName);

                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());

                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (JwtException exception) {
            setErrorResponse(HttpStatus.NOT_ACCEPTABLE, response, exception);
        } catch (Exception exception) {
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, exception);
        }
    }

    private void setErrorResponse(HttpStatus status, HttpServletResponse response, Exception exception) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        final HttpResponse httpResponse =
                new HttpResponse(status.value(),
                        HttpStatus.valueOf(status.value()).getReasonPhrase(),
                        exception.getMessage());
        try {
            final String json = httpResponse.covertToJson();
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

