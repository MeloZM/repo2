package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavorriteDao;
import cn.itcast.travel.dao.impl.FavorriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavorriteService;

public class FavorriteServiceImpl implements FavorriteService {
    private FavorriteDao favorriteDao = new FavorriteDaoImpl();

    @Override
    public boolean isFavorrite(String rid, int uid) {
        Favorite favorite = favorriteDao.findByRidAndUid(Integer.parseInt(rid), uid);

        return favorite != null;
    }

    @Override
    public void add(String rid, int uid) {
        favorriteDao.add(Integer.parseInt(rid),uid);
    }
}
