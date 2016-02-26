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
        String zipFile = "/root/workspace/ReportGen/3tb_s7_hsw_ssd_fair.zip";
        String destination = "/root/workspace/ReportGen/";
        ZipUtil zu = new ZipUtil();
        GenReport gr = new GenReport();
        FileUtil fu = new FileUtil(zu.unzip(zipFile, destination));
//	      String queryDirs =fu.getQueryDirs(args[0], args[1]);
        List<Map<String, String>> queryList = fu.parseQueryProperty();
        Map<String, Object> queryResult = fu.parseQueryResult();
        gr.createDoc(queryList, queryResult);
    }
}