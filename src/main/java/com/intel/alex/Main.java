package com.intel.alex;

import com.intel.alex.Utils.FileUtil;
import com.intel.alex.Utils.XMLUtil;
import com.intel.alex.Utils.ZipUtil;

import java.util.List;
import java.util.Map;


/**
 * Created by root on 16-2-16.
 */

public class Main {
    public static void main(String[] args) throws Exception {
        //get the BigBench log file and unzip destination from args
        GenReport gr = new GenReport();
        ZipUtil zu = new ZipUtil();
        FileUtil fu = new FileUtil(zu.unzip(args[0], args[1]));
        XMLUtil xu = new XMLUtil(args[2]);
        List<Map<String, String>> queryList = fu.parseQueryProperty();
        Map<String, Object> queryResult = fu.parseQueryResult();
        Map<String, Object> xmlMap = xu.parseXMLFile();
        gr.createDoc(queryList, queryResult, xmlMap);
    }
}