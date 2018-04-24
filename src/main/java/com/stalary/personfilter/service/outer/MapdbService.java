package com.stalary.personfilter.service.outer;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.stalary.personfilter.data.dto.ReceiveResume;
import com.stalary.personfilter.data.dto.SendResume;
import com.stalary.personfilter.data.entity.mysql.UserInfo;
import com.stalary.personfilter.data.vo.ReceiveInfo;
import com.stalary.personfilter.data.vo.SendInfo;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.service.kafka.Producer;
import com.stalary.personfilter.service.mongodb.ResumeService;
import com.stalary.personfilter.service.mysql.RecruitService;
import com.stalary.personfilter.service.mysql.UserService;
import lombok.extern.slf4j.Slf4j;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.stalary.personfilter.utils.Constant.*;

/**
 * MapdbService
 *
 * @author lirongqian
 * @since 2018/04/18
 */
@Service
@Slf4j
public class MapdbService {

    @Autowired
    private Gson gson;

    @Autowired
    private Producer producer;

    @Autowired
    private UserService userService;

    @Autowired
    private RecruitService recruitService;

    @Autowired
    private ResumeService resumeService;

    /**
     * 投递简历
     */
    public void postResume(Long recruitId, String title) {
        Long userId = UserHolder.get().getId();
        String json = gson.toJson(new SendResume(userId, recruitId, title, LocalDateTime.now()));
        // 投递简历
        producer.send(SEND_RESUME, HANDLE_RESUME, json);
        // 向接受方发送通知
        producer.send(SEND_RESUME, RECEIVE, json);
        // 向投递方发送通知
        producer.send(SEND_RESUME, SEND, json);
    }
    /**
     * 处理简历
     */
    public void handleResume(SendResume sendResume) {
        // 最大容量1GB，最大增速256MB
        DB db = DBMaker.fileDB("mapdb.txt")
                .checksumHeaderBypass()
                .allocateStartSize(1024 * 1024 * 1024)
                .allocateIncrement(256 * 1024 * 1024)
                .make();
        // 构建投递列表
        HTreeMap<String, String> post = db.
                hashMap("post_map", Serializer.STRING, Serializer.STRING)
                .createOrOpen();
        Long userId = sendResume.getUserId();
        Long recruitId = sendResume.getRecruitId();
        String postStr = post.get(getKey(SEND, userId.toString()));
        // 获取map，存在时，加入投递列表，不存在时，初始化
        if (postStr != null) {
            SendInfo sendInfo = gson.fromJson(postStr, SendInfo.class);
            sendInfo.getSendList().add(sendResume);
            post.put(getKey(SEND, userId.toString()), gson.toJson(sendInfo));
        } else {
            SendInfo sendInfo = new SendInfo(Lists.newArrayList(sendResume));
            post.put(getKey(SEND, userId.toString()), gson.toJson(sendInfo));
        }
        // 构建获取列表
        HTreeMap<String, String> get = db.
                hashMap("get_map", Serializer.STRING, Serializer.STRING)
                .createOrOpen();
        String getStr = get.get(getKey(RECEIVE, recruitId.toString()));
        UserInfo userInfo = userService.findOne(userId);
        // 计算匹配度
        int rate = resumeService.calculate(recruitId, userId);
        ReceiveResume resume = new ReceiveResume(sendResume.getTitle(), userInfo.getNickname(), rate, LocalDateTime.now());
        if (getStr != null) {
            ReceiveInfo receiveInfo = gson.fromJson(getStr, ReceiveInfo.class);
            receiveInfo.getReceiveList().add(resume);
            get.put(getKey(RECEIVE, recruitId.toString()), gson.toJson(receiveInfo));
        } else {
            ReceiveInfo receiveInfo = new ReceiveInfo(Lists.newArrayList(resume));
            post.put(getKey(RECEIVE, recruitId.toString()), gson.toJson(receiveInfo));
        }
        db.close();
    }

    /**
     * 获取投递简历列表
     *
     * @return
     */
    public SendInfo getSendList() {
        DB db = DBMaker.fileDB("mapdb.txt")
                .checksumHeaderBypass()
                .allocateStartSize(1024 * 1024 * 1024)
                .allocateIncrement(256 * 1024 * 1024)
                .make();
        HTreeMap<String, String> map = db.
                hashMap("post_map", Serializer.STRING, Serializer.STRING)
                .createOrOpen();
        Long userId = UserHolder.get().getId();
        String result = map.get(getKey(SEND, userId.toString()));
        db.close();
        return gson.fromJson(result, SendInfo.class);
    }

    /**
     * 获取收到简历列表
     */
    public ReceiveInfo getReceiveList() {
        DB db = DBMaker.fileDB("mapdb.txt")
                .checksumHeaderBypass()
                .allocateStartSize(1024 * 1024 * 1024)
                .allocateIncrement(256 * 1024 * 1024)
                .make();
        HTreeMap<String, String> map = db.
                hashMap("get_map", Serializer.STRING, Serializer.STRING)
                .createOrOpen();
        Long userId = UserHolder.get().getId();
        ReceiveInfo receiveInfo = new ReceiveInfo();
        List<ReceiveResume> receiveList = receiveInfo.getReceiveList();
        // 存入所有收到的简历
        recruitService
                .findByUserId(userId)
                .forEach(recruit -> {
                    String result = map.get(getKey(RECEIVE, recruit.getId().toString()));
                    ReceiveInfo getInfo = gson.fromJson(result, ReceiveInfo.class);
                    receiveList.addAll(getInfo.getReceiveList());
                });
        db.close();
        return receiveInfo;
    }
}