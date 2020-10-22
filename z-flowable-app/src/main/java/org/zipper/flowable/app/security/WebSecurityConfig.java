package org.zipper.flowable.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.zipper.flowable.app.security.form.FormAuthenticationFailureHandler;
import org.zipper.flowable.app.security.form.FormAuthenticationSuccessHandler;
import org.zipper.flowable.app.security.jwt.JwtAuthenticationConfigurer;
import org.zipper.flowable.app.security.jwt.JwtAuthenticationFailureHandler;
import org.zipper.flowable.app.security.jwt.JwtAuthenticationProvider;
import org.zipper.flowable.app.security.jwt.JwtAuthenticationSuccessHandler;

import javax.annotation.Resource;

/**
 * 这个配置类由 {@code @EnableWebSecurity}启用Spring Security的web安全支持并提供Spring MVC集成。
 * 它还扩展了{@code WebSecurityConfigurerAdapter}并覆盖了它的一些方法来设置web安全配置的一些细节。
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private FormAuthenticationFailureHandler formAuthenticationFailureHandler;
    @Resource
    private FormAuthenticationSuccessHandler formAuthenticationSuccessHandler;
    @Resource
    private JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    @Resource
    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    @Resource
    private SystemLogoutSuccessHandler systemLogoutSuccessHandler;
    @Resource
    private DaoUserDetailService daoUserDetailService;
    @Resource
    private DaoPasswordEncoder daoPasswordEncoder;
    @Resource
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Resource
    private SystemAuthenticationEntryPoint systemAuthenticationEntryPoint;

    /**
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //放行认证接口配置
        http
                .authorizeRequests()
                .antMatchers("/swagger-resources/**", "/v3/api-docs", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .cors().and().csrf().disable()
                .formLogin()
                .successHandler(formAuthenticationSuccessHandler)
                .failureHandler(formAuthenticationFailureHandler).and()
                .logout()
                .logoutSuccessHandler(systemLogoutSuccessHandler).and()
                .apply(new JwtAuthenticationConfigurer<>())
                .failureHandler(jwtAuthenticationFailureHandler)
                .successHandler(jwtAuthenticationSuccessHandler).and()
                .exceptionHandling()
                .authenticationEntryPoint(systemAuthenticationEntryPoint);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(daoAuthenticationProvider())
                .authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(daoUserDetailService);
        provider.setPasswordEncoder(daoPasswordEncoder);
        return provider;
    }
}
