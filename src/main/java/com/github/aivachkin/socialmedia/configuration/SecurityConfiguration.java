package com.github.aivachkin.socialmedia.configuration;

import com.github.aivachkin.socialmedia.filter.JwtFilter;
import com.github.aivachkin.socialmedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Класс - конфигурация безопасности
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;

    private final UserRepository userRepository;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
    };

    /**
     * Бин для получения пользователя из базы для дальнейшего его использования в провайдере
     *
     * @return пользователь из базы (или исключение, если не найден)
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> (UserDetails) userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }


    /**
     * Провайдер - объект доступа к данным, отвечающий за сведения о пользователе, за кодирование пароля
     *
     * @return провайдер
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    /**
     * Бин для кодирования пароля пользователя
     *
     * @return объект для хэширования пароля (алгоритм bcrypt)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // Выдача токена по логину и паролю и получение нового access токена по refresh токену - без защиты
    // Остальные эндпоипнты - только для аутентифицированных пользователей
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(
                        authz -> {
                            try {
                                authz
                                        .antMatchers("/api/auth/login", "/api/auth/token", "/api/users/register")
                                        .permitAll()
                                        .antMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .anyRequest().authenticated()
                                        .and()
                                        .formLogin()
                                        .and()
                                        .logout().logoutSuccessUrl("/api/auth/login")
                                        .and()
                                        .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                .build();
    }
}
