package com.stalary.personfilter.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    /**
     * 投递时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
}