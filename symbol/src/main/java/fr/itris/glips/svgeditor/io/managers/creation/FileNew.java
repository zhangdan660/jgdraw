package fr.itris.glips.svgeditor.io.managers.creation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.jdesktop.swingx.JXTaskPane;
import org.w3c.dom.Document;

import com.nbtoptec.symbol.status.Snapshot;
import com.nbtoptec.symbol.status.StatusAction;
import com.nbtoptec.symbol.status.SymbolStatusManager;

import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.io.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class handling the file creation
 * @author Jordi SUC
 */
public class FileNew {

	/**
	 * the dialog used to specify the parameters for the new files to create
	 */
	private NewDialog newDialog;
	
	/**
	 * the labels
	 */
	private String untitledLabel="";
	
	/**
	 * the constructor of the class
	 * @param ioManager the io manager
	 */
	public FileNew(IOManager ioManager){
		
		//gets the labels from the resources
		ResourceBundle bundle=ResourcesManager.bundle;
		
		if(bundle!=null){
		    
			try{
				untitledLabel=bundle.getString("FileNewUntitled");
			}catch (Exception ex){}
		}
		
		//creating the dialog used to specify the parameters for the new files to create
		if(Editor.getParent() instanceof Frame){
			
			newDialog=new NewDialog(this, (Frame)Editor.getParent());
			
		}else if(Editor.getParent() instanceof JDialog){
			
			newDialog=new NewDialog(this, (JDialog)Editor.getParent());
		}
	}
	
	/**
	 * asks the user for the parameters to create the new file
	 * @param relativeComponent the component relatively 
	 * to which the dialog should be displayed
	 */
	public void askForNewFileParameters(JComponent relativeComponent){
		
		newDialog.showDialog(relativeComponent);
	}
	
	/**
	 * creates a new document
	 * @param width the width of the new document
	 * @param height the height of the new document
	 */
	public void createNewDocument(String width, String height){
	    
		SVGHandle handle=
			Editor.getEditor().getHandlesManager().
				createSVGHandle(untitledLabel);
		
		if(handle!=null){
		    
		    handle.getScrollPane().getSVGCanvas().
		    	newDocument(width, height);
		}
	}
	
	/**
	 * creates a new document
	 * @param width the width of the new document
	 * @param height the height of the new document
	 */
	public void createNewDocument2(String width, String height, String groupName, String symbolName){
	    
		SVGHandle handle=
			Editor.getEditor().getHandlesManager().
				createSVGHandle(symbolName);
		
		if(handle!=null){
		    
			String symbolValue = "状态0";
			
			String statusvalue = "0";
			
		    handle.getScrollPane().getSVGCanvas().
		    	newDocument(width, height);
		    
		    handle.getCanvas().getDocument().getDocumentElement().setAttribute("status", symbolValue);
		    
		    Document tmpdoc = SymbolMap.newDocument();
		    
		    tmpdoc.getDocumentElement().setAttribute("status", symbolValue);
		    
		    // add by zd
		    // init left menu
		    
		    if(!SymbolMap.docmap.containsKey(groupName))
		    {
		    	SymbolMap.docmap.put(groupName, new HashMap<String, Document>());
		    	
		    	SymbolMap.grouplist.add(groupName);
		    	
		    }
		    
		    SymbolMap.IDmap.put(symbolName+"_"+statusvalue, groupName);
		    
		    SymbolMap.docmap.get(groupName).put(symbolName+"_"+statusvalue, tmpdoc);//handle.getCanvas().getDocument());
		    
		    SymbolMap.shapemap.put(symbolName+"_"+statusvalue, SymbolMap.toString(handle.getCanvas().getDocument()));
		    
		    SymbolStatusManager ssm =  handle.getSymbolStatusManager();
		    
		    DrawUnit du = new DrawUnit();
		    
		    du.setName(symbolValue+"("+statusvalue+")");
		    
		    du.setId(symbolName+"_"+statusvalue);
		    
		    du.setIcon(ResourcesManager.getIcon("blank64", false));
		    
		    //记录元件状态面板记录
		    Snapshot snapshot = ssm.getSnapshot();
		    
		    snapshot.groupName = groupName;
		    
		    snapshot.statusId = statusvalue;
		    
		    snapshot.symbolId = symbolName+"_"+statusvalue;
		    
		    snapshot.symbolName = symbolName;
		    
		    ssm.insert(new StatusAction(du, handle, tmpdoc));
		    
		    
		    //左侧菜单栏
		    SymbolManager sm = Editor.getEditor().getSymbolManager();
		    
		    sm.loadMenuProperties();
		    
		    sm.update();
		    
		   /* DrawUnit du2 = new DrawUnit();
		    
		    du2.setName(symbolName);
		    
		    du2.setId(du.getId());
		    
		    du2.setIcon(ResourcesManager.getIcon("blank32", false));
		    
		    //添加菜单项
		    HashMap<JXTaskPane, ArrayList<DrawAction>> taskmap = Editor.getEditor().getSymbolManager().taskmap;
		
		    boolean flag = false;
		    for(Entry<JXTaskPane, ArrayList<DrawAction>> entry : taskmap.entrySet())
    		{
    			String title = entry.getKey().getTitle();
    			
    			if(title.equalsIgnoreCase(groupName))
    			{
    				taskmap.get(entry.getKey()).add(new DrawAction(du2));
    				
    				flag = true;		//已存在大类名字
    			}
    		}
		    
		    if(!flag)
		    {
		    	JXTaskPane taskPane = new JXTaskPane();
		    	
				taskPane.setTitle(groupName);
				
				taskmap.put(taskPane, new ArrayList<DrawAction>());
				
				taskmap.get(taskPane).add(new DrawAction(du2));
				
		    } 
		    
		  //刷新菜单栏
		    SymbolMap.menumap.clear();
		    for(Entry<JXTaskPane, ArrayList<DrawAction>> entry : taskmap.entrySet())
    		{
    			JXTaskPane taskpane = entry.getKey();
    			taskpane.removeAll();
    			ArrayList<DrawAction> list = entry.getValue();
    			for(DrawAction action : list)
    			{
    			    Component c = 	taskpane.add(action);
    				new SymbolPopMenu(c, Editor.getEditor().getSymbolManager().popupMenu);		//绑定鼠标右键事件
    				SymbolMap.menumap.put(c, action.getName());
    			}
    			Editor.getEditor().getSymbolManager().container.add(taskpane);
    			taskpane.revalidate();
//    			container.revalidate(); //刷新菜单栏
    		}
		    Editor.getEditor().getSymbolManager().container.repaint();*/
		}
	}
	
}
