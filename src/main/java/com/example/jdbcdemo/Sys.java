package com.example.jdbcdemo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class Sys implements Serializable {

    private static final long serialVersionUID = 1L;
    String Manufacturer;
    String Model;
    String SerialNumber;
    String firmwareManufacturer;
    String firmwareName;
    String firmwareVersion;
    String baseboardManufacturer;
    String baseboardModel;
    String baseboardVersion;
    String baseboardSerialNumber;

    public Map result(){

        Map map = new HashMap();
        map.put("pc制造商:",getManufacturer());
        map.put("型号:",getModel());
        map.put("序列号:",getSerialNumber());
        map.put("固件制造商:",getFirmwareManufacturer());
        map.put("固件名:",getFirmwareName());
        map.put("固件版本:",getFirmwareVersion());
        map.put("主板制造商:",getBaseboardManufacturer());
        map.put("主板型号:",getBaseboardModel());
        map.put("主板版本:",getBaseboardVersion());
        map.put("主板序列号:",getBaseboardSerialNumber());
        return map;

    }

}
