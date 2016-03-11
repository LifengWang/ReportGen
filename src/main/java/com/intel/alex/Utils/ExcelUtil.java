package com.intel.alex.Utils;


import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by root on 3/7/16.
 */
public class ExcelUtil {
    private final String logDir;

    public ExcelUtil(String logDir) {
        this.logDir = logDir;
    }

    public void createExcel() {
        File excFile = new File("/home/BigBenchTimes.xls");
        if (createRawDataSheet(excFile)) {
            createBBSheet(excFile);
            createPowerSheet(excFile);
            createThroughputSheet(excFile);
        }
    }


    private boolean createRawDataSheet(File excel) {
        try {
            WritableWorkbook book;//
            File excFile = new File("/home/BigBenchTimes.xls");//
            book = Workbook.createWorkbook(excFile);//
            int num = book.getNumberOfSheets();
            WritableSheet sheet = book.createSheet("BigBenchTimes", num);
            FileReader fileReader = new FileReader(new File(logDir + "/run-logs/BigBenchTimes.csv"));
            BufferedReader br = new BufferedReader(fileReader);
            int i = 0;
            String line;
            while ((line = br.readLine()) != null) {
                String s[] = line.split(";");
                if (s.length > 3) {
                    for (int j = 0; j < s.length; j++) {
                        if (i == 0) {
                            Label title = new Label(j, i, s[j]);
                            sheet.addCell(title);
                        } else if (j == 0 || j == 2 || j == 3 || j == 4 | j == 5 | j == 8) {
                            if (s[j] != null && !s[j].equals("")) {
                                Number ints = new Number(j, i, Long.parseLong(s[j]));
                                sheet.addCell(ints);
                            } else {
                                Label label = new Label(j, i, s[j]);
                                sheet.addCell(label);
                            }
                        } else if (j == 9) {
                            Number doubles = new Number(j, i, Double.parseDouble(s[j]));
                            sheet.addCell(doubles);
                        } else {
                            Label label = new Label(j, i, s[j]);
                            sheet.addCell(label);
                        }
                    }
                }
                i++;
            }
            br.close();
            fileReader.close();
            book.write();
            book.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void createPowerSheet(File excFile) {
    }

    private void createThroughputSheet(File excFile) {
    }

    private void createBBSheet(File excFile) {
    }

//    public void csv2Excel() {
//        File csvFile = new File(logDir + "/run-logs/BigBenchTimes.csv");
//        FileReader fileReader;
//        try {
//            WritableWorkbook book;//
//            File excFile = new File("/home/BigBenchTimes.xls");//
//            book = Workbook.createWorkbook(excFile);//
//            int num = book.getNumberOfSheets();
//            WritableSheet sheet0 = book.createSheet("BigBenchTimes", num);
//            WritableSheet sheet1 = book.createSheet("PowerTestTime", num + 1); //1 is needed to change
//            WritableSheet sheet2 = book.createSheet("ThroughPutTestTime_allstreams", num + 2);
//
////            String title = getTitle(csvFile);
//
//            fileReader = new FileReader(csvFile);
//            BufferedReader br = new BufferedReader(fileReader);
//            int i = 0;
//            int p = 0;
//            int t = 0;
//            String line;
//            while ((line = br.readLine()) != null) {
//                String s[] = line.split(";");
//                if (s.length > 3) {
//                    for (int j = 0; j < s.length; j++) {
//                        if (i == 0) {
//                            Label title = new Label(j, i, s[j]);
//                            sheet0.addCell(title);
//                        } else if (j == 0 || j == 2 || j == 3 || j == 4 | j == 5 | j == 8) {
//                            if (s[j] != null && !s[j].equals("")) {
//                                Number ints = new Number(j, i, Long.parseLong(s[j]));
//                                sheet0.addCell(ints);
//                            } else {
//                                Label label = new Label(j, i, s[j]);
//                                sheet0.addCell(label);
//                            }
//                        } else if (j == 9) {
//                            Number doubles = new Number(j, i, Double.parseDouble(s[j]));
//                            sheet0.addCell(doubles);
//                        } else {
//                            Label label = new Label(j, i, s[j]);
//                            sheet0.addCell(label);
//                        }
//
//                    }
////                    if (s[1].equals("POWER_TEST")) {
////                        for (int j = 0; j < s.length; j++) {
////                            Label label = new Label(j, p, s[j]);
////                            sheet1.addCell(label);
////                        }
////                        p++;
////                    }
////                    if (s[1].equals("THROUGHPUT_TEST_1")) {
////                        for (int j = 0; j < s.length; j++) {
////                            Label label = new Label(j, t, s[j]);
////                            sheet2.addCell(label);
////                        }
////                        t++;
////                    }
//                }
//                i++;
//            }
//            br.close();
//            fileReader.close();
//            book.write();
//            book.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
}
