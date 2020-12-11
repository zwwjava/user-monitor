package com.qs.monitor.vo.task;

import java.util.Date;

/**
 * Created by ppp on 17-10-24.
 */
public class JobStatusVo {

    private String group;

    private String name;

    private boolean executing;

    private int exeCount;//执行次数统计

    private int errCount;

    private long runTime;//累积执行时长

    private Long latestRunTime;//末次执行时长

    private Long runTimeAverage;//平均执行时长

    private Date fireTime;

    private Date firstFireTime;

    private Date nextFireTime;

    private Date previousFireTime;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExecuting() {
        return executing;
    }

    public void setExecuting(boolean executing) {
        this.executing = executing;
    }

    public int getExeCount() {
        return exeCount;
    }

    public void setExeCount(int exeCount) {
        this.exeCount = exeCount;
    }

    public int getErrCount() {
        return errCount;
    }

    public void setErrCount(int errCount) {
        this.errCount = errCount;
    }

    public long getRunTime() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    public Long getLatestRunTime() {
        return latestRunTime;
    }

    public void setLatestRunTime(Long latestRunTime) {
        this.latestRunTime = latestRunTime;
    }

    public Long getRunTimeAverage() {
        return runTimeAverage;
    }

    public void setRunTimeAverage(Long runTimeAverage) {
        this.runTimeAverage = runTimeAverage;
    }

    public Date getFireTime() {
        return fireTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }

    public Date getFirstFireTime() {
        return firstFireTime;
    }

    public void setFirstFireTime(Date firstFireTime) {
        this.firstFireTime = firstFireTime;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Date getPreviousFireTime() {
        return previousFireTime;
    }

    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

}
