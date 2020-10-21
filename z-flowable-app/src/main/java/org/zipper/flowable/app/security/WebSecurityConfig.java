package org.zipper.flowable.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.zipper.flowable.app.security.access.SystemAccessDeniedHandler;
import org.zipper.flowable.app.security.password.DaoPasswordEncoder;
import org.zipper.flowable.app.security.userdetails.DaoUserDetailService;
import org.zipper.flowable.app.security.web.SystemAuthenticationEntryPoint;
import org.zipper.flowable.app.security.authentication.SystemAuthenticationSuccessHandler;
import org.zipper.flowable.app.security.authentication.logout.SystemLogoutSuccessHandler;
import org.zipper.flowable.app.security.authentication.SystemAuthenticationFailureHandler;

import javax.annotation.Resource;

/**
 * 这个配置类由 {@code @EnableWebSecurity}启用Spring Security的web安全支持并提供Spring MVC集成。
 * 它还扩展了{@code WebSecurityConfigurerAdapter}并覆盖了它的一些方法来设置web安全配置的一些细节。
 */
@Configuration
@EnableWebSecurity
@EnableRedisHttpSession
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private SystemAuthenticationEntryPoint systemAuthenticationEntryPoint;
    @Resource
    private SystemAuthenticationFailureHandler systemAuthenticationFailureHandler;
    @Resource
    private SystemAuthenticationSuccessHandler systemAuthenticationSuccessHandler;
    @Resource
    private SystemAccessDeniedHandler systemAccessDeniedHandler;
    @Resource
    private SystemLogoutSuccessHandler systemLogoutSuccessHandler;
    @Resource
    private DaoUserDetailService daoUserDetailService;
    @Resource
    private DaoPasswordEncoder daoPasswordEncoder;

    /**
     * 这个方法定义哪些URL路径应该被保护，哪些不应该被保护
     * 特别地，/和/home路径被配置为不需要任何身份验证。
     * 所有其他路径都必须经过身份验证。
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(daoAuthenticationProvider())
                //未登录时访问资源
                .exceptionHandling()
                .authenticationEntryPoint(systemAuthenticationEntryPoint)
                .accessDeniedHandler(systemAccessDeniedHandler)
                .and()
                //放行/认证资源
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                //登录
                .formLogin()
                .failureHandler(systemAuthenticationFailureHandler)
                .successHandler(systemAuthenticationSuccessHandler)
                .and()
                //登出
                .logout()
                .logoutSuccessHandler(systemLogoutSuccessHandler)
                .permitAll()
                .and()
                .sessionManagement()
                .and()
                .cors().disable()
                .csrf().disable();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(daoUserDetailService);
        provider.setPasswordEncoder(daoPasswordEncoder);
        return provider;
    }
}
