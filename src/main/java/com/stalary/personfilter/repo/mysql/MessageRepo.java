package com.stalary.personfilter.repo.mysql;

import com.stalary.personfilter.data.entity.mysql.Message;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Stalary
 * @description
 * @date 2018/4/17
 */
@Transactional(rollbackFor = Exception.class)
@Repository
public interface MessageRepo extends BaseRepo<Message, Long> {

    /**
     * 通过toId查找接收的站内信
     * @param toId
     * @return
     */
    List<Message> findByToIdOrderByCreateTimeDesc(Long toId);

    /**
     * 通过fromId查找发送的站内信
     */
    List<Message> findByFromId(Long fromId);

    /**
     * 设置状态为已读
     * @param id
     */
    @Modifying
    @Query("update Message m set m.readState=true where m.id=?1")
    void read(Long id);

    /**
     * 查找收到的未读通知
     * @param fromId
     * @param readState
     * @return
     */
    List<Message> findByToIdAndReadState(Long fromId, Boolean readState);
}
