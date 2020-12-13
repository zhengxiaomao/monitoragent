package com.example.jdbcdemo;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.NumberUtil;
//import com.gw.ard.common.tools.IpUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.hardware.CentralProcessor.TickType;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.Util;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.*;

@Component
@Data
@EnableScheduling
public class SystemHardwareInfo  implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int OSHI_WAIT_SECOND = 1000;

    static HashMap cpuRet = new HashMap();

    /**
     * CPU相关信息
     */
    private Cpu cpu = new Cpu();

    /**
     * 內存相关信息
     */
    private Mem mem = new Mem();

    /**
     * JVM相关信息
     */
    private Jvm jvm = new Jvm();

    /**
     * 系统硬件相关
     */

    private  Sys sys = new Sys();


    /**
     * 磁盘相关信息
     */

    private Disk disk1 = new Disk();



    private List<SysFile> sysFiles = new LinkedList<SysFile>();


    @Scheduled(cron="*/5 * * * * ?")
    public void copyTo() throws Exception {
        System.out.println("收集系统信息......");
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();

        setCpuInfo(hal.getProcessor());

        setMemInfo(hal.getMemory());

        setSystemInfo(hal.getComputerSystem());

        setDiskInfo(hal.getDiskStores());

        setJvmInfo();

        setSysFiles(si.getOperatingSystem());

        setCpuInfo(hal.getProcessor());

        setNetworkInterfaces(hal.getNetworkIFs());

        setSysFiles(si.getOperatingSystem());


    }


    /**
     * 设置CPU信息
     */
    private void setCpuInfo(CentralProcessor processor) throws Exception {


        FileOutputStream outstr = new FileOutputStream("/tmp/2.txt");
        ObjectOutputStream outObj = new ObjectOutputStream(outstr);
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long sys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
        cpuRet.put("contextSwitches",String.format("%d",processor.getContextSwitches()));
        cpuRet.put("interrupts",String.format("%d",processor.getInterrupts()));
        cpuRet.put("user",String.format("%.2f",100d * user/totalCpu));
        cpuRet.put("nice",String.format("%.2f",100d * nice / totalCpu));
        cpuRet.put("sys",String.format("%.2f",100d * sys / totalCpu));
        cpuRet.put("idle",String.format("%.2f",100d * idle / totalCpu));
        cpuRet.put("iowait",String.format("%.2f",100d * iowait / totalCpu));
        cpuRet.put("irq",String.format("%.2f",100d * irq / totalCpu));
        cpuRet.put("sofrtirq",String.format("%.2f",100d * softirq / totalCpu));
        cpuRet.put("steal",String.format("%.2f",100d * steal / totalCpu));
//        Long start=System.currentTimeMillis();
//        outObj.writeObject(hm);
//        outObj.flush();
//        outObj.close();
//        Long end=System.currentTimeMillis();
//        System.out.println("cpu序列化耗时:"+(end-start));


    }

    private void setDiskInfo(HWDiskStore[] diskStores) throws Exception {


        List diskInfo = new ArrayList();
        FileOutputStream outstr = new FileOutputStream("/tmp/DiskInfo.txt");
        ObjectOutputStream outObj = new ObjectOutputStream(outstr);

        for (HWDiskStore disk : diskStores) {
            HashMap hm = new HashMap();
            hm.put("磁盘名:",disk.getName());
            hm.put("磁盘大小:",convertFileSize(disk.getSize()));
            hm.put("读取次数:",disk.getReads());
            hm.put("写入次数:",disk.getWrites());
            hm.put("写入字节:",convertFileSize(disk.getWriteBytes()));
            hm.put("读取字节:",convertFileSize(disk.getReadBytes()));
            diskInfo.add(hm);

        }
        outObj.writeObject(diskInfo);
        outObj.flush();
        outObj.close();

    }



    private  void setNetworkInterfaces(NetworkIF[] networkIFs) throws Exception {

        List netInfo = new ArrayList();
        FileOutputStream outstr = new FileOutputStream("/tmp/NetInfo.txt");
        ObjectOutputStream outObj = new ObjectOutputStream(outstr);
        for (NetworkIF net : networkIFs) {
            HashMap hm = new HashMap();
            hm.put("netCardName", net.getDisplayName());
            hm.put("macAddr", net.getMacaddr());
            hm.put("ip", Arrays.toString(net.getIPv4addr()));
            hm.put("receiveBytes", convertFileSize(net.getBytesRecv()));
            hm.put("receivePackets",net.getPacketsRecv());
            hm.put("sentPackets",net.getPacketsSent());
            hm.put("receivePacketsErrors", net.getInErrors());
            hm.put("sentPacketsErrors", net.getPacketsSent());
            hm.put("sentBytes",convertFileSize(net.getBytesSent()));
            hm.put("sentPacketsErrors", net.getOutErrors());
            netInfo.add(hm);

        }
        outObj.writeObject(netInfo);
        System.out.println("done");
        outObj.flush();
        outObj.close();
    }
    private  void setSystemInfo(ComputerSystem computerSystem) throws Exception{

        FileOutputStream outstr = new FileOutputStream("/tmp/SysInfo.txt");
        ObjectOutputStream outObj = new ObjectOutputStream(outstr);
        String Manufacturer=computerSystem.getManufacturer();
        String Model=computerSystem.getModel();
        String SerialNumber=computerSystem.getSerialNumber();
        final Firmware firmware = computerSystem.getFirmware();
        String firmwareManufacturer=firmware.getManufacturer();
        String firmwareName=firmware.getName();
        String firmwareVersion=firmware.getVersion();
        final Baseboard baseboard = computerSystem.getBaseboard();
        String baseboardManufacturer=baseboard.getManufacturer();
        String baseboardModel=baseboard.getModel();
        String baseboardVersion=baseboard.getVersion();
        String baseboardSerialNumber=baseboard.getSerialNumber();
        sys.setManufacturer(Manufacturer);
        sys.setModel(Model);
        sys.setSerialNumber(SerialNumber);
        sys.setFirmwareManufacturer(firmwareManufacturer);
        sys.setFirmwareName(firmwareName);
        sys.setFirmwareVersion(firmwareVersion);
        sys.setBaseboardManufacturer(baseboardManufacturer);
        sys.setBaseboardModel(baseboardModel);
        sys.setBaseboardSerialNumber(baseboardSerialNumber);
        sys.setBaseboardVersion(baseboardVersion);
        outObj.writeObject(sys);
        outObj.flush();
        outObj.close();

    }

    /**
     * 设置内存信息
     */
    private void setMemInfo(GlobalMemory memory) throws Exception{

            FileOutputStream outstr = new FileOutputStream("/tmp/obj.txt");
            ObjectOutputStream outObj = new ObjectOutputStream(outstr);
            Long start = System.currentTimeMillis();
            mem.setTotal(memory.getTotal());
            mem.setUsed(memory.getTotal() - memory.getAvailable());
            mem.setFree(memory.getAvailable());
            outObj.writeObject(mem);
            outObj.flush();
            outObj.close();
            Long end = System.currentTimeMillis();
//            System.out.println("内存序列化时间:"+(end-start));

    }



    /**
     * 设置Java虚拟机
     */
    private void setJvmInfo() throws Exception{
        FileOutputStream outstr = new FileOutputStream("/tmp/JvmInfo.txt");
            ObjectOutputStream outObj = new ObjectOutputStream(outstr);
            Properties props = System.getProperties();
            jvm.setTotal(Runtime.getRuntime().totalMemory());
            jvm.setMax(Runtime.getRuntime().maxMemory());
            jvm.setFree(Runtime.getRuntime().freeMemory());
            jvm.setVersion(props.getProperty("java.version"));
            jvm.setHome(props.getProperty("java.home"));
            outObj.writeObject(jvm); //序列化
            outObj.flush();
            outObj.close();


    }



    /**
     * 设置磁盘信息
     */
    private void setSysFiles(OperatingSystem os) throws Exception{
        InetAddress addr = InetAddress.getLocalHost();
        FileSystem fileSystem = os.getFileSystem();
        OSFileStore[] fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(convertFileSize(total));
            sysFile.setFree(convertFileSize(free));
            sysFile.setUsed(convertFileSize(used));
            sysFile.setUsage(NumberUtil.round(NumberUtil.mul(used, total, 4), 100).doubleValue());
            sysFiles.add(sysFile);

        }
    }






    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB" , (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB" , f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB" , f);
        } else {
            return String.format("%d B" , size);
        }
    }
}

