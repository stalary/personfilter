package com.stalary.personfilter.data.vo;

import com.stalary.personfilter.data.dto.HR;
import com.stalary.personfilter.data.entity.mysql.Company;
import com.stalary.personfilter.data.entity.mysql.Recruit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @model RecruitAndHrAndCompany
 * @description 招聘详细信息对象
 * @field recruit 招聘信息(见Recruit)
 * @field hr 招聘信息(见HR)
 * @field company 招聘信息(见Company)
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruitAndHrAndCompany {

    private Recruit recruit;

    private HR hr;

    private Company company;
}