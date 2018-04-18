package com.stalary.personfilter.data.vo;

import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.entity.mongodb.Resume;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ResumeAndUser
 *
 * @author lirongqian
 * @since 2018/04/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeAndUser {

    private Resume resume;

    private User user;
}