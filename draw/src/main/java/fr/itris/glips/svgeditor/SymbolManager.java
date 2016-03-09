package fr.itris.glips.svgeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.util.SAXDocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Namespace;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.VerticalLayout;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

public class SymbolManager {
	/**
	 * create menu
	 * @return
	 */
	
	final Dimension dim = new Dimension(32,32);  //icon的尺寸
	 
	private Document symbol = null;
	
	private String style = null;
	 
	public HashMap<JXTaskPane, ArrayList<DrawAction>> taskmap = new HashMap<JXTaskPane, ArrayList<DrawAction>>();
	
	HashMap<String, HashMap<String, Document>> docmap = new HashMap<String, HashMap<String,Document>>();
	

	public JPanel createMenuPanle()
	{
		//create panel contained serarch and menu
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		//create the menu tree
		JXTaskPaneContainer container = new JXTaskPaneContainer(){
			
			private static final long serialVersionUID = 1L;

			// Issue #1189-swingx: reasonable implementation of block increment
			@Override
			public int getScrollableBlockIncrement(Rectangle visibleRect,
					int orientation, int direction) {
				
				return SwingConstants.VERTICAL == orientation ? visibleRect.height :
					visibleRect.width;
			}
		};
		container.setLayout(new VerticalLayout(0));
		container.setBorder(BorderFactory.createEmptyBorder());
		
		HashMap<JXTaskPane, ArrayList<DrawAction>> task = loadMenuProperties();
		
		LoadStyle(); //导入样式
		
		for(Entry<JXTaskPane, ArrayList<DrawAction>> entry : task.entrySet())
		{
			JXTaskPane taskpane = entry.getKey();
			container.add(taskpane);
			
			ArrayList<DrawAction> list = entry.getValue();
			for(DrawAction action : list)
			{
				taskpane.add(action);
			}
		}
		
		//搜索框
		JGFindBar searchPanel = new JGFindBar(taskmap, container);
		panel.add(searchPanel,BorderLayout.NORTH );
		
		JScrollPane menu = new JScrollPane(container);
		menu.setLayout(new ScrollPaneLayout());
		panel.add(menu, BorderLayout.CENTER);
		return panel;
	}
	
	/**
	 *    读取元件类，生成菜单目录
	 */
	public HashMap<JXTaskPane, ArrayList<DrawAction>> loadMenuProperties()
	{
		docmap = loadSymbol();
		ArrayList<Group> groups = new ArrayList<Group>();
		
		for( Entry<String, HashMap<String, Document>> e : docmap.entrySet())
		{
			
			Group group = new Group();
			group.setName(e.getKey());
			groups.add(group);
			
			for( Entry<String, Document> e2 : e.getValue().entrySet())
			{
				
				EUnit unit = new EUnit();
				unit.setName(e2.getKey());
				unit.setShortDescription(e2.getKey());
				unit.setImage(SVG2BufferedImage.createImage(dim, e2.getValue()));
				group.add(unit);
				
			}
		}
		
		for(Group g : groups)
		{
			JXTaskPane taskPane = new JXTaskPane();
			taskPane.setTitle(g.getName());
			taskmap.put(taskPane, new ArrayList<DrawAction>());
			
			for(EUnit e : g.getList())
			{
				if(e.getName().endsWith("0"))			//只显示 状态为0的元件
				{
					DrawUnit du = new DrawUnit();
					du.setId(e.getName());
					du.setIcon(e.getIcon());
					String name = e.getName().substring(0, e.getName().lastIndexOf("_"));
					if(name.length()>6 && name.contains(":"))
					{
						du.setName(name.split(":")[1]);
					} else {
						du.setName(name);
					}
					du.setShortDescription(e.getShortDescription());
					
					taskmap.get(taskPane).add(new DrawAction(du));
				}
			}
		}
		
		return taskmap;
	}
	
