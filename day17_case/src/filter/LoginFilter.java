package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/*
*登录验证的过滤器
*/
@WebFilter("/*")
public class LoginFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println(req);
       //0.强制转化
        HttpServletRequest request = (HttpServletRequest)req;
        //1.获取资源的请求路径(当前页面的连接)
        String uri = request.getRequestURI();
        if (uri.contains("/login.jsp") || uri.contains("/LoginServlet") ||uri.contains("/css/")|| uri.contains("/js/")||uri.contains("/fonts/")||uri.contains("/CheckCodeServlet")){
            //用户就是进去登录页面,放行
            chain.doFilter(req,resp);
        }else {
            //不是登录请求，需验证用户是否登录
            //3.从session中获取user
            Object user = request.getSession().getAttribute("LoginUser");
            if (user != null){
                //已经登陆了，可以访问内部页面，放行
                chain.doFilter(req,resp);
            }else{
                //没有登录，还访问内部页面，应跳转到登录页面
                request.setAttribute("login_msg","您尚未登录，请登录");
                request.getRequestDispatcher("/login.jsp").forward(request,resp);
            }
        }
        //1.chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

    public void destroy() {
    }

}
