package com.nbtoptec.draw.layer;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * 图层面板
 * 
 * @author ben
 *
 */
@SuppressWarnings("serial")
public class LayersPane extends JPanel {

	/**
	 * child table panel
	 */
	private JTable tablePane;

	/**
	 * the relative SVGLayers
	 */
	private SVGLayers svgLayers;

	private DefaultTableModel tableModel;
	
	private JPopupMenu popupMenu;
	
	private HashMap<String,JMenuItem>  menus = null;
	
	private HashMap<String,JMenuItem>  popmenus = null;
	
	private int layernum = 0;
	
	public LayersPane(SVGLayers svglayers) {
		
		this.svgLayers = svglayers;
		build();
	}

	/**
	 * 初始化
	 */
	private void build() {
		
		createTableModel();
		tablePane = new JTable(tableModel);		
		setLayout(new BorderLayout());
		add(tablePane.getTableHeader(), BorderLayout.PAGE_START);
		add(tablePane, BorderLayout.CENTER);
		setColumnPara();
		
		popupMenu = new JPopupMenu();
		
		tablePane.addMouseListener(new MouseAdapter(){
			
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println(e.getButton());
				if(menus == null){
					
					menus = svgLayers.getMenuItems();
					
					popmenus = new HashMap<String,JMenuItem>();
					
					addpop("LayerNew",menus.get("LayerNew"));
					
					addpop("LayerDel",menus.get("LayerDel"));
					
					popupMenu.addSeparator();
					
					addpop("LayerTop",menus.get("LayerTop"));
					
					addpop("LayerUp",menus.get("LayerUp"));
					
					addpop("LayerBottom",menus.get("LayerBottom"));
					
					addpop("LayerDown",menus.get("LayerDown"));
					
					popupMenu.addSeparator();
					
					addpop("ToolFrame_Layers",menus.get("ToolFrame_Layers"));
					
				}
				
				final int row = tablePane.rowAtPoint(e.getPoint());
				boolean up=row>0;
				boolean down=row>0 && row != tablePane.getRowCount()-1;
				
				menus.get("LayerDel").setEnabled(row>=0);
				menus.get("LayerUp").setEnabled(up);
				menus.get("LayerTop").setEnabled(up);
				menus.get("LayerBottom").setEnabled(down);
				menus.get("LayerDown").setEnabled(down);
				
				popmenus.get("LayerDel").setEnabled(row>=0);
				popmenus.get("LayerUp").setEnabled(up);
				popmenus.get("LayerTop").setEnabled(up);
				popmenus.get("LayerBottom").setEnabled(down);
				popmenus.get("LayerDown").setEnabled(down);
				
				if(SwingUtilities.isRightMouseButton(e)){
					
					popupMenu.show(tablePane, e.getPoint().x, e.getPoint().y);
				}
				layernum = row;
				
				//激活鼠标所在行
			/*	for(int i=0; i<tableModel.getRowCount(); i++)			
				{
					if((i == layernum) != (boolean)tableModel.getValueAt(i, 2))
					{
						tableModel.setValueAt(i == layernum, i, 2);
						getSvgLayers().getLayer(i).setEdit((boolean)tableModel.getValueAt(i, 2));		//设置可编辑属性到SVGLayerManager队列
					}
				}
				//修改当前handle的parentElement
				if(!getSvgLayers().getLastHandle().getSelection().getParentElement().equals(
						getSvgLayers().getLastHandle().getLayerManager().getEditingLayer().getElement()))
				{
					getSvgLayers().getLastHandle().getSelection().setParentElement(
							getSvgLayers().getLastHandle().getLayerManager().getEditingLayer().getElement(), true);
				}	*/			
				super.mousePressed(e);
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
			
		});
	}

	/**
	 * 设置列宽和列显示、编辑方式
	 */
	private void setColumnPara() {
		
		TableColumn col = null;
		for (int i = 0; i < 4; i++) {
			col = tablePane.getColumnModel().getColumn(i);
			col.setPreferredWidth(30);
			col.setCellEditor(tablePane.getDefaultEditor(Boolean.class));
			col.setCellRenderer(tablePane.getDefaultRenderer(Boolean.class));
		}
		tablePane.getColumnModel().getColumn(4).setPreferredWidth(80);
		
	}