	/**
	 * 读取symbol.svg文件 ,转成BufferedImage
	 */
	public HashMap<String, HashMap<String, Document>> loadSymbol()
	{
		HashMap<String, HashMap<String, Document>> symbolmap = new HashMap<String, HashMap<String,Document>>();
		
		Document doc = File2XML("symbol.svg");
		
		setSymbol(doc);
		
		if(doc != null)
		{
			
			try {
				Element root = doc.getDocumentElement();
				
				for(Node defs = root.getFirstChild(); defs != null ; defs = defs.getNextSibling())
				{
					//找到 defs节点
					if("defs".equalsIgnoreCase(defs.getNodeName()))
					{
						for(Node symbol = defs.getFirstChild(); symbol != null; symbol = symbol.getNextSibling())
						{
							if("symbol".equalsIgnoreCase(symbol.getNodeName()))
							{
								Element e = (Element) symbol;
								
								String name = e.getAttribute("name");
								
								String content = Node2Str(e);
								
								if(name !=null && !name.isEmpty() && !symbolmap.containsKey(name))
								{
									
									symbolmap.put(name, new HashMap<String, Document>());
									
								}
								
								if(content != null)
								{
									
//									SVG2JPG  trans = new SVG2JPG(Str2XML(content.trim()));		//创建doc转jpg的对象
									
									if(symbolmap.containsKey(name))
									{
										
										String id = e.getAttribute("id");
										
										symbolmap.get(name).put(id,  Str2XML(content.trim()));
										
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
		
		return symbolmap;
	}
	
	/**
	 * 读取style样式
	 */
	public void LoadStyle()
	{
		try {
			
			SAXReader reader = new SAXReader();             
			org.dom4j.Document   document = reader.read(new File("style.svg"));
			
			if(document != null)
			{
				org.dom4j.Element e = (org.dom4j.Element) document.selectSingleNode("/svg/defs/style");
//				System.out.println(e.getText());
				style = e.getText();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从svg节点内容提取组装xmlstr
	 * @return
	 */
	public String Node2Str(Element e)
	{
		org.dom4j.Document doc = DocumentHelper.createDocument(); // 建立documnet对象
		org.dom4j.Element root = doc.addElement("svg","http://www.w3.org/2000/svg");
		
		root.addAttribute("viewBox", e.getAttribute("viewBox"));
		root.add(new Namespace("xlink", "http://www.w3.org/1999/xlink"));
		
		String[] box = e.getAttribute("viewBox").split(",");
		if(box.length !=4)
		{
			box = e.getAttribute("viewBox").split("\\s");
		}
		if(box.length == 4)
		{
			root.addAttribute("width",  box[2]);
			root.addAttribute("height", box[3]);
		} else {
			root.addAttribute("width",  "64");
			root.addAttribute("height", "64");
		}
		
		try {
			for(Node shape = e.getFirstChild(); shape != null ; shape = shape.getNextSibling())
			{
				if(shape instanceof org.apache.batik.dom.GenericText)
					continue;
				
				Element shapeobj = (Element) shape;
				
				NamedNodeMap attrs = shapeobj.getAttributes();
				
				if(attrs != null)
				{
					org.dom4j.Element de = root.addElement(shapeobj.getNodeName());
					
					for (int i = 0; i < attrs.getLength(); i++) {
						
						Attr attr = (Attr)attrs.item(i);
						
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
	 * 将Xml字符串转成Document对象
	 * 
	 * @param String
	 * @return Document
	 */
	public static SVGDocument Str2XML(String strXMLParams)
	{
		Document doc = null;
		
	     try {
	    	 
	    	 SAXDocumentFactory sax = new SAXDocumentFactory(SVGDOMImplementation.getDOMImplementation(), "");
	    	 InputStream is  = new ByteArrayInputStream(strXMLParams .getBytes("utf8"));
//	         InputSource inp = new InputSource(is);
	         doc =  sax.createDocument("",is);
                              			
		} catch (Exception e) {
			e.printStackTrace();
		}
	     
		return (SVGDocument)doc;
	}
	
	/**
	 * 将文件转成Document对象
	 * 
	 * @param String
	 * @return Document
	 */
	public static SVGDocument File2XML(String filename)
	{
		Document doc = null;
		
	     try {
	    	 
	    	 SAXDocumentFactory sax = new SAXDocumentFactory(SVGDOMImplementation.getDOMImplementation(), "");
//	    	 InputStream is  = new FileInputStream(filename);
//	         InputSource inp = new InputSource(is);
	         doc =  sax.createDocument(filename);
                              			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (SVGDocument)doc;
	}
	
	/**
	 * dom4j 将Document对象转成Xml字符串
	 * 
	 * @param Document
	 * @return String
	 */
	public static String XML2Str(org.dom4j.Document doc)
	{
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf8");
		StringWriter sw = new StringWriter();
		XMLWriter xw = new XMLWriter(sw, format);
		
		try
		{
			xw.write(doc);
			xw.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			xw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		xw = null;
		try
		{
			sw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return sw.toString();
	}
	
	
	public Document getSymbol() {
		return symbol;
	}

	public void setSymbol(Document symbol) {
		this.symbol = symbol;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
