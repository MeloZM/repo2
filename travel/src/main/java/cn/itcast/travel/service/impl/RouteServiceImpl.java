package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavorriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImageDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavorriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImageDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService{
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImageDao routeImageDao = new RouteImageDaoImpl();
    private SellerDao sellerDao= new SellerDaoImpl();
    private FavorriteDao favorriteDao = new FavorriteDaoImpl();
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {

        //封装pageBean
        PageBean<Route> pb = new PageBean<Route>();
        pb.setCurrentPage(currentPage); //当前页码
        pb.setPageSize(pageSize);  //每页显示的条数
        //设置总的记录数
        int totalCount =routeDao.findTotalCount(cid,rname) ;
        pb.setTotalCount(totalCount);

        //设置当前页面的数据集合
        int start = (currentPage - 1) * pageSize;  //每页开始的记录数
        List<Route> list = routeDao.findByPage(cid,start,pageSize,rname);
        pb.setList(list);
        //设置总页数
        int totalPage = (totalCount % pageSize) == 0 ? (totalCount/pageSize) : (totalCount/pageSize + 1);
        pb.setTotalPage(totalPage);
        return pb;
    }

    @Override
    public Route findOne(String rid) {
        //1.根据rid 去route表中查询route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));
        //2.根据route的rid 查询图片集合
        List<RouteImg> routeImgList =routeImageDao.findByRid(route.getRid());
        //2.2将集合设置到route对象
        route.setRouteImgList(routeImgList);
        //3.根据route的sid查询商家对象
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);
        //4. 查询收藏次数
       int count =  favorriteDao.findCountByRid(route.getRid());
       route.setCount(count);
        return route;
    }
}
