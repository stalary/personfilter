package com.stalary.personfilter.controller;

import com.stalary.personfilter.annotation.LoginRequired;
import com.stalary.personfilter.data.dto.SendResume;
import com.stalary.personfilter.data.entity.mysql.Recruit;
import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.service.mongodb.ResumeService;
import com.stalary.personfilter.service.mysql.RecruitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * RecruitController
 *

 */
/**
 * @model RecruitController
 * @description 招聘相关接口
 * @author lirongqian
 * @since 2018/04/17
 **/
@RestController
@RequestMapping("/recruit")
@Slf4j
public class RecruitController {

    @Resource
    private RecruitService recruitService;

    @Resource
    private ResumeService resumeService;

    /**
     * @method add 添加招聘信息
     * @param recruit 招聘信息对象
     * @return Recruit 招聘信息对象
     **/
    @PostMapping
    public ResponseMessage add(
            @RequestBody Recruit recruit) {
        Recruit saveRecruit = recruitService.saveRecruit(recruit);
        return ResponseMessage.successMessage(saveRecruit);
    }

    /**
     * @method delete 删除招聘信息
     * @param id 招聘信息id
     **/
    @DeleteMapping
    public ResponseMessage delete(
            @RequestParam Long id) {
        recruitService.deleteById(id);
        return ResponseMessage.successMessage();
    }

    /**
     * @method allRecruit 查看所有招聘信息
     * @param key 关键字
     * @param page 当前页数
     * @param size 每页数据量
     * @return RecruitAndCompany 招聘信息
     **/
    @GetMapping
    public ResponseMessage allRecruit(
            @RequestParam(required = false, defaultValue = "") String key,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "4") int size) {
        return ResponseMessage.successMessage(recruitService.allRecruit(key, page, size));
    }

    /**
     * 投递简历的步骤
     * 1 投递简历，在mapdb中构成投递表
     * 2 向hr发送简历接收通知(站内信，邮件)
     * 3 向投递者发送简历投递成功的通知
     * 4 向hr和投递者push更新后的未读通知数量
     *
     */
    /**
     * @method postResume 投递简历
     * @param sendResume 投递简历对象
     **/
    @PostMapping("/resume")
    @LoginRequired
    public ResponseMessage postResume(
            @RequestBody SendResume sendResume) {
        log.info("sendResume" + sendResume);
        resumeService.postResume(sendResume.getRecruitId(), sendResume.getTitle());
        return ResponseMessage.successMessage("投递成功");
    }

    /**
     * @method getSendList 查看个人投递列表
     * @return SendInfo 投递信息
     **/
    @GetMapping("/send")
    @LoginRequired
    public ResponseMessage getSendList() {
        return ResponseMessage.successMessage(resumeService.getSendList());
    }

    /**
     * @method getReceiveList 查看获取的简历列表
     * @return
     **/
    @GetMapping("/receive")
    @LoginRequired
    public ResponseMessage getReceiveList() {
        return ResponseMessage.successMessage(resumeService.getReceiveList());
    }

    /**
     * @method getInfo 查看招聘信息
     * @param id 岗位id
     * @return RecruitAndHrAndCompany 招聘信息
     **/
    @GetMapping("/{id}")
    public ResponseMessage getInfo(
            @PathVariable("id") Long id) {
        return ResponseMessage.successMessage(recruitService.getRecruitInfo(id));
    }

    /**
     * @method getHrInfo 获取当前hr的招聘信息
     * @return Recruit 招聘信息
     **/
    @GetMapping("/hr")
    @LoginRequired
    public ResponseMessage getHrInfo() {
        return ResponseMessage.successMessage(recruitService.findByUserId(UserHolder.get().getId()));
    }
}