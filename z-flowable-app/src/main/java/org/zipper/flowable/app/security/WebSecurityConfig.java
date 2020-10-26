package org.zipper.flowable.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
 * @author zhuxj
 * @since 2020/10/21
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Resource
    private SystemAuthenticationEntryPoint systemAuthenticationEntryPoint;
    @Resource
    private SystemAccessDeniedHandler systemAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                //以下路径不需要认证
                .antMatchers(
                        "/swagger-resources/**",
                        "/v3/api-docs",
                        "/swagger-ui/**",
                        "/auth/signUp").permitAll()
                //余下所有请求需要认证
                .anyRequest().authenticated().and()
                //采用无状态session策略
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                //开启跨域支持，禁用CSRF
                .cors().and().csrf().disable()
                //使用默认的用户名密码登录拦截
                .formLogin()
                //设置登录成功后的处理
                .successHandler(formAuthenticationSuccessHandler)
                //设置登录失败后的处理
                .failureHandler(formAuthenticationFailureHandler).and()
                //使用默认登出端点
                .logout()
                //设置登出成功后的处理
                .logoutSuccessHandler(systemLogoutSuccessHandler).and()
                //采用自定义的token认证拦截器
                .apply(new JwtAuthenticationConfigurer<>())
                //设置token认证失败后的处理
                .failureHandler(jwtAuthenticationFailureHandler)
                //设置token认证成功后的处理
                .successHandler(jwtAuthenticationSuccessHandler).and()
                .exceptionHandling()
                //全局的认证入口
                .authenticationEntryPoint(systemAuthenticationEntryPoint)
                .accessDeniedHandler(systemAccessDeniedHandler);
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
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }
}
