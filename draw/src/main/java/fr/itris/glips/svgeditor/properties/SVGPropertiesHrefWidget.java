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

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import net.sf.json.JSONObject;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesHrefWidget extends SVGPropertiesWidget {

	/**
	 * the constructor of the class
	 * 
	 * @param propertyItem
	 *            a property item
	 */
	public SVGPropertiesHrefWidget(SVGPropertyItem propertyItem) {

		super(propertyItem);

		buildComponent();
	}

	/**
	 * builds the component that will be displayed
	 */
	protected void buildComponent() {

		final Editor editor = propertyItem.getProperties().getSVGEditor();
		final ResourceBundle bundle = ResourcesManager.bundle;

		final JTextField textField = new JTextField(
				propertyItem.getGeneralPropertyValue(), 10);
		textField.moveCaretPosition(0);

		final JButton okButton = new JButton();
		final JComboBox<String> targetBox = new JComboBox<String>();
		targetBox.addItem("_blank");
		targetBox.addItem("_self");
		targetBox.addItem("_parent");
		targetBox.addItem("_top");
		targetBox.addItem("_window");
		Insets buttonInsets = new Insets(1, 1, 1, 1);
		okButton.setMargin(buttonInsets);
		okButton.setText(bundle.getString("labelok"));

		Map<String, String> dataMap = propertyItem.getGeneralPropertyValueMap();
		if (dataMap != null && !dataMap.isEmpty()) {
			textField.setText(dataMap.get("xlink:href"));
			targetBox.setSelectedItem(dataMap.get("target"));
		}

		final ActionListener listener = new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				Map<String, String> values = new HashMap<String, String>();
				values.put("xlink:href", textField.getText().trim());
				values.put("target", targetBox.getSelectedItem().toString());
				propertyItem.changePropertyValue(values);
			}
		};

		// adds a listener to the button
		okButton.addActionListener(listener);

		// creates the component that will be returned
		JPanel validatedPanel = new JPanel();
		validatedPanel.setLayout(new BorderLayout());
		validatedPanel.add(textField, BorderLayout.NORTH);
		validatedPanel.add(okButton, BorderLayout.EAST);
		validatedPanel.add(targetBox, BorderLayout.CENTER);

		component = validatedPanel;

		// creates the disposer
		disposer = new Runnable() {

			public void run() {

				okButton.removeActionListener(listener);
			}
		};
	}
}
