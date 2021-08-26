package dao.impl;

import dao.UserDao;
import domain.User2;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserDaoImpl implements UserDao {
    @Override
    @Test
    public List<User2> findAll() {
        //2.创建JDBCTemplate对象
        JdbcTemplate template = new JdbcTemplate(JDBCUtil.getDataSource());
        //使用JDBC完成查询
        //1.定义sql
        String sql = "select * from user2";
        List<User2> user2 = template.query(sql, new BeanPropertyRowMapper<User2>(User2.class));
        return user2;
    }

    @Override
    public User2 findUserByUsernameAndPassword(String username, String password) {
        //2.创建JDBCTemplate对象
        JdbcTemplate template = new JdbcTemplate(JDBCUtil.getDataSource());
        //使用JDBC完成查询
        try {
            //1.定义sql
            String sql = "select * from user2 where username =? and password = ?";
            User2 user2 = template.queryForObject(sql, new BeanPropertyRowMapper<User2>(User2.class), username, password);
            return user2;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addUser(User2 user2) {
        //2.创建JDBCTemplate对象
        JdbcTemplate template = new JdbcTemplate(JDBCUtil.getDataSource());
        //使用JDBC完成查询

            //1.定义sql
            String sql = "insert into user2 values(null,?,?,?,?,?,?,null,null)";
            template.update(sql, user2.getName(), user2.getGender(), user2.getAge(), user2.getAddress(), user2.getQq(), user2.getEmail());

    }

    @Override
    public void deleteUser(int id) {
        //2.创建JDBCTemplate对象
        JdbcTemplate template = new JdbcTemplate(JDBCUtil.getDataSource());
        //使用JDBC完成查询

        //1.定义sql
        String sql = "delete from user2 where id =?";

        template.update(sql,id);
    }

    @Override
    public User2 findUserById(int id) {
        //2.创建JDBCTemplate对象
        JdbcTemplate template = new JdbcTemplate(JDBCUtil.getDataSource());
        //使用JDBC完成查询

        //1.定义sql
        String sql = "select  * from user2 where id =?";
        User2 user2 = template.queryForObject(sql, new BeanPropertyRowMapper<User2>(User2.class), id);
        return user2;

    }

    @Override
    public void updateUser(User2 user2) {
        //2.创建JDBCTemplate对象
        JdbcTemplate template = new JdbcTemplate(JDBCUtil.getDataSource());
        //使用JDBC完成查询

        //1.定义sql
        String sql = "update user2 set name = ?,gender =?,age =?,address=?,qq=?,email=? where id=?";
        template.update(sql,user2.getName(),user2.getGender(),user2.getAge(),user2.getAddress(),user2.getQq(),user2.getEmail(),user2.getId());
    }

    @Override
    public int findTotalCount(Map<String, String[]> condition) {

        //2.创建JDBCTemplate对象
        JdbcTemplate template = new JdbcTemplate(JDBCUtil.getDataSource());
        //定义sql初始化模板
        String sql = "select count(*) from user2 where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        //2.遍历map
        Set<String> keySet = condition.keySet();
        //定义参数的集合
        List<Object> params = new ArrayList<Object>();
        for (String key : keySet) {

            //排除分页条件参数
            if("currentPage".equals(key) || "rows".equals(key)){
                continue;
            }

            //获取value
            String value = condition.get(key)[0];
            //判断value是否有值
            if(value != null && !"".equals(value)){
                //有值
                sb.append(" and "+key+" like ? ");
                params.add("%"+value+"%");//？条件的值
            }
        }
        System.out.println(sb.toString());
        System.out.println(params);

        return template.queryForObject(sb.toString(),Integer.class,params.toArray());
    }

    @Override
    public List<User2> findByPage(int start, int rows, Map<String, String[]> condition) {
        JdbcTemplate template = new JdbcTemplate(JDBCUtil.getDataSource());
        String sql = "select * from user2  where 1 = 1 ";

        StringBuilder sb = new StringBuilder(sql);
        //2.遍历map
        Set<String> keySet = condition.keySet();
        //定义参数的集合
        List<Object> params = new ArrayList<Object>();
        for (String key : keySet) {

            //排除分页条件参数
            if("currentPage".equals(key) || "rows".equals(key)){
                continue;
            }

            //获取value
            String value = condition.get(key)[0];
            //判断value是否有值
            if(value != null && !"".equals(value)){
                //有值
                sb.append(" and "+key+" like ? ");
                params.add("%"+value+"%");//？条件的值
            }
        }

        //添加分页查询
        sb.append(" limit ?,? ");
        //添加分页查询参数值
        params.add(start);
        params.add(rows);
        sql = sb.toString();
        System.out.println(sql);
        System.out.println(params);

        return template.query(sql,new BeanPropertyRowMapper<User2>(User2.class),params.toArray());


    }


}