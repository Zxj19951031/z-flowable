package org.zipper.flowable.app.security.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

public class JwtAuthenticationConfigurer<T extends JwtAuthenticationConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;
    private String[] permissiveUrl;

    @Override
    public void configure(B builder) throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
        filter.setFailHandler(failureHandler);
        filter.setPermissiveUrl(permissiveUrl);
        filter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        filter.setSuccessHandler(successHandler);

        builder.addFilterBefore(filter, LogoutFilter.class);
    }


    public JwtAuthenticationConfigurer<T, B> failureHandler(AuthenticationFailureHandler handler) {
        this.failureHandler = handler;
        return this;
    }

    public JwtAuthenticationConfigurer<T, B> permissiveUrl(String... urls) {
        this.permissiveUrl = urls;
        return this;
    }

    public JwtAuthenticationConfigurer<T, B> successHandler(AuthenticationSuccessHandler handler) {
        this.successHandler = handler;
        return this;
    }
}
