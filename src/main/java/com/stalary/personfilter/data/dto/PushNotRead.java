package com.stalary.personfilter.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * PushNotRead
 *
 * @author lirongqian
 * @since 2018/04/23
 */
@Data
@AllArgsConstructor
public class PushNotRead {

    /**
     * 关联的userId
     */
    private Long userId;

    /**
     * 未读消息数量
     */
    private Integer count;
}