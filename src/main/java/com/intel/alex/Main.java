package com.intel.alex;

import com.intel.alex.Utils.FileUtil;
import com.intel.alex.Utils.XMLUtil;
import com.intel.alex.Utils.ZipUtil;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * Created by root on 16-2-16.
 */

public class Main {
    public static void main(String[] args) throws Exception {
        //get the BigBench log file and unzip destination from args
        String zipFile = "/home/ReportGen/3tb_s7_hsw_ssd_fair.zip";
        String destination = "/home/ReportGen/";
        String xmlDir = "/home/script/";
        //File xmlFile = new File(xmlDir);
        //XMLUtil xu = new XMLUtil();
        GenReport gr = new GenReport();
        ZipUtil zu = new ZipUtil();
//        FileUtil fu = new FileUtil(zu.unzip(arg[0], arg[1]));
        FileUtil fu = new FileUtil(zu.unzip(zipFile, destination));
        XMLUtil xu = new XMLUtil(xmlDir);
        List<Map<String, String>> queryList = fu.parseQueryProperty();
        Map<String, Object> queryResult = fu.parseQueryResult();
        Map<String, Object> xmlMap = xu.parseXMLFile();
        gr.createDoc(queryList, queryResult, xmlMap);
    }
}