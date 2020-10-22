package org.zipper.flowable.app.entity;

import org.zipper.flowable.app.constant.enums.Status;

import java.time.LocalDateTime;

/**
 * 用户实体
 * @author zhuxj
 * @since 2020/10/22
 */
public class Member {

    private Integer id;
    private String username;
    private String password;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Status status;


    public Member() {
    }

    public Member(String username, String password) {
        this.username = username;
        this.password = password;
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
        this.status = Status.VALID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
