package fr.itris.glips.rtda.action;

import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.widget.*;
import java.awt.*;

/**
 * the class of the login command
 * @author ITRIS, Jordi SUC
 */
public abstract class AbstractLogin extends fr.itris.glips.rtda.animaction.Action{
	
	/**
	 * the constructor of the class
	 * @param picture the svg picture this action is linked to
	 * @param projectName the name of the project this action is linked to
	 * @param parent the top level container
	 * @param component the component on which the action is registered
	 * @param actionObject the object to which the action applies, if it is not the provided component
	 * @param actionElement the dom element defining the action
	 * @param jwidgetRuntime the jwidget runtime object, if this action is linked to a jwidget
	 */
	public AbstractLogin(SVGPicture picture, String projectName, Container parent, 
		JComponent component, Object actionObject, Element actionElement, 
			JWidgetRuntime jwidgetRuntime) {
		
		super(picture, projectName, parent, component, actionObject, 
				actionElement, jwidgetRuntime);
	}
	
	/**
	 * initializes the action
	 */
	protected void initializeAction(){
		
		//getting the tool tip
		try{
			toolTipText=bundle.getString("tooltip_login");
		}catch (Exception ex){}
		
        initializeAuthorizationTag();
	}
	
	@Override
	public Runnable dataChanged(DataEvent evt) {
		
		super.dataChanged(evt);
		checkIsAuthorized();
		return null;
	}

	@Override
	public void performAction(Object evt) {
		
		if(isAuthorized){

			LoginDialog loginDialog=null;
			Container container=component.getTopLevelAncestor();
			
			if(container instanceof Frame){
				
				loginDialog=new LoginDialog((Frame)container);
				
			}else if(container instanceof JApplet){
				
				loginDialog=new LoginDialog(picture.getMainDisplay().getTopLevelFrame());

			}else{
				
				loginDialog=new LoginDialog((JDialog)container);
			}
			
			loginDialog.showDialog(component);
			
			if(loginDialog.hasSucceeded()){
				
				picture.getMainDisplay().getUserRights().setCurrentUser(component,
					loginDialog.getLogin(), loginDialog.getPassword());
			}
			
			loginDialog.disposeDialog();
		}
	}
}
