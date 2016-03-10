package com.intel.alex.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 16-2-17.
 */
public class FileUtil {
    private final String logDir;

    public FileUtil(String logDir) {
        this.logDir = logDir;
    }

    public List<Map<String, String>> parseQueryProperty() {
        String queryDirs = logDir + "/bigBench-configs/hive/queries";
        File f = new File(queryDirs);
        List<Map<String, String>> queryList = new ArrayList<Map<String, String>>();
        if (!f.isDirectory()) {
            System.out.println("wrong queries path");
        }
        File queryProperty;
        for (int i = 1; i <= 30; i++) {
            if (i <= 9) {
                queryProperty = new File(queryDirs + "/q0" + i + "/engineLocalSettings.sql");
            } else {
                queryProperty = new File(queryDirs + "/q" + i + "/engineLocalSettings.sql");
            }
            Map<String, String> propertyMap = new HashMap<String, String>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(queryProperty));
                String s;
                while ((s = br.readLine()) != null) {
                    if (!s.contains("--") && !s.equals("")) {
                        String s1[] = s.split(" ");
                        if (s1.length > 1) {
                            String s2[] = s1[1].split("=");
                            if (s2.length > 1) {
                                propertyMap.put(s2[0], s2[1].split(";")[0]);
                            }
                        }
                    }
                }
                br.close();
                queryList.add(i - 1, propertyMap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return queryList;
    }

    public Map<String, Object> parseQueryResult() {
        Map<String, Object> bbMap = new HashMap<String, Object>();
        List<Map<String, Object>> queryList = new ArrayList<Map<String, Object>>();
        File timesCSV = new File(logDir + "/run-logs/BigBenchTimes.csv");
        try {
            BufferedReader br = new BufferedReader(new FileReader(timesCSV));
            String line;
            while ((line = br.readLine()) != null) {
                String s[] = line.split(";");
                if (s[1].equals("BENCHMARK")) {
                    bbMap.put("tbe", s[9]);
                }
                if (s[1].equals("LOAD_TEST")) {
                    bbMap.put("tle", s[9]);
                }
                if (s[1].equals("POWER_TEST")) {
                    if (s[3].equals("")) {
                        bbMap.put("tpe", s[9]);
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("number", s[3]);
                        map.put("time", s[9]);
                        queryList.add(map);
                    }
                    bbMap.put("queryList", queryList);
                }
//                if (s[1].equals("THROUGHPUT_TEST_1") && s[3].equals("")) {
                if (s[1].equals("THROUGHPUT_TEST_1") && s[3].equals("")) {
                    bbMap.put("tte", s[9]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File BigBenchResult = new File(logDir + "/run-logs/BigBenchResult.log");
        try {
            BufferedReader br = new BufferedReader(new FileReader(BigBenchResult));
            String line;
            while ((line = br.readLine()) != null) {
                String s[] = line.split(" ");
                if (s.length > 3) {
                    if (s[1].equals("T_LD")) {
                        bbMap.put("ld", s[6]);
                    }
                    if (s[1].equals("T_PT")) {
                        bbMap.put("pt", s[3]);
                    }
                    if (s[1].equals("T_TT")) {
                        bbMap.put("tt", s[3]);
                    }
                    if (s[1].equals("VALID")) {
                        bbMap.put("bbqpm", s[4]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bbMap;
    }

    public Map<String, Object> parseTTResult() {
        Map<String, Object> ttMap = new HashMap<String, Object>();
        List<Map<String, Object>> queryList = new ArrayList<Map<String, Object>>();
        File timesCSV = new File(logDir + "/run-logs/BigBenchTimes.csv");
        try {
            BufferedReader br = new BufferedReader(new FileReader(timesCSV));
            String line;
//            int stream;
            while ((line = br.readLine()) != null) {
                String s[] = line.split(";");
                if (s.length >3) {
                    if (s[1].equals("THROUGHPUT_TEST_1") ) {
                        ttMap.put("tt"+s[2], s[9]);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ttMap;
    }


}
