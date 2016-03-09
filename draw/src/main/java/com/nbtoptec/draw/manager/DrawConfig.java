package com.nbtoptec.draw.manager;


import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DrawConfig {
	
	public static String addr;
	
	public static String user;
	
	public static String passwd;
	
	public static String remotePath;
	
	public static String localPath;
	
	static {
		loadSymbol(); // 初始化
	}
	
	public static void loadSymbol()
	{
		SAXReader reader = new SAXReader();  
        // 通过read方法读取一个文件 转换成Document对象  
        try {
			Document document = reader.read(new File("draw.xml"));
			if(document != null)
			{
				 //获取根节点元素对象  
				Element e = (Element) document.selectSingleNode("/configs/server");
				
				addr = e.selectSingleNode("addr").getText();
				
				user = e.selectSingleNode("user").getText();
				
				passwd = e.selectSingleNode("passwd").getText();
				
				remotePath = e.selectSingleNode("remotepath").getText();
				
				localPath = e.selectSingleNode("localpath").getText();
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
}
