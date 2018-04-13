package com.stalary.personfilter.data.dto;

import com.stalary.personfilter.utils.PFUtil;
import lombok.*;

/**
 * Skill
 *
 * @author lirongqian
 * @since 2018/04/13
 */
@NoArgsConstructor
@ToString
public class Skill {

    /**
     * 技能名称
     */
    @Getter
    @Setter
    private String name;

    /**
     * 熟练程度，分为了解，熟悉，掌握，精通，分别对应1，2，3，4(为了方便计算分数)
     */
    private int level;

    public String getLevel() {
        switch (this.level) {
            case 1:
                return PFUtil.KNOW;
            case 2:
                return PFUtil.FAMILIAR;
            case 3:
                return PFUtil.CONTROL;
            case 4:
                return PFUtil.MASTER;
            default:
                return null;
        }
    }

    private void setLevel(String level) {
        switch (level) {
            case PFUtil.KNOW:
                this.level = 1;
                break;
            case PFUtil.FAMILIAR:
                this.level = 2;
                break;
            case PFUtil.CONTROL:
                this.level = 3;
                break;
            case PFUtil.MASTER:
                this.level = 4;
                break;
            default:
                this.level = 0;
                break;
        }
    }

    public Skill(String name, String level) {
        switch (level) {
            case PFUtil.KNOW:
                this.level = 1;
                break;
            case PFUtil.FAMILIAR:
                this.level = 2;
                break;
            case PFUtil.CONTROL:
                this.level = 3;
                break;
            case PFUtil.MASTER:
                this.level = 4;
                break;
            default:
                this.level = 0;
                break;
        }
        this.name = name;
    }


}