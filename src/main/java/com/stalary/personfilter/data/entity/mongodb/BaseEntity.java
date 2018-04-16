package com.stalary.personfilter.data.entity.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stalary.personfilter.annotation.AutoValue;
import com.stalary.personfilter.annotation.CreateTime;
import com.stalary.personfilter.annotation.UpdateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * BaseEntity
 *
 * @author lirongqian
 * @since 2018/04/13
 */
@Data
public abstract class BaseEntity {

    /**
     * 唯一id
     */
    @Id
    @Field("_id")
    @AutoValue
    private long id;

    /**
     * 修改时间
     */
    @UpdateTime
    @JsonIgnore
    private LocalDateTime updateTime;

    @CreateTime
    @JsonIgnore
    private LocalDateTime createTime;

}