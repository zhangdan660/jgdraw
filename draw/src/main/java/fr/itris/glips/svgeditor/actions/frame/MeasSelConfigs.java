package fr.itris.glips.svgeditor.actions.frame;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * 读取查询数据的配置信息
 * 
 * @author zmm
 *
 */
public final class MeasSelConfigs {

	private Map<String, String> sqlMap;

	public MeasSelConfigs() {

		Document doc = null;
		Node node;
		try {
			doc = ResourcesManager.getXMLDocument("sqlTemplate.xml");
			XPathFactory f = XPathFactory.newInstance();
			XPath path = f.newXPath();
			NodeList list = (NodeList) path.evaluate("sqlMap/select", doc,
					XPathConstants.NODESET);

			sqlMap = new HashMap<String, String>();
			int len = list.getLength();
			for (int i = 0; i < len; i++) {
				node = list.item(i);
				sqlMap.put(node.getAttributes().getNamedItem("id")
						.getNodeValue(), node.getTextContent());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {

		}
	}

	public Map<String, String> getSqlMap() {
		return sqlMap;
	}

}
