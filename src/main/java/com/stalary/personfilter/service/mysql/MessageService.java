package com.stalary.personfilter.service.mysql;

import com.stalary.personfilter.data.entity.mysql.Message;
import com.stalary.personfilter.repo.mysql.MessageRepo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MessageService
 *
 * @author lirongqian
 * @since 2018/04/17
 */
@Service
public class MessageService extends BaseService<Message, MessageRepo> {

    MessageService(MessageRepo repo) {
        super(repo);
    }

    public List<Message> findByToId(Long toId) {
        return repo.findByToId(toId);
    }

    public List<Message> findByFromId(Long fromId) {
        return repo.findByFromId(fromId);
    }
}