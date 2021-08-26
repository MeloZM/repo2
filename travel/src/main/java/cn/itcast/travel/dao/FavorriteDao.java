package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavorriteDao {

    /*
    * 根据rid和uid查询收藏信息
    * */
    public Favorite findByRidAndUid(int rid, int uid);
/*
* 根据rid查询收藏次数
* */

    int findCountByRid(int rid);

    void add(int rid, int uid);
}
