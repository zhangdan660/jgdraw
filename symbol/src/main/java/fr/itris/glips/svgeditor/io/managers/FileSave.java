package fr.itris.glips.svgeditor.io.managers;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.*;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.*;
import org.w3c.dom.svg.SVGDocument;

import com.nbtoptec.symbol.status.Snapshot;

import fr.itris.glips.library.monitor.*;
import fr.itris.glips.library.util.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.io.*;
import fr.itris.glips.svgeditor.io.managers.dialog.*;
import fr.itris.glips.svgeditor.io.managers.monitor.*;

/**
 * the class handling the saving of files
 * @author Jordi SUC
 */
public class FileSave {

	/**
	 * the io manager
	 */
	private IOManager ioManager;
	
	/**
	 * the file chooser dialog
	 */
	private FileChooserDialog fileChooserDialog;
	
	/**
	 * the constructor of the class
	 * @param ioManager the io manager
	 */
	public FileSave(IOManager ioManager){
		
		this.ioManager=ioManager;

		//creating the file chooser dialog
		if(Editor.getParent() instanceof Frame){
			
			fileChooserDialog=new FileChooserDialog(
				(Frame)Editor.getParent(), FileChooserDialog.SAVE_FILE_MODE);
			
		}else if(Editor.getParent() instanceof JDialog){
			
			fileChooserDialog=new FileChooserDialog(
				(JDialog)Editor.getParent(), FileChooserDialog.SAVE_FILE_MODE);
		}
		
		//setting the file filter
		fileChooserDialog.setFileFilter(new SVGFileFilter());
	}
	
