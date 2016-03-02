package com.intel.alex;

import com.intel.alex.Utils.HadoopConfUtil;
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
    private static final String sqlFileStr = "hiveSettings.sql";
    private static HadoopConfUtil hcu=new HadoopConfUtil();
    GenReport() {
        configuration = new Configuration();
        configuration.setClassicCompatible(true);
        configuration.setDefaultEncoding("utf-8");
    }

    public void createDoc(List<Map<String, String>> queryList, Map<String, Object> queryResult) {
//        Map<String, List<Map<String, Object>>> dataMap = new HashMap<String, List<Map<String, Object>>>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        hcu.getHadoopConfiguration(dataMap);
        getData(dataMap, queryList);
        getQueryTime(dataMap, queryResult);
        ClassLoader classLoader = getClass().getClassLoader();
        configuration.setClassLoaderForTemplateLoading(classLoader, "");

        Template t = null;
        try {
            //input the document template
            t = configuration.getTemplate("test2.ftl");
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

    private void getData(Map<String, Object> dataMap, List<Map<String, String>> queryList) {
        List<Map<String, Object>> propertyList = new ArrayList<Map<String, Object>>();
        //write the distinct property set hivesql into the sql file
        Set<String> propertySet = new HashSet<String>();
        for (Map<String, String> map : queryList) {
            Set<String> keySet = map.keySet();
            for (String name : keySet)
                propertySet.add(name);

        }
        writeSQLFile(propertySet);
        //execHiveSet
        List<String> defaultValues = exeHiveSet();
        Map<String, String> defaultMap = new HashMap<String, String>();
        int itr = 0;
        for (String property : propertySet) {
            String[] results = defaultValues.get(itr).split("=");
            if (results.length == 2)
                defaultMap.put(property, results[1]);
            else defaultMap.put(property, "N/A");
            itr++;
        }

        for (int i = 1; i <= 30; i++) {
//            Map<String, Object> map=new HashMap<String, Object>();
            Map<String, String> propertyMap = queryList.get(i - 1);
            Set<String> keySet = propertyMap.keySet();
            for (Object key : keySet) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("query", i);
                map.put("category", key.toString());
                map.put("default", defaultMap.get(key));
                map.put("change", propertyMap.get(key));
                propertyList.add(map);
            }
            dataMap.put("propertyList", propertyList);
        }
    }

    private static void writeSQLFile(Set<String> propertySet) {
        FileOutputStream fo = null;
        try {
            File sqlFile = new File(sqlFileStr);
            if (sqlFile.exists()) {
                //System.out.println("file already exists");
                sqlFile.delete();
                sqlFile.createNewFile();
            }

            fo = new FileOutputStream(sqlFile, true);
            for (String property : propertySet) {
                StringBuffer sb = new StringBuffer();
                sb.append("set " + property + ";\n");
                fo.write(sb.toString().getBytes("utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static List<String> exeHiveSet() {
        List<String> results = new ArrayList<String>();
        List<String> command = new ArrayList<String>();
        command.add("hive");
        command.add("-f");
        command.add(sqlFileStr);
        try {
            ProcessBuilder hiveProcessBuilder = new ProcessBuilder(command);
            Process hiveProcess = hiveProcessBuilder.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(hiveProcess.getInputStream()));
            String data = null;
            while ((data = br.readLine()) != null) {
                results.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void getQueryTime(Map<String, Object> dataMap, Map<String, Object> queryResult) {
        dataMap.putAll(queryResult);
    }

    private void getDocProperty(Map<String, Object> dataMap, Map<String, Object> docProperty){
        dataMap.putAll(docProperty);
    }

}
