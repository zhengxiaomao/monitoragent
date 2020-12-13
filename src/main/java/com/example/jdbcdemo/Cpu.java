package com.example.jdbcdemo;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

@Data
@Component
public class Cpu implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * 核心数
     */

    Object contextSwitches;

    public Object getContextSwitches() {
        return contextSwitches;
    }

    public Object getInterrupts() {
        return interrupts;
    }

    public Object getIdle() {
        return idle;
    }

    public Object getSteal() {
        return steal;
    }

    public Object getIrq() {
        return irq;
    }

    public Object getSoftirq() {
        return softirq;
    }

    public Object getIowait() {
        return iowait;
    }

    public Object getSys() {
        return sys;
    }

    public Object getUser() {
        return user;
    }

    public Object getNice() {
        return nice;
    }

    Object interrupts;
    Object idle;
    Object steal;
    Object irq;
    Object softirq;
    Object iowait;
    Object sys;
    Object user;
    Object nice;



    public Map result(){
        Map map = new Hashtable();
        map.put("contextSwitches",getContextSwitches());
        map.put("interrupts",getInterrupts());
        map.put("user",getUser());
        map.put("nice",getNice());
        map.put("sys",getSys());
        map.put("idle",getIdle());
        map.put("iowait",getSoftirq());
        map.put("irq",getIrq());
        map.put("softirq",getSoftirq());
        map.put("steal",getSteal());


        return map;
    }
}
