package com.github.aivachkin.socialmedia.filter;

import com.github.aivachkin.socialmedia.domain.JwtAuthentication;
import com.github.aivachkin.socialmedia.service.JwtProvider;
import com.github.aivachkin.socialmedia.utility.JwtUtility;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Собственный фильтр, осуществляющий аутентификацию пользователей
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;

    /**
     * Метод для генерации объекта аутентификации и размещения его в SecurityContext
     *
     * @param request текущий запрос
     * @param response текущий ответ
     * @param filterChain список фильтров, предназначенных для обработки
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {

        final String token = getTokenFromRequest((HttpServletRequest) request);
        if (token != null && jwtProvider.validateAccessToken(token)) {

            final Claims claims = jwtProvider.getAccessClaims(token);
            final JwtAuthentication jwtInfoToken = JwtUtility.generate(claims);

            jwtInfoToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Метод для получения из HTTP-запроса значения заголовка Authorization
     *
     * @param request входящий HTTP-запрос
     * @return токен, полученный из входящего запроса
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}
