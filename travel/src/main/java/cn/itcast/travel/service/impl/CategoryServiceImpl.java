package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();
    @Override
    public List<Category> findAll() {
        //1.从redis中查询
        Jedis jedis = JedisUtil.getJedis();
        //按照uid的顺序存储，用sortedset存储
        //1.查询sortedset中的（cid）和（cname）
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);

        List<Category> cs = null;
        //2.判断查询的集合是否为空
        if (categorys == null || categorys.size() == 0){
            System.out.println("redis为空，走数据库");
            //3.如果为空，需要从数据库查询，将数据存入redis
            //3.1 从数据库查询
            cs = categoryDao.findAll();
            //3.2 将集合存到redis 的 category缓存中
            for (int i= 0; i < cs.size(); i++) {
                jedis.zadd("category",cs.get(i).getCid(),cs.get(i).getCname());
            }

        }else {
            System.out.println("redis不为空，走redis");
            //redis中有数据
            cs = new ArrayList<Category>();
            for(Tuple c: categorys){
                Category category = new Category();
                category.setCname(c.getElement());
                category.setCid((int)c.getScore());
                cs.add(category);
            }

        }
        //4.如果不为空，直接返回。
        return cs;
    }
}
