package com.stalary.personfilter.controller;

import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.data.entity.mysql.Company;
import com.stalary.personfilter.service.mysql.CompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CompanyController
 *
 * @author lirongqian
 * @since 2018/04/14
 */
@Api(tags = "公司操作接口")
@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /**
     * 查找所有公司，分页
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    @ApiOperation(value = "查找所有公司", notes = "分页，一页4个")
    public ResponseMessage allCompany(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "4") int size) {
        Pair<List<Company>, Integer> pair = companyService.allCompany(page, size);
        Map<String, Object> map = new HashMap<>(2);
        map.put("total", pair.getValue1());
        map.put("companyList", pair.getValue0());
        return ResponseMessage.successMessage(map);
    }

    @GetMapping("/noPage")
    @ApiOperation(value = "不分页的查找公司", notes = "不分页查找所有公司，用于获取公司列表,传入key则按关键字查找")
    public ResponseMessage allCompanyNoPage(
            @RequestParam(required = false, defaultValue = "") String key) {
        return ResponseMessage.successMessage(companyService.findCompany(key));
    }

    /**
     * 添加公司
     * @param company
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加公司", notes = "传入公司对象")
    public ResponseMessage addCompany(
            @RequestBody Company company) {
        return ResponseMessage.successMessage(companyService.save(company));
    }

    /**
     * 查看公司详情包括招聘信息
     */
    @GetMapping("{id}")
    @ApiOperation(value = "查看公司详情", notes = "传入公司id")
    public ResponseMessage getInfo(
            @PathVariable("id") Long id) {
        return ResponseMessage.successMessage(companyService.getInfo(id));
    }
}