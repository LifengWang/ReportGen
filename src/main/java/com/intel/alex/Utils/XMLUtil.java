package com.intel.alex.Utils;


/**
 * Created by root on 16-2-17.
 */

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class XMLUtil {
    public static List<Map<String, String>> parseXMLFile(File f) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(f);
        NodeList clusterNodes = document.getChildNodes();
        List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
        for (int i = 0; i < clusterNodes.getLength(); i++) {
            Node cNode = clusterNodes.item(i);
            NodeList cNodeInfo = cNode.getChildNodes();
            for (int j = 0; j < cNodeInfo.getLength(); j++) {
                Node node = cNodeInfo.item(j);
                if (!isTextNode(node)) {
                    NodeList cNodeMeta = node.getChildNodes();
                    Map<String, String> nodeMap = new HashMap<String, String>();
                    for (int k = 0; k < cNodeMeta.getLength(); k++) {
                        Node meta = cNodeMeta.item(k);
                        if (!isTextNode(meta)) {
                            String key = meta.getNodeName();
                            String value = meta.getTextContent();
                            nodeMap.put(key, value);
                        }
                    }
                    ret.add(nodeMap);
                }
            }
        }
        return ret;
    }

    private static boolean isTextNode(Node n) {
        return n.getNodeType() == Node.TEXT_NODE;
    }

}
