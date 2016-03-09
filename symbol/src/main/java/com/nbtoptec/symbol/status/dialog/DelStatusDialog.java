package com.nbtoptec.symbol.status.dialog;

import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.nbtoptec.symbol.status.SymbolStatus;

import fr.itris.glips.library.widgets.TitledDialog;
import fr.itris.glips.svgeditor.SymbolMap;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

@SuppressWarnings("serial")
public class DelStatusDialog extends TitledDialog{

	/**
	 * new status manager
	 */
	private SymbolStatus symbolStatus;
	
	
	public DelStatusDialog(SymbolStatus symbolStatus, Frame parent) {
		
		super(parent, true, true);
		this.symbolStatus = symbolStatus;
		
	}

	public DelStatusDialog(SymbolStatus symbolStatus, JDialog parent) {

		super(parent, true);
		this.symbolStatus = symbolStatus;
	}
	
	@Override
	protected JPanel buildContentPanel() {
		
		//getting the labels
//		ResourceBundle bundle=ResourcesManager.bundle;
//		String titleDialogLabel=bundle.getString("FileNewTitle");
		String titleDialogMessageLabel="确定删除" + SymbolMap.symbolid+"的状态：" + SymbolMap.statusname +"?";
		
		//setting the title
//		setTitleMessage(titleDialogLabel);
		
		//setting the information message
		setMessage(titleDialogMessageLabel, INFORMATION_TYPE);
		
		//creating and filling the panel that will contain the widgets
		JPanel widgetsPanel=new JPanel();
		widgetsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout layout=new GridBagLayout();
		widgetsPanel.setLayout(layout);
		
		//creating the ok and cancel listener
		ActionListener buttonsListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
	
				if(evt.getSource().equals(okButton)){
					symbolStatus.delStatus();
				}
				
				setVisible(false);
			}
		};
		
		okButtonListener=buttonsListener;
		cancelButtonListener=buttonsListener;
		
		okButton.addActionListener(buttonsListener);
		cancelButton.addActionListener(buttonsListener);

		return widgetsPanel;
	}

}
