package com.intel.alex;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


/**
 * Created by root on 16-2-16.
 */
public class GenReport {
    private Configuration configuration=null;

    public GenReport(){
        configuration = new Configuration();
        configuration.setClassicCompatible(true);
        configuration.setDefaultEncoding("utf-8");
    }
    public void createDoc(List<Map<String, String>> queryList) throws IOException {
        Map dataMap=new HashMap();
        getData(dataMap, queryList);
//        configuration.setDirectoryForTemplateLoading(new File("/root/workspace/ReportGen/src/main/resources/template"));
        System.out.println(this.getClass().getResource(""));
        configuration.setClassForTemplateLoading(this.getClass(), "/template");
        Template t=null;
        try {
            //test.ftl为要装载的模板
            t = configuration.getTemplate("test.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //输出文档路径及名称
        File outFile = new File("test.doc");
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

    private static void getData(Map dataMap,List<Map<String, String>> queryList){
        List<Map<String, Object>> newsList=new ArrayList<Map<String,Object>>();
        for(int i=1;i<=30;i++){
//            Map<String, Object> map=new HashMap<String, Object>();
            Map<String,String> propertyMap = queryList.get(i-1);
            Set keySet = propertyMap.keySet();
            for (Object key: keySet){
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("query",i);
                map.put("category", key.toString());
                map.put("default","");
                map.put("change", propertyMap.get(key));
                newsList.add(map);
            }
            dataMap.put("newsList",newsList);

        }



    }

    private void getData(Map dataMap){

        List<Map<String, Object>> newsList=new ArrayList<Map<String,Object>>();
        for(int i=1;i<=3;i++){
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("query",i);
            map.put("category", "category"+i);
            map.put("category", "category"+i*2);
            map.put("category", "category"+i*3);
            map.put("default", "default"+i);
            map.put("default", "default"+i*2);
            map.put("default", "default"+i*3);
            map.put("change", "change"+i);
            map.put("change", "change"+i*2);
            map.put("change", "change"+i*3);
            newsList.add(map);
        }
        dataMap.put("newsList",newsList);

    }

}
