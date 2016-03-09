/*
 * Created on 2 fevr. 2005
 */
package fr.itris.glips.rtdaeditor.test.display;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

import org.w3c.dom.svg.SVGDocument;

import fr.itris.glips.rtda.MainDisplay;
import fr.itris.glips.rtda.components.viewbrowser.SVGCanvasContainer;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;


/**
 * the class displaying the dialog into which the test will be shown
 * 
 * @author ITRIS, Jordi SUC
 */
public class DialogTest{
    
	/**
	 * the main display
	 */
	private MainDisplay mainDisplay;
    
    /**
     * the split pane
     */
    private JSplitPane splitPane;

    /**
     * the canvas container
     */
    private SVGCanvasContainer canvasContainer;
    
    /**
     * the panel containing the controls 
     */
    private ControlsPanel controlsPanel;
    
    /**
     * the size of the screen
     */
    private Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    
    /**
     * the bounds when the dialog is in normal mode
     */
    private Rectangle normalBounds=new Rectangle();
    
    /**
     * whether the dialog is in full screen mode or not
     */
    private boolean isInFullScreenMode=false;

    /**
     * the normal dialog
     */
    private JFrame frame=new JFrame();

	protected JFXPanel webBrowser;
    
    /**
     * the constructor of the class
     */
    public DialogTest(){}
    
