package com.intel.alex;

import com.intel.alex.Utils.FileUtil;
import com.intel.alex.Utils.XMLUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


import java.io.*;

import java.util.*;

/**
 * Created by root on 16-2-16.
 */

public class Main {
    public static void main(String[] args){
        GenReport gr = new GenReport();
        FileUtil fu = new FileUtil();
//	String queryDirs =fu.getQueryDirs(args[0],args[1]);
        String queryDirs =fu.getQueryDirs("/root/workspace/ReportGen/logs-20160204-080510-hive-sf3000.zip", "/root/workspace/ReportGen");
        List<Map<String, String>> queryList = fu.parseFile(queryDirs);
        try {
            gr.createDoc(queryList);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

//    private static void getData(Map dataMap,List<Map<String, String>> queryList){
//        List<Map<String, Object>> newsList=new ArrayList<Map<String,Object>>();
//        for(int i=1;i<=30;i++){
//            Map<String, Object> map=new HashMap<String, Object>();
//            Map<String,String> propertyMap = queryList.get(i-1);
//            Set keySet = propertyMap.keySet();
//            for (Object key: keySet){
//                map.put("query",i);
//                map.put("category", key.toString());
//                map.put("change", propertyMap.get(key));
//            }
////            map.put("query",i);
////            map.put("category", "category"+i);
////            map.put("change", "change"+i*3);
////            map.put("category", "category"+i*2);
////            map.put("category", "category"+i*3);
////            map.put("default", "default"+i);
////            map.put("default", "default"+i*2);
////            map.put("default", "default"+i*3);
//
//            newsList.add(map);
//        }
//        dataMap.put("newsList",newsList);
//
//    }

//    public static List<Map<String,String>> getInfo(){
//        List<Map<String, String>> nodeList = null;
//        try {
//            nodeList = XMLUtil.parseXMLFile(new File("/root/workspace/ReportGen/src/main/resources/hsw_info.xml"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return nodeList;
//    }
}
