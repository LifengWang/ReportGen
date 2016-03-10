package com.intel.alex.Utils;


import jxl.Workbook;

import jxl.write.*;

import java.io.*;
import java.lang.*;

/**
 * Created by root on 3/7/16.
 */
public class ExcelUtil {
    private final String logDir;

    public ExcelUtil(String logDir) {
        this.logDir = logDir;
    }

    public void csv2Excel() {
        File csvFile = new File(logDir + "/run-logs/BigBenchTimes.csv");
        FileReader fileReader;
        try{
            WritableWorkbook book;//
            File excFile = new File("/home/BigBenchTimes.xls");//
            book = Workbook.createWorkbook(excFile);//
            int num = book.getNumberOfSheets();
            WritableSheet sheet0 = book.createSheet("BigBenchTimes",num);
            WritableSheet sheet1 = book.createSheet("PowerTestTime",num+1); //1 is needed to change
            WritableSheet sheet2 = book.createSheet("ThroughPutTestTime_allstreams",num+2);

            fileReader = new FileReader(csvFile);
            BufferedReader br = new BufferedReader(fileReader);
            int i=0;
            int p=0;
            int t=0;
            String line ;
            while ((line=br.readLine())!=null){
                String s[]= line.split(";");
                if(s.length > 3){
                    for(int j=0; j< s.length; j++){
                        Label label = new Label(j, i, s[j]);
                        sheet0.addCell(label);
                    }
                    if(s[1].equals("POWER_TEST")){
                        for(int j =0; j< s.length; j++){
                            Label label =new Label(j,p,s[j]);
                            sheet1.addCell(label);
                        }
                        p++;
                    }
                    if(s[1].equals("THROUGHPUT_TEST_1")){
                        for(int j =0; j< s.length; j++){
                            Label label =new Label(j,t,s[j]);
                            sheet2.addCell(label);
                        }
                        t++;
                    }
                }
                i++;
            }
            br.close();
            fileReader.close();
            book.write();
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
