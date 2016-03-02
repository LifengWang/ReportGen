package com.intel.alex;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import org.apache.hadoop.*;

/**
 * Created by root on 16-2-16.
 */
class GenReport {
    private Configuration configuration = null;
    private static final String sqlFileStr = "hiveSettings.sql";
    private static final String[] confArray={"yarn.scheduler.maximum-allocation-mb","yarn.scheduler.minimum-allocation-mb",
            "yarn.resourcemanager.scheduler.class","yarn.nodemanager.resource.memory-mb",
            "yarn.nodemanager.resource.cpu-vcores","yarn.app.mapreduce.am.resource.mb",
            "mapreduce.map.memory.mb","mapreduce.reduce.memory.mb",
            "mapreduce.map.java.opts","mapreduce.reduce.java.opts","mapreduce.job.reduce.slowstart.completedmaps"};
    private static final String[] paramArray={"schedulermax","schedulermin","schedulerclass","containermemory","cpuvcores","mapreduceamresource","mapmemory",
            "reducememory","mapopts","reduceopts","slowstart"};
    private static final File yarnFile=new File("yarn-site.xml");
    private static final File mapFile=new File("/etc/hadoop/conf/mapred-site.xml");
    private static final File hadoopEnvFile=new File("/etc/hadoop/conf/hadoop-env.sh");
    private static final File hiveEnvFile=new File("/etc/hive/conf/hive-env.sh");
    GenReport() {
        configuration = new Configuration();
        configuration.setClassicCompatible(true);
        configuration.setDefaultEncoding("utf-8");
    }

    public void createDoc(List<Map<String, String>> queryList, Map<String, Object> queryResult) {
//        Map<String, List<Map<String, Object>>> dataMap = new HashMap<String, List<Map<String, Object>>>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        getHadoopConfiguration(dataMap);
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
    private void getHadoopConfiguration(Map<String, Object> dataMap){
        org.apache.hadoop.conf.Configuration conf=new org.apache.hadoop.conf.Configuration(true);
        InputStream yarnIn=null;
        InputStream mapredIn=null;
        BufferedReader hadoopEnvReader=null;
        BufferedReader hiveEnvReader=null;
        String hadoopEnv=null,hiveEnv=null;
        try{
            //get configurations from yarn-site.xml, mapred-site.xml
            yarnIn=new FileInputStream(yarnFile);
            mapredIn=new FileInputStream(mapFile);

            conf.addResource(yarnIn);
            conf.addResource(mapredIn);

            for(int i=0;i<confArray.length;i++)
                if(i==2||i==4||i==10)
                    dataMap.put(paramArray[i],conf.get(confArray[i]));
                else if(i==8||i==9){
                    String[] res=conf.get(confArray[i]).split(" ");
                    for(String s:res){
                        if(s.contains("-Xmx")){
                            dataMap.put(paramArray[i],getOptsGval(s.substring(4)));
                        }
                    }
                }
                else{
                    dataMap.put(paramArray[i],getmbGval(conf.get(confArray[i])));

                }
            //get configurations from hadoop-env.sh, hive-env.sh
            hadoopEnvReader=new BufferedReader(new FileReader(hadoopEnvFile));
            hiveEnvReader=new BufferedReader(new FileReader(hiveEnvFile));
            String hadoopline,hiveline;
            while((hadoopline=hadoopEnvReader.readLine())!=null){
                if(hadoopline.contains("export YARN_OPTS=")){
                    hadoopEnv=hadoopline;
                    break;
                }

            }
            while((hiveline=hiveEnvReader.readLine())!=null){
                if(hiveline.contains("export HADOOP_CLIENT_OPTS=")){
                    hiveEnv=hiveline;
                    break;
                }

            }
            dataMap.put("yarnopts",getOptsGval(hadoopEnv.split(" ")[2].substring(4)));
            dataMap.put("hiveopts",getOptsGval(hiveEnv.split(" ")[2].substring(4)));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                yarnIn.close();
                mapredIn.close();
                hadoopEnvReader.close();
                hiveEnvReader.close();

            } catch (IOException e){
                e.printStackTrace();
            }
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
    private static String getOptsGval(String value){
        DecimalFormat df = new DecimalFormat("##.##");
        Double d=Double.parseDouble(value)/1024.0/1024.0/1024.0;
        return df.format(d)+"G";
    }
    private static String getmbGval(String value){
        DecimalFormat df = new DecimalFormat("##.##");
        Double d=Double.parseDouble(value)/1024.0;
        return df.format(d)+"G";
    }
    private void getQueryTime(Map<String, Object> dataMap, Map<String, Object> queryResult) {
        dataMap.putAll(queryResult);
    }

    private void getDocProperty(Map<String, Object> dataMap, Map<String, Object> docProperty){
        dataMap.putAll(docProperty);
    }

}
