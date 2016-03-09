package com.nbtoptec.symbol.status.dialog;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.nbtoptec.symbol.status.SymbolStatus;

import fr.itris.glips.library.widgets.TitledDialog;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

@SuppressWarnings("serial")
public class NewStatusDialog extends TitledDialog {
	
	/**
	 * new status manager
	 */
	private SymbolStatus symbolStatus;
	
	private JTextField statusNameText;
	
	private JTextField statusIdText;

	public NewStatusDialog(SymbolStatus symbolStatus, Frame parent) {
		
		super(parent, true, true);
		this.symbolStatus = symbolStatus;
		
	}

	public NewStatusDialog(SymbolStatus symbolStatus, JDialog parent) {

		super(parent, true);
		this.symbolStatus = symbolStatus;
	}

	@Override
	protected JPanel buildContentPanel() {
		
		//getting the labels
//		ResourceBundle bundle=ResourcesManager.bundle;
		String widthLabel ="状态名";
		String heightLabel="状态值";
//		String titleDialogLabel=bundle.getString("FileNewTitle");
		String titleDialogMessageLabel="新建元件状态或修改元件状态";

		//setting the title
//		setTitleMessage(titleDialogLabel);
		
		//setting the information message
		setMessage(titleDialogMessageLabel, INFORMATION_TYPE);
		
		//creating the jlabels for the spinners
		JLabel widthLbl=new JLabel(widthLabel+" : ");
		widthLbl.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel heightLbl=new JLabel(heightLabel+" : ");
		heightLbl.setHorizontalAlignment(SwingConstants.LEFT);
		
		//create the text for input
		statusNameText = new JTextField(10);
		statusNameText.setHorizontalAlignment(SwingConstants.RIGHT);
		statusIdText = new JTextField(10);
		statusIdText.setHorizontalAlignment(SwingConstants.RIGHT);
		
		
		//creating and filling the panel that will contain the widgets
		JPanel widgetsPanel=new JPanel();
		widgetsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout layout=new GridBagLayout();
		widgetsPanel.setLayout(layout);
		GridBagConstraints c=new GridBagConstraints();  //组件布局方式
		c.fill=GridBagConstraints.BOTH;			//当格子有剩余空间时，填充水平空间
		c.insets=new Insets(2, 2, 2, 2);				//组件彼此的间距
		
		/***
		 * Insets是AWT里面一个类的名字，代表着类Insets，它的用途是用来定义组件容器周围的空间大小，其中带有四个参数：
			Insets(第一个参数，第二个参数，第三个参数，第四个参数 )
			第一个参数代表距上面有几个点的空白，第二个参数代
			表距左边有几个点的空白，第三个参数代表距下边有几个点的空白区域，第
			四个参数代表距右边留几个点的空白区域。
		 */
		
		c.anchor=GridBagConstraints.WEST;				//当组件没有空间大时，使组件处在东部
		c.gridx=0;										//X2 组件的横坐标
		c.gridy=0;										//Y0组件的纵坐标
		layout.setConstraints(widthLbl, c);
		widgetsPanel.add(widthLbl);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.weightx=50;									//行的权重
		layout.setConstraints(statusNameText, c);
		widgetsPanel.add(statusNameText);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=0;
		c.gridy=1;
		layout.setConstraints(heightLbl, c);
		widgetsPanel.add(heightLbl);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.weightx=50;
		layout.setConstraints(statusIdText, c);
		widgetsPanel.add(statusIdText);
		
		//creating the ok and cancel listener
		ActionListener buttonsListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
	
				if(evt.getSource().equals(okButton)){
					symbolStatus.editStatus(statusNameText.getText(), statusIdText.getText());
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


	public void setStatusNameText(String textvalue) {
		this.statusNameText.setText(textvalue);
	}

	public void setStatusIdText(String textvalue) {
		this.statusIdText.setText(textvalue);
	}

}
