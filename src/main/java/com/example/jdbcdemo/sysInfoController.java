package com.example.jdbcdemo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("sys")
public class sysInfoController {

    @Resource
    sysInfoService sysInfoService;

    @ApiOperation(value = "获取内存信息")
    @RequestMapping(value = "/mem",method = RequestMethod.GET)
    @ResponseBody
    public Map getMem() throws Exception{
        return sysInfoService.getMemInfo();
    }
    @ApiOperation(value = "获取jvm信息")
    @RequestMapping(value = "/jvm",method = RequestMethod.GET)
    public Map getJvm() throws Exception{
        return sysInfoService.getJvmInfo();
    }
    @ApiOperation(value = "获取cpu信息")
    @RequestMapping(value = "/cpu",method = RequestMethod.GET)
    public Map getCpu() throws Exception{
        return sysInfoService.getCpuInfo();
    }

    @ApiOperation(value = "获取硬件系统信息")
    @RequestMapping(value = "/sys",method = RequestMethod.GET)
    public Map getSys() throws Exception{
        return sysInfoService.getSysInfo();
    }

    @ApiOperation(value = "获取磁盘信息")
    @RequestMapping(value = "/disk",method = RequestMethod.GET)
    public Map getDisk() throws Exception{
         return  sysInfoService.getDiskInfo();
    }

    @ApiOperation(value = "获取网络信息")
    @RequestMapping(value = "/net",method = RequestMethod.GET)
    public Map getNet() throws Exception{
        return  sysInfoService.getNetInfo();
    }





}
