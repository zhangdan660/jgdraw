package fr.itris.glips.svgeditor;


import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SVGUtil
{

	public static Document merge(Map<String, String> pathMap) throws Exception
	{
		if (pathMap == null || pathMap.isEmpty())
			return null;

		String stylePath = pathMap.get("stylePath");
		String symbolPath = pathMap.get("symbolPath");
		String layerPath = pathMap.get("layerPath");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document styleDoc = builder.parse(stylePath);
		Document symbolDoc = builder.parse(symbolPath);
		Document layerDoc = builder.parse(layerPath);
		NodeList symbolList = symbolDoc.getElementsByTagName("symbol");
		NodeList layerList = layerDoc.getElementsByTagName("svg").item(0).getChildNodes();

		Document doc = builder.newDocument();
		Element svg = (Element) doc.importNode(styleDoc.getElementsByTagName("svg").item(0), true);
		Node defs = svg.getElementsByTagName("defs").item(0);
		if (symbolList != null && symbolList.getLength() > 0)
		{
			for (int i = 0; i < symbolList.getLength(); i++)
			{
				defs.appendChild(doc.importNode(symbolList.item(i), true));
			}
		}
		svg.appendChild(defs);
		if (layerList != null && layerList.getLength() > 0)
		{
			for (int i = 0; i < layerList.getLength(); i++)
			{
				svg.appendChild(doc.importNode(layerList.item(i), true));
			}
		}
		doc.appendChild(svg);
		return doc;
	}


	public static String toString(Document doc) throws Exception
	{
		StringWriter sw = new StringWriter();
		Transformer serializer = TransformerFactory.newInstance().newTransformer();
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		serializer.transform(new DOMSource(doc), new StreamResult(sw));
		return sw.toString();
	}


	public static Document convertVml(Document doc) throws Exception
	{

		Map<String, List<String>> symbolIdMap = new HashMap<String, List<String>>();
		NodeList symbolNodes = doc.getElementsByTagName("symbol");
		Element symbolEl = null;
		String key = null;
		int endIndex = 0;
		for (int i = 0; i < symbolNodes.getLength(); i++)
		{
			symbolEl = (Element) symbolNodes.item(i);
			endIndex = symbolEl.getAttribute("id").indexOf("_");
			if (endIndex == -1)
				continue;
			key = symbolEl.getAttribute("id").substring(0, endIndex);
			if (!symbolIdMap.containsKey(key))
				symbolIdMap.put(key, new ArrayList<String>());
			symbolIdMap.get(key).add("#" + symbolEl.getAttribute("id"));
		}

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		Document docForVml = (Document) doc.cloneNode(true);
		NodeList refList = (NodeList) xpath.evaluate("//ref[@psr_id!='']", doc, XPathConstants.NODESET);
		if (refList.getLength() > 0)
		{
			Element g = null, g1 = null, use = null, use1 = null, refdata = null;
			List<String> ids = null;
			NodeList useList = null;
			for (int i = 0; i < refList.getLength(); i++)
			{
				refdata = (Element) refList.item(i);
				g = (Element) refdata.getParentNode().getParentNode();
				use = (Element) g.getElementsByTagName("use").item(0);
				if (use != null)
				{

					endIndex = use.getAttribute("xlink:href").indexOf("_");
					if (endIndex == -1)
						continue;
					key = use.getAttribute("xlink:href").substring(1, endIndex);
					if (symbolIdMap.containsKey(key))
					{
						g1 = (Element) xpath.evaluate("//g[contains(@id,'" + g.getAttribute("id") + "')]", docForVml, XPathConstants.NODE);
						useList = g1.getElementsByTagName("use");
						for (int k = 0; k < useList.getLength(); k++)
						{
							use1 = (Element) useList.item(k);
							use1.setAttribute("dis", "display:block");
							use1.setAttribute("key", refdata.getAttribute("psr_id"));
						}

						ids = symbolIdMap.get(key);
						for (int j = 0; j < ids.size(); j++)
						{
							if (use.getAttribute("xlink:href").equals(ids.get(j)))
								continue;
							Element useTemp = (Element) use.cloneNode(true);
							useTemp.setAttribute("xlink:href", ids.get(j));
							useTemp.setAttribute("dis", "display:none");
							useTemp.setAttribute("key", refdata.getAttribute("psr_id"));
							g1.appendChild(docForVml.importNode(useTemp, true));
						}
					}
				}
			}
		}
		return docForVml;
	}
}
