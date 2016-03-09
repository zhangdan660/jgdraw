package com.nbtoptec.symbol.status;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;
import com.nbtoptec.symbol.status.dialog.DelStatusDialog;
import com.nbtoptec.symbol.status.dialog.NewStatusDialog;
import com.nbtoptec.symbol.status.dialog.WarnDialog;

import fr.itris.glips.svgeditor.DrawUnit;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.SymbolMap;
import fr.itris.glips.svgeditor.actions.toolbar.ToolsFrame;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class SymbolStatus  extends ModuleAdapter{
	
	/**
	 * the ids of the module
	 */
	final private String idstatus = "SymbolStatus";

	/**
	 * the labels
	 */
	private String labelstatus = "";

	/**
	 * the editor
	 */
	private Editor editor = null;
	
	/**
	 * the panel in which the widgets panel will be inserted
	 */
	private StatusPane statusPanel;
	
	/**
	 * the frame into which the properties panel will be inserted
	 */
	private ToolsFrame statusFrame;

	/**
	 * the bounds of the tool frame
	 */
	@SuppressWarnings("unused")
	private Rectangle frameBounds = null;

	/**
	 * the bundle used to get labels
	 */
	private ResourceBundle bundle = null;
	
	/**
	 * the dialog used to specify the parameters for the new status to create
	 */
	private NewStatusDialog newDialog;
	
	/**
	 * the dialog used to del symbol status
	 */
	private DelStatusDialog delDialog;
	
	/**
	 * the pop menu
	 */
	private JPopupMenu popupMenu = new JPopupMenu();
	
	/**
	 * saving menuItems used to create popupMenu
	 */
	private HashMap<String,JMenuItem>  popmenus = null;
	
	/**
	 * menus
	 */
	private HashMap<String, JMenuItem> menuItems = new HashMap<String, JMenuItem>();

	/**
	 * point to the current svgHandler
	 */
	private SVGHandle lastHandle;
	
	public SymbolStatus(Editor editor) {
		this.editor = editor;

		// gets the labels from the resources
		bundle = ResourcesManager.bundle;

		if (bundle != null) {
			try {
				labelstatus = bundle.getString("label_status");
			} catch (Exception ex) {
			}
		}

		// 创建普通菜单
		createMenuItems();
		
		final HandlesListener svgHandlesListener = new HandlesListener() {

			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {
				
				final SVGHandle handle = getSVGEditor().getHandlesManager().getCurrentHandle();
				
				// if a new svg document has been created, the defs element is
				// added, and a new resource state object is added to the stateMap
				if (handle != lastHandle) {
					
					lastHandle = handle;
					
					if (statusPanel.isVisible() || handle == null) {
						
						handleStatus(handle);
						
					}
				}
			}
		};

		
		// adds the svg handles change listener
		editor.getHandlesManager().addHandlesListener(svgHandlesListener);
		
		statusPanel= new StatusPane(this);
		
		statusFrame = new ToolsFrame(editor, idstatus, labelstatus, statusPanel);

		// getting the preferred bounds
		frameBounds = editor.getPreferredWidgetBounds("status");
		
		//创建右键弹出式菜单
		createPopMenuItems();

		// setting the visibility changes handler
		Runnable visibilityRunnable = new Runnable() {

			public void run() {

				SVGHandle svgHandle = getSVGEditor().getHandlesManager().getCurrentHandle();

				if(svgHandle != null)
				{
					svgHandle.getSymbolStatusManager().setPopupMenu(popupMenu);		//添加右键弹出菜单
				}

				if (!statusFrame.isVisible() || svgHandle == null) {

					handleStatus(null);
					

				} else {

					handleStatus(svgHandle);
				}
				
			}
		};

		this.statusFrame.setVisibilityChangedRunnable(visibilityRunnable);
		
		//creating the dialog used to specify the parameters for the new files to create
		if(Editor.getParent() instanceof Frame){
			
			newDialog=new NewStatusDialog(this, (Frame)Editor.getParent());
			
//			delDialog=new DelStatusDialog(this, (Frame)Editor.getParent());
			
		}else if(Editor.getParent() instanceof JDialog){
			
			newDialog=new NewStatusDialog(this, (JDialog)Editor.getParent());
			
//			delDialog=new DelStatusDialog(this, (Frame)Editor.getParent());
		}
	}

	/**
	 * manages the display of the symbol panel
	 * 
	 * @param handle
	 *            the current svg handle
	 */
	protected void handleStatus(SVGHandle handle) {

		if (!statusFrame.isVisible()) {

			handle = null;
		}

		statusPanel.removeAllStatus();
		
		if (handle != null && statusFrame.isVisible()) {
			
			statusPanel.addStatus(handle);
			
			statusPanel.revalidate();
			
		}
	}
	
	
	private void createMenuItems() {

		String[] ids = { "StatusNew", "StatusDel", "Edit"};
		
		// creating the arrays
		String[] labels = new String[ids.length];
		
		Icon[] icons = new Icon[ids.length];
		
		Icon[] disabledIcons = new Icon[ids.length];
		
		// creating the items and setting their properties
		ActionListener listener = null;

		for (int i = 0; i < ids.length; i++) {
			// getting the labels
			labels[i] = ResourcesManager.bundle.getString(ids[i] + "ItemLabel");

			// getting the icons
			icons[i] = ResourcesManager.getIcon(ids[i], false);
			
			disabledIcons[i] = ResourcesManager.getIcon(ids[i], true);
			
			// createing the menu
			JMenuItem menu = new JMenuItem();

			// creating the listener
			listener = new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					if(editor.getHandlesManager().getCurrentHandle()!= null)
					{
						
						String cmd = e.getActionCommand();
						
						if("新建".equalsIgnoreCase(cmd))
						{
							lastHandle.getSymbolStatusManager().getSnapshot().command= "New";
							
							newDialog.setStatusIdText("");
							
							newDialog.setStatusNameText("");
							
							newDialog.showDialog(null);
						
						} else if("删除".equalsIgnoreCase(cmd))
						{
							
							lastHandle.getSymbolStatusManager().getSnapshot().command= "Del";

							delDialog=new DelStatusDialog((SymbolStatus)Editor.getEditor().getModule(idstatus), (Frame)Editor.getParent());
							
							delDialog.showDialog(null);
							
						} else if("编辑".equalsIgnoreCase(cmd))
						{
							
							lastHandle.getSymbolStatusManager().getSnapshot().command = "Edit";
							
							StatusAction sa = lastHandle.getSymbolStatusManager().getStatusAction();
							
							String  symbolId = sa.getTarget().getId();
							
							newDialog.setStatusIdText(symbolId.substring(symbolId.lastIndexOf("_")+1));
							
							newDialog.setStatusNameText(sa.getLocaldoc().getDocumentElement().getAttribute("status"));
							
							newDialog.showDialog(null);
						}
					}
				}
				
			};

			menu.addActionListener(listener);

			// setting the properties of the menu items
			menu.setText(labels[i]);
			
			menu.setIcon(icons[i]);
			
			menu.setDisabledIcon(disabledIcons[i]);
			
			menuItems.put(ids[i], menu);
		}
	
	}
	
	public void createPopMenuItems()
	{
		
		//创建 右键弹出菜单,添加隐藏菜单
		menuItems = getMenuItems();
			
		popmenus = new HashMap<String,JMenuItem>();
		
		addpop("StatusNew",menuItems.get("StatusNew"));
		
		addpop("StatusDel",menuItems.get("StatusDel"));
		
		popupMenu.addSeparator();
		
		addpop("Edit",menuItems.get("Edit"));
		
		
		popupMenu.addSeparator();
		
		addpop("ToolFrame_SymbolStatus",menuItems.get("ToolFrame_SymbolStatus"));
		
		popmenus.get("StatusNew").setEnabled(true);
		
		popmenus.get("StatusDel").setEnabled(true);
		
		popmenus.get("Edit").setEnabled(true);
			
	}
	
	private void addpop(String key,JMenuItem item) {
		
		JMenuItem menu = new JMenuItem();
		
		menu.addActionListener(item.getActionListeners()[0]);
		
		// setting the properties of the menu items
		menu.setText(item.getText());
		
		menu.setIcon(item.getIcon());
		
		menu.setDisabledIcon(item.getDisabledIcon());
		
		menu.setEnabled(item.isEnabled());
		
		popupMenu.add(menu);
		
		popmenus.put(key, menu);
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {
		
		HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
		
		map.putAll(menuItems);
		
		map.put("ToolFrame_" + this.idstatus, statusFrame.getMenuItem());
		
		return map;
	}
	

	@Override
	public HashMap<String, AbstractButton> getToolItems() {
		
		HashMap<String, AbstractButton> map = new HashMap<String, AbstractButton>();
		
		map.put(idstatus, statusFrame.getToolBarButton());
		
		return map;
	}
	
	/**
	 * 添加元件新状态,修改元件编号
	 */
	public void editStatus(String statusName, String newStatusId)
	{
		if("New".endsWith(lastHandle.getSymbolStatusManager().getSnapshot().command))
		{
//			System.out.println(Snapshot.groupName+"\t" + Snapshot.symbolName+"_"+ statusId);
			if(SymbolMap.docmap.containsKey(lastHandle.getSymbolStatusManager().getSnapshot().groupName) )
			{
				if(checkDuplicateID(newStatusId))
				{
					return;
				}
			}
			
			System.out.println("create new symobl stauts!");
			
		    AddStatus(statusName, newStatusId, null);
			
		} else if("Edit".equalsIgnoreCase(lastHandle.getSymbolStatusManager().getSnapshot().command)){
			
			System.out.println("edit symbol stauts!");

			String symbolId = lastHandle.getSymbolStatusManager().getSymboId();
			
			Document rootdoc = lastHandle.getSymbolStatusManager().getStatusAction().getLocaldoc();
			
			String statusid = symbolId.substring(symbolId.lastIndexOf("_")+1);
			
			if(!statusid.equalsIgnoreCase(newStatusId))	//改状态值
			{
				//ID 是否已经被使用
				if(checkDuplicateID(newStatusId))
				{
					return;
				}
			} 
			
			AddStatus(statusName, newStatusId, rootdoc);
		}
	}
	
	/**
	 * 删除元件状态
	 */
	public void delStatus()
	{
		if( "Del".equalsIgnoreCase(lastHandle.getSymbolStatusManager().getSnapshot().command))
		{
			
			System.out.println("delete status");

			int index =  lastHandle.getSymbolStatusManager().getSeq();
			if(index >=0 )
			{
				String symbolId  = lastHandle.getSymbolStatusManager().getSymboId();
				String groupName = lastHandle.getSymbolStatusManager().getSnapshot().groupName;
				
				lastHandle.getSymbolStatusManager().delete(index);
				
				SymbolMap.docmap.get(groupName).remove(symbolId);
				
				if(symbolId.equalsIgnoreCase(lastHandle.getSymbolStatusManager().getSnapshot().symbolId))
				{
					//被删除的状态是正在显示的，则刷新canvas
					if(lastHandle.getSymbolStatusManager().getStatuslist().size()>=1)
					{
						StatusAction sa = lastHandle.getSymbolStatusManager().getStatuslist().get(0);
						
						lastHandle.getSymbolStatusManager().getSnapshot().symbolId = sa.getTarget().getId();
						
						refreshCanvas(sa.getLocaldoc());
					}
				}
				
				//刷新状态面板
				lastHandle.getSymbolStatusManager().refresh();
			}
		}
	}
	
	public  boolean  checkDuplicateID(String statusId)
	{
		if(SymbolMap.docmap.get(lastHandle.getSymbolStatusManager().getSnapshot().groupName).containsKey(lastHandle.getSymbolStatusManager().getSnapshot().symbolName+"_"+ statusId))
		{
			
			System.out.println("此ID已经被使用，无法创建重复ID!");
			
			WarnDialog wd = new WarnDialog((Frame)Editor.getParent());
			
			wd.showDialog(null,null);
			
			return true;
		}
		
		return false;
	}
	
	public void AddStatus(String statusName, String statusId, Document doc)
	{
		Document rootdoc = SymbolMap.newDocument();		//每个状态对应的doc对象
		
		if(doc != null)
		{
			rootdoc = doc;
		} 
		
		Element root = rootdoc.getDocumentElement();
		
		root.setAttribute("status", statusName);		//每个状态的状态名
		
		
		if(doc != null)			
		{
			//修改状态属性
			StatusAction sa = lastHandle.getSymbolStatusManager().getStatusAction();
			
			String newSymbolId = lastHandle.getSymbolStatusManager().getSnapshot().symbolName+"_" + statusId;
			
			sa.getTarget().setName(statusName+"("+statusId+")");		//状态名
			
			sa.getTarget().setId(newSymbolId);							//元件id
			
			lastHandle.getSymbolStatusManager().update(sa, lastHandle.getSymbolStatusManager().getSeq());
			
			SymbolMap.docmap.get(lastHandle.getSymbolStatusManager().getSnapshot().groupName).remove(lastHandle.getSymbolStatusManager().getSymboId());
			
			SymbolMap.docmap.get(lastHandle.getSymbolStatusManager().getSnapshot().groupName).put(newSymbolId, rootdoc);
			
		} else {
			
			lastHandle.getSymbolStatusManager().getSnapshot().statusName = statusName;
			
			lastHandle.getSymbolStatusManager().getSnapshot().symbolId = lastHandle.getSymbolStatusManager().getSnapshot().symbolName+"_"+statusId;
			
			//新增加状态
			DrawUnit du = new DrawUnit();
			
			du.setName(statusName+"("+statusId+")");		//状态名
			
			du.setId(lastHandle.getSymbolStatusManager().getSnapshot().symbolId);								//元件id
			
			du.setIcon(SymbolMap.Doc2Icon(rootdoc, new Dimension(64, 64)));		//状态图标
			
			lastHandle.getSymbolStatusManager().insert(new StatusAction(du, editor.getHandlesManager().getCurrentHandle(), rootdoc));
			
			SymbolMap.docmap.get(lastHandle.getSymbolStatusManager().getSnapshot().groupName).put(lastHandle.getSymbolStatusManager().getSnapshot().symbolId, rootdoc);

			refreshCanvas(rootdoc);
		}
		
		//刷新状态面板
		lastHandle.getSymbolStatusManager().refresh();
		
	}
	
	/**
	 * 刷新Canvas
	 */
	public void refreshCanvas(Document localdoc)
	{
		if(localdoc != null)
		{
			
			Document svg = lastHandle.getCanvas().getDocument();
			removeAllChild(svg.getDocumentElement());
			copyNode(localdoc, svg);
			lastHandle.getCanvas().requestRepaintContent();
			lastHandle.getSelection().setSelectionSubMode(0);
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
	

	public Editor getSVGEditor() {
		
		return editor;
	}

	public SVGHandle getLastHandle() {
		return lastHandle;
	}

	public void setLastHandle(SVGHandle lastHandle) {
		this.lastHandle = lastHandle;
	}

	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public String getName()
	{
		return idstatus;
	}
}
