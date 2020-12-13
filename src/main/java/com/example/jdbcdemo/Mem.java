package com.example.jdbcdemo;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class Mem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;

    public Double getTotal() {
        return NumberUtil.div(total, (1024 * 1024 * 1024), 2);
    }

    public Double getUsed() {
        return NumberUtil.div(used, (1024 * 1024 * 1024), 2);
    }


    public Double getFree() {
        return NumberUtil.div(free, (1024 * 1024 * 1024), 2);
    }

    public Double getUsage() {
        return NumberUtil.mul(NumberUtil.div(used, total, 4), 100);
    }

    public Map result(){

        Map map = new HashMap();
        map.put("Total",getTotal());
        map.put("Used",getUsed());
        map.put("free",getFree());
        return map;

    }

}
