package com.stalary.personfilter.holder;

import com.stalary.personfilter.data.dto.ProjectInfo;

/**
 * ProjectHolder
 *
 * @author lirongqian
 * @since 2018/04/09
 */
public class ProjectHolder {

    private static final ThreadLocal<ProjectInfo> projectThreadLocal = new ThreadLocal<>();

    public static ProjectInfo get() {
        return projectThreadLocal.get();
    }

    public static void set(ProjectInfo projectInfo) {
        projectThreadLocal.set(projectInfo);
    }

    public static void remove() {
        projectThreadLocal.remove();
    }
}