package com.intel.alex.Utils;


/**
 * Created by root on 16-2-17.
 */

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class XMLUtil {
    private final String xmlDir;

    public XMLUtil(String xmlDir) {
        this.xmlDir = xmlDir;
    }

    //public Map<String, Object> parseXMLFile(File xmlFile) throws Exception {
    public Map<String, Object> parseXMLFile() {
//        String xmlPath = xmlDir + ;
        File xmlFile = new File(xmlDir + "/info_end.xml");
        Map<String, Object> xmlMap = new HashMap<String, Object>();
        int clusterSize = 0;
        //int i =0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(xmlFile));
            String line;


            //List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();

            while ((line = bufferedReader.readLine()) != null) {
                String s[] = line.split("[<>]");
                if (s[1].equals("node")) {
                    clusterSize++;
                    continue;
                }
                if (s[1].equals("platform")) {
                    //i++;
                    if (!s[2].equals(xmlMap.get(s[2]))) {
                        xmlMap.put("pf", s[2]);
                    }
                    continue;
                }
                if (s[1].equals("frequency")) {
                    if (!s[2].equals(xmlMap.get(s[2]))) {
                        xmlMap.put("CPU", s[2]);
                    }
                    continue;
                }
                if (s[1].equals("memory")) {
                    if (!s[2].equals(xmlMap.get(s[2]))) {
                        xmlMap.put("Memory", s[2]);
                    }
                    continue;
                }
                if (s[1].equals("network")) {
                    if (!s[2].equals(xmlMap.get(s[2]))) {
                        xmlMap.put("Network", s[2]);
                    }
                    continue;
                }
                if (s[1].equals("system-release")) {
                    if (!s[2].equals(xmlMap.get(s[2]))) {
                        xmlMap.put("OS", s[2]);
                    }
                    continue;
                }
                if (s[1].equals("kernel-release")) {
                    if (!s[2].equals(xmlMap.get(s[2]))) {
                        xmlMap.put("Kernel", s[2]);
                    }
                    continue;
                }
                if (s[1].equals("jdk")) {
                    if (!s[2].equals(xmlMap.get(s[2]))) {
                        xmlMap.put("JDK", s[2]);
                    }
                    continue;
                }
                if (s[1].equals("python")) {
                    if (!s[2].equals(xmlMap.get(s[2]))) {
                        xmlMap.put("Python", s[2]);
                    }
                    continue;
                }
                if (s[1].equals("Hadoop/YARN/HDFS")) {
                    if (!s[2].equals(xmlMap.get(s[2]))) {
                        xmlMap.put("Hadoop", s[2]);
                    }
                    continue;
                }
                if (s[1].equals("spark")) {
                    if (!s[2].equals(xmlMap.get(s[2]))) {
                        xmlMap.put("Spark", s[2]);
                    }
                    continue;
                }
                if (s[1].equals("hive")) {
                    if (!s[2].equals(xmlMap.get(s[2]))) {
                        xmlMap.put("Hive", s[2]);
                    }
                    continue;
                }
                if (s[1].equals("disk_total")) {
                    if (!s[2].equals(xmlMap.get(s[2]))) {
                        xmlMap.put("Disk", s[2]);
                    }
                    continue;
                }
                if (s[1].equals("CDH")) {
                    if (!s[2].equals(xmlMap.get(s[2]))) {
                        xmlMap.put("CDH", s[2]);
                    }

                }


            }
//            System.out.println(clusterSize);
            xmlMap.put("ClusterSize", clusterSize);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xmlMap;

    }
}


//public class XMLUtil {
//    public static List<Map<String, String>> parseXMLFile(File f) throws Exception {
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder db = dbf.newDocumentBuilder();
//        Document document = db.parse(f);
//        NodeList clusterNodes = document.getChildNodes();
//        List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
//        for (int i = 0; i < clusterNodes.getLength(); i++) {
//            Node cNode = clusterNodes.item(i);
//            NodeList cNodeInfo = cNode.getChildNodes();
//            for (int j = 0; j < cNodeInfo.getLength(); j++) {
//                Node node = cNodeInfo.item(j);
//                if (!isTextNode(node)) {
//                    NodeList cNodeMeta = node.getChildNodes();
//                    Map<String, String> nodeMap = new HashMap<String, String>();
//                    for (int k = 0; k < cNodeMeta.getLength(); k++) {
//                        Node meta = cNodeMeta.item(k);
//                        if (!isTextNode(meta)) {
//                            String key = meta.getNodeName();
//                            String value = meta.getTextContent();
//                            nodeMap.put(key, value);
//                        }
//                    }
//                    ret.add(nodeMap);
//                }
//            }
//        }
//        return ret;
//    }
//
//    private static boolean isTextNode(Node n) {
//        return n.getNodeType() == Node.TEXT_NODE;
//    }
//
//}
