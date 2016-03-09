package fr.itris.glips.svgeditor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DecomposeSymbol {
	
	ArrayList<String> symbollist = new ArrayList<String>();
	HashMap<String, String> symbolmap = new HashMap<String, String>();
	
	String xmlhead = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
	String svghead = "<svg viewBox=\"0 0 32 32\" width=\"32\" height=\"32\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:cge=\"http://www.cim.com\" >";
	String svghead2 = "<svg>";
	String svgtail= "</svg>";
	
	public void split(String sourceStr)
	{
		if(sourceStr.contains("symbol>"))
		{
			int index = sourceStr.indexOf("symbol>") + 7;
			symbollist.add(sourceStr.substring(0, index).replaceAll("xlink:href", "xlinkhref"));
			String tmp = sourceStr.substring(0, index);
			String tmp2 = tmp.replaceAll("xlink:href", "xlinkhref");
			Document doc = Str2XML(xmlhead + svghead2 + tmp2 + svgtail);
			if(doc !=null)
			{
				Element e = (Element) doc.selectSingleNode("/svg/symbol");
				System.out.println(e.attributeValue("id"));
				if(e.attributeValue("id") != null)
				{
					symbolmap.put(e.attributeValue("id"), xmlhead + svghead + tmp.substring(tmp.indexOf(">")+1, tmp.lastIndexOf("<"))+ svgtail);
				}
			}
			split(sourceStr.substring(index));
		}
	}
	
	
	/**
	 * 将Xml字符串转成Document对象
	 * 
	 * @param String
	 * @return Document
	 */
	public static Document Str2XML(String strXMLParams)
	{
		// String 转流对象
		Document doc =null;
		SAXReader reader = new SAXReader();
		try
		{
			doc = reader.read(new ByteArrayInputStream(strXMLParams
			    .getBytes("utf8")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return doc;
	}
	
	public static void main(String[] args) throws Exception
	{
		File f = new File("D:\\symbol.svg");
		BufferedReader br = new BufferedReader(new FileReader(f));
		StringBuffer sb = new StringBuffer();
		String line = null;
		while((line = br.readLine())!=null)
		{
//			System.out.println(line);
			sb.append(line);
		}
		br.close();
		DecomposeSymbol ds = new DecomposeSymbol();
		if(sb.toString().contains("<symbol"))
		{
			int startdex = sb.toString().indexOf("symbol")-1;
			ds.split(sb.toString().substring(startdex));
		}

		
//		SVG2JPG  trans = new SVG2JPG(Util.Str2XML2(ds.xmlhead + ds.svghead + ds.symbollist.get(1)+ds.svgtail));
//		trans.createImage(new Dimension(
//	        	(int)16, (int)16), false);
	}
}
