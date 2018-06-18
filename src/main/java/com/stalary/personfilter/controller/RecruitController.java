package com.stalary.personfilter.controller;

import com.stalary.personfilter.annotation.LoginRequired;
import com.stalary.personfilter.data.dto.SendResume;
import com.stalary.personfilter.data.entity.mysql.Recruit;
import com.stalary.personfilter.data.vo.RecruitAndCompany;
import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.service.mysql.RecruitService;
import com.stalary.personfilter.service.outer.MapdbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RecruitController
 *
 * @author lirongqian
 * @since 2018/04/17
 */
@Api(tags = "招聘操作接口")
@RestController
@RequestMapping("/recruit")
@Slf4j
public class RecruitController {

    @Autowired
    private RecruitService recruitService;

    @Autowired
    private MapdbService mapdbService;

    @PostMapping
    @ApiOperation(value = "添加或更新招聘信息", notes = "传入招聘对象")
    public ResponseMessage add(
            @RequestBody Recruit recruit) {
        Recruit saveRecruit = recruitService.saveRecruit(recruit);
        return ResponseMessage.successMessage(saveRecruit);
    }

    @DeleteMapping
    @ApiOperation(value = "删除招聘信息", notes = "传入招聘信息的id")
    public ResponseMessage delete(
            @RequestParam Long id) {
        recruitService.deleteById(id);
        return ResponseMessage.successMessage();
    }

    @GetMapping
    @ApiOperation(value = "查看所有招聘信息", notes = "分页默认1页4条，传入key则按关键字搜索")
    public ResponseMessage allRecruit(
            @RequestParam(required = false, defaultValue = "") String key,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "4") int size) {
        Pair<List<RecruitAndCompany>, Integer> pair = recruitService.allRecruit(key, page, size);
        Map<String, Object> map = new HashMap<>(2);
        map.put("total", pair.getValue1());
        map.put("recruitList", pair.getValue0());
        return ResponseMessage.successMessage(map);
    }

    /**
     * 投递简历的步骤
     * 1 投递简历，在mapdb中构成投递表
     * 2 向hr发送简历接收通知(站内信，邮件)
     * 3 向投递者发送简历投递成功的通知
     * 4 向hr和投递者push更新后的未读通知数量
     * @return
     */
    @PostMapping("/resume")
    @ApiOperation(value = "投递简历", notes = "需要传入岗位id")
    @LoginRequired
    public ResponseMessage postResume(
            @RequestBody SendResume sendResume) {
        log.info("sendResume" + sendResume);
        mapdbService.postResume(sendResume.getRecruitId(), sendResume.getTitle());
        return ResponseMessage.successMessage("投递成功");
    }

    @GetMapping("/send")
    @ApiOperation(value = "投递列表", notes = "查看个人投递列表")
    @LoginRequired
    public ResponseMessage getSendList() {
        return ResponseMessage.successMessage(mapdbService.getSendList());
    }

    @GetMapping("/receive")
    @ApiOperation(value = "简历列表", notes = "查看获取的简历列表")
    @LoginRequired
    public ResponseMessage getReceiveList() {
        return ResponseMessage.successMessage(mapdbService.getReceiveList());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查看招聘信息", notes = "传入岗位id")
    public ResponseMessage getInfo(
            @PathVariable("id") Long id) {
        return ResponseMessage.successMessage(recruitService.getRecruitInfo(id));
    }

    @GetMapping("/hr")
    @ApiOperation(value = "查看当前hr的招聘信息")
    @LoginRequired
    public ResponseMessage getHrInfo() {
        return ResponseMessage.successMessage(recruitService.findByUserId(UserHolder.get().getId()));
    }
}