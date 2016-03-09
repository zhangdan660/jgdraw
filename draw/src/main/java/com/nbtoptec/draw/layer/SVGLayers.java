package com.nbtoptec.draw.layer;

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
import javax.swing.JMenuItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.actions.toolbar.ToolsFrame;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class SVGLayers extends ModuleAdapter {
	/**
	 * the ids of the module
	 */
	final private String idlayers = "Layers";

	/**
	 * the labels
	 */
	private String labellayers = "";

	/**
	 * the editor
	 */
	private Editor editor = null;

	/**
	 * the panel in which the widgets panel will be inserted
	 */
	private LayersPane layersPanel;

	/**
	 * the frame into which the properties panel will be inserted
	 */
	private ToolsFrame layersFrame;

	/**
	 * the bounds of the tool frame
	 */
	private Rectangle frameBounds = null;

	/**
	 * the bundle used to get labels
	 */
	private ResourceBundle bundle = null;
	

	/**
	 * menus
	 */
	private HashMap<String, JMenuItem> menuItems = new HashMap<String, JMenuItem>();

	private SVGHandle lastHandle;
	
	public SVGLayers(Editor editor) {
		this.editor = editor;

		// gets the labels from the resources
		bundle = ResourcesManager.bundle;

		if (bundle != null) {
			try {
				labellayers = bundle.getString("label_layers");
			} catch (Exception ex) {
			}
		}

		createMenuItems();
		
		final HandlesListener svgHandlesListener = new HandlesListener() {

			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {

				// removes all the components in the panel of the resources
//				layersPanel.removeAllLayers();

				final SVGHandle handle = getSVGEditor().getHandlesManager().getCurrentHandle();

				// if a new svg document has been created, the defs element is
				// added,
				// and a new resource state object is added to the stateMap
				if (handle != lastHandle) {
					lastHandle = handle;
					if (layersPanel.isVisible() || handle == null) {
						handleLayers(handle);
					}
				}
			}
		};

		
		// adds the svg handles change listener
		editor.getHandlesManager().addHandlesListener(svgHandlesListener);

		layersPanel = new LayersPane(this);
		
		layersFrame = new ToolsFrame(editor, idlayers, labellayers, layersPanel);

		// getting the preferred bounds
		frameBounds = editor.getPreferredWidgetBounds("layers");
		
		// setting the visibility changes handler
		Runnable visibilityRunnable = new Runnable() {

			public void run() {

				SVGHandle svgHandle = getSVGEditor().getHandlesManager().getCurrentHandle();

				if (!layersFrame.isVisible() || svgHandle == null) {

					handleLayers(null);

				} else {

					handleLayers(svgHandle);
				}
			}
		};

		this.layersFrame.setVisibilityChangedRunnable(visibilityRunnable);
	}

	/**
	 * manages the display of the layer panel
	 * 
	 * @param handle
	 *            the current svg handle
	 */
	protected void handleLayers(SVGHandle handle) {

		if (!layersFrame.isVisible()) {

			handle = null;
		}

		// removes all the components of the panel of the resources
		 layersPanel.removeAllLayers();

		if (handle != null && layersFrame.isVisible()) {
			layersPanel.addLayers(handle.getLayerManager().getLayerlist());
			layersPanel.setPreferredSize(new Dimension(frameBounds.width, frameBounds.height));
			layersPanel.revalidate();
		}
	}

	private void createMenuItems() {
		
		String[] ids = { "LayerNew", "LayerDel", "LayerUp", "LayerDown", "LayerTop", "LayerBottom" };
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
						doAction(e.getActionCommand());
					}
				}
				
				public void doAction(String command)
				{
					
					if("新建...".equalsIgnoreCase(command))		//新建图层
					{
						String layname = "图层"+editor.getHandlesManager().getCurrentHandle().getLayerManager().getID();
						Document doc = lastHandle.getCanvas().getDocument();
						Element g = doc.createElementNS(doc.getDocumentElement().getNamespaceURI(),"g");
						g.setAttributeNS(null,"layer", layname);
						doc.getDocumentElement().appendChild(g);
						
						//新建图层
						SVGLayer layer = new SVGLayer();
						layer.setEdit(true);
						layer.setVisible(true);
						layer.setName(layname);
						layer.setElement(g);
						
						addLayer(layer, 0);			//加入到SVGLayerManager队列
						layersPanel.addLayer(layer);
						
//						addLayer(layer, editor.getHandlesManager().getCurrentHandle().getLayerManager().getSize());
						layersPanel.setLayernum(editor.getHandlesManager().getCurrentHandle().getLayerManager().getSize()-1);
					} else if("删除".equalsIgnoreCase(command))	//删除图层
					{		
						//删除图层
						Element g = lastHandle.getLayerManager().getLayer(layersPanel.getLayernum()).getElement();		//找到图层对应的element
						if(layersPanel.getLayerCount() == 1)
						{
							//剩下唯一图层，不能删除提示
							DeleteLayerDialog dialog = new DeleteLayerDialog((Frame)Editor.getParent());
							dialog.showDialog(null, null);
							return;
						} else {
							//删除图层时，弹出确认提示
							DeleteLayerWarnDialog warn = new DeleteLayerWarnDialog((Frame)Editor.getParent());
							warn.showDialog(null, null);
							if(!warn.isDel())
							{
								return;
								
							}
						}
						
						System.out.println(layersPanel.getLayernum() + "\t删除图层");
						/*NodeList list = g.getChildNodes(); 		//先删除图层上的节点
						for(int i=0; i< list.getLength(); i++)
						{
							g.removeChild(list.item(i));
						}*/
						lastHandle.getCanvas().getDocument().getDocumentElement().removeChild(g);		//移除g图层对应 elememt
						lastHandle.getCanvas().requestRepaintContent();									//激活图形重绘
						removeLayer(layersPanel.getLayernum());		//从SVGLayerManager移除
						layersPanel.removeLayer(layersPanel.getLayernum());
						
						layersPanel.setLayernum(0);
					} else if("移到顶层".equalsIgnoreCase(command))
					{
						System.out.println(layersPanel.getLayernum() + "\t移到顶层");
						Element g = lastHandle.getLayerManager().getLayer(layersPanel.getLayernum()).getElement();		//找到图层对应的element
						Node parent = g.getParentNode();			//获取父节点
						Node firstg = parent.getFirstChild().getNextSibling();  //获取第一个g
						lastHandle.getCanvas().getDocument().getDocumentElement().removeChild(g);		//移除g图层对应 elememt
						parent.insertBefore(g, firstg);
						
						bringLayerTop(layersPanel.getLayernum());
						layersPanel.bringLayerTop(layersPanel.getLayernum());
						
						layersPanel.setLayernum(editor.getHandlesManager().getCurrentHandle().getLayerManager().getSize()-1);
						
					} else if("上移一层".equalsIgnoreCase(command))
					{
						
						System.out.println(layersPanel.getLayernum() + "\t上移一层");
						Element g = lastHandle.getLayerManager().getLayer(layersPanel.getLayernum()).getElement();		//找到图层对应的element
						Node parent = g.getParentNode();				//获取父节点
						Node nextg = g.getNextSibling().getNextSibling();
						lastHandle.getCanvas().getDocument().getDocumentElement().removeChild(g);		//移除g图层对应 elememt
						parent.insertBefore(g, nextg);
						
						bringLayerDown(layersPanel.getLayernum());
						layersPanel.bringLayerUp(layersPanel.getLayernum());
						
						layersPanel.setLayernum(layersPanel.getLayernum()-1);
						
					} else if("移到底层".equalsIgnoreCase(command))
					{
					
						System.out.println(layersPanel.getLayernum() + "\t移到底层");
						Element g = lastHandle.getLayerManager().getLayer(layersPanel.getLayernum()).getElement();		//找到图层对应的element
						Node svgroot = g.getParentNode();			//获取父节点
						lastHandle.getCanvas().getDocument().getDocumentElement().removeChild(g);		//移除g图层对应 elememt
						svgroot.appendChild(g);
						
						bringLayerBottom(layersPanel.getLayernum());
						layersPanel.bringLayerBottom(layersPanel.getLayernum());
						
						layersPanel.setLayernum(0);
						
					} else if("下移一层".equalsIgnoreCase(command))
					{
						
						System.out.println(layersPanel.getLayernum() + "\t下移一层");
						Element g = lastHandle.getLayerManager().getLayer(layersPanel.getLayernum()).getElement();		//找到图层对应的element
						Node parent = g.getParentNode();			//获取父节点
						Node preg = g.getPreviousSibling();
						lastHandle.getCanvas().getDocument().getDocumentElement().removeChild(g);		//移除g图层对应 elememt
						parent.insertBefore(g, preg);
						
						bringLayerUp(layersPanel.getLayernum());
						layersPanel.bringLayerDown(layersPanel.getLayernum());
						
						layersPanel.setLayernum(layersPanel.getLayernum()+1);
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

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {
		
		HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
		map.putAll(menuItems);
		map.put("ToolFrame_" + this.idlayers, layersFrame.getMenuItem());
		return map;
	}

	@Override
	public HashMap<String, AbstractButton> getToolItems() {
		
		HashMap<String, AbstractButton> map = new HashMap<String, AbstractButton>();
		map.put(idlayers, layersFrame.getToolBarButton());
		return map;
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

	public SVGLayer getLayer(int index) {
		
		return editor.getHandlesManager().getCurrentHandle().getLayerManager().getLayer(index);
	}

	public void addLayer(SVGLayer layer, int index) {
		
		editor.getHandlesManager().getCurrentHandle().getLayerManager().addLayer(layer, index);
	}

	public void removeLayer(int index) {
		editor.getHandlesManager().getCurrentHandle().getLayerManager().removeLayer(index);
	}

	public void bringLayerTop(int index) {
		
		editor.getHandlesManager().getCurrentHandle().getLayerManager().bringLayerTop(index);
	}

	public void bringLayerBottom(int index) {
		
		editor.getHandlesManager().getCurrentHandle().getLayerManager().bringLayerBottom(index);
	}

	public void bringLayerUp(int index) {
		
		editor.getHandlesManager().getCurrentHandle().getLayerManager().bringLayerUp(index);
	}

	public void bringLayerDown(int index) {
		
		editor.getHandlesManager().getCurrentHandle().getLayerManager().bringLayerDown(index);
	}
	
}
