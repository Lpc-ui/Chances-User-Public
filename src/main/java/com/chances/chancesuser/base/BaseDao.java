package com.chances.chancesuser.base;

import org.springframework.data.domain.Example;
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
}
