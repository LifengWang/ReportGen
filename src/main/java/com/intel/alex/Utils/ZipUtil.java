package com.intel.alex.Utils;


import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Created by root on 16-2-19.
 */
public class ZipUtil {

    public void unzip(String zipFile, String destination) {
        try {
            ZipFile zf = new ZipFile(zipFile);
            zf.extractAll(destination);
        } catch (ZipException e) {
            e.printStackTrace();
        }
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
