package com.stalary.personfilter.controller;

import com.stalary.personfilter.data.dto.ResponseMessage;
import com.stalary.personfilter.data.entity.mysql.Recruit;
import com.stalary.personfilter.service.mysql.RecruitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * RecruitController
 *
 * @author lirongqian
 * @since 2018/04/17
 */
@Api(tags = "招聘操作接口")
@RestController
@RequestMapping("/recruit")
public class RecruitController {

    @Autowired
    private RecruitService recruitService;

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
        return ResponseMessage.successMessage(recruitService.allRecruit(key, page, size));
    }

}