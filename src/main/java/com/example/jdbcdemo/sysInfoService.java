package com.example.jdbcdemo;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class sysInfoService {



    SystemHardwareInfo systemHardwareInfo = new SystemHardwareInfo();



    public Map getMemInfo() throws Exception{

            FileInputStream inputStream = new FileInputStream("/tmp/obj.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Mem mem = (Mem) objectInputStream.readObject();
            return mem.result();
    }


    public Map getJvmInfo() throws Exception {
        FileInputStream inputStream = new FileInputStream("/tmp/JvmInfo.txt");  //反序列化
        RandomAccessFile raf = new RandomAccessFile("/tmp/JvmInfo.txt","rws");
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Jvm jvm = (Jvm) objectInputStream.readObject();
        return jvm.result();

    }
    public Map getCpuInfo() throws Exception{
        FileInputStream inputStream = new FileInputStream("/tmp/2.txt");  //反序列化
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Map cpu = (Map) SystemHardwareInfo.cpuRet;
        return cpu;

    }

    public Map getSysInfo() throws Exception{

        FileInputStream inputStream = new FileInputStream("/tmp/SysInfo.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Sys sys = (Sys) objectInputStream.readObject();
        return sys.result();
    }

    public Map getDiskInfo() throws Exception{


        HashMap mp = new HashMap();
        FileInputStream inputStream = new FileInputStream("/tmp/DiskInfo.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        List disk = (List) objectInputStream.readObject();
        for(int i=0;i<disk.size();i++){
            mp.put(i,disk.get(i));
        }
        return mp;

    }

    public Map getNetInfo() throws Exception{


        Map mp = new HashMap();
        FileInputStream inputStream = new FileInputStream("/tmp/NetInfo.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        List net = (List) objectInputStream.readObject();
        for(int i=0;i<net.size();i++){
            mp.put(i,net.get(i));
        }
        return mp;

    }




}