	/**
	 * 创建表格数据模型 for JTable
	 */
	private void createTableModel() {
		
		Object[] cols = { "加锁", "显示", "编辑", "链接", "名称" };
		tableModel = new DefaultTableModel(cols, 0);
		
		//监听表格数据改变
		tableModel.addTableModelListener(new TableModelListener() {

			/*
			 * //** 将表格上的内容更新到图层对象
			 *//**/
			@Override
			public void tableChanged(TableModelEvent e) {

				switch(e.getType())
				{
				case TableModelEvent.INSERT:
					
//					System.out.println("insert column");
					layernum = 0;		//修改可编辑行
					break;
					
				case TableModelEvent.UPDATE:
					
					if(e.getFirstRow() >=0 && e.getColumn()>=0)
					{
						Object value = tableModel.getValueAt(e.getFirstRow(),e.getColumn());
						
						Boolean v;
//						int index = tableModel.getRowCount() - e.getFirstRow() - 1;
						int index = layernum;
						SVGLayer layer = getSvgLayers().getLayer(index);
						
						if (layer != null) {
							switch (e.getColumn()) {
							case 0:		//锁定
								v = (Boolean) value;
								layer.setLock(v);
								/*if (v) {
									layer.setEdit(false);
									tableModel.setValueAt(false, index, 2);
									
									layer.setLink(false);
									tableModel.setValueAt(false, index, 3);
								} */
								break;
								
							case 1:		//可视
								v = (Boolean) value;
								layer.setVisible(v);
								if (!v) {
									layer.setEdit(false);
									tableModel.setValueAt(false, index, 2);
									layer.getElement().setAttribute("visibility", "hidden");
									
									layer.setLink(false);
									tableModel.setValueAt(false, index, 3);
								} else {
									
									layer.getElement().setAttribute("visibility", "visible");
								}
								svgLayers.getLastHandle().getCanvas().requestRepaintContent();									//激活图形重绘
								break;
								
							case 2:		//可编辑
								v = (Boolean) value;
								layer.setEdit(v);
								if (v) {
									layer.setLink(false);
									tableModel.setValueAt(false, index, 3);
								}
								editLock();		//唯一可编辑图层
								break;
								
							case 3:			//链接
								v = (Boolean) value;
								layer.setLink(v);
								break;
								
							case 4:			//名称
								layer.setName(value.toString());
								break;
							}
							
						}
					
					} else {
						editLock();		//唯一可编辑图层
					}
					break;
					
				case TableModelEvent.DELETE:
					
					System.out.println("delete column");
					layernum = 0;		//修改可编辑行
					editLock();		//唯一可编辑图层
					break;
				}
				
			}
			
			/**
			 * 唯一可编辑图层
			 */
			public void editLock()
			{
				for(int i=0; i<tableModel.getRowCount(); i++)
				{
					if((i == layernum) != (boolean)tableModel.getValueAt(i, 2))
					{
						tableModel.setValueAt(i == layernum, i, 2);
					}
					SVGLayer layer = getSvgLayers().getLayer(i);
					layer.setLock((boolean)tableModel.getValueAt(i, 0));
					layer.setVisible((boolean)tableModel.getValueAt(i, 1));
					layer.setEdit((boolean)tableModel.getValueAt(i, 2));		//设置可编辑属性到SVGLayerManager队列
					layer.setLink((boolean)tableModel.getValueAt(i, 3));
					layer.setName(tableModel.getValueAt(i, 4).toString());
				}
				
				//修改当前handle的parentElement
				if(getSvgLayers().getLastHandle()!=null && !getSvgLayers().getLastHandle().getSelection().getParentElement().equals(
						getSvgLayers().getLastHandle().getLayerManager().getEditingLayer().getElement()))
				{
					getSvgLayers().getLastHandle().getSelection().setParentElement(
							getSvgLayers().getLastHandle().getLayerManager().getEditingLayer().getElement(), true);
				}
			}
			
		});
	}

	/**
	 * 添加一个图层
	 * 
	 * @param layer
	 */
	public void addLayer(SVGLayer layer) 
	{
		
//		tableModel.addRow(new Object[]{layer.isLock(),layer.isVisible(),
//				layer.isEdit(),layer.isLink(),layer.getName()});
		
		tableModel.insertRow(0, new Object[]{layer.isLock(),layer.isVisible(),
				layer.isEdit(),layer.isLink(),layer.getName()});
		
		tablePane.setPreferredSize(tablePane.getMinimumSize());
		
		tableModel.fireTableDataChanged();
	}
	

	/**
	 * 添加所有图层
	 * 
	 * @param list
	 */
	public void addLayers(List<SVGLayer> list)
	{
		
		for (SVGLayer layer : list) {
			
			tableModel.addRow(new Object[]{layer.isLock(),layer.isVisible(),
					layer.isEdit(),layer.isLink(),layer.getName()});
			
			tablePane.setPreferredSize(tablePane.getMinimumSize());
			
			tableModel.fireTableDataChanged();
		}
	}

	/**
	 * 删除一个图层
	 * 
	 * @param layer
	 */
	public void removeLayer(int index) 
	{
		tableModel.removeRow(index);
	}
	
	/**
	 * 移到图层到底
	 * @param index
	 */
	public void bringLayerBottom(int index)
	{
		
		tableModel.moveRow(index, index, 0);
	}
	
	/**
	 * 移到图层到顶
	 * @param index
	 */
	public void bringLayerTop(int index) 
	{
		tableModel.moveRow(index, index, tableModel.getRowCount()-1);
	}
	
	/**
	 * 移到图层到上一层
	 * @param index
	 */
	public void bringLayerUp(int index) 
	{
		tableModel.moveRow(index, index, index-1);
	}
	
	/**
	 * 移到图层到下一层
	 * @param index
	 */
	public void bringLayerDown(int index)
	{
		tableModel.moveRow(index, index, index+1);
	}

	public void removeAllLayers() 
	{
		
		tableModel.setRowCount(0);
		
	}

	/*	*//**
	 * 同步图层属性到图层面板中
	 * 
	 * @param list
	 */
	/*
	 * public void sync(List<SVGLayer> list) { for(int i = 0;i <
	 * list.size();i++){ SVGLayer layer = list.get(i); tableModel.setValueAt(new
	 * Boolean(layer.isLock()), i, 0); tableModel.setValueAt(new
	 * Boolean(layer.isVisible()), i, 1); tableModel.setValueAt(new
	 * Boolean(layer.isEdit()), i, 2); tableModel.setValueAt(new
	 * Boolean(layer.isLink()), i, 3); tableModel.setValueAt(layer.getName(), i,
	 * 4); } }
	 */

	public SVGLayers getSvgLayers() {
		
		return svgLayers;
		
	}

	public void setSvgLayers(SVGLayers svgLayers) {
		
		this.svgLayers = svgLayers;
		
	}

	public int getLayernum() {
		return layernum;
	}

	public void setLayernum(int layernum) {
		this.layernum = layernum;
	}
	
	/**
	 * 获得当前行数
	 * @return
	 */
	public int getLayerCount()
	{
		return tableModel.getRowCount();
	}

}
