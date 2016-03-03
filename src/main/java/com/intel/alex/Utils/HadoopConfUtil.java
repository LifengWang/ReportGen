package com.intel.alex.Utils;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * Created by root on 3/2/16.
 */
public class HadoopConfUtil {
    private static final String[] confArray = {"yarn.scheduler.maximum-allocation-mb", "yarn.scheduler.minimum-allocation-mb",
            "yarn.resourcemanager.scheduler.class", "yarn.nodemanager.resource.memory-mb",
            "yarn.nodemanager.resource.cpu-vcores", "yarn.app.mapreduce.am.resource.mb",
            "mapreduce.map.memory.mb", "mapreduce.reduce.memory.mb",
            "mapreduce.map.java.opts", "mapreduce.reduce.java.opts", "mapreduce.job.reduce.slowstart.completedmaps"};
    private static final String[] paramArray = {"schedulermax", "schedulermin", "schedulerclass", "containermemory", "cpuvcores", "mapreduceamresource", "mapmemory",
            "reducememory", "mapopts", "reduceopts", "slowstart"};
    private static final File yarnFile = new File("yarn-site.xml");
    private static final File mapFile = new File("/etc/hadoop/conf/mapred-site.xml");
    private static final File hadoopEnvFile = new File("/etc/hadoop/conf/hadoop-env.sh");
    private static final File hiveEnvFile = new File("/etc/hive/conf/hive-env.sh");

    //    public Map<String, Object> getHadoopConfiguration(){
    public void getHadoopConfiguration(Map<String, Object> dataMap) {
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration(true);
        //Map<String, Object> hadoopConfMap = new HashMap<String, Object>();
        InputStream yarnIn = null;
        InputStream mapredIn = null;
        BufferedReader hadoopEnvReader = null;
        BufferedReader hiveEnvReader = null;
        String hadoopEnv = null, hiveEnv = null;
        try {
            //get configurations from yarn-site.xml, mapred-site.xml
            yarnIn = new FileInputStream(yarnFile);
            mapredIn = new FileInputStream(mapFile);

            conf.addResource(yarnIn);
            conf.addResource(mapredIn);

            for (int i = 0; i < confArray.length; i++) {
                if (i == 2 || i == 4 || i == 10)
                    dataMap.put(paramArray[i], conf.get(confArray[i]));
                else if (i == 8 || i == 9) {
                    String[] res = conf.get(confArray[i]).split(" ");
                    for (String s : res) {
                        if (s.contains("-Xmx")) {
                            dataMap.put(paramArray[i], getOptsGval(s.substring(4)));
                        }
                    }
                } else {
                    dataMap.put(paramArray[i], getmbGval(conf.get(confArray[i])));

                }
            }
            //get configurations from hadoop-env.sh, hive-env.sh
            hadoopEnvReader = new BufferedReader(new FileReader(hadoopEnvFile));
//            BufferedReader hadoopEnvReader = new BufferedReader(new FileReader(hadoopEnvFile));
            hiveEnvReader = new BufferedReader(new FileReader(hiveEnvFile));
            String hadoopline, hiveline;
            while ((hadoopline = hadoopEnvReader.readLine()) != null) {
                if (hadoopline.contains("export YARN_OPTS=") && hadoopline.charAt(0) != '#') {
                    hadoopEnv = hadoopline;
                    break;
                }

            }
            while ((hiveline = hiveEnvReader.readLine()) != null) {
                if (hiveline.contains("export HADOOP_CLIENT_OPTS=") && hiveline.charAt(0) != '#') {
                    hiveEnv = hiveline;
                    break;
                }

            }
            dataMap.put("yarnopts", getOptsGval(getXmxVal(hadoopEnv)));
            dataMap.put("hiveopts", getOptsGval(getXmxVal(hiveEnv)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert yarnIn != null;
                yarnIn.close();
                assert mapredIn != null;
                mapredIn.close();
                assert hadoopEnvReader != null;
                hadoopEnvReader.close();
                assert hiveEnvReader != null;
                hiveEnvReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        return hadoopConfMap;


    }

    private String getXmxVal(String line) {
        String[] res = line.split("=");
        String vals = res[1].substring(1, res[1].length() - 1);
        String[] param = vals.split(" ");
        for (String s : param) {
            if (s.contains("-Xmx")) {
                return s.substring(4);
            }
        }
        return null;
    }

    private String getOptsGval(String value) {
        DecimalFormat df = new DecimalFormat("##.##");
        Double d = Double.parseDouble(value) / 1024.0 / 1024.0 / 1024.0;
        return df.format(d) + "G";
    }

    private String getmbGval(String value) {
        DecimalFormat df = new DecimalFormat("##.##");
        Double d = Double.parseDouble(value) / 1024.0;
        return df.format(d) + "G";
    }
}
