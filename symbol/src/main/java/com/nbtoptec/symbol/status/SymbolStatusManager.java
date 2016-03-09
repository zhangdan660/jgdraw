package com.nbtoptec.symbol.status;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPopupMenu;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import fr.itris.glips.svgeditor.DrawUnit;
import fr.itris.glips.svgeditor.SymbolMap;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * used to refresh status pane
 * @author zhangdan
 *
 */
public class SymbolStatusManager {

	/**
	 * the related svg handle
	 */
	private SVGHandle handle;
	
	private Snapshot snapshot = new Snapshot();
	
	private JPopupMenu popupMenu;
	
	private Dimension dim = new Dimension(64, 64);
	
	private JXTaskPane taskPane = new JXTaskPane();
	
	
	/**
	 * 元件面板列表
	 */
	private ArrayList<StatusAction> statuslist = new ArrayList<StatusAction>();
	
	/**
	 * 元件状态列表
	 */
	private ArrayList<String>  idlist = new ArrayList<String>();
	
	/**
	 * 组件对应的id号
	 */
	private  HashMap<Component, String> comp2Idmap = new HashMap<Component, String>();
	
	
	public SymbolStatusManager(SVGHandle handle)
	{
		this.handle = handle;
	}

	public ArrayList<StatusAction> getStatuslist() {
		return statuslist;
	}

	public void setStatuslist(ArrayList<StatusAction> statuslist) {
		this.statuslist = statuslist;
	}
	
	public Snapshot getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(Snapshot snapshot) {
		this.snapshot = snapshot;
	}

	public ArrayList<String> getIdlist() {
		return idlist;
	}

	public void setIdlist(ArrayList<String> idlist) {
		this.idlist = idlist;
	}

	public HashMap<Component, String> getComp2Idmap() {
		return comp2Idmap;
	}

	public void setComp2Idmap(HashMap<Component, String> comp2Idmap) {
		this.comp2Idmap = comp2Idmap;
	}

	public void setPopupMenu(JPopupMenu popupMenu) {
		this.popupMenu = popupMenu;
	}

	public void insert(StatusAction sa)
	{
		statuslist.add(sa);
		idlist.add(sa.getTarget().getId());
	}
	
	public void update(StatusAction sa , int index)
	{
		if(index >=0 && index < statuslist.size())
		{
			statuslist.set(index, sa);
		}
	}
	
	public void delete(int index)
	{
		if(index >=0 && index < statuslist.size())
		{
			statuslist.remove(index);
			idlist.remove(index);
		}
	}
	
	/**
	 * 获取组件对应的序号
	 * @param c
	 * @return
	 */
	public int getSeq()
	{
		if(snapshot.c != null)
		{
			if(comp2Idmap.containsKey(snapshot.c))
			{
				return idlist.indexOf(comp2Idmap.get(snapshot.c));
			}
		}
		
		return -1;
	}
	
	public String getSymboId()
	{
//		System.out.println("seqid: " + getSeq());
		
		if(getSeq() >= 0)
		{
			return idlist.get(getSeq());
		}
		return "";
	}
	
	
	public StatusAction getStatusAction()
	{
		if(getSeq() >= 0)
		{
			return statuslist.get(getSeq());
		}
		return null;
	}
	
	/**
	 * 刷新元件状态面板显示
	 */
	public void refresh()
	{
		taskPane.removeAll();
		
		taskPane.revalidate();
		
		SymbolStatusManager ssm = handle.getSymbolStatusManager();
		
		System.out.println("refresh current handle name: " + handle.getName());
		
		ArrayList<StatusAction> tmplist = new ArrayList<StatusAction>();
		
		ArrayList<String>	tmplist2 = new ArrayList<String>();
		
		/*for(StatusAction sa : ssm.getStatuslist() )
		{
			
			DrawUnit du = new DrawUnit();
			du.setName(sa.getTarget().getName());
			du.setId(sa.getTarget().getId());
			du.setIcon(SymbolMap.Doc2Icon(sa.getLocaldoc(), dim));
			
			StatusAction st = new StatusAction(du, sa.getHandle(), sa.getLocaldoc());
			tmplist.add(st);
			Component c = taskPane.add(st);
			
			ssm.getComp2Idmap().put(c, st.getTarget().getId());
			
			new PopMenu(c, popupMenu, handle);		//绑定鼠标右键事件
			
		}*/
		
		int count = 0;
		
		HashMap<String, StatusAction> seqmap = ssm.genertseq();
		
		for(int i=0; count<ssm.getStatuslist().size(); i++)
		{
			if(seqmap.containsKey(""+i))
			{
				StatusAction sa = seqmap.get(""+i);
				
				DrawUnit du = new DrawUnit();
				du.setName(sa.getTarget().getName());
				du.setId(sa.getTarget().getId());
				du.setIcon(SymbolMap.Doc2Icon(sa.getLocaldoc(), dim));
				
				StatusAction st = new StatusAction(du, sa.getHandle(), sa.getLocaldoc());
				tmplist.add(st);
				tmplist2.add(st.getTarget().getId());
				Component c = taskPane.add(st);
				
				ssm.getComp2Idmap().put(c, st.getTarget().getId());
				
				new PopMenu(c, popupMenu, handle);		//绑定鼠标右键事件
				
				count ++;
			}
		}
		
		setStatuslist(tmplist);
		
		setIdlist(tmplist2);
		
		taskPane.revalidate();
		
		taskPane.repaint();
		
	}
	
	public HashMap<String, StatusAction> genertseq()
	{
		HashMap<String, StatusAction> seqmap = new HashMap<String, StatusAction>();
		for(String str : idlist)
		{
			String id = str.substring(str.lastIndexOf("_")+1);
			seqmap.put(str.substring(str.lastIndexOf("_")+1), statuslist.get(idlist.indexOf(str)));
			
		}
		return seqmap;
	}

	public JXTaskPane getTaskpane() {
		return taskPane;
	}

	public void setTaskpane(JXTaskPane taskpane) {
		this.taskPane = taskpane;
	}
}
