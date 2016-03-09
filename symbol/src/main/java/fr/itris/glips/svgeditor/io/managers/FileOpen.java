package fr.itris.glips.svgeditor.io.managers;

import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.w3c.dom.Document;

import com.nbtoptec.symbol.status.Snapshot;
import com.nbtoptec.symbol.status.StatusAction;

import fr.itris.glips.library.monitor.Monitor;
import fr.itris.glips.svgeditor.DrawUnit;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.SymbolMap;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.io.IOManager;
import fr.itris.glips.svgeditor.io.managers.dialog.FileChooserDialog;
import fr.itris.glips.svgeditor.io.managers.dialog.SVGFileFilter;
import fr.itris.glips.svgeditor.io.managers.monitor.OpenMonitor;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * the class handling the opening of files
 * @author Jordi SUC
 */
public class FileOpen {
	
	/**
	 * the io manager
	 */
	private IOManager ioManager;
	
	/**
	 * the file chooser dialog
	 */
	private FileChooserDialog fileChooserDialog;

	/**
	 * the labels
	 */
	private String warningNotNullMessage="", 
		warningNullMessage="", warningTitle="";
	
	/**
	 * the size of icon
	 */
	final Dimension dim = new Dimension(64,64);  //icon的尺寸
	
	/**
	 * the constructor of the class
	 * @param ioManager the io manager
	 */
	public FileOpen(IOManager ioManager){
		
		this.ioManager=ioManager;
		
		//creating the file chooser dialog
		if(Editor.getParent() instanceof Frame){
			
			fileChooserDialog=new FileChooserDialog(
				(Frame)Editor.getParent(), FileChooserDialog.OPEN_FILE_MODE);
			
		}else if(Editor.getParent() instanceof JDialog){
			
			fileChooserDialog=new FileChooserDialog(
				(JDialog)Editor.getParent(), FileChooserDialog.OPEN_FILE_MODE);
		}
		
		//setting the file filter
		fileChooserDialog.setFileFilter(new SVGFileFilter());
		
		//getting the labels from the resources store
		ResourceBundle bundle=ResourcesManager.bundle;
		
		if(bundle!=null){
		    
			try{
				warningNotNullMessage=bundle.getString("OpenFailedErrorFileNotNullMessage");
				warningNullMessage=bundle.getString("OpenFailedErrorFileNullMessage");
				warningTitle=bundle.getString("OpenFailedErrorTitle");
			}catch (Exception ex){}
		}
	}
	
	/**
	 * shows a file chooser so that the user can choose the files to open
	 * @param relativeComponent the component relatively 
	 * to which the dialog will be shown
	 */
	public void askUserForFile(JComponent relativeComponent){
		
		fileChooserDialog.showDialog(relativeComponent);
		
		//getting the array of the files selected by the user
		File[] selectedFiles=fileChooserDialog.getSelectedFiles();

		if(selectedFiles!=null && selectedFiles.length>0){
			
			Monitor monitor=null;
			
			for(File file : selectedFiles){
				
				//creating the monitor
				monitor=new OpenMonitor(file,
					Editor.getParent(), relativeComponent, 0, 100);
				
				//opening the file
				open(file, monitor);
			}
		}
	}
	