    /**
     * initializes the dialog
     * @param aMainDisplay the main display
     */
    public void initialize(MainDisplay aMainDisplay){
    	
    	this.mainDisplay=aMainDisplay;

        //getting the labels
        ResourceBundle bundle=ResourcesManager.bundle;
        String titleLabel=bundle.getString("rtdaanim_test_dialogTitle");
        
        //creating the canvas container
        canvasContainer=
        	mainDisplay.getMainViewBrowser().getCanvasContainer();
        
        //creating the controls panel
        controlsPanel=new ControlsPanel(this);
        
        //creating the split pane
        splitPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(1);
        splitPane.setDividerLocation(0.8);
        splitPane.setOneTouchExpandable(true);
        splitPane.setTopComponent(canvasContainer);
        splitPane.setBottomComponent(controlsPanel);

        //the layout for this dialog content pane
        frame.getContentPane().setLayout(new BoxLayout(
        	frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setTitle(titleLabel);

		//the listener to the state of the window
        frame.addWindowListener(new WindowAdapter(){

        	@Override
        	public void windowClosing(WindowEvent evt) {
 
                refreshDialogState(false);
                frame.setVisible(false);
        	}
		});
        
        //packing the dialogs
        frame.getContentPane().add(splitPane);
        frame.pack();
        
        //setting the size for this dialog
        Rectangle bounds=new Rectangle();
        
        bounds.width=3*screenSize.width/4;
        bounds.height=3*screenSize.height/4;
        bounds.x=(screenSize.width-bounds.width)/2;
        bounds.y=(screenSize.height-bounds.height)/2;

        normalBounds=bounds;
        frame.setBounds(normalBounds);
    }
    
    /**
     * initializes the dialog
     * @param aMainDisplay the main display
     */
    public void initialize2(MainDisplay aMainDisplay){
    	
    	this.mainDisplay=aMainDisplay;
    	
    	//getting the labels
        ResourceBundle bundle=ResourcesManager.bundle;
        String titleLabel=bundle.getString("rtdaanim_test_dialogTitle");
        
        
        //the layout for this dialog content pane
        frame.getContentPane().setLayout(new BoxLayout(
        	frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setTitle(titleLabel);
        
        //creating the controls panel
        controlsPanel=new ControlsPanel(this);
        
        //add by zd
        webBrowser = new JFXPanel();
        frame.add(webBrowser);  
        
        //packing the dialogs
        frame.pack();
        
        //setting the size for this dialog
        Rectangle bounds=new Rectangle();
        
        bounds.width=3*screenSize.width/4;
        bounds.height=3*screenSize.height/4;
        bounds.x=(screenSize.width-bounds.width)/2;
        bounds.y=(screenSize.height-bounds.height)/2;

        normalBounds=bounds;
        frame.setBounds(normalBounds);
    }
    
    /**
     * @return the main display
     */
    public MainDisplay getMainDisplay() {
		return mainDisplay;
	}
    
    /**
     * @return whether the dialog is in the full screen mode
     */
    public boolean isInFullScreenMode() {
		return isInFullScreenMode;
	}
    
    /**
     * handles the full screen mode
     */
    protected void handleFullScreenMode(){

        frame.setVisible(false);
    	
        if(! isInFullScreenMode){
            
        	frame.dispose();
        	frame.setUndecorated(true);
        	frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            splitPane.validate();

            isInFullScreenMode=true;
            
        }else{
            
        	frame.dispose();
        	frame.setUndecorated(false);
        	frame.setExtendedState(Frame.NORMAL);
            splitPane.validate();
            frame.setBounds(normalBounds);
            
            isInFullScreenMode=false;
        }
        
        frame.setVisible(true);
    }
    
    /**
     * refreshes the state of the dialog
     * @param visible whether the dialog will be visible
     */
    public void refreshDialogState(boolean visible){

    	mainDisplay.getAnimationsHandler().stop();
    	mainDisplay.getMainViewBrowser().disposeAll();
        controlsPanel.getTableBuilder().disposeTable();
        
        if(visible){

            SVGHandle handle=
            	Editor.getEditor().getHandlesManager().getCurrentHandle();
            
            if(handle!=null){
                
            	//setting the svg document to be displayed
            /*	mainDisplay.getMainViewBrowser().getPictureLoader().setCurrentPicture(
            			(SVGDocument)handle.getScrollPane().getSVGCanvas().getDocument(),
            				handle.getScrollPane().getSVGCanvas().getURI(), false);
                
                //refreshing the simulation values panel
            	controlsPanel.refreshSimulationValuesPanel();*/
                
                //setting the bounds
            	frame.setVisible(true);
            }
            
            //add by zd
            showTest();
            
//            mainDisplay.getAnimationsHandler().start();
        }else{
        	
        	frame.setVisible(false);
        }
        
    }
    
    /**
     * @return the normal dialog
     */
    public JFrame getFrame() {
		return frame;
	}
    
    /**
     * @return Returns the canvas container
     */
    public SVGCanvasContainer getCanvasContainer() {
    	
        return canvasContainer;
    }
    
    /**
     * refreshes the simulation values panel that will be displayed
     * to allow the users to specify the values they want for the simulation
     */
    public void refreshSimulationValuesPanel(){
    	
    	controlsPanel.refreshSimulationValuesPanel();
    }
    
    /**
     * show  animation in javafx
     * @author zhangdan
     */
    public void showTest()
    {
    	Platform.runLater(new Runnable() {
            
        	@Override
        	public void run()
        	{
        		
        		String url = Editor.getEditor().getHandlesManager().getCurrentHandle().getName();
        		int WIDTH=3*screenSize.width/4;
        		int HEIGHT=3*screenSize.height/4;
        		
        		Group root = new Group();
        		Scene scene = new Scene(root, WIDTH, HEIGHT);
        		webBrowser.setScene(scene);
        		Double widthDouble = new Integer(WIDTH).doubleValue();
        		Double heightDouble = new Integer(HEIGHT).doubleValue();
        		
        		VBox box = new VBox(10);
        		HBox urlBox = new HBox(10);
        		final TextField urlTextField = new TextField();
        		urlTextField.setText(url);
        		
        		WebView view = new WebView();
        		view.setMinSize(widthDouble, heightDouble);
        		view.setPrefSize(widthDouble, heightDouble);
        		final WebEngine eng = view.getEngine();
        		eng.load(url);
        		root.getChildren().add(view);
        		
        		box.getChildren().add(urlBox);
        		box.getChildren().add(view);
        		root.getChildren().add(box);
        	}
        });
    }
}
