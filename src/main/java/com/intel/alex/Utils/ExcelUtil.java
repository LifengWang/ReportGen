package com.intel.alex.Utils;


import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Number;

import java.io.*;

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
            createPhaseSheet(excFile);
            createPowerSheet(excFile);
            createThroughputSheet(excFile);
        }
    }


    private boolean createRawDataSheet(File excFile) {
        try {
            WritableWorkbook book;//
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
        try {
            FileInputStream fin = new FileInputStream(excFile);
            Workbook wb = Workbook.getWorkbook(fin);
            WritableWorkbook wwb = Workbook.createWorkbook(excFile, wb);
            int num = wwb.getNumberOfSheets();
            Sheet sheet = wwb.getSheet(0);
            WritableSheet newSheet = wwb.createSheet("PowerTest", num);
            Label queryNumber = new Label(0, 0, "QueryNumber");
            Label queryTime = new Label(1, 0, "QueryTime(s)");
            newSheet.addCell(queryNumber);
            newSheet.addCell(queryTime);
            int i = 1;
            for (int r = 0; r < sheet.getRows(); r++) {
                if (sheet.getCell(1, r).getContents().equals("POWER_TEST") && !sheet.getCell(3, r).getContents().equals("")) {
                    Number ints = new Number(0, i, Integer.parseInt(sheet.getCell(3, r).getContents()));
                    newSheet.addCell(ints);
                    Number doubles = new Number(1, i, Double.parseDouble(sheet.getCell(9, r).getContents()));
                    newSheet.addCell(doubles);
                    i++;
                }
            }
            wwb.write();
            wwb.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    private void createThroughputSheet(File excFile) {
        try {
            FileInputStream fin = new FileInputStream(excFile);
            Workbook wb = Workbook.getWorkbook(fin);
            WritableWorkbook wwb = Workbook.createWorkbook(excFile, wb);
            int num = wwb.getNumberOfSheets();
            Sheet sheet = wwb.getSheet(0);
            WritableSheet newSheet = wwb.createSheet("Throughput", num);
            Label queryNumber = new Label(0, 0, "QueryNumber");
            newSheet.addCell(queryNumber);
            for(int i =1 ; i<=30;i++){
                Number queries = new Number(0, i, i);
                newSheet.addCell(queries);
            }
            for (int r = 0; r < sheet.getRows(); r++) {
                if (sheet.getCell(1, r).getContents().equals("THROUGHPUT_TEST_1") && !sheet.getCell(3, r).getContents().equals("")) {
                    int stream = Integer.parseInt(sheet.getCell(2, r).getContents());
                    Label streamNumber = new Label(stream + 1, 0, "Stream" + stream);
                    newSheet.addCell(streamNumber);
                    Number doubles = new Number(stream + 1, Integer.parseInt(sheet.getCell(3, r).getContents()), Double.parseDouble(sheet.getCell(9, r).getContents()));
                    newSheet.addCell(doubles);
                }
            }
            wwb.write();
            wwb.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    private void createPhaseSheet(File excFile) {
        try {
            FileInputStream fin = new FileInputStream(excFile);
            Workbook wb = Workbook.getWorkbook(fin);
            WritableWorkbook wwb = Workbook.createWorkbook(excFile, wb);
            int num = wwb.getNumberOfSheets();
            Sheet sheet = wwb.getSheet(0);
            WritableSheet newSheet = wwb.createSheet("PhaseTime", num);
            Label queryNumber = new Label(0, 0, "Phase");
            Label queryTime = new Label(1, 0, "ElapsedTimes(s)");
            newSheet.addCell(queryNumber);
            newSheet.addCell(queryTime);
            int i = 1;
            for (int r = 0; r < sheet.getRows(); r++) {
                if (sheet.getCell(2, r).getContents().equals("")) {
                    Label phase = new Label(0, i, (sheet.getCell(1, r).getContents()));
                    newSheet.addCell(phase);
                    Number doubles = new Number(1, i, Double.parseDouble(sheet.getCell(9, r).getContents()));
                    newSheet.addCell(doubles);
                    i++;
                }
            }
            wwb.write();
            wwb.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
}
