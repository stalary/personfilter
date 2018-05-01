package com.stalary.personfilter.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ReadMessage
 *
 * @author lirongqian
 * @since 2018/04/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadMessage {

    private Long id;

    private Long userId;
}