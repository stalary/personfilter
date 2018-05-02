package com.stalary.personfilter.data.entity.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.reflect.TypeToken;
import com.stalary.personfilter.data.dto.SkillRule;
import com.stalary.personfilter.factory.BeansFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Recruit
 * 招聘信息
 * @author lirongqian
 * @since 2018/04/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recruit")
@Entity
public class Recruit extends BaseEntity {

    /**
     * 关联的公司id
     */
    private Long companyId;

    /**
     * 关联的hr id
     */
    private Long hrId;

    /**
     * 招聘内容
     */
    private String content;

    /**
     * 招聘标题
     */
    private String title;

    /**
     * 需要的技能列表，对前端隐藏
     */
    @Transient
    private List<SkillRule> skillList;

    @JsonIgnore
    private String skillStr;

    @JsonIgnore
    public void serializeFields() {
        this.skillStr = BeansFactory.getGson().toJson(skillList);
    }

    @JsonIgnore
    public void deserializeFields() {
        this.skillList = BeansFactory.getGson().fromJson(skillStr, new TypeToken<List<SkillRule>>(){}.getType());
    }

}