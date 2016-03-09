package fr.itris.glips.svgeditor.io.managers;

import java.awt.Frame;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import fr.itris.glips.library.monitor.Monitor;
import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.EditorToolkit;
import fr.itris.glips.svgeditor.SVGUtil;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.io.IOManager;
import fr.itris.glips.svgeditor.io.managers.dialog.FileChooserDialog;
import fr.itris.glips.svgeditor.io.managers.dialog.SVGFileFilter;
import fr.itris.glips.svgeditor.io.managers.monitor.SaveMonitor;

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

			//creating the monitor
			Monitor monitor=new SaveMonitor(
				Editor.getParent(), relativeComponent, 0, 
					getNodesCount(handle.getCanvas().getDocument()));
			
			//saving the svg document into the found file
			saveDocument(handleFile, handle, monitor, false);
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
        		
        		if(file.getName().endsWith(EditorToolkit.VML_FILE_EXTENSION))		//vml格式保存
        		{
        			try {
						
        				Document document = SVGUtil.convertVml(doc);
        				String svgStr = SVGUtil.toString(document);
        				
        				File xsltFile = new File("svg2vml.xsl");
        				Source svgSource = new StreamSource(new ByteArrayInputStream(svgStr.getBytes()));
        				Source xsltSource = new StreamSource(xsltFile);
        				TransformerFactory transFact = TransformerFactory.newInstance();
        				Transformer trans = transFact.newTransformer(xsltSource);
        				Properties pros = trans.getOutputProperties();
        				pros.setProperty(OutputKeys.ENCODING, "UTF-8");
        				pros.setProperty(OutputKeys.METHOD, "html");
        				pros.setProperty(OutputKeys.VERSION, "4.0");
        				
        				trans.setOutputProperties(pros);
        				StringWriter strWriter = new StringWriter();
        				StreamResult vmlResult = new StreamResult(strWriter);
        				trans.transform(svgSource, vmlResult);
        				
        				PrintWriter pw = new PrintWriter(new FileOutputStream(file));
        				pw.write(strWriter.toString());
        				pw.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
        		} else {
        			
        			XMLPrinter.printXML(doc, file, monitor);
        		}
        		
        	}
        });
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
