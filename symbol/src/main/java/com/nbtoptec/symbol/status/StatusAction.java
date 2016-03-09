package com.nbtoptec.symbol.status;

import java.awt.event.ActionEvent;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nbtoptec.symbol.status.dialog.Warn3Dialog;

import fr.itris.glips.svgeditor.AbstractHyperlinkAction;
import fr.itris.glips.svgeditor.DrawUnit;
import fr.itris.glips.svgeditor.SymbolMap;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;

@SuppressWarnings("serial")
public class StatusAction extends AbstractHyperlinkAction<DrawUnit>{
	
	private SVGHandle handle;
	
	private Document localdoc;
	
//	private DrawUnit du;
	
	public StatusAction(DrawUnit drawUnit, SVGHandle handle, Document document)
	{
		super(drawUnit);
		
		this.handle = handle;
		this.localdoc = document;
	}
	
	@Override
	protected void installTarget() {
		if (getTarget() == null) return;
		setSmallIcon(getTarget().getIcon());
		setName(getTarget().getName());
//		setShortDescription(getTarget().getShortDescription());
	}
	 
	@Override
	public void actionPerformed(ActionEvent evt) {

		if(handle != null)
		{
			//提示是否先保存状态
			if(handle.isModified())
			{
				//修改状态未保存
				Warn3Dialog wd = new Warn3Dialog(null);
				wd.showDialog(null);
				if(wd.isFlag())
				{
					Snapshot snapshot = handle.getSymbolStatusManager().getSnapshot();
					
					SymbolMap.docmap.get(snapshot.groupName).put(snapshot.symbolId, deepClone(handle.getCanvas().getDocument()));
					
					SymbolMap.shapemap.put(snapshot.symbolId, SymbolMap.toString(handle.getCanvas().getDocument()));	//修改到左侧菜单栏
					
					handle.getSymbolStatusManager().getStatuslist().get(
							handle.getSymbolStatusManager().getIdlist().indexOf(snapshot.symbolId)).setLocaldoc(
									deepClone(handle.getCanvas().getDocument()));
					
					handle.getSymbolStatusManager().refresh();
					
					
					handle.setModified(false);
				}
			}
			
			
			Snapshot snapshot = handle.getSymbolStatusManager().getSnapshot();
			snapshot.symbolId = getTarget().getId();
			
			Document svg = handle.getCanvas().getDocument();
			removeAllChild(svg.getDocumentElement());
			copyNode(localdoc, svg);
			handle.getCanvas().requestRepaintContent();
			handle.setModified(false);		//画布有修改
			
//			System.out.println(SymbolMap.toString(handle.getCanvas().getDocument()));
		}
	}

	public void removeAllChild(Element svgroot)
	{
		
		NodeList nl = svgroot.getChildNodes();
		for( int i = nl.getLength()-1; i >= 0 ; i--)
		{
			svgroot.removeChild(nl.item(i));
		}
	}
	
	/**
	 * 
	 * @param src
	 * @param desc
	 * @return
	 */
	public Document copyNode(Document src, Document desc)
	{
		//设置状态名
		desc.getDocumentElement().setAttribute("status", src.getDocumentElement().getAttribute("status"));
		
		for(Node shape = src.getDocumentElement().getFirstChild(); shape != null ; shape = shape.getNextSibling())
		{
			if(shape instanceof org.apache.batik.dom.GenericText)
				continue;
			
			Element shapeobj = (Element) shape;
			
			Element newe = null;
			if("line".equalsIgnoreCase(shapeobj.getNodeName()) || "polygon".equalsIgnoreCase(shapeobj.getNodeName()))
			{
				newe = desc.createElementNS(shapeobj.getNamespaceURI(), "path");
				
			} else if("circle".equalsIgnoreCase(shapeobj.getNodeName()))
			{
				newe = desc.createElementNS(shapeobj.getNamespaceURI(), "ellipse");
				
			} else{
				
				newe = desc.createElementNS(shapeobj.getNamespaceURI(), shapeobj.getNodeName());
			}
				
				NamedNodeMap attrs = shapeobj.getAttributes();
				
				if(attrs != null)
				{
					
					for (int i = 0; i < attrs.getLength(); i++) {
						
						Attr attr = (Attr)attrs.item(i);
						
						if(SymbolMap.specialShapeAtt.contains(attr.getName()))
						{
							if("r".equalsIgnoreCase(attr.getName()))
							{
								newe.setAttribute("rx", attr.getValue());
								newe.setAttribute("ry", attr.getValue());
							} else if("x1".equalsIgnoreCase(attr.getName()) ||
									"y1".equalsIgnoreCase(attr.getName()) ||
									"x2".equalsIgnoreCase(attr.getName()) ||
									"y2".equalsIgnoreCase(attr.getName()))
							{
								newe.setAttribute("d", "M " +shapeobj.getAttribute("x1")+" "
										+ shapeobj.getAttribute("y1") +" L " + shapeobj.getAttribute("x2")
										+" " + shapeobj.getAttribute("y2"));
							} else if("points".equalsIgnoreCase(attr.getName()))
							{
				
								newe.setAttribute("d", "M" + attr.getValue().replaceAll("\\s", " L ").replaceAll(",", " ") +"Z");
							}
							
						} else {
							
							newe.setAttribute(attr.getName(), attr.getValue());
						}
					}
				}
				
				desc.getDocumentElement().appendChild(newe);
				
		}
		return desc;
	}
	
	/**
	 * deep clone
	 * @param snapshot 
	 * @param srcdoc
	 * @return
	 */
	public Document deepClone(Document src)
	{
		
		Document desc = SymbolMap.newDocument();
		
		Element descroot = desc.getDocumentElement();
		
		Element srcroot = src.getDocumentElement();
		
		descroot.setAttribute("viewBox", srcroot.getAttribute("viewBox"));
		
		descroot.setAttribute("status", srcroot.getAttribute("status"));
		
		for(Node shape = src.getDocumentElement().getFirstChild(); shape != null ; shape = shape.getNextSibling())
		{
			if(shape instanceof org.apache.batik.dom.GenericText || "defs".equalsIgnoreCase(shape.getNodeName()))
				continue;
			
			Element shapeobj = (Element) shape;
			
			Element newe = desc.createElementNS(shapeobj.getNamespaceURI(), shapeobj.getNodeName());
			
			NamedNodeMap attrs = shapeobj.getAttributes();
			
			if(attrs != null)
			{
				
				for (int i = 0; i < attrs.getLength(); i++) {
					
					Attr attr = (Attr)attrs.item(i);
					
					newe.setAttribute(attr.getName(), attr.getValue());
				}
			}
			
			desc.getDocumentElement().appendChild(newe);
			
		}
		return desc;
		
	}

	public Document getLocaldoc() {
		return localdoc;
	}

	public void setLocaldoc(Document localdoc) {
		this.localdoc = localdoc;
	}

	public SVGHandle getHandle() {
		return handle;
	}
	
}
