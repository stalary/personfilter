package com.stalary.personfilter.data.entity.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @model Message
 * @description 站内信对象
 * @field fromId 发送方id，系统发送则id为0
 * @field toId 接收方id
 * @field title 站内信标题
 * @field content 内容
 * @field readState 是否已阅
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
@Entity
public class Message extends BaseEntity {

    private Long fromId;

    private Long toId;

    private String title;

    private String content;

    private Boolean readState = false;
}