	/**
	 * open the provided file
	 * @param file a file
	 * @param monitor the object used to monitor the opening of the file
	 */
	public void open(final File file, final Monitor monitor){
		
		SVGHandle handle=null;
		
		if(file!=null && file.exists()){
		    
			//checking if a svg file having the same name already exists
			String path=file.toURI().toASCIIString();
			handle=Editor.getEditor().getHandlesManager().getHandle(path);
			
			if(handle!=null){
				
				Editor.getEditor().getHandlesManager().setCurrentHandle(path);
				
			}else{
				
				//creating a new handle
				handle=Editor.getEditor().getHandlesManager().
					createSVGHandle(file.toURI().toASCIIString());
				
				//adding the file name to the list of the recent files
				Editor.getEditor().getResourcesManager().addRecentFile(file.toURI().toASCIIString());
				Editor.getEditor().getResourcesManager().notifyListeners();
				final SVGCanvas canvas=handle.getScrollPane().getSVGCanvas();
				
				Runnable runnable=new Runnable(){
					
					public void run() {

						//setting the uri of the svg file
						canvas.setURI(file.toURI().toASCIIString(), monitor);
					}
				};
				
				ioManager.requestExecution(runnable);
			}
			
		}else{
			
			//computing the warning message
			String message=warningNullMessage;
			
			if(file!=null){
				
				message=warningNotNullMessage+
					file.getAbsolutePath()+".</body></html>";
			}
			
			//if the file could not be opened, a dialog is 
			//displayed to notify that an error occured
			JOptionPane.showMessageDialog(
				Editor.getParent(), message, 
					warningTitle, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/**
	 * open the provided file
	 * @param content of a file
	 * @param monitor the object used to monitor the opening of the file
	 */
	public void open(final String content, String path,  final Monitor monitor){
		
		SVGHandle handle=null;
		
		if(content!=null && !content.isEmpty())
		{
			
			handle=Editor.getEditor().getHandlesManager().getHandle(path);
			
			if(handle!=null){
				
				Editor.getEditor().getHandlesManager().setCurrentHandle(path);
				
			}else{
				
				//creating a new handle
				handle=Editor.getEditor().getHandlesManager().
					createSVGHandle(path);
				
				final SVGCanvas canvas=handle.getScrollPane().getSVGCanvas();
				
				Runnable runnable=new Runnable(){
					
					public void run() {

						//setting the uri of the svg file
						canvas.setContent(content, null);
					}
				};
				
				ioManager.requestExecution(runnable);

			}
			
		}
	}
	
	/**
	 * open the provided file
	 * @param content of a file
	 * @param monitor the object used to monitor the opening of the file
	 */
	public void open(final String content, String path,  final Monitor monitor, String symbolId){
		
		SVGHandle handle=null;
		
		if(content!=null && !content.isEmpty())
		{
			
			handle=Editor.getEditor().getHandlesManager().getHandle(path);
			
			if(handle!=null){
				
				Editor.getEditor().getHandlesManager().setCurrentHandle(path);
				
			}else{
				
				//creating a new handle
				handle=Editor.getEditor().getHandlesManager().
					createSVGHandle(path);
				
				final SVGCanvas canvas=handle.getScrollPane().getSVGCanvas();
				
				Runnable runnable=new Runnable(){
					
					public void run() {

						//setting the uri of the svg file
						canvas.setContent(content, null);
					}
				};
				
				ioManager.requestExecution(runnable);
				
				// add by zd 
				// executed when opening new symobl
				for( Entry<String, HashMap<String, Document>> e : SymbolMap.docmap.entrySet())
				{
					if(e.getValue().containsKey(symbolId))
					{
						//设定默认打开的图形
						Snapshot snapshot = handle.getSymbolStatusManager().getSnapshot();
						
						snapshot.groupName = e.getKey();								//记录组元件名
						
						snapshot.symbolName = handle.getName();							//记录元件
						
						snapshot.symbolId = symbolId;
					}
					
					for(Entry<String, Document> sym : e.getValue().entrySet())
					{
						if(sym.getKey().contains(path+"_"))
						{
							
							Document doc = sym.getValue();
							
							String statusname = doc.getDocumentElement().getAttribute("status");
							
							String statusid = sym.getKey().substring(sym.getKey().lastIndexOf("_")+1);
							
							
							DrawUnit du = new DrawUnit();
							
							du.setName(statusname+"("+statusid+")");						//元件状态显示的名字
							
							du.setId(sym.getKey());											//每个元件状态的唯一Id
							
							du.setIcon(SymbolMap.Doc2Icon(doc, dim));			//元件png图片
							
							handle.getSymbolStatusManager().insert(new StatusAction(du, handle, doc));		
							
						}
					}
					
				}
			}
			
		}
	}
	
	/**
	 * @return the linked list of the recently opened files. 
	 */
	public LinkedList<File> getRecentFiles(){
		
		//the list that will be returned
		LinkedList<File> recentFiles=
			new LinkedList<File>();
		
		//getting the collection of the path of the last recently opened svg files
		Collection<String> filePaths=
			Editor.getEditor().getResourcesManager().getRecentFiles();
		
		//filling the list
		File file=null;
		
		for(String path : filePaths){
			
			try{
				file=new File(new URI(path));
			}catch (Exception ex){file=null;}
			
			if(file!=null){
				
				recentFiles.add(file);
			}
		}	

		return recentFiles;
	}
}
