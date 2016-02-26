package com.intel.alex.Utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by root on 16-2-22.
 */
public class TestZipUtil {
    ZipUtil zu = new ZipUtil();
    String zipFile = "/root/workspace/ReportGen/3tb_s7_hsw_ssd_fair.zip";
    String destination = "/root/workspace/ReportGen/";

    @Test
    public void testUnzip() throws Exception {
        String dir = zu.unzip(zipFile,destination);
        Assert.assertEquals("failed wrong unzip dir","/root/workspace/ReportGen/3tb_s7_hsw_ssd_fair",dir);
    }
}
