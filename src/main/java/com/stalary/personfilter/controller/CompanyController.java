package com.stalary.personfilter.controller;

import com.stalary.personfilter.data.dto.ResponseMessage;
import com.stalary.personfilter.data.entity.mysql.Company;
import com.stalary.personfilter.service.mysql.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * CompanyController
 *
 * @author lirongqian
 * @since 2018/04/14
 */
@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResponseMessage allCompany(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "4") int size) {
        return ResponseMessage.successMessage(companyService.allCompany(page, size));
    }

    @PostMapping ResponseMessage addCompany(
            @RequestBody Company company) {
        return ResponseMessage.successMessage(companyService.save(company));
    }
}