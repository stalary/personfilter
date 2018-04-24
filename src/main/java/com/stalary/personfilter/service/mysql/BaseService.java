package com.stalary.personfilter.service.mysql;

import com.stalary.personfilter.repo.mysql.BaseRepo;

import java.util.List;
import java.util.Optional;

/**
 * BaseService
 *
 * @author lirongqian
 * @since 2018/04/14
 */
public abstract class BaseService<T, R extends BaseRepo<T, Long>> {

    protected R repo;

    BaseService(R repo) {
        this.repo = repo;
    }

    public T findOne(Long id) {
        return repo.findById(id).orElse(null);
    }

    public T save(T entity) {
        return repo.save(entity);
    }

    public void save(List<T> entityList) {
        repo.saveAll(entityList);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public void delete(T entity) {
        repo.delete(entity);
    }

}