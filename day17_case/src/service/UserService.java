package service;

import domain.PageBean;
import domain.User2;

import java.util.List;
import java.util.Map;

/*
* 用户管理的业务接口
* */
public interface UserService {
    /*
    * 查询所有用户信息
    */
    public List<User2> findAll();
    public User2 login(User2 user2);

    void addUser(User2 user2);

    void deleteUser(String id);

    User2 findUserById(String id);

    void updateUser(User2 user2);

    void delSelected(String[] Allid);

    PageBean<User2> findUserByPage(String currentPage, String rows, Map<String, String[]> condition);
}
