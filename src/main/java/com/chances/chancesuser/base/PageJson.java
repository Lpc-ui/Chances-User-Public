package com.chances.chancesuser.base;

import com.chances.chancesuser.utils.BeanCopyUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 分页Json
 *
 * @param <T> json对象类
 * @author lipengcheng
 */
@Data
@Accessors(chain = true)//链式调用
public class PageJson<T> {
    //当前页的json集合
    private List<T> jsonList;
    //map结构
    private LinkedHashMap<String, List<T>> jsonListMap;
    //页面量
    private Integer pageSize;
    //当前页
    private Integer pageNum;
    //中页数
    private Integer pageTotal;
    //总记录数
    private Long dataTotal;


    /**
     * 如果T为数据库实体 转换成对应VO对象
     *
     * @param entityClass 目的类对象
     * @param <V>         目的类对象
     * @return pageJson
     */
    public <V> PageJson<V> convertVo(Class<V> entityClass) throws Exception {
        PageJson<V> vPageJson = new PageJson<>();
        ArrayList<V> vs = new ArrayList<>();
        for (T t : this.jsonList) {
            V v = entityClass.newInstance();
            BeanCopyUtil.copyProperties(t, v);
            vs.add(v);
        }
        vPageJson.setJsonList(vs);
        vPageJson.setJsonListMap(null);
        vPageJson.setPageSize(getPageSize());
        vPageJson.setPageNum(getPageNum());
        vPageJson.setPageTotal(getPageTotal());
        vPageJson.setDataTotal(getDataTotal());
        return vPageJson;
    }
}
