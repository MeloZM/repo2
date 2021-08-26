package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import domain.PageBean;
import domain.User2;
import service.UserService;

import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    @Override
    public List<User2> findAll() {
        UserDao dao = new UserDaoImpl();
        //调用dao完成查询
        return dao.findAll();
    }

    @Override
    public User2 login(User2 user2) {
        UserDao dao = new UserDaoImpl();
        return dao.findUserByUsernameAndPassword(user2.getUsername(),user2.getPassword());
    }

    @Override
    public void addUser(User2 user2) {
        UserDao dao = new UserDaoImpl();
         dao.addUser(user2);
    }

    @Override
    public void deleteUser(String id) {
        UserDao dao = new UserDaoImpl();
        dao.deleteUser(Integer.parseInt(id));
    }

    @Override
    public User2 findUserById(String id) {
        UserDao dao = new UserDaoImpl();
        return dao.findUserById(Integer.parseInt(id));
    }

    @Override
    public void updateUser(User2 user2) {
        UserDao dao = new UserDaoImpl();
        dao.updateUser(user2);
    }

    @Override
    public void delSelected(String[] Allid) {
        UserDao dao = new UserDaoImpl();
        //1.遍历数组
        if (Allid != null && Allid.length >0){
            for (String i:Allid){
                dao.deleteUser(Integer.parseInt(i));
            }
        }
    }

    @Override
    public PageBean<User2> findUserByPage(String _currentPage, String _rows, Map<String, String[]> condition) {
        UserDao dao = new UserDaoImpl();
        int currentPage = Integer.parseInt(_currentPage);
        int rows = Integer.parseInt(_rows);
        //1.创建空的PageBean对象
        PageBean<User2> pb = new PageBean<User2>();
        //2.设置参数
        pb.setCurrentPage(currentPage);
        pb.setRows(rows);
        //3.调用dao查询总记录数
        int totalCount = dao.findTotalCount(condition);
        pb.setTotalCount(totalCount);
        //4.调用dao查询List集合
        //计算开始记录索引
        int start = (currentPage - 1) * rows;
        List<User2> list = dao.findByPage(start,rows,condition);
        pb.setList(list);
        //5.计算总页码
        int totalPage = (totalCount % rows) == 0 ? totalCount/rows : totalCount/rows + 1;
        pb.setTotalPage(totalPage);
        return pb;
    }
}
