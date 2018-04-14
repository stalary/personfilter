package com.stalary.personfilter.data.entity.mongodb;

import com.stalary.personfilter.utils.Constant;
import lombok.*;

/**
 * Skill
 *
 * @author lirongqian
 * @since 2018/04/13
 */
@NoArgsConstructor
@ToString
public class Skill extends BaseEntity {

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

    /**
     * 存储简历id
     */
    @Getter
    @Setter
    private long resumeId;

    public String getLevel() {
        switch (this.level) {
            case 1:
                return Constant.KNOW;
            case 2:
                return Constant.FAMILIAR;
            case 3:
                return Constant.CONTROL;
            case 4:
                return Constant.MASTER;
            default:
                return null;
        }
    }

    private void setLevel(String level) {
        switch (level) {
            case Constant.KNOW:
                this.level = 1;
                break;
            case Constant.FAMILIAR:
                this.level = 2;
                break;
            case Constant.CONTROL:
                this.level = 3;
                break;
            case Constant.MASTER:
                this.level = 4;
                break;
            default:
                this.level = 0;
                break;
        }
    }

    public Skill(String name, String level, Long resumeId) {
        switch (level) {
            case Constant.KNOW:
                this.level = 1;
                break;
            case Constant.FAMILIAR:
                this.level = 2;
                break;
            case Constant.CONTROL:
                this.level = 3;
                break;
            case Constant.MASTER:
                this.level = 4;
                break;
            default:
                this.level = 0;
                break;
        }
        this.name = name;
        this.resumeId = resumeId;
    }


}