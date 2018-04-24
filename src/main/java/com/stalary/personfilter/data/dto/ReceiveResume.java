package com.stalary.personfilter.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ReceiveResume
 * 接收简历
 * @author lirongqian
 * @since 2018/04/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveResume {

    /**
     * 岗位标题
     */
    private String title;

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 简历匹配度
     */
    private Integer rate;

    /**
     * 接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
}