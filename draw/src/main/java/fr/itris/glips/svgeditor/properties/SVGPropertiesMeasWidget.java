/*
 * Created on 19 janv. 2005
 * 
 =============================================
                   GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
GLIPS Graffiti Editor, a SVG Editor
Copyright (C) 2004 Jordi SUC, Philippe Gil, SARL ITRIS

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact : jordi.suc@itris.fr; philippe.gil@itris.fr

 =============================================
 */
package fr.itris.glips.svgeditor.properties;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.actions.frame.MeasSelFrame;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesMeasWidget extends SVGPropertiesWidget {

	/**
	 * the constructor of the class
	 * 
	 * @param propertyItem
	 *            a property item
	 */
	public SVGPropertiesMeasWidget(SVGPropertyItem propertyItem) {

		super(propertyItem);

		buildComponent();
	}

	/**
	 * builds the component that will be displayed
	 */
	protected void buildComponent() {

		final Editor editor = propertyItem.getProperties().getSVGEditor();
		final ResourceBundle bundle = ResourcesManager.bundle;
		final SVGHandle handle = editor.getHandlesManager().getCurrentHandle();

		// the text field in which the value will be entered

		final JLabel masterStateLabel = new JLabel("主状态/值");
		final JLabel deputyStateLabel = new JLabel("副状态/值");

		final JTextField primaryMeasValue = new JTextField();
		final JTextField primaryMeasText = new JTextField();
		final JTextField secondaryMeasValue = new JTextField();
		final JTextField secondaryMeasText = new JTextField();
		final JTextField psrValue = new JTextField();
		Map<String, String> dataMap = propertyItem.getGeneralPropertyValueMap();
		if (dataMap != null && !dataMap.isEmpty()) {
			psrValue.setText(dataMap.get("psr_id"));
			primaryMeasValue.setText(dataMap.get("primary_meas"));
			primaryMeasText.setText(dataMap.get("primary_meas_text"));
			secondaryMeasValue.setText(dataMap.get("secondary_meas"));
			secondaryMeasText.setText(dataMap.get("secondary_meas_text"));
		}
		final JButton okButton = new JButton();
		Insets buttonInsets = new Insets(1, 1, 1, 1);
		okButton.setMargin(buttonInsets);

		final JButton cmdButton = new JButton();
		okButton.setMargin(buttonInsets);

		String errorTitle = "", errorIdMessage = "";

		if (bundle != null) {

			try {
				okButton.setText("选择");
				cmdButton.setText("确定");
				errorTitle = bundle.getString("property_errortitle");
				errorIdMessage = bundle.getString("property_erroridmessage");
			} catch (Exception ex) {
			}
		}

		final String ferrorTitle = errorTitle;
		final String ferrorIdMessage = errorIdMessage;
		final ActionListener listener = new ActionListener() {
			MeasSelFrame measFrame = null;

			public void actionPerformed(ActionEvent arg0) {
				if (measFrame == null) {
					JTextField[] jf = new JTextField[] { primaryMeasValue,
							primaryMeasText, secondaryMeasValue,
							secondaryMeasText, psrValue };
					measFrame = new MeasSelFrame(jf);
				}
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				int width = 360;
				int height = 450;
				measFrame.setBounds((d.width - width) / 2,
						(d.height - height) / 2, width, height);
				measFrame.setSize(width, height);
				measFrame.setResizable(false);
				measFrame.setVisible(true);
			}
		};

		// adds a listener to the button
		okButton.addActionListener(listener);
		cmdButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Map<String, String> values = new HashMap<String, String>();
				values.put("primary_meas", primaryMeasValue.getText());
				values.put("primary_meas_text", primaryMeasText.getText());
				values.put("secondary_meas", secondaryMeasValue.getText());
				values.put("secondary_meas_text", secondaryMeasText.getText());
				values.put("psr_id", psrValue.getText());
				propertyItem.changePropertyValue(values);

			}
		});
		// creates the component that will be returned
		JPanel validatedPanel = new JPanel();
		validatedPanel
				.setLayout(new BoxLayout(validatedPanel, BoxLayout.Y_AXIS));
		JPanel xPanel1 = new JPanel();
		xPanel1.setLayout(new BoxLayout(xPanel1, BoxLayout.X_AXIS));
		JPanel xPanel2 = new JPanel();
		xPanel2.setLayout(new BoxLayout(xPanel2, BoxLayout.X_AXIS));
		JPanel xPanel3 = new JPanel();
		xPanel3.setLayout(new BoxLayout(xPanel3, BoxLayout.X_AXIS));
		validatedPanel.add(xPanel1);
		validatedPanel.add(Box.createVerticalStrut(10));
		validatedPanel.add(xPanel2);
		validatedPanel.add(Box.createVerticalStrut(10));
		validatedPanel.add(xPanel3);
		okButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		xPanel1.add(Box.createVerticalStrut(10));
		xPanel1.add(okButton);
		psrValue.setVisible(false);
		xPanel1.add(psrValue);
		xPanel1.add(cmdButton);
		xPanel2.add(masterStateLabel);
		primaryMeasValue.setVisible(false);
		xPanel2.add(primaryMeasValue);
		xPanel2.add(Box.createVerticalStrut(10));
		primaryMeasText.setEditable(false);
		xPanel2.add(primaryMeasText);
		xPanel2.add(Box.createVerticalStrut(10));
		xPanel3.add(deputyStateLabel);
		secondaryMeasValue.setVisible(false);
		xPanel3.add(Box.createVerticalStrut(10));
		xPanel3.add(secondaryMeasValue);
		xPanel3.add(Box.createVerticalStrut(10));
		secondaryMeasText.setEditable(false);
		xPanel3.add(secondaryMeasText);

		component = validatedPanel;

		// creates the disposer
		disposer = new Runnable() {

			public void run() {

				okButton.removeActionListener(listener);
			}
		};
	}
}
