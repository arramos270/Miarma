package com.example.Miarma.Security.jwt;

import com.example.Miarma.Models.UserEntity;
import com.example.Miarma.Services.UserEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Log
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserEntityService userService;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getJwtFromRequest(request);

        try {
            if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {

                //Long userId = jwtProvider.getUserIdFromJwt(token);
                UUID userId = jwtProvider.getUserIdFromJwt(token);

                Optional<UserEntity> userEntity = userService.findById(userId);

                if (userEntity.isPresent()) {
                    UserEntity user = userEntity.get();
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    user.getRole(),
                                    user.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);


                }

                // Si no encontramos al usuario por ID, no se guarda un UsernamePasswordAuthenticationToken
                // en el contexto de seguridad, y por tanto, el usuario no estará autenticado.
                // Por tanto, Spring tratará de autenticarlo en el siguiente filtro, y si no lo consigue,
                // devolverá un error.

            }

        } catch (Exception ex) {
            // Informar en el log
            log.info("No se ha podido establecer el contexto de seguridad (" + ex.getMessage() + ")");
        }

        filterChain.doFilter(request, response);




    }

    private String getJwtFromRequest(HttpServletRequest request) {
        // Authorization: Bearer eltoken.qiemas.megusta
        String bearerToken = request.getHeader(JwtProvider.TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtProvider.TOKEN_PREFIX)) {
            return bearerToken.substring(JwtProvider.TOKEN_PREFIX.length());
        }
        return null;
    }
}
