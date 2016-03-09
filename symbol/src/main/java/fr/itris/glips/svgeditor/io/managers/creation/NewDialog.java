package fr.itris.glips.svgeditor.io.managers.creation;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import fr.itris.glips.library.widgets.*;
import fr.itris.glips.svgeditor.SymbolMap;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the dialog used to choose the parameters 
 * used to create a new svg file
 * @author ITRIS, Jordi SUC
 */
@SuppressWarnings("serial")
public class NewDialog extends TitledDialog{

	/**
	 * the file creation manager
	 */
	private FileNew fileCreationManager;
	
	/**
	 * the width spinner widget
	 */
	private IntegerSpinnerWidget widthSpinner;
	
	/**
	 * the height spinner widget
	 */
	private IntegerSpinnerWidget heightSpinner;
	
	private JTextField SymbollibraryText;
	
	private JTextField SymbolnameText; 
	
//	private JTextField SymbolvalueText;
	
//	private JTextField StatusvalueText;
	
	/**
	 * the textfield listener
	 */
	private CaretListener textFieldListener;
	
	/**
	 * the constructor of the class
	 * @param fileCreationManager the file creation manager
	 * @param parent the parent of the dialog
	 */
	public NewDialog(FileNew fileCreationManager, Frame parent){
		
		super(parent, true, true);
		this.fileCreationManager=fileCreationManager;
	}
	
	/**
	 * the constructor of the class
	 * @param fileCreationManager the file creation manager
	 * @param parent the parent of the dialog
	 */
	public NewDialog(FileNew fileCreationManager, JDialog parent){
		
		super(parent, true);
		this.fileCreationManager=fileCreationManager;
	}
	
