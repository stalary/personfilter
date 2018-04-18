package com.stalary.personfilter.controller;

import com.google.gson.Gson;
import com.stalary.personfilter.annotation.LoginRequired;
import com.stalary.personfilter.data.entity.mysql.Recruit;
import com.stalary.personfilter.data.vo.RecruitAndHrAndCompany;
import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.service.mapdb.MapdbService;
import com.stalary.personfilter.service.mysql.RecruitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private Gson gson;

    @Autowired
    private MapdbService mapdbService;

    @PostMapping
    @ApiOperation(value = "添加或更新招聘信息", notes = "传入招聘对象")
    public ResponseMessage add(
            @RequestBody Recruit recruit) {
        return ResponseMessage.successMessage(recruitService.saveRecruit(recruit));
    }

    @GetMapping
    @ApiOperation(value = "查看所有招聘信息", notes = "分页默认1页4条，传入key则按关键字搜索")
    public ResponseMessage allRecruit(
            @RequestParam(required = false, defaultValue = "") String key,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "4") int size) {
        List<RecruitAndHrAndCompany> list = recruitService.allRecruit(key, page, size);
        log.info("list: " + list);
        return ResponseMessage.successMessage(list);
    }

    @PostMapping("/resume")
    @ApiOperation(value = "投递简历", notes = "需要传入岗位id")
    @LoginRequired
    public ResponseMessage postResume(
            @RequestParam Long recruitId,
            @RequestParam String title) {
        return ResponseMessage.successMessage(mapdbService.postResume(recruitId, title));
    }

    @GetMapping("/send")
    @LoginRequired
    public ResponseMessage getSendList() {
        return ResponseMessage.successMessage(mapdbService.getAll());
    }

}