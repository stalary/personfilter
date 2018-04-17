package com.stalary.personfilter.repo.mysql;

import com.stalary.personfilter.data.entity.mysql.Message;

import java.util.List;

/**
 * @author Stalary
 * @description
 * @date 2018/4/17
 */
public interface MessageRepo extends BaseRepo<Message, Long> {

    /**
     * 通过toId查找接收的站内信
     * @param toId
     * @return
     */
    List<Message> findByToId(Long toId);

    /**
     * 通过fromId查找发送的站内信
     */
    List<Message> findByFromId(Long fromId);
}