	@Override
	protected JPanel buildContentPanel(){
		
		//getting the labels
		ResourceBundle bundle=ResourcesManager.bundle;
		String widthLabel=bundle.getString("labelwidth");
		String heightLabel=bundle.getString("labelheight");
//		String titleDialogLabel=bundle.getString("FileNewTitle");
		final String titleDialogMessageLabel=bundle.getString("FileNewMessage");
		
		//getting the labels
		final String textShapeErrorEmptyStringLabel=bundle.getString("TextShapeErrorEmptyString");

		//setting the title
//		setTitleMessage(titleDialogLabel);
		
		//setting the information message
		setMessage(titleDialogMessageLabel, INFORMATION_TYPE);
		
		//creating the jlabels for the spinners
		JLabel widthLbl=new JLabel(widthLabel+" : ");
		widthLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel heightLbl=new JLabel(heightLabel+" : ");
		heightLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel widthPxLbl=new JLabel("px");
		JLabel heightPxLbl=new JLabel("px");
		
		//createing the jlabes for jg requirement
		JLabel Symbollibrary = new JLabel("元件大类: ");
		Symbollibrary.setHorizontalAlignment(SwingConstants.RIGHT);
		SymbollibraryText = new JTextField(10);
		SymbollibraryText.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel Symbolname = new JLabel("元件名: ");
		Symbolname.setHorizontalAlignment(SwingConstants.RIGHT);
		SymbolnameText = new JTextField(10);
		SymbolnameText.setHorizontalAlignment(SwingConstants.LEFT);
		
//		JLabel Symbolvalue = new JLabel("状态名: ");
//		Symbolvalue.setHorizontalAlignment(SwingConstants.RIGHT);
//		SymbolvalueText = new JTextField(10);
//		SymbolvalueText.setHorizontalAlignment(SwingConstants.RIGHT);
//		
//		JLabel Statusvalue = new JLabel("状态值: ");
//		Statusvalue.setHorizontalAlignment(SwingConstants.RIGHT);
//		StatusvalueText = new JTextField(10);
//		StatusvalueText.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//creates the spinners
		widthSpinner=new IntegerSpinnerWidget(1, 1, Integer.MAX_VALUE-1, 1);
		heightSpinner=new IntegerSpinnerWidget(1, 1, Integer.MAX_VALUE-1, 1);
		
		//initializing the spinners
		widthSpinner.init(64);
		heightSpinner.init(64);
		
		//creating and filling the panel that will contain the widgets
		JPanel widgetsPanel=new JPanel();
		widgetsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout layout=new GridBagLayout();
		widgetsPanel.setLayout(layout);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(2, 2, 2, 2);
		
		c.anchor=GridBagConstraints.EAST;
		c.gridx=0;
		c.gridy=0;
		layout.setConstraints(widthLbl, c);
		widgetsPanel.add(widthLbl);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.weightx=50;
		layout.setConstraints(widthSpinner, c);
		widgetsPanel.add(widthSpinner);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=2;
		c.weightx=0;
		layout.setConstraints(widthPxLbl, c);
		widgetsPanel.add(widthPxLbl);
		
		c.anchor=GridBagConstraints.EAST;
		c.gridx=0;
		c.gridy=1;
		layout.setConstraints(heightLbl, c);
		widgetsPanel.add(heightLbl);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.weightx=50;
		layout.setConstraints(heightSpinner, c);
		widgetsPanel.add(heightSpinner);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=2;
		c.weightx=0;
		layout.setConstraints(heightPxLbl, c);
		widgetsPanel.add(heightPxLbl);
		
		//元件库
		c.anchor=GridBagConstraints.EAST;				//当组件没有空间大时，使组件处在东部
		c.gridx=0;										//X2 组件的横坐标
		c.gridy=2;										//Y0组件的纵坐标
		layout.setConstraints(Symbollibrary, c);
		widgetsPanel.add(Symbollibrary);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.weightx=50;									//行的权重
		layout.setConstraints(SymbollibraryText, c);
		widgetsPanel.add(SymbollibraryText);
		
		//元件
		c.anchor=GridBagConstraints.EAST;
		c.gridx=0;
		c.gridy=3;
		layout.setConstraints(Symbolname, c);
		widgetsPanel.add(Symbolname);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.weightx=50;
		layout.setConstraints(SymbolnameText, c);
		widgetsPanel.add(SymbolnameText);
		
//		//状态名
//		c.anchor=GridBagConstraints.EAST;
//		c.gridx=0;
//		c.gridy=4;
//		layout.setConstraints(Symbolvalue, c);
//		widgetsPanel.add(Symbolvalue);
//		
//		c.anchor=GridBagConstraints.WEST;
//		c.gridx=1;
//		c.weightx=50;
//		layout.setConstraints(SymbolvalueText, c);
//		widgetsPanel.add(SymbolvalueText);
//
//		//状态值
//		c.anchor=GridBagConstraints.EAST;
//		c.gridx=0;
//		c.gridy=5;
//		layout.setConstraints(Statusvalue, c);
//		widgetsPanel.add(Statusvalue);
//		
//		c.anchor=GridBagConstraints.WEST;
//		c.gridx=1;
//		c.weightx=50;
//		layout.setConstraints(StatusvalueText, c);
//		widgetsPanel.add(StatusvalueText);
		
		//creating the listener to the text field button
		textFieldListener=new CaretListener(){
			
			public void caretUpdate(CaretEvent e) {
				
				if(SymbolnameText.getText().equals("")){
					
					setMessage(textShapeErrorEmptyStringLabel, TitledDialog.ERROR_TYPE);
					okButton.setEnabled(false);
					
				}else{
					if(SymbolMap.docmap.containsKey(SymbollibraryText.getText()))
					{
						//找到了同名元件大类名
						if(SymbolMap.docmap.get(SymbollibraryText.getText()).containsKey(SymbolnameText.getText()+"_0"))
						{
							setMessage("元件重名，请更改", TitledDialog.ERROR_TYPE);
							okButton.setEnabled(false);
							return;
						}
					}
					
					setMessage(titleDialogMessageLabel, TitledDialog.INFORMATION_TYPE);
					okButton.setEnabled(true);
				}
			}
		};
		
		SymbolnameText.addCaretListener(textFieldListener);
//		
		//creating the ok and cancel listener
		ActionListener buttonsListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
	
				if(evt.getSource().equals(okButton)){
					
					fileCreationManager.createNewDocument2(
						widthSpinner.getWidgetValue()+"", heightSpinner.getWidgetValue()+"", 
						SymbollibraryText.getText(), SymbolnameText.getText());
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
	
	@Override
	public void showDialog(JComponent relativeComponent) {

		widthSpinner.takeFocus();
		super.showDialog(relativeComponent);
	}
	
	@Override
	public void disposeDialog() {
		
		widthSpinner.dispose();
		heightSpinner.dispose();
		
		super.disposeDialog();
	}
}
