package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    //1. 用户注册
    boolean regist(User user);

   // 2.用户邮件验证码激活
    boolean active(String code);

    User login(User user);
}
