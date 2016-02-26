package com.intel.alex.Utils;


import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Created by root on 16-2-19.
 */
public class ZipUtil {

    public String unzip(String zipFile, String destination) {
        String[] s = zipFile.split("/");
        String[] s2 = s[s.length - 1].split(".zip");
        String dir = s2[s2.length - 1];
        try {
            ZipFile zf = new ZipFile(zipFile);
            zf.extractAll(destination);
        } catch (ZipException e) {
            e.printStackTrace();
        }
        System.out.println(destination + zipFile);

        return destination + "/" + dir;
    }

    public void unzip(String zipFile) {
        try {
            ZipFile zf = new ZipFile(zipFile);
            zf.extractAll(this.getClass().getResource("").getPath());
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}
