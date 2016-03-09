package fr.itris.glips.svgeditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.VerticalLayout;
import org.w3c.dom.Document;

import com.nbtoptec.symbol.status.dialog.DelSymbolDialog;
import com.nbtoptec.symbol.status.dialog.EidtorSymbolDialog;
import com.nbtoptec.symbol.status.dialog.Warn2Dialog;

import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class SymbolManager {
	/**
	 * create menu
	 * @return
	 */
	
	final Dimension dim = new Dimension(32,32);  //icon的尺寸
	 
	private Document symbol = null;
	 
	public HashMap<JXTaskPane, ArrayList<DrawAction>> taskmap = new HashMap<JXTaskPane, ArrayList<DrawAction>>();
	
	public HashMap<String, HashMap<String, Document>> docmap = new HashMap<String, HashMap<String,Document>>();
	
	public HashMap<String, JXTaskPane> groupmap = new HashMap<String, JXTaskPane>();
	
	public JXTaskPaneContainer container;
	
	/**
	 * the pop menu
	 */
	public JPopupMenu popupMenu = new JPopupMenu();
	
	/**
	 * saving menuItems used to create popupMenu
	 */
	private HashMap<String,JMenuItem>  popmenus = null;
	
	/**
	 * menus
	 */
	private HashMap<String, JMenuItem> menuItems = new HashMap<String, JMenuItem>();
	
	private DelSymbolDialog delDialog ;
	
	private EidtorSymbolDialog editDialog ;

	public JPanel createMenuPanle()
	{
		createMenuItems();
		
		//创建右键弹出式菜单
		createPopMenuItems();
		
		//create panel contained serarch and menu
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		//create the menu tree
		container = new JXTaskPaneContainer(){
			
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
		
		SymbolMap.menumap.clear();
		
		for(String groupname : SymbolMap.grouplist)
		{
			JXTaskPane taskpane = groupmap.get(groupname);
			container.add(taskpane);
			
			ArrayList<DrawAction> list = task.get(taskpane);
			for(DrawAction action : list)
			{
			    Component c = 	taskpane.add(action);
				new SymbolPopMenu(c, popupMenu);		//绑定鼠标右键事件
				SymbolMap.menumap.put(c, action.getName());
			}
		}
		
		//搜索框
		JGFindBar searchPanel = new JGFindBar(taskmap, container, popupMenu);
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
		docmap = SymbolMap.docmap;
		
		ArrayList<Group> groups = new ArrayList<Group>();
		
		for(String groupname : SymbolMap.grouplist)
		{
			Group group = new Group();
			group.setGroupName(groupname);
			groups.add(group);
			
			for( Entry<String, Document> e2 : docmap.get(groupname).entrySet())
			{
				
				EUnit unit = new EUnit();
				unit.setName(e2.getKey());
				unit.setShortDescription(e2.getKey());
				unit.setImage(SVG2BufferedImage.createImage(dim, e2.getValue()));
				group.add(unit);
				
			}
		}
		
		groupmap.clear();		//清空缓存
		
		for(Group g : groups)
		{
			if(g.getList().size() > 0)
			{
				JXTaskPane taskPane = new JXTaskPane();
				taskPane.setTitle(g.getGroupName());
				taskmap.put(taskPane, new ArrayList<DrawAction>());
				
				groupmap.put(g.getGroupName(), taskPane);	//用于菜单排序
				
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
		}
		
		return taskmap;
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
					if("新建".equalsIgnoreCase(e.getActionCommand()))
					{
						Editor.getEditor().getIOManager().getFileNewManager().
						askForNewFileParameters(null);
						
					} else if ("删除".equalsIgnoreCase(e.getActionCommand()))
					{
						if(SymbolMap.currentMenu != null)
						{
							if(Editor.getEditor().getHandlesManager().getHandlesInName().contains(SymbolMap.currentMenu))
							{
								//待修改的元件处于打开状态，提示关闭
								Warn2Dialog wd = new Warn2Dialog(null);
								wd.showDialog(null);
								return;
							}
							
							delDialog  = new DelSymbolDialog(Editor.getEditor().getSymbolManager(), (Frame)Editor.getParent());
							
							delDialog.showDialog(null);
						}
					} else if("编辑".equalsIgnoreCase(e.getActionCommand()))
					{
						if(Editor.getEditor().getHandlesManager().getHandlesInName().contains(SymbolMap.currentMenu))
						{
							//待修改的元件处于打开状态，提示关闭
							Warn2Dialog wd = new Warn2Dialog(null);
							wd.showDialog(null);
							return;
						}
						
						editDialog = new EidtorSymbolDialog(Editor.getEditor().getSymbolManager(), (Frame)Editor.getParent());
						
						editDialog.setGroupnameText(SymbolMap.IDmap.get(SymbolMap.currentMenu+"_0"));
						
						editDialog.setSymbolnameText(SymbolMap.currentMenu);
						
						editDialog.showDialog(null);
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

	public HashMap<String, JMenuItem> getMenuItems() {
		
		HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
		
		map.putAll(menuItems);
		
		return map;
	}
	
	public void edit(String newGroupName, String newSymbolName)
	{
		ArrayList<String> list = new ArrayList<String>();		//记录匹配currentMenu的 symbolId
		ArrayList<String> tmplist = new ArrayList<String>();			//记录匹配newSymbolName的symbolId
		
		if(!SymbolMap.docmap.containsKey(newGroupName))
		{
			SymbolMap.docmap.put(newGroupName, new HashMap<String, Document>());
			
			SymbolMap.grouplist.add(newGroupName);
		}

		//查找选中菜单对应的symbolid列表
		for(Entry<String, String> entry : SymbolMap.IDmap.entrySet())
		{
			if(entry.getKey().startsWith(SymbolMap.currentMenu+"_"))
			{
				list.add(entry.getKey());
			}
		}

		String groupname = SymbolMap.getGroupName();
		if(groupname != null && !newGroupName.equalsIgnoreCase(groupname))
		{
			//修改元件大类的名字,SymMap.docmap,IDmap
			for(String str : list)
			{
				//修改docmap
				Document doc = SymbolMap.docmap.get(groupname).get(str);
				
				SymbolMap.docmap.get(groupname).remove(str);
				
				SymbolMap.docmap.get(newGroupName).put(str, doc);
				
				SymbolMap.IDmap.put(str, newGroupName);
			}
			
		}
		if (!newSymbolName.equalsIgnoreCase(SymbolMap.currentMenu))
		{
			//修改元件名SymMap.docmap,IDmap
			HashMap<String, Document> docmap =SymbolMap.docmap.get(newGroupName);
			
			for(String str : list)
			{
				Document doc = docmap.get(str);
				
				docmap.remove(str);
				
				docmap.put(str.replaceAll(SymbolMap.currentMenu, newSymbolName), doc);
				
				//修改IDmap
				SymbolMap.IDmap.remove(str);
				
				SymbolMap.IDmap.put(str.replaceAll(SymbolMap.currentMenu, newSymbolName), newGroupName);

				tmplist.add(str.replaceAll(SymbolMap.currentMenu, newSymbolName));
				
				//修改shapemap
				SymbolMap.shapemap.put(str.replaceAll(SymbolMap.currentMenu, newSymbolName), SymbolMap.shapemap.get(str));
			}
			
		}
		if(!newGroupName.equalsIgnoreCase(groupname) || !newSymbolName.equalsIgnoreCase(SymbolMap.currentMenu))
		{
			update();
		}
	}
	
	/**
	 * 刷新菜单栏
	 */
	public void update()
	{
		container.removeAll();
		container.revalidate();
		taskmap.clear();
		SymbolMap.menumap.clear();
		
		HashMap<JXTaskPane, ArrayList<DrawAction>> task = loadMenuProperties(); 
		
		for(String groupname : SymbolMap.grouplist)
		{
			JXTaskPane taskpane = groupmap.get(groupname);
			if(taskpane != null)
			{
				container.add(taskpane);
				
				ArrayList<DrawAction> list = task.get(taskpane);
				for(DrawAction action : list)
				{
					Component c = 	taskpane.add(action);
					new SymbolPopMenu(c, popupMenu);		//绑定鼠标右键事件
					SymbolMap.menumap.put(c, action.getName());
				}
			}
		}
		container.revalidate();
		container.repaint();
	}
	
	/**
	 * 删除元件菜单项
	 */
	public void delete()
	{
		
		//从SymbolMap.docmap移除对应的菜单项
		ArrayList<String> matchlist = new ArrayList<String>();
		for(Entry<String, HashMap<String, Document>> entry : SymbolMap.docmap.entrySet())
		{
			for(Entry<String, Document> sym : entry.getValue().entrySet())
			{
				if(sym.getKey().startsWith(SymbolMap.currentMenu+"_"))
				{
					matchlist.add(sym.getKey());		//记录符合的元件
				}
			}
			
			for(String matchname : matchlist)
			{
				entry.getValue().remove(matchname);	//移除符合的元件
			}
			
			matchlist.clear();
		}
		
		update();
		
	}

	/*public void delete2()
	{
		container.removeAll();		//清空菜单
		SymbolMap.menumap.clear();
		HashMap<JXTaskPane, Integer> tmpmap = new HashMap<JXTaskPane, Integer>(); //记录菜单单元项个数
		
		for(Entry<JXTaskPane, ArrayList<DrawAction>> entry : taskmap.entrySet())
		{
			boolean match = false;
			JXTaskPane taskpane = entry.getKey();
			taskpane.removeAll();
			ArrayList<DrawAction> list = entry.getValue();
			ArrayList<DrawAction> tmplist = new ArrayList<DrawAction>(); //保存待删除的菜单项
			for(DrawAction action : list)
			{
				if(!SymbolMap.currentMenu.equalsIgnoreCase(action.getName()))
				{
					
					Component c = 	taskpane.add(action);
					new SymbolPopMenu(c, Editor.getEditor().getSymbolManager().popupMenu);		//绑定鼠标右键事件
					SymbolMap.menumap.put(c, action.getName());
					
					match = true;
				} else{
					tmplist.add(action);
				}
				
			}
			if(match)
			{
				Editor.getEditor().getSymbolManager().container.add(taskpane);
				taskpane.revalidate();
				Editor.getEditor().getSymbolManager().container.revalidate();
			}
			list.removeAll(tmplist);
			
			tmpmap.put(taskpane, list.size());
		}
		for(Entry<JXTaskPane, Integer> entry : tmpmap.entrySet())
		{
			if(entry.getValue() == 0)
			{
				taskmap.remove(entry.getKey());
			}
		}
		
		Editor.getEditor().getSymbolManager().container.repaint();
		
		//从SymbolMap.docmap移除对应的菜单项
		ArrayList<String> matchlist = new ArrayList<String>();
		for(Entry<String, HashMap<String, Document>> entry : SymbolMap.docmap.entrySet())
		{
			for(Entry<String, Document> sym : entry.getValue().entrySet())
			{
				if(sym.getKey().startsWith(SymbolMap.currentMenu+"_"))
				{
					matchlist.add(sym.getKey());		//记录符合的元件
				}
			}
			
			for(String matchname : matchlist)
			{
				entry.getValue().remove(matchname);	//移除符合的元件
			}
			
			matchlist.clear();
		}
	}*/
	
	public Document getSymbol() {
		return symbol;
	}

	public void setSymbol(Document symbol) {
		this.symbol = symbol;
	}

}
