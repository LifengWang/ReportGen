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
    private String zipFile;
    private String destination;

    public FileUtil(String zipFile, String destination) {
        this.zipFile = zipFile;
        this.destination = destination;
    }

    public List<Map<String, String>> parseQueryProperty() {
        String queryDirs = getQueryDirs(zipFile, destination);
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
                        String s2[] = s1[1].split("=");
                        propertyMap.put(s2[0], s2[1].split(";")[0]);
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

    public Map<String, String> parseQueryResult() {
        String queryDirs = getQueryDirs(zipFile, destination);
        Map<String, String> bbMap = new HashMap<String, String>();
        File f = new File(queryDirs);
        return bbMap;
    }

    public String getQueryDirs(String zipFile, String destination) {
        String[] s = zipFile.split("/");
        String[] s2 = s[s.length - 1].split(".zip");
        String folderName = s2[s2.length - 1];
        return destination + "/" + folderName + "/bigBench-configs/hive/queries";
    }

}
