package com.stalary.personfilter.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RecruitLow
 *
 * @author lirongqian
 * @since 2018/04/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruitLow {

    /**
     * 岗位id
     */
    private Long id;

    /**
     * 岗位标题
     */
    private String title;
}