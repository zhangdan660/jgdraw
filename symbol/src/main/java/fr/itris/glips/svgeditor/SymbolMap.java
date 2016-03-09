package fr.itris.glips.svgeditor;

import java.awt.Component;
import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.util.SAXDocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Namespace;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

import fr.itris.glips.library.util.XMLPrinter;

public class SymbolMap {

	final static String xmlns = "http://www.w3.org/2000/svg";

	final static String xmlink = "http://www.w3.org/1999/xlink";

	// 记录symbolID与doc的对应关系
	public static HashMap<String, HashMap<String, Document>> docmap = new HashMap<String, HashMap<String, Document>>();

	// 记录SymbolID与 Symbolgroup的对应关系
	public static HashMap<String, String> IDmap = new HashMap<String, String>();

	// 记录symbolID与doc的文本形式对应关系
	public static HashMap<String, String> shapemap = new HashMap<String, String>();

	// 记录 特殊的shape
	public static ArrayList<String> specialShapeAtt = new ArrayList<String>();

	// 记录左侧菜单栏的各个菜单项的名字
	public static HashMap<Component, String> menumap = new HashMap<Component, String>();

	// 记录元件大类的顺序
	public static ArrayList<String> grouplist = new ArrayList<String>();

	public static String currentMenu;

	// 用于删除元件状态
	public static String symbolid;

	public static String statusname;

	static {
		loadSymbol(); // 初始化
	}

