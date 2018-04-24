package com.stalary.personfilter.data.vo;

import com.stalary.personfilter.data.dto.ReceiveResume;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * ReceiveInfo
 *
 * @author lirongqian
 * @since 2018/04/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveInfo {

    private List<ReceiveResume> receiveList = new ArrayList<>();
}