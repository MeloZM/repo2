package cn.itcast.travel.service;

public interface FavorriteService {

    /*
    * 判断是否收藏
    * */
    public boolean isFavorrite(String rid, int uid);

    void add(String rid, int uid);
}
