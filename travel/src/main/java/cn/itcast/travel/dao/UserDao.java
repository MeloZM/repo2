package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    //1. 根据用户名查询用户信息
    public User findByUsername(String username);

    //2. 用户信息添加
    public void save(User user);

    //3.根据激活码查询用户对象
    User findByCode(String code);
    //4.修改指定用户的激活状态
    void updateStatus(User user);

    User findByUsernameAndPassword(String username, String password);
}
