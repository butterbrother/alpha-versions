package org.butterbrother.simplejmx;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Тестовая реализация MBean
 */
public class Test {
    public static void main(String args[]) throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName frm = new ObjectName("org.butterbrother.simplejmx:type=MemInfo");

        MemoryInfo meminf = new MemoryInfo();

        mbs.registerMBean(meminf, frm);

        Thread.sleep(Long.MAX_VALUE);
    }
}
