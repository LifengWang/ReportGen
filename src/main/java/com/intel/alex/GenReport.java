package com.intel.alex;

import com.intel.alex.Utils.DocPropertyUtil;
import com.intel.alex.Utils.HadoopConfUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;


/**
 * Created by root on 16-2-16.
 */
class GenReport {
    private static final Logger logger = Logger.getLogger(GenReport.class.getName());
    private Configuration configuration = null;
    private static final String sqlFileStr = "hiveSettings.sql";
    private static final HadoopConfUtil hcu = new HadoopConfUtil();

    //HadoopConfUtil hcu=new HadoopConfUtil();
    GenReport() {
        Version v = new Version("2.3.23");
        configuration = new Configuration(v);
        configuration.setClassicCompatible(true);
        configuration.setDefaultEncoding("utf-8");
    }

    public void createDoc(List<Map<String, String>> queryList, Map<String, Object> queryResult, Map<String, Object> xmlMap) {
//        Map<String, List<Map<String, Object>>> dataMap = new HashMap<String, List<Map<String, Object>>>();
        Map<String, Object> dataMap = new HashMap<String, Object>();

//        dataMap.putAll(hcu.getHadoopConfiguration());
        hcu.getHadoopConfiguration(dataMap);
        getData(dataMap, queryList);
        getQueryTime(dataMap, queryResult);
        getXMLInfo(dataMap, xmlMap);
        getDocProperty(dataMap);
        ClassLoader classLoader = getClass().getClassLoader();
        configuration.setClassLoaderForTemplateLoading(classLoader, "");

        Template t = null;
        try {
            //input the document template
            t = configuration.getTemplate("BBReportTemplate.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //output the generated document
        File outFile = new File("BBReport.doc");
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            assert t != null;
            t.process(dataMap, out);
            assert out != null;
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
            for (Map.Entry<String, String> entry : propertyMap.entrySet()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("query", i);
                map.put("category", entry.getKey());
                map.put("default", defaultMap.get(entry.getKey()));
                map.put("change", entry.getValue());
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
//                System.out.println("file already exists");
                boolean deleted = sqlFile.delete();
                boolean created = sqlFile.createNewFile();
                if (!deleted&&!created){
                    logger.debug("delete the existed file failed");
                }

            }

            fo = new FileOutputStream(sqlFile, true);
            for (String property : propertySet) {
                fo.write(("set " + property + ";\n").getBytes("utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fo != null) {
                    fo.close();
                }
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
            String data;
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

    private void getDocProperty(Map<String, Object> dataMap) {
        DocPropertyUtil dpu = new DocPropertyUtil();
        dataMap.putAll(dpu.getDocProperty());
    }

    private void getXMLInfo(Map<String, Object> dataMap, Map<String, Object> xmlMap) {
        dataMap.putAll(xmlMap);
    }
}
