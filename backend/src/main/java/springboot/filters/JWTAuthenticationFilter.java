package springboot.filters;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springboot.Repository.OwnerRepository;
import springboot.model.Owner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static springboot.constants.JWTConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    private OwnerRepository ownerRepository;
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, OwnerRepository ownerRepository) {
        this.authenticationManager = authenticationManager;
        this.ownerRepository =ownerRepository;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            Owner creds = new ObjectMapper()
                    .readValue(req.getInputStream(), Owner.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        long ownerId = this.ownerRepository.findByEmail(((User)auth.getPrincipal()).getUsername()).getId();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        res.addHeader(OWNER_ID_HEADER_STRING, Long.toString(ownerId));

//        res.addCookie(new Cookie("ownerId",Long.toString(ownerId)));
//        res.addCookie(new Cookie("token", token));
//        chain.doFilter(req,res);
    }
}

