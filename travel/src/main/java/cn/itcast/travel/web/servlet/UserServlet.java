package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
   //声明service

   private UserService service = new UserServiceImpl();

    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码
        String check = request.getParameter("check");
        //从session中获验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String)session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER"); //保证验证码只能用一次
        //比较
        if (checkcode_server == null || ! checkcode_server.equalsIgnoreCase(check)){
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误！");
            //将info序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
            //验证码错误
        }
        //1. 获取数据
        Map<String, String[]> map = request.getParameterMap();
        //2.封装对象
        User user =new User();
        try {
            BeanUtils.populate(user,map);
                /*BeanUtils.populate( Object user, Map map)，
                    这个方法会遍历map<key, value>中的key，如果user中有这个属性，就把这个key对应的value值赋给user的属性。
                */
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用Service完成注册
        //UserService service = new UserServiceImpl();
        boolean flag = service.regist(user);
        //4。 响应数据
        ResultInfo info = new ResultInfo();
        if (flag){
            //注册成功
            info.setFlag(true);
        }else {
            info.setFlag(false);
            info.setErrorMsg("注册失败！");
            //注册失败
        }
        //将info对象序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        System.out.println(json);
        //解决json乱码
        response.setContentType("application/json;charset=utf-8");
        //将数据输出到客户端浏览器
        System.out.println(json);
        response.getWriter().write(json);

    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码
        String check = request.getParameter("check");
        //从session中获验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String)session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER"); //保证验证码只能用一次
        //比较
        if (checkcode_server == null || ! checkcode_server.equalsIgnoreCase(check)){
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误！");
            //将info序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
            //验证码错误
        }

        //1.获取用户名和密码
        Map<String, String[]> map = request.getParameterMap();

        //2.封装user对象
        User user = new User();
        try {

            BeanUtils.populate(user,map);
            /*BeanUtils.populate( Object user, Map map)，
                这个方法会遍历map<key, value>中的key，如果user中有这个属性，就把这个key对应的value值赋给user的属性。
                */
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        request.getSession().setAttribute("user",user);

        //3.调用service查询
        UserService service =new UserServiceImpl();
        User u = service.login(user);
        //4.判断用户对象是否为null
        ResultInfo info = new ResultInfo();
        if (u == null){
            //用户名或密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误!");
        }
        //判断用户是否激活
        if (u !=null && !"Y".equals(u.getStatus())){
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活!");
        }
        //6.判断登录成功
        if (u !=null && "Y".equals(u.getStatus())){
            request.getSession().setAttribute("user",u);//登录成功标记
            info.setFlag(true);
        }
/*        //相应数据
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        //将map集合转为json，并且从Servlet传递给客户端
        mapper.writeValue(response.getOutputStream(),info);*/

        //将info对象序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);

        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  //取代了findUserServlet
        //1.从session中获取登录用户
        Object user = request.getSession().getAttribute("user");
        //将user写回客户端。
        //将user对象序列化为json

        ObjectMapper mapper = new ObjectMapper();
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        //将json数据写回客户端
        mapper.writeValue(response.getWriter(),user);

    }
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {//取代了exitServlet
        //销毁session
        request.getSession().invalidate(); //销毁session
        //2.跳转到登录页面
        response.sendRedirect(request.getContextPath()+"/login.html");

    }

    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {//取代了 activeUserServlet
        //1.获取激活码
        String code = request.getParameter("code");
        //3.判断标记
        String msg = null;
        //2.调用service完成激活
        //UserService service = new UserServiceImpl();
        boolean flag = service.active(code);


        if(flag != false){
            //激活成功
            //request.getRequestDispatcher("login.html").forward(request,response);
            msg = "激活成功，请<a href='http://localhost:8080/travel/login.html'>登录</a>";
        }else{
            //激活失败
            msg = "激活失败，请联系管理员!";
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(msg);


    }

}