	/**
	 * 读取symbol.svg文件 ,转成BufferedImage
	 */
	public static HashMap<String, HashMap<String, Document>> loadSymbol() {
		HashMap<String, HashMap<String, Document>> symbolmap = new HashMap<String, HashMap<String, Document>>();

		Document doc = File2XML("symbol.svg");

		if (doc != null) {

			try {
				Element root = doc.getDocumentElement();

				for (Node defs = root.getFirstChild(); defs != null; defs = defs.getNextSibling()) {
					// 找到 defs节点
					if ("defs".equalsIgnoreCase(defs.getNodeName())) {
						for (Node symbol = defs.getFirstChild(); symbol != null; symbol = symbol.getNextSibling()) {
							if ("symbol".equalsIgnoreCase(symbol.getNodeName())) {
								Element e = (Element) symbol;

								String gruopName = e.getAttribute("name");

								String content = Node2Str(e);

								if (gruopName != null && !gruopName.isEmpty() && !symbolmap.containsKey(gruopName)) {

									symbolmap.put(gruopName, new HashMap<String, Document>());

									grouplist.add(gruopName); // 用于菜单排序
								}

								if (content != null) {

									if (symbolmap.containsKey(gruopName)) {

										String symbolid = e.getAttribute("id");

										symbolmap.get(gruopName).put(symbolid, Str2XML(content.trim()));

										shapemap.put(symbolid, content);

										IDmap.put(symbolid, gruopName);

									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		SymbolMap.docmap = symbolmap;

		specialShapeAtt.add("r");
		specialShapeAtt.add("points");
		specialShapeAtt.add("x1");
		specialShapeAtt.add("y1");
		specialShapeAtt.add("x2");
		specialShapeAtt.add("y2");

		return symbolmap;
	}

	/**
	 * 从svg节点内容提取组装xmlstr
	 * 
	 * @return
	 */
	public static String Node2Str(Element e) {
		org.dom4j.Document doc = DocumentHelper.createDocument(); // 建立documnet对象
		org.dom4j.Element root = doc.addElement("svg", xmlns);

		root.addAttribute("viewBox", e.getAttribute("viewBox"));
		root.add(new Namespace("xlink", xmlink));
		root.addAttribute("status", e.getAttribute("status")); // 状态名

		String[] box = e.getAttribute("viewBox").split("[^0-9]");

		if (box.length == 4) {
			root.addAttribute("width", box[2]);
			root.addAttribute("height", box[3]);
		}else{
			System.out.println(e.getAttribute("viewBox"));
		}
		try {
			for (Node shape = e.getFirstChild(); shape != null; shape = shape.getNextSibling()) {
				if (shape instanceof org.apache.batik.dom.GenericText)
					continue;

				Element shapeobj = (Element) shape;

				NamedNodeMap attrs = shapeobj.getAttributes();

				if (attrs != null) {
					org.dom4j.Element de = root.addElement(shapeobj.getNodeName());

					for (int i = 0; i < attrs.getLength(); i++) {

						Attr attr = (Attr) attrs.item(i);

						de.addAttribute(attr.getName(), attr.getValue());
					}
				}
			}

		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
		}

		return XML2Str(doc);
	}

	/**
	 * 创建空白的doc，只包含长和宽
	 */
	public static Document newDocument() {

		org.dom4j.Document doc = DocumentHelper.createDocument(); // 建立documnet对象
		org.dom4j.Element root = doc.addElement("svg", xmlns);

		root.addAttribute("viewBox", "0,0,64,64");
		root.add(new Namespace("xlink", xmlink));

		root.addAttribute("width", "64");
		root.addAttribute("height", "64");

		return Str2XML(XML2Str(doc));
	}

	/**
	 * 将Xml字符串转成Document对象
	 * 
	 * @param String
	 * @return Document
	 */
	public static SVGDocument Str2XML(String strXMLParams) {
		Document doc = null;

		try {

			SAXDocumentFactory sax = new SAXDocumentFactory(SVGDOMImplementation.getDOMImplementation(), "");
			InputStream is = new ByteArrayInputStream(strXMLParams.getBytes("utf8"));
			// InputSource inp = new InputSource(is);
			doc = sax.createDocument("", is);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return (SVGDocument) doc;
	}

	/**
	 * 将文件转成Document对象
	 * 
	 * @param String
	 * @return Document
	 */
	public static SVGDocument File2XML(String filename) {
		Document doc = null;

		try {

			SAXDocumentFactory sax = new SAXDocumentFactory(SVGDOMImplementation.getDOMImplementation(), "");
			// InputStream is = new FileInputStream(filename);
			// InputSource inp = new InputSource(is);
			doc = sax.createDocument(filename);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (SVGDocument) doc;
	}

	/**
	 * dom4j 将Document对象转成Xml字符串
	 * 
	 * @param Document
	 * @return String
	 */
	public static String XML2Str(org.dom4j.Document doc) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf8");
		StringWriter sw = new StringWriter();
		XMLWriter xw = new XMLWriter(sw, format);

		try {
			xw.write(doc);
			xw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			xw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		xw = null;
		try {
			sw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

	/**
	 * 把documnet对象转成Icon对象
	 * 
	 * @param doc
	 * @param dim
	 * @return
	 */
	public static Icon Doc2Icon(Document doc, Dimension dim) {
		if (doc != null) {
			if (dim == null) {
				dim = new Dimension(32, 32);
			}

			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();

				ImageIO.write(SVG2BufferedImage.createImage(dim, doc), "png", out);

				return new ImageIcon(out.toByteArray());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 把Document对象转成字符串
	 * 
	 * @param doc
	 * @return
	 */
	public static String toString(Document doc) {

		// converts the svg document into xml strings
		StringBuffer buffer = new StringBuffer("");

		for (Node node = doc.getFirstChild(); node != null; node = node.getNextSibling()) {

			XMLPrinter.writeNode(node, buffer, 0, true, null);

			// System.out.println(buffer.toString());

		}
		return buffer.toString();
	}

	/**
	 * 右键点击菜单时，查找对应的元件大类名
	 * 
	 * @return
	 */
	public static String getGroupName() {
		for (Entry<String, String> entry : IDmap.entrySet()) {
			if (entry.getKey().startsWith(currentMenu + "_")) {
				return entry.getValue();
			}
		}
		return null;
	}

}
