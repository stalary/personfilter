package com.stalary.personfilter.data.entity.mysql;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Message
 * 站内信
 * @author lirongqian
 * @since 2018/04/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
@Entity
public class Message extends BaseEntity {

    /**
     * 发送方id
     */
    private Long fromId;

    /**
     * 接收方id
     */
    private Long toId;

    /**
     * 站内信标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否已阅,0未阅，1已阅，2忽略
     */
    @ApiModelProperty(value = "false")
    private Boolean readState;
}