package com.stalary.personfilter.service.mapdb;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.stalary.personfilter.data.dto.RecruitLow;
import com.stalary.personfilter.data.dto.SendInfo;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.utils.Constant;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MapdbService
 *
 * @author lirongqian
 * @since 2018/04/18
 */
@Service
public class MapdbService {

    @Autowired
    private Gson gson;

    /**
     * 投递简历
     * @param recruitId
     * @param title
     * @return
     */
    public SendInfo postResume(Long recruitId, String title) {
        DB db = DBMaker.fileDB("mapdb.txt")
                .checksumHeaderBypass()
                .allocateStartSize(1024 * 1024 * 1024)
                .allocateIncrement(256 * 1024 * 1024)
                .make();
        // 最大容量1GB，最大增速256MB
        HTreeMap<String, String> map = db.
                hashMap("recruit_map", Serializer.STRING, Serializer.STRING)
                .createOrOpen();
        Long userId = UserHolder.get().getId();
        String s = map.get(Constant.SENDINFO + Constant.SPLIT + userId);
        if (s != null) {
            SendInfo sendInfo = gson.fromJson(s, SendInfo.class);
            sendInfo.getRecruitLowList().add(new RecruitLow(recruitId, title));
            map.put(Constant.SENDINFO + Constant.SPLIT + userId, gson.toJson(sendInfo));
        } else {
            SendInfo sendInfo = new SendInfo(userId, Lists.newArrayList(new RecruitLow(recruitId, title)));
            map.put(Constant.SENDINFO + Constant.SPLIT + userId, gson.toJson(sendInfo));
        }
        String result = map.get(Constant.SENDINFO + Constant.SPLIT + userId);
        map.close();
        return gson.fromJson(result, SendInfo.class);
    }

    /**
     * 获取所有简历
     * @return
     */
    public SendInfo getAll() {
        DB db = DBMaker.fileDB("mapdb.txt")
                .checksumHeaderBypass()
                .allocateStartSize(1024 * 1024 * 1024)
                .allocateIncrement(256 * 1024 * 1024)
                .make();
        HTreeMap<String, String> map = db.
                hashMap("recruit_map", Serializer.STRING, Serializer.STRING)
                .open();
        Long userId = UserHolder.get().getId();
        String result = map.get(Constant.SENDINFO + Constant.SPLIT + userId);
        map.close();
        return gson.fromJson(result, SendInfo.class);
    }
}