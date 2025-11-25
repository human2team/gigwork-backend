package com.example.gigwork.util;

import com.example.gigwork.model.Region;
import java.util.ArrayList;
import java.util.List;

public class RegionParser {
    public static List<Region> parse(String xml) {
        List<Region> regions = new ArrayList<>();
        try {
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(new java.io.ByteArrayInputStream(xml.getBytes("UTF-8")));
            org.w3c.dom.NodeList rows = doc.getElementsByTagName("row");
            for (int i = 0; i < rows.getLength(); i++) {
                org.w3c.dom.Element row = (org.w3c.dom.Element) rows.item(i);
                String code = getTagValue("region_cd", row);
                String name = getTagValue("locatadd_nm", row);
                String sido = getTagValue("sido_cd", row);
                String sgg = getTagValue("sgg_cd", row);
                String umd = getTagValue("umd_cd", row);
                if (code != null && name != null && sido != null && sgg != null && umd != null) {
                    regions.add(new Region(code, name, sido, sgg, umd));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return regions;
    }

    private static String getTagValue(String tag, org.w3c.dom.Element element) {
        org.w3c.dom.NodeList nlList = element.getElementsByTagName(tag);
        if (nlList != null && nlList.getLength() > 0) {
            org.w3c.dom.Node node = nlList.item(0);
            if (node != null && node.getFirstChild() != null) {
                return node.getFirstChild().getNodeValue();
            }
        }
        return null;
    }
}
