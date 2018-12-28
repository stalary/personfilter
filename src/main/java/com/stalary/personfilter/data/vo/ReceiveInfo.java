package com.stalary.personfilter.data.vo;

import com.stalary.personfilter.data.dto.ReceiveResume;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @model ReceiveInfo
 * @description 简历列表
 * @field receiveList 收到的简历列表(见ReceiveResume)
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveInfo {

    private List<ReceiveResume> receiveList = new ArrayList<>();
}