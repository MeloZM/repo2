package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavorriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavorriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();
    private FavorriteService favorriteService = new FavorriteServiceImpl();

    /*
    * 分页查询
    * */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //1.接收参数  转换成int
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("PageSize");
        String cidStr = request.getParameter("cid");

        //接收rname：实现查询功能
        String rname = request.getParameter("rname");   //乱码问题
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");//解决乱码问题

        int cid = 0; //类别id
        int currentPage = 0;  //当前页码
        int pageSize = 0; //每页的条数
        //2.处理参数
        if (cidStr !=null && cidStr.length()>0 && !"null".equals(cidStr) ){
            cid = Integer.parseInt(cidStr);
        }

        if (currentPageStr !=null && currentPageStr.length()>0){
            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }

        if (pageSizeStr !=null && pageSizeStr.length()>0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else {
            pageSize = 10;  //默认每页显示5条记录
        }
        //3.调用service查询pageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize,rname);
        System.out.println(pb.toString());

        //4.将pageBean对象序列化为json,返回
        //将info对象序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(pb);

        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);


    }

    /*
    * 根据id查询一个旅游线路的详情信息
    *
    * */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收id
        String rid = request.getParameter("rid");
        //2.调用sercvice查询route对象
         Route route = routeService.findOne(rid);
        //3.转为json写回客户端
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(route);

        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

    }
        /*
        * 判断当前用户登录是否收藏了该线路
       */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 获取线路id
        String rid = request.getParameter("rid");

        //2. 获取当前登录的用户 user
        User user =(User) request.getSession().getAttribute("user");

        int uid; //用户id
        if (user ==null){
            //用户尚未登录
            uid = 0;
        }else {
            //用户已登录
            uid = user.getUid();
        }
        //3. 调用FavorriteService查询是否收藏
        boolean flag = favorriteService.isFavorrite(rid, uid);
        //4.序列化为json,返回
        //将info对象序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(flag);

        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

    }

    /*
    * 添加收藏功能
    * */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取线路rid
        String rid = request.getParameter("rid");
        //2. 获取当前登录的用户
        User user =(User) request.getSession().getAttribute("user");

        int uid; //用户id
        if (user ==null){
            //用户尚未登录
            return;
        }else {
            //用户已登录
            uid = user.getUid();
        }
        //3.调用service添加
        favorriteService.add(rid,uid);

    }


}
