package dao;

import domain.User2;

import java.util.List;
import java.util.Map;

public interface UserDao {
    public List<User2> findAll();

    public User2 findUserByUsernameAndPassword(String username, String password);

    public void addUser(User2 user);

    void deleteUser(int id);

    User2 findUserById(int id);
    void updateUser(User2 user2);


    int findTotalCount(Map<String, String[]> condition);

    List<User2> findByPage(int start, int rows, Map<String, String[]> condition);
}
