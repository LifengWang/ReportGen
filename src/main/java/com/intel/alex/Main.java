package com.intel.alex;

import com.intel.alex.Utils.ExcelUtil;
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
        //FileUtil fu = new FileUtil(zu.unzip(args[0], args[1]));
        FileUtil fu = new FileUtil(zu.unzip(args[0]));
        //zu.unzip(args[0]);
        //System.out.println(zu.unzip(args[0], args[1]));
        XMLUtil xu = new XMLUtil(args[1]);
        //ExcelUtil eu = new ExcelUtil(zu.unzip(args[0], args[1]));
        ExcelUtil eu = new ExcelUtil(zu.unzip(args[0]));
        List<Map<String, String>> queryList = fu.parseQueryProperty();
        Map<String, Object> queryResult = fu.parseQueryResult();
        Map<String, Object> xmlMap = xu.parseXMLFile();
        if (args[2].equals("both")){
            gr.createDoc(queryList, queryResult, xmlMap);
            eu.createExcel();
        }
        else if (args[2].equals("doc")) {
            gr.createDoc(queryList, queryResult, xmlMap);
        }
        else if(args[2].equals("xls")) {
            eu.createExcel();
        }
        else{
            System.out.println("For the third argument, please enter 'doc' to generate BigBench Report; ");
            System.out.println("Enter 'xls' to generate excel file of elapse time for each query and phrase.");
            System.out.println("Enter 'both' to generate both BigBench Report and excel file of elapse time for each query and phrase.");
        }
    }
}