package com.example.jdbcdemo;


import lombok.Data;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class Disk {

    String diskName;
    String diskModel;
    String diskSerial;
    Long diskSize;


    public Map result(){

        Map map = new HashMap();

        map.put("磁盘名称:",getDiskName());
        map.put("磁盘型号:",getDiskModel());
        map.put("磁盘序列号:",getDiskSerial());
        map.put("磁盘大小:",getDiskSize());
        return map;

    }
}
