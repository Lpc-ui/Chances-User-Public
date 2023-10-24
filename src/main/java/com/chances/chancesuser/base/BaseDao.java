package com.chances.chancesuser.base;

import com.chances.chancesuser.utils.BeanCopyUtil;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 公共持久层
 */
public interface BaseDao<T extends BaseModel> extends JpaRepository<T, Long> {
    /**
     * 保存实体到数据库
     *
     * @param entity 实体
     */
    default void insert(T entity) {
        this.save(entity);
    }

    /**
     * 保存多个实体到数据库
     *
     * @param entities 多个实体
     */
    default void insert(Iterable<T> entities) {
        this.saveAll(entities);
    }

    /**
     * 根据ID查询
     *
     * @param id 表主健
     * @return 对应实体
     */
    default T getById(Long id) {
        return this.findById(id).orElse(null);
    }

    /**
     * 根据Ids查询
     *
     * @param ids 多个id
     * @return 实体集合
     */
    default List<T> getByIds(Iterable<Long> ids) {
        return this.findAllById(ids);
    }

    /**
     * 根据实体获取单个实体:存在多个报错
     *
     * @param entity 设置属性值的实体:按照属性值进行and查询
     * @return 实体集合
     */
    default T getOneByEntity(T entity) {
        return this.findOne(Example.of(entity)).orElse(null);
    }

    /**
     * 条件分页
     * 新建实体继承PageBo;设置条件
     * 可进行多字段排序
     *
     * @param entityPageBo 查询BO
     * @param entityClass  数据库实体类对象
     * @param sortProperty 排序字段,默认ASC 排序格式:  Sort.Order.desc("property"); Sort.Order.asc("property");
     * @param <V>          封装的Vo继承自PageBo
     * @return PageJson
     */
    @SuppressWarnings("all")
    default <V extends PageBo> PageJson<T> conditionPage(V entityPageBo, Class<T> entityClass, Sort.Order... sortProperty) throws Exception {
        return conditionPage(entityPageBo, entityClass, null, sortProperty);
    }

    /**
     * 条件分页
     * 新建实体继承PageBo;设置条件
     * 可进行多字段排序
     *
     * @param entityPageBo 查询BO
     * @param entityClass  数据库实体类对象
     * @param sortProperty 排序字段,默认ASC 排序格式:  Sort.Order.desc("property"); Sort.Order.asc("property");
     * @param <V>          封装的Vo继承自PageBo
     * @param matching     规则匹配:可由ExampleMatcher.matching()创建
     * @return PageJson
     */
    @SuppressWarnings("all")
    default <V extends PageBo> PageJson<T> conditionPage(V entityPageBo, Class<T> entityClass, ExampleMatcher matching, Sort.Order... sortProperty) throws Exception {
        T t = entityClass.newInstance();
        BeanCopyUtil.copyProperties(entityPageBo, t);
        Sort sortAll = Sort.by(sortProperty);
        PageRequest pageRequest = PageRequest.of(entityPageBo.getPageNum(), entityPageBo.getPageSize(), sortAll);
        Page<T> page = matching == null ? this.findAll(Example.of(t), pageRequest) : this.findAll(Example.of(t, matching), pageRequest);
        return new PageJson<T>().setPageTotal(page.getTotalPages()).setJsonList(page.getContent()).setPageNum(entityPageBo.getPageNum()).setPageSize(entityPageBo.getPageSize()).setDataTotal(page.getTotalElements());
    }
}
