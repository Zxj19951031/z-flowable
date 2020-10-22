package org.zipper.flowable.app.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@ConfigurationProperties(prefix = "jwt")
public class DaoUserDetailService implements UserDetailsService {

    private long timeout = 3600;
    private String saltRedisKeyPrefix = "jwt";

    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return User.builder().username(s).password("123456").authorities(new ArrayList<>()).build();
    }

    public String saveToken(UserDetails user) {
        String salt = BCrypt.gensalt();
        String key = saltRedisKeyPrefix + ":" + user.getUsername();
        redisTemplate.opsForValue().set(key, salt, timeout, TimeUnit.SECONDS);
        Algorithm algorithm = Algorithm.HMAC256(salt);
        long timestamps = System.currentTimeMillis();
        Date issuedAt = new Date(timestamps);
        Date expiresAt = new Date(timestamps + timeout * 1000);
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    public void removeToken(String name) {
        String key = saltRedisKeyPrefix + ":" + name;
        redisTemplate.delete(key);
    }

    public UserDetails loadUserByToken(DecodedJWT jwt) {

        String username = jwt.getSubject();
        String key = saltRedisKeyPrefix + ":" + username;
        String salt = (String) redisTemplate.opsForValue().get(key);
        UserDetails user = loadUserByUsername(username);

        return User.builder().username(user.getUsername()).password(salt).authorities(user.getAuthorities()).build();
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getSaltRedisKeyPrefix() {
        return saltRedisKeyPrefix;
    }

    public void setSaltRedisKeyPrefix(String saltRedisKeyPrefix) {
        this.saltRedisKeyPrefix = saltRedisKeyPrefix;
    }

}
