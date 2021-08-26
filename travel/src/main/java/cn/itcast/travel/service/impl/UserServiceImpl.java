package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao userdao = new UserDaoImpl();
    //1. 用户注册
    @Override
    public boolean regist(User user) {
        //1.根据用户名查询用户对象
        User u = userdao.findByUsername(user.getUsername());
        //判断u是否为null
        if (u != null){
            //用户名存在，注册失败
            return false;
        }
            //2.注册成功，保存用户信息
            //2.1 设置激活码，唯一字符串
            user.setCode(UuidUtil.getUuid());  //设置唯一字符串
            //2.2设置激活状态
            user.setStatus("N");
            userdao.save(user);
        //3. 激活邮件发送，邮件正文？
        String content="<a href='http://localhost:8080/travel/user/active?code="+user.getCode()+"'>点击激活【张猛小甜瓜旅游网】</a>";
        MailUtils.sendMail(user.getEmail(),content,"小甜瓜旅游网激活邮件");
            return true;
    }

    //用户邮件验证码激活
    @Override
    public boolean active(String code) {
        //1.根据激活码查询用户对象
        User user = userdao.findByCode(code);
        if(user != null){
            //2.调用dao的修改激活状态的方法
            userdao.updateStatus(user);
            return true;
        }else{
            return false;
        }



    }

    //登录方法
    @Override
    public User login(User user) {
        return userdao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }


}
