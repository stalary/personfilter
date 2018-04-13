package com.stalary.personfilter.data.entity;

import com.stalary.personfilter.annotation.AutoValue;
import com.stalary.personfilter.annotation.CreateTime;
import com.stalary.personfilter.annotation.UpdateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
    private LocalDateTime updateTime;

    @CreateTime
    private LocalDateTime createTime;

}