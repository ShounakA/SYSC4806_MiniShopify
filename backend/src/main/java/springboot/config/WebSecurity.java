package springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Bean;
import springboot.Repository.OwnerRepository;
import springboot.filters.JWTAuthenticationFilter;
import springboot.filters.JWTAuthorizationFilter;
import springboot.services.OwnerDetailsServiceImpl;

import static springboot.constants.JWTConstants.SIGN_UP_URL;
@EnableWebSecurity
@Configuration
public class WebSecurity extends WebSecurityConfigurerAdapter {

        private OwnerDetailsServiceImpl userDetailsService;
        private BCryptPasswordEncoder bCryptPasswordEncoder;
        private OwnerRepository ownerRepository;
        public WebSecurity(OwnerDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder,OwnerRepository ownerRepository) {
            this.userDetailsService = userDetailsService;
            this.bCryptPasswordEncoder = bCryptPasswordEncoder;
            this.ownerRepository =ownerRepository;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable().authorizeRequests()
                    .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                    .antMatchers(HttpMethod.GET, "/api/shop/*").permitAll()
                    .antMatchers("/api/public/*").permitAll()
                    .antMatchers("/").permitAll()
                    .antMatchers("/h2-console/*").permitAll()
                    .antMatchers("/assets/**", "static/assets/**", "/img/**", "**/favicon.ico").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .addFilter(new JWTAuthenticationFilter(authenticationManager(),this.ownerRepository))
                    .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                    // this disables session creation on Spring Security
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
            auth.inMemoryAuthentication()
                    .passwordEncoder(bCryptPasswordEncoder)
                    .withUser("ADMIN")
                    .password(bCryptPasswordEncoder.encode("Pass4Admin"))
                    .roles("USER");
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
            return source;
        }
}
