package com.nbtoptec.draw.layer;

import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fr.itris.glips.library.widgets.TitledDialog;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;

public class DeleteLayerWarnDialog extends TitledDialog {

	private static final long serialVersionUID = 1L;
	
	private boolean del;
	
	/**
	 * the constructor of the class
	 * @param parent the parent of the dialog
	 */
	public DeleteLayerWarnDialog(Frame parent){
		
		super(parent, true, true);
	}
	
	@Override
	protected JPanel buildContentPanel() {
		// TODO Auto-generated method stub
		
		//creating and filling the panel that will contain the widgets
		JPanel widgetsPanel=new JPanel();
		widgetsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout layout=new GridBagLayout();
		widgetsPanel.setLayout(layout);
		
		//creating the ok and cancel listener
		ActionListener buttonsListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				if(evt.getSource().equals(okButton)){
					
					del = true;
				} else if(evt.getSource().equals(cancelButton))
				{
					del = false;
				}
				setVisible(false);
			}
		};
		
		okButtonListener=buttonsListener;
		cancelButtonListener=buttonsListener;
		
		okButton.addActionListener(buttonsListener);
		cancelButton.addActionListener(buttonsListener);
		setMessage("是否确认删除图层", TitledDialog.ERROR_TYPE);
		
		return widgetsPanel;
	}

	/**
	 * shows the dialog
	 * @param relativeComponent the component relatively 
	 * to which the dialog will be shown
	 * @param handle a svg handle
	 */
	public void showDialog(JComponent relativeComponent, SVGHandle handle) {
		
		super.showDialog(relativeComponent);
	}
	
	@Override
	public void disposeDialog() {
		
		super.disposeDialog();
	}

	public boolean isDel() {
		return del;
	}

	public void setDel(boolean del) {
		this.del = del;
	}
}
