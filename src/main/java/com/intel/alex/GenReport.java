package com.intel.alex;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.*;


/**
 * Created by root on 16-2-16.
 */
class GenReport {
    private Configuration configuration = null;

    GenReport() {
        configuration = new Configuration();
        configuration.setClassicCompatible(true);
        configuration.setDefaultEncoding("utf-8");
    }

    public void createDoc(List<Map<String, String>> queryList, Map<String, String> queryResult) {
//        Map<String, List<Map<String, Object>>> dataMap = new HashMap<String, List<Map<String, Object>>>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        getData(dataMap, queryList);
        getBBData(dataMap, queryResult);
//        configuration.setDirectoryForTemplateLoading(new File("/root/workspace/ReportGen/src/main/resources/template"));
        System.out.println(this.getClass().getResource(""));
        configuration.setClassForTemplateLoading(this.getClass(), "/template");
        Template t = null;
        try {
            //input the document template
            t = configuration.getTemplate("test.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //output the generated document
        File outFile = new File("test2.doc");
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            t.process(dataMap, out);
            out.flush();
            out.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void getData(Map<String, Object> dataMap, List<Map<String, String>> queryList) {
        List<Map<String, Object>> newsList = new ArrayList<Map<String, Object>>();
        for (int i = 1; i <= 30; i++) {
//            Map<String, Object> map=new HashMap<String, Object>();
            Map<String, String> propertyMap = queryList.get(i - 1);
            Set<String> keySet = propertyMap.keySet();
            for (Object key : keySet) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("query", i);
                map.put("category", key.toString());
                map.put("default", "");
                map.put("change", propertyMap.get(key));
                newsList.add(map);
            }
            dataMap.put("newsList", newsList);
        }
    }

    private static void getBBData(Map<String, Object> dataMap, Map<String, String> queryResult) {

    }

//    private void getData(Map dataMap) {
//        List<Map<String, Object>> newsList=new ArrayList<Map<String, Object>>();
//        for(int i=1;i<=3;i++){
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("query",i);
//            map.put("category", "category"+i);
//            map.put("category", "category"+i*2);
//            map.put("category", "category"+i*3);
//            map.put("default", "default"+i);
//            map.put("default", "default"+i*2);
//            map.put("default", "default"+i*3);
//            map.put("change", "change"+i);
//            map.put("change", "change"+i*2);
//            map.put("change", "change"+i*3);
//            newsList.add(map);
//        }
//        dataMap.put("newsList",newsList);
//    }

}
