package com.stalary.personfilter.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * @author Stalary
 * @description
 * @date 2018/4/13
 */
@NoRepositoryBean
public interface BaseRepo<T, Long extends Serializable> extends MongoRepository<T, Long> {

}
