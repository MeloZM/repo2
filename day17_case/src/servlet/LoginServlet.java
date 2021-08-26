package servlet;

import domain.User2;
import org.apache.commons.beanutils.BeanUtils;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(request);
        //1.设置编码
        request.setCharacterEncoding("utf-8");
        //2.获取数据
        String verifycode = request.getParameter("verifycode"); //验证码
        //2.1获取生成的验证码
        HttpSession session = request.getSession();
        String checkCode_session = (String) session.getAttribute("CHECKCODE_SERVER");
        //删除session中存储的验证码
        session.removeAttribute("checkCode_session");
        if (! checkCode_session.equalsIgnoreCase(verifycode)){//验证码不正确
         request.setAttribute("login_msg","验证码错误！");
         request.getRequestDispatcher("/login.jsp").forward(request,response);
         return;

        }
        Map<String, String[]> map = request.getParameterMap();
        //3.封装User对象
        User2 user2 = new User2();
        try {
            BeanUtils.populate(user2,map);
            /*BeanUtils.populate( Object user, Map map)，
                这个方法会遍历map<key, value>中的key，如果user中有这个属性，就把这个key对应的value值赋给user的属性。
            */
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //4.调用service查询
        UserService service = new UserServiceImpl();
        User2 loginUser = service.login(user2);
        //5.判断是否登陆成功
        if (loginUser != null){
            //登陆成功
            //将用户存入Session
            session.setAttribute("LoginUser",loginUser);
            //跳转页面
             response.sendRedirect(request.getContextPath()+"/index.jsp");
            //request.getRequestDispatcher("/index.jsp").forward(request,response);

        }else {
            //登录失败
            //提示信息
            request.setAttribute("login_msg","用户名或密码错误！");
            //跳转登录页面
            request.getRequestDispatcher("/login.jsp").forward(request,response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
