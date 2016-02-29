package com.intel.alex.Utils;

import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lifeng on 16-2-25.
 */
public class TestFileUtil {
    private FileUtil fu = new FileUtil("/root/workspaces/ReportGen/3tb_s7_hsw_ssd_fair");
    String logDir = "/root/workspaces/ReportGen/3tb_s7_hsw_ssd_fair";

    @Test
    public void testParseQueryResult() throws Exception {
        Map<String, Object> bbMap;
        bbMap = fu.parseQueryResult();

        System.out.println(bbMap.keySet());
        for (Object k : bbMap.keySet()) {
            System.out.println(k + ":" + bbMap.get(k));
        }
    }


//    @Test
//    public void testName() throws Exception {
//        Map<String, Object> bbMap = new HashMap<String, Object>();
//        File BigBenchResult = new File(logDir + "/run-logs/BigBenchResult.log");
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(BigBenchResult));
//            String line;
//            while ((line = br.readLine()) != null) {
//                String s[] = line.split(" ");
//                if (s.length > 3){
//                    if (s[1].equals("T_LD")){
//                        bbMap.put("T_PT", s[6]);
//                    }
//                    if (s[1].equals("T_PT")){
//                        bbMap.put("T_PT", s[3]);
//                    }
//                    if (s[1].equals("T_TT")){
//                        bbMap.put("T_TT", s[3]);
//                    }
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
}
