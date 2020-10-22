package org.zipper.flowable.app.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final RequestMatcher requestMatcher;
    private List<RequestMatcher> permissiveRequestMatchers;
    private AuthenticationManager authenticationManager;


    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;

    public JwtAuthenticationFilter() {
        this.requestMatcher = new RequestHeaderRequestMatcher(AUTHORIZATION_HEADER);
        this.permissiveRequestMatchers = new ArrayList<>();
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
        Assert.notNull(successHandler, "AuthenticationSuccessHandler must be specified");
        Assert.notNull(failureHandler, "AuthenticationFailureHandler must be specified");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //对于符合拦截规则的请求进行拦截，否则递交给下个拦截
        if (requireAuthenticate(httpServletRequest)) {
            Authentication authentication = null;
            AuthenticationException exception = null;

            try {
                String token = getJwtToken(httpServletRequest);
                if (StringUtils.isNotBlank(token)) {
                    JwtAuthenticationToken authToken = new JwtAuthenticationToken(JWT.decode(token));
                    authentication = this.getAuthenticationManager().authenticate(authToken);
                } else {
                    exception = new InsufficientAuthenticationException("JWT is Empty");
                }
            } catch (JWTDecodeException e) {
                logger.error("JWT format error", e);
                exception = new InsufficientAuthenticationException("JWT format error", exception);
            } catch (InternalAuthenticationServiceException e) {
                logger.error("An internal error occurred while trying to authenticate the user.", e);
                exception = e;
            } catch (AuthenticationException e) {
                exception = e;
            }

            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                successHandler.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
            } else if (!permissiveRequest(httpServletRequest)) {
                SecurityContextHolder.clearContext();
                failureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, exception);

            }

        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);


    }

    private boolean permissiveRequest(HttpServletRequest request) {
        if (permissiveRequestMatchers == null)
            return false;
        for (RequestMatcher permissiveMatcher : permissiveRequestMatchers) {
            if (permissiveMatcher.matches(request))
                return true;
        }
        return false;
    }

    public void setPermissiveUrl(String... urls) {
        if (urls != null) {
            for (String url : urls) {
                permissiveRequestMatchers.add(new AntPathRequestMatcher(url));
            }
        }
    }

    private String getJwtToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }

    public void setFailHandler(AuthenticationFailureHandler failHandler) {
        this.failureHandler = failHandler;
    }

    private boolean requireAuthenticate(HttpServletRequest request) {
        return this.requestMatcher.matches(request);
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setSuccessHandler(AuthenticationSuccessHandler handler) {
        this.successHandler = handler;
    }
}
