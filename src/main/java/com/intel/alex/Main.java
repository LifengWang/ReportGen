package com.intel.alex;

import com.intel.alex.Utils.FileUtil;
import com.intel.alex.Utils.ZipUtil;

import java.util.List;
import java.util.Map;


/**
 * Created by root on 16-2-16.
 */

public class Main {
    public static void main(String[] args) {
        //get the BigBench log file and unzip destination from args
        String zipFile = "/root/workspaces/ReportGen/3tb_s7_hsw_ssd_fair.zip";
        String destination = "/root/workspaces/ReportGen/";
        GenReport gr = new GenReport();
        ZipUtil zu = new ZipUtil();
//        FileUtil fu = new FileUtil(zu.unzip(arg[0], arg[1]));
        FileUtil fu = new FileUtil(zu.unzip(zipFile, destination));
        List<Map<String, String>> queryList = fu.parseQueryProperty();
        Map<String, Object> queryResult = fu.parseQueryResult();
        gr.createDoc(queryList, queryResult);
    }
}