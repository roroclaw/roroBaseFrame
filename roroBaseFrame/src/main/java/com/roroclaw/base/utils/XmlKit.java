package com.roroclaw.base.utils;

import org.dom4j.Document;
import org.dom4j.Node;

public class XmlKit {
	
	public static String getNodeText(Document document,String xpath){
		String text = "";
		Node node = document.selectSingleNode(xpath);
		if(node != null){
			text = node.getText();
		}
		return text;
	}
}
