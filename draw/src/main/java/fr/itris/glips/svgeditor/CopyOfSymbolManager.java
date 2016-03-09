package fr.itris.glips.svgeditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.util.SAXDocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Namespace;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.VerticalLayout;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.InputSource;

public class CopyOfSymbolManager {
	/**
	 * create menu
	 * @return
	 */
	
	final int IconSize = 32;			//icon的尺寸
	 
	private Document symbol = null;
	 
	public HashMap<JXTaskPane, ArrayList<DrawAction>> taskmap = new HashMap<JXTaskPane, ArrayList<DrawAction>>();
	

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
		HashMap<String, HashMap<String, BufferedImage>> imagemap = loadSymbol();
		ArrayList<Group> groups = new ArrayList<Group>();
		
		for( Entry<String, HashMap<String, BufferedImage>> e : imagemap.entrySet())
		{
			
			Group group = new Group();
			group.setName(e.getKey());
			groups.add(group);
			
			for( Entry<String, BufferedImage> e2 : e.getValue().entrySet())
			{
				
				EUnit unit = new EUnit();
				unit.setName(e2.getKey());
				unit.setImage(e2.getValue());
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
					du.setName(e.getName().substring(0, e.getName().lastIndexOf("_")));
					du.setShortDescription(e.getShortDescription());
					
					taskmap.get(taskPane).add(new DrawAction(du));
				}
			}
		}
		
		/*for(Group g : groups)
		{
			
			JXTaskPane taskPane = new JXTaskPane();
			taskPane.setTitle(g.getName());
			container.add(taskPane);
			
			for (EUnit e : g.getList())
			{
				if( e.getName().endsWith("0"))		//只显示 状态为0的元件
				{
					
					DrawUnit du = new DrawUnit();
					du.setId(e.getName());
					du.setIcon(e.getIcon());
					du.setName(e.getName().substring(0, e.getName().lastIndexOf("_")));
					du.setShortDescription(e.getShortDescription());
					
					taskPane.add(new DrawAction(du, taskPane));
					
				}
				
			}
			
		}*/
		
		return taskmap;
	}
	
	/**
	 * 读取symbol.svg文件 ,转成BufferedImage
	 */
	public HashMap<String, HashMap<String, BufferedImage>> loadSymbol()
	{
		HashMap<String, HashMap<String, BufferedImage>> symbolmap = new HashMap<String, HashMap<String,BufferedImage>>();
		
		Document doc = File2XML("symbol.xml");
		
		setSymbol(doc);
		
		if(doc != null)
		{
			
			try {
				
				XPathFactory f = XPathFactory.newInstance();
				
				XPath path = f.newXPath();
				
				NodeList list = (NodeList) path.evaluate("svg/defs/symbol", doc, XPathConstants.NODESET);
				
				for(int i=0; i< list.getLength(); i++)
				{
					
					Element e = (Element)list.item(i);				//获取symbol的id作为标识
					
					String name = e.getAttribute("name");
					
					String content = Node2Str(path, e);
					
//					System.out.println(content);

					if(name !=null && !name.isEmpty() && !symbolmap.containsKey(name))
					{
						
						symbolmap.put(name, new HashMap<String, BufferedImage>());
						
					}
					
					if(content != null)
					{
						
						
						if(symbolmap.containsKey(name))
						{
							
							String id = e.getAttribute("id");
							
//							symbolmap.get(name).put(id,  SVG2BufferedImage.createImage(new Dimension(IconSize, IconSize)));
					
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
	 * 从svg节点内容提取组装xmlstr,方法2
	 * @return
	 */
	
	public SVGDocument Node2Str2(XPath path, Element e)
	{
		DOMImplementation impl=SVGDOMImplementation.getDOMImplementation();
		String svgNS=SVGDOMImplementation.SVG_NAMESPACE_URI;
		SVGDocument doc=(SVGDocument)impl.createDocument(svgNS, "svg", null);
		
		//gets the root element (the svg element)
		Element svgRoot=doc.getDocumentElement();
		String[] box = e.getAttribute("viewBox").split(",");
		
		if(box.length == 4)
		{
			//set the width and height attribute on the root svg element
			svgRoot.setAttributeNS(null, "width", box[2]);
			svgRoot.setAttributeNS(null, "height", box[3]);
			svgRoot.setAttribute("viewBox","0 0 "+box[2]+" "+box[3]);
			
		}
		
		//creating a defs element
		Element defsElement=doc.createElementNS(doc.getNamespaceURI(), "defs");
		svgRoot.appendChild(defsElement);
		
		try {
			
			NodeList list = (NodeList) path.evaluate("circle", e, XPathConstants.NODESET);
			svgRoot = addElement(list, doc, svgRoot, "circle");
			
			list = (NodeList) path.evaluate("line", e, XPathConstants.NODESET);
			svgRoot = addElement(list, doc, svgRoot, "line");
			
			list = (NodeList) path.evaluate("rect", e, XPathConstants.NODESET);
			svgRoot = addElement(list, doc, svgRoot, "rect");
			
			list = (NodeList) path.evaluate("polygon", e, XPathConstants.NODESET);
			svgRoot = addElement(list, doc, svgRoot, "polygon");
			
			list = (NodeList) path.evaluate("path", e, XPathConstants.NODESET);
			svgRoot = addElement(list, doc, svgRoot, "path");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return doc;
	}
	
	/**
	 * 从list提取Node，加到svgRoot
	 * @param list
	 * @param svgRoot
	 */
	public Element addElement(NodeList list, SVGDocument doc, Element svgRoot, String nodename)
	{
		
		for(int i=0; i<list.getLength(); i++)
		{
			
			Element de = doc.createElement(nodename);
			svgRoot.appendChild(de);
			
			Element e = (Element) list.item(i);
			if(e.getAttribute("cx") != null && !e.getAttribute("cx").isEmpty())			//cx
			{
				de.setAttribute("cx", e.getAttribute("cx"));
			} 
			if(e.getAttribute("cy") != null && !e.getAttribute("cy").isEmpty())	//cy
			{
				de.setAttribute("cy", e.getAttribute("cy"));
			} 
			if(e.getAttribute("r") != null && !e.getAttribute("r").isEmpty())	//y
			{
				de.setAttribute("r", e.getAttribute("r"));
			} 
			if(e.getAttribute("x1") != null && !e.getAttribute("x1").isEmpty())
			{
				de.setAttribute("x1", e.getAttribute("x1"));
			}   
			if(e.getAttribute("x2") != null && !e.getAttribute("x2").isEmpty())
			{
				de.setAttribute("x2", e.getAttribute("x2"));
			}   
			if(e.getAttribute("y1") != null && !e.getAttribute("y1").isEmpty())
			{
				de.setAttribute("y1", e.getAttribute("y1"));
			}   
			if(e.getAttribute("y2") != null && !e.getAttribute("y2").isEmpty())
			{
				de.setAttribute("y2", e.getAttribute("y2"));
			} 
			if(e.getAttribute("x") != null && !e.getAttribute("x").isEmpty())
			{
				de.setAttribute("x", e.getAttribute("x"));
			}  
			if(e.getAttribute("y") != null && !e.getAttribute("y").isEmpty())
			{
				de.setAttribute("y", e.getAttribute("y"));
			}  
			if(e.getAttribute("width") != null && !e.getAttribute("width").isEmpty())
			{
				de.setAttribute("width", e.getAttribute("width"));
			}   
			if(e.getAttribute("height") != null && !e.getAttribute("height").isEmpty())
			{
				de.setAttribute("height", e.getAttribute("height"));
			}  
			if(e.getAttribute("points") != null && !e.getAttribute("points").isEmpty())
			{
				de.setAttribute("points", e.getAttribute("points"));
			}  
			if(e.getAttribute("d") != null && !e.getAttribute("d").isEmpty())
			{
				de.setAttribute("d", e.getAttribute("d"));
			} 
			if(e.getAttribute("stroke-width") != null && !e.getAttribute("stroke-width").isEmpty())		//stroke-width
			{
				de.setAttribute("stroke-width", e.getAttribute("stroke-width"));
			} 
			if(e.getAttribute("fill") != null && !e.getAttribute("fill").isEmpty())			//fill
			{
				de.setAttribute("fill", e.getAttribute("fill"));
			} 
			if(e.getAttribute("stroke") != null && !e.getAttribute("stroke").isEmpty())	//stroke
			{
				de.setAttribute("stroke", e.getAttribute("stroke"));
			}  
			if(e.getAttribute("Plane") != null && !e.getAttribute("Plane").isEmpty())	//Plane
			{
				de.setAttribute("Plane", e.getAttribute("Plane"));
			} 
			if(e.getAttribute("AFMask") != null && !e.getAttribute("AFMask").isEmpty())	//AFMask
			{
				de.setAttribute("AFMask", e.getAttribute("AFMask"));
			}  
			if(e.getAttribute("transform") != null && !e.getAttribute("transform").isEmpty())	//transform
			{
				de.setAttribute("transform", e.getAttribute("transform"));
			} 
		}
		
		return svgRoot;
	}
	
	/**
	 * 从svg节点内容提取组装xmlstr
	 * @return
	 */
	public String Node2Str(XPath path, Element e)
	{
		org.dom4j.Document doc = DocumentHelper.createDocument(); // 建立documnet对象
		org.dom4j.Element root = doc.addElement("svg","http://www.w3.org/2000/svg");
		
		root.addAttribute("viewBox", e.getAttribute("viewBox"));
		root.add(new Namespace("xlink", "http://www.w3.org/1999/xlink"));
		
		String[] box = e.getAttribute("viewBox").split(",");
		
		if( box.length  == 4)
		{
			root.addAttribute( "width", box[2]);
			root.addAttribute("height", box[3]);
		}
		
		try {
			
			NodeList list = (NodeList) path.evaluate("circle", e, XPathConstants.NODESET);
			root = addElement(root, "circle", list);
			
			list = (NodeList) path.evaluate("line", e, XPathConstants.NODESET);
			root = addElement(root, "line", list);
			
			list = (NodeList) path.evaluate("rect", e, XPathConstants.NODESET);
			root = addElement(root, "rect", list);
			
			list = (NodeList) path.evaluate("polygon", e, XPathConstants.NODESET);
			root = addElement(root, "polygon", list);
			
			list = (NodeList) path.evaluate("path", e, XPathConstants.NODESET);
			root = addElement(root, "path", list);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return XML2Str(doc);
	}
	
	/**
	 * 分解Nodelist，组装root
	 * @param root
	 * @param name
	 * @param list
	 */
	public org.dom4j.Element addElement(org.dom4j.Element root, String name, NodeList list)
	{
		for(int i=0; i<list.getLength(); i++)
		{
			
			Element e = (Element) list.item(i);
			org.dom4j.Element de = root.addElement(name);
			
			if(e.getAttribute("cx") != null && !e.getAttribute("cx").isEmpty())			//cx
			{
				de.addAttribute("cx", e.getAttribute("cx"));
			} 
			if(e.getAttribute("cy") != null && !e.getAttribute("cy").isEmpty())	//cy
			{
				de.addAttribute("cy", e.getAttribute("cy"));
			} 
			if(e.getAttribute("r") != null && !e.getAttribute("r").isEmpty())	//y
			{
				de.addAttribute("r", e.getAttribute("r"));
			} 
			if(e.getAttribute("x1") != null && !e.getAttribute("x1").isEmpty())
			{
				de.addAttribute("x1", e.getAttribute("x1"));
			}   
			if(e.getAttribute("x2") != null && !e.getAttribute("x2").isEmpty())
			{
				de.addAttribute("x2", e.getAttribute("x2"));
			}   
			if(e.getAttribute("y1") != null && !e.getAttribute("y1").isEmpty())
			{
				de.addAttribute("y1", e.getAttribute("y1"));
			}   
			if(e.getAttribute("y2") != null && !e.getAttribute("y2").isEmpty())
			{
				de.addAttribute("y2", e.getAttribute("y2"));
			} 
			if(e.getAttribute("x") != null && !e.getAttribute("x").isEmpty())
			{
				de.addAttribute("x", e.getAttribute("x"));
			}  
			if(e.getAttribute("y") != null && !e.getAttribute("y").isEmpty())
			{
				de.addAttribute("y", e.getAttribute("y"));
			}  
			if(e.getAttribute("width") != null && !e.getAttribute("width").isEmpty())
			{
				de.addAttribute("width", e.getAttribute("width"));
			}   
			if(e.getAttribute("height") != null && !e.getAttribute("height").isEmpty())
			{
				de.addAttribute("height", e.getAttribute("height"));
			}  
			if(e.getAttribute("points") != null && !e.getAttribute("points").isEmpty())
			{
				de.addAttribute("points", e.getAttribute("points"));
			}  
			if(e.getAttribute("d") != null && !e.getAttribute("d").isEmpty())
			{
				de.addAttribute("d", e.getAttribute("d"));
			} 
			if(e.getAttribute("stroke-width") != null && !e.getAttribute("stroke-width").isEmpty())		//stroke-width
			{
				de.addAttribute("stroke-width", e.getAttribute("stroke-width"));
			} 
			if(e.getAttribute("fill") != null && !e.getAttribute("fill").isEmpty())			//fill
			{
				de.addAttribute("fill", e.getAttribute("fill"));
			} 
			if(e.getAttribute("stroke") != null && !e.getAttribute("stroke").isEmpty())	//stroke
			{
				de.addAttribute("stroke", e.getAttribute("stroke"));
			}  
			if(e.getAttribute("Plane") != null && !e.getAttribute("Plane").isEmpty())	//Plane
			{
				de.addAttribute("Plane", e.getAttribute("Plane"));
			} 
			if(e.getAttribute("AFMask") != null && !e.getAttribute("AFMask").isEmpty())	//AFMask
			{
				de.addAttribute("AFMask", e.getAttribute("AFMask"));
			}  
			if(e.getAttribute("transform") != null && !e.getAttribute("transform").isEmpty())	//transform
			{
				de.addAttribute("transform", e.getAttribute("transform"));
			} 
		}
		
		return root;
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

	public static void main(String[] args) {
		
		CopyOfSymbolManager sm = new CopyOfSymbolManager();
		sm.loadSymbol();
		
	}
}
