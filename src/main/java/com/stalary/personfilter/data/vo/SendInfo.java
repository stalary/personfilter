package com.stalary.personfilter.data.vo;

import com.stalary.personfilter.data.dto.SendResume;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SendInfo
 * 用户的投递信息
 * @author lirongqian
 * @since 2018/04/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendInfo {
    /**
     * 岗位列表
     */
    private List<SendResume> sendList;
}