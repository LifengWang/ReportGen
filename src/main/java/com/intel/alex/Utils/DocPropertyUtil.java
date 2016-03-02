package com.intel.alex.Utils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lifeng on 3/1/16.
 */
public class DocPropertyUtil {

    public Map<String, Object> getDocProperty() {
        Map<String, Object> dpMap = new HashMap<String, Object>();
        dpMap.put("date", this.getDate());
        return dpMap;
    }

    //Get the current date as the date when generate the document
    private String getDate() {
        LocalDate today = LocalDate.now();
        return today.toString();
    }
}
