package com.stalary.personfilter.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SendResume
 * 投递简历
 * @author lirongqian
 * @since 2018/04/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendResume {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 岗位id
     */
    private Long recruitId;
}