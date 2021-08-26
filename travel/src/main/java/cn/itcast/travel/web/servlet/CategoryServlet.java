package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {
    /*
     * 查询所有
     *
     * */
    private CategoryService service = new CategoryServiceImpl();
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.调用service查询所有
        List<Category> list = service.findAll();
        System.out.println(list);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(list);
       // System.out.println(json);
        //解决json乱码
        response.setContentType("application/json;charset=utf-8");
        //将数据输出到客户端浏览器
        response.getWriter().write(json);

    }
    
}