	/**
	 * saves the document denoted by the handle 
	 * into the file the user has chosen, or the file that was 
	 * previously chosen or opened
	 * @param handle a svg handle
	 * @param saveAs whether the action is a saveAs one
	 * @param relativeComponent the component relatively 
	 * to which the dialog will be shown
	 * @return whether the save action has been launched
	 */
	public boolean saveHandleDocument(SVGHandle handle, 
			boolean saveAs, JComponent relativeComponent){
		
		//getting the file corresponding to the current handle
		File handleFile=getFile(handle);
		if(!saveAs)
		{
			handleFile = new File("symbol.xml");
			if(!handleFile.exists())
				try {
					handleFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		} 

		if(handleFile==null || saveAs){
			
			//getting the file where to save the document
			fileChooserDialog.showDialog(relativeComponent);
			
			//getting the selected file
			File selectedFile=fileChooserDialog.getSelectedFile();
			
			if(selectedFile!=null){
				
				selectedFile=checkFileExtension(selectedFile);

				//creating the monitor
				Monitor monitor=new SaveMonitor(
					Editor.getParent(), relativeComponent, 
						0, getNodesCount(handle.getCanvas().getDocument()));
				
				//saving the svg document into the selected file
				saveDocument(selectedFile, handle, monitor, true);
				return true;
			}
			
		}else{
				
			//saving the svg document into the found file
			saveDocument2(handleFile, handle, null, false);
			return true;
		}
		
		return false;
	}
	
	/**
	 * saves the provided document into the provided file
	 * @param file a file
	 * @param handle the handle to save
	 * @param monitor the monitor used to display 
	 * the state of the save action
	 * @param handleNameChanged whether the handle name has changed
	 */
	public void saveDocument(
		final File file, final SVGHandle handle, final Monitor monitor, 
			boolean handleNameChanged){
		
		//getting the path corresponding to the file
		String filePath=file.toURI().toASCIIString();
		
		if(handleNameChanged){
			
			//setting the new name for the handle
			Editor.getEditor().getHandlesManager().
			changeName(handle.getName(), filePath);
			handle.getCanvas().setNewURI(filePath);
			
			//storing the name of the file in the recent open files
			Editor.getEditor().getResourcesManager().addRecentFile(filePath);
			Editor.getEditor().getResourcesManager().notifyListeners();
		}
		
		//setting the state of the handle to no more modified
		handle.setModified(false);
		final Document doc=handle.getCanvas().getDocument();
        
        //creating the runnable that will be added to the runnable queue
        ioManager.requestExecution(new Runnable(){
        	
        	public void run() {

                //prints the document into a file
        		XMLPrinter.printXML(doc, file, monitor);
        	}
        });
	}
	
	/**
	 * another method saving document to file
	 * @param file
	 * @param handle
	 * @param monitor
	 * @param handleNameChanged
	 * @author zhangdan
	 */
	public void saveDocument2(
			final File file, final SVGHandle handle, final Monitor monitor, 
			boolean handleNameChanged){
		
		//getting the path corresponding to the file
		if(handle !=null && file != null)
			handle.getCanvas().setNewURI(file.toURI().toASCIIString());
		
		//setting the state of the handle to no more modified
		if(handle != null && handle.isModified())
		{
			
			Snapshot snapshot = handle.getSymbolStatusManager().getSnapshot();
			
			SymbolMap.docmap.get(snapshot.groupName).put(snapshot.symbolId, deepClone(handle.getCanvas().getDocument()));
			
			SymbolMap.shapemap.put(snapshot.symbolId, SymbolMap.toString(handle.getCanvas().getDocument()));	//修改到左侧菜单栏
			
//			System.out.println(SymbolMap.shapemap.get(snapshot.symbolId));
			
			handle.getSymbolStatusManager().getStatuslist().get(
					handle.getSymbolStatusManager().getIdlist().indexOf(snapshot.symbolId)).setLocaldoc(
							deepClone(handle.getCanvas().getDocument()));
		
			/*if(handle.getSymbolStatusManager().getStatusAction() == null)
			{
				//默认打开第一个状态
				handle.getSymbolStatusManager().getStatuslist().get(0).setLocaldoc(deepClone(handle.getCanvas().getDocument()));
			} else {
				
				
				handle.getSymbolStatusManager().getStatusAction().setLocaldoc(deepClone(handle.getCanvas().getDocument()));
			}*/
			
			handle.getSymbolStatusManager().refresh();
			
			System.out.println("save: " + snapshot.symbolId);
			
			handle.setModified(false);
		
			//左侧菜单栏
			SymbolManager sm = Editor.getEditor().getSymbolManager();
			
			sm.loadMenuProperties();
			
			sm.update();
		
		}
		//creating the runnable that will be added to the runnable queue
		ioManager.requestExecution(new Runnable(){
			
			public void run() {
				
				DOMImplementation impl=SVGDOMImplementation.getDOMImplementation();
				String svgNS=SVGDOMImplementation.SVG_NAMESPACE_URI;
				SVGDocument doc=(SVGDocument)impl.createDocument(svgNS, "svg", null);
				
				//gets the root element (the svg element)
				Element svgRoot=doc.getDocumentElement();
				
				//set the width and height attribute on the root svg element
				svgRoot.setAttributeNS(null, "width" , "600");
				svgRoot.setAttributeNS(null, "height", "400");
				svgRoot.setAttribute("viewBox","0 0 600 400");
				
				//creating a defs element
				Element defsElement=doc.createElementNS(doc.getNamespaceURI(), "defs");
				svgRoot.appendChild(defsElement);
				
				//prints the document into a file
				for( Entry<String, HashMap<String, Document>> e : SymbolMap.docmap.entrySet())
				{
					for( Entry<String, Document> sym : e.getValue().entrySet())
					{
						Element root= sym.getValue().getDocumentElement();
						
						Element symbol = doc.createElementNS(null, "symbol");
						
						symbol.setAttribute("status", root.getAttribute("status"));
						
						symbol.setAttribute("viewBox", root.getAttribute("viewBox"));
						
						symbol.setAttribute("id", sym.getKey());
						
						symbol.setAttribute("name", e.getKey());
						
						defsElement.appendChild(symbol);
						
						for(Node shape = root.getFirstChild(); shape != null ; shape = shape.getNextSibling())
						{
							if(shape instanceof org.apache.batik.dom.GenericText)
								continue;
							
							Element shapeobj = (Element) shape;
						
							Element newe = doc.createElementNS(shapeobj.getNamespaceURI(), shapeobj.getNodeName());
							
							NamedNodeMap attrs = shapeobj.getAttributes();
							
							if(attrs != null)
							{
								
								for (int i = 0; i < attrs.getLength(); i++) {
									
									Attr attr = (Attr)attrs.item(i);
									
									//如果是double型，保留2位小数
									try {
										String value = attr.getValue();
										Double.parseDouble(value);
										if(value.substring(value.indexOf(".")).length() > 2)
										{
											newe.setAttribute(attr.getName(), value.substring(0, value.indexOf(".")+3));
										} else {
											newe.setAttribute(attr.getName(), attr.getValue());
										}
									} catch (Exception e2) {
										newe.setAttribute(attr.getName(), attr.getValue());
									} 
								}
							}
							
							symbol.appendChild(newe);
						}
						
					}
				}
				try {
					StringBuffer sb = new StringBuffer(SymbolMap.toString(doc));
					ByteBuffer byteBuffer=ByteBuffer.wrap(sb.toString().getBytes("UTF-8"));
					FileOutputStream out=new FileOutputStream(file);
					WritableByteChannel channel=Channels.newChannel(out);
					channel.write(byteBuffer);
					channel.close();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		        
				//弹出保存提示
//				InfoDialog dialog = new InfoDialog((Frame)Editor.getParent());
//				dialog.showDialog(null,null);
			}
		});
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
	
	/**
	 * checks if the provided file has an extension. 
	 * If not, the "svg" extension is added to it.
	 * @param file a file
	 * @return the file whose extension has been corrected if needed
	 */
	protected File checkFileExtension(File file){
		
		File newFile=file;
		
		//getting the path of the file
		String filePath=file.toURI().toASCIIString();
		
		if(filePath.indexOf(".")==-1){
			
			//the file has no extension, the svg one is then added
			filePath+=EditorToolkit.SVG_FILE_EXTENSION;
			
			//creating the new file
			try{
				newFile=new File(new URI(filePath));
			}catch (Exception ex){}
		}
		
		return newFile;
	}
	
	/**
	 * returns the file corresponding to this svg handle
	 * @param handle a svg handle
	 * @return the file corresponding to this svg handle
	 */
	protected File getFile(SVGHandle handle){
		
		File file=null;
		
		try{
			file=new File(new URI(handle.getName()));
		}catch(Exception ex){}
		
		if(file!=null && ! file.exists()){
			
			file=null;
		}
		
		return file;
	}
	
	/**
	 * returns the nodes number that could be 
	 * found in the provided document
	 * @param doc a document
	 * @return the nodes number that could be 
	 * found in the provided document
	 */
	protected int getNodesCount(Document doc){
		
		int nodesCount=
			XMLPrinter.getNodesCount(doc);
		nodesCount+=nodesCount/5;
		
		return nodesCount;
	}
}
