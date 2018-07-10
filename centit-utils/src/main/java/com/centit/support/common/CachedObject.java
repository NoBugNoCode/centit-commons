package com.centit.support.common;

import com.centit.support.algorithm.DatetimeOpt;

import java.util.Date;
import java.util.function.Supplier;

public class CachedObject<T> {

    public static final long NOT_REFRESH_PERIOD = 43200L;
    private T target;
    private boolean evicted;
    private Date refreshTime;
    //分钟
    private long freshPeriod;
    private Supplier<T> refresher;

    public  CachedObject(Supplier<T> refresher){
        this.target = null;
        this.evicted = true;
        this.refresher = refresher;
        //默认时间一个月
        this.freshPeriod = NOT_REFRESH_PERIOD;
    }

    /**
     *
     * @param refresher 重新获取代码的接口
     * @param freshPeriod 保鲜时间，单位为分钟
     */
    public  CachedObject(Supplier<T> refresher, long freshPeriod){
        this.target = null;
        this.evicted = true;
        this.refresher = refresher;
        this.freshPeriod = freshPeriod;
    }

    /**
     * @param freshPeriod 刷新周期 单位分钟
     */
    public void setFreshPeriod(int freshPeriod) {
        this.freshPeriod = freshPeriod;
    }

    public synchronized void evictObject(){
        evicted = true;
    }

    public synchronized T getCachedObject(){
        if(this.target == null || this.evicted ||
                System.currentTimeMillis() > refreshTime.getTime() + freshPeriod * 60000 ){
            target = refresher.get();
            refreshTime = DatetimeOpt.currentUtilDate();
            this.evicted = false;
        }
        return target;
    }
}
