package com.stalary.personfilter.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

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