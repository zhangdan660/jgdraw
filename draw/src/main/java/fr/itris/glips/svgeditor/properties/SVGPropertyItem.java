/*
 * Created on 2 juin 2004
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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoActionList;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

/**
 * @author ITRIS, Jordi SUC
 *
 *         The class allowing to know the name and value of a property
 */
public class SVGPropertyItem {

	/**
	 * the list of the selected nodes
	 */
	private LinkedList nodeList;

	/**
	 * the type, the name and the value of the property, the constraint linked
	 * with this property the generalPopertyValue is the value that will be
	 * displayed for the whole nodes of the list
	 */
	private String propertyType, propertyName, propertyValueType,
			defaultPropertyValue = "", propertyConstraint = "",
			generalPropertyValue = "";

	private Map<String, String> generalPropertyValueMap = null,
			defaultPropertyValueMap = null;

	/**
	 * the labels associated with the property
	 */
	private String propertyLabel;

	/**
	 * the map associating a node to the value of the property
	 */
	private LinkedHashMap propertyValues = new LinkedHashMap();

	/**
	 * the map associating one value name of the property to its value
	 */
	private LinkedHashMap valuesMap;

	/**
	 * the map associating one value name of the property to its label
	 */
	private LinkedHashMap valuesLabelMap;

	/**
	 * the undo/redo labels
	 */
	private String undoredoproperties = "";

	/**
	 * the bundle used to get labels
	 */
	private ResourceBundle bundle = null;

	/**
	 * the properties object
	 */
	private SVGProperties properties = null;

	/**
	 * the constructor of the class
	 * 
	 * @param properties
	 *            the properties object
	 * @param nodeList
	 *            the list of the nodes
	 * @param propertyType
	 *            the type of the property
	 * @param propName
	 *            the name of the property
	 * @param propertyValueType
	 *            the name of the widget that has to be used to modify the
	 *            property
	 * @param defaultPropertyValue
	 *            the default value of the property
	 * @param propertyConstraint
	 *            whether the property is required or not
	 * @param valuesMap
	 *            the map associated the name of a value to its value
	 */
	public SVGPropertyItem(SVGProperties properties, LinkedList nodeList,
			String propertyType, String propName, String propertyValueType,
			String defaultPropertyValue, String propertyConstraint,
			LinkedHashMap valuesMap) {

		this.properties = properties;
		this.nodeList = nodeList;
		this.propertyType = propertyType;

		try {
			this.propertyName = propName.substring(propName.indexOf("_") + 1,
					propName.length());
		} catch (Exception ex) {
			this.propertyName = propName;
		}

		this.propertyValueType = propertyValueType;
		this.defaultPropertyValue = defaultPropertyValue;
		this.propertyConstraint = propertyConstraint;
		this.valuesMap = valuesMap;

		this.bundle = ResourcesManager.bundle;

		// gets the property label from the resources
		if (bundle != null) {

			try {
				propertyLabel = bundle.getString(propName);
				undoredoproperties = bundle.getString("undoredoproperties");
			} catch (Exception ex) {
			}

			if (propertyLabel == null
					|| (propertyLabel != null && propertyLabel.equals(""))) {

				propertyLabel = this.propertyName;
			}
		}

		// fils the valuesLabelMap with the labels associated with each value
		// name of a property if it exits
		if (valuesMap != null && valuesMap.size() > 0 && bundle != null) {

			valuesLabelMap = new LinkedHashMap();
			String name = "", label = "";

			for (Iterator it = valuesMap.keySet().iterator(); it.hasNext();) {

				try {
					name = (String) it.next();
				} catch (Exception ex) {
					name = null;
				}

				if (name != null && !name.equals("")) {

					try {
						label = bundle.getString(name);
					} catch (Exception ex) {
						label = "";
					}

					// if no label has been found, the label is set to the name
					if (label == null || (label != null && label.equals(""))) {

						label = name;
					}

					valuesLabelMap.put(name, label);
				}
			}
		}

		// sets the value of the property taking it from the node attributes
		if (propertyType != null && this.propertyName != null) {

			if (propertyType.equals("style")) {

				generalPropertyValue = getStylePropertyValue(this.propertyName);

			} else if (propertyType.equals("attribute")) {

				generalPropertyValue = getAttributeValue(this.propertyName);
				generalPropertyValueMap = getAttributeValueMap(this.propertyName);

			} else if (propertyType.equals("child")) {

				generalPropertyValue = getChildValue(this.propertyName);
			}
		}
	}

	/**
	 * @return Returns the properties.
	 */
	public SVGProperties getProperties() {
		return properties;
	}

	/**
	 * @return the list of the nodes
	 */
	public Collection getNodeList() {
		return nodeList;
	}

	/**
	 * @return the type of the property
	 */
	public String getPropertyType() {
		return propertyType;
	}

	/**
	 * @return the name of the property
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @return the valueType of the property
	 */
	public String getPropertyValueType() {
		return propertyValueType;
	}

	/**
	 * @return the default value of the property
	 */
	public String getDefaultPropertyValue() {
		return defaultPropertyValue;
	}

	/**
	 * @return the property constraint (normal or required)
	 */
	public String getPropertyConstraint() {
		return propertyConstraint;
	}

	/**
	 * @return the value of the property
	 */
	public String getGeneralPropertyValue() {
		return generalPropertyValue;
	}

	public Map<String, String> getGeneralPropertyValueMap() {
		return generalPropertyValueMap;
	}

	/**
	 * returns the value of the property for a given node
	 * 
	 * @param node
	 *            a node
	 * @return the value of the property corresponding to the node
	 */
	public String getPropertyValue(Node node) {

		String val = "";

		if (node != null) {

			try {

				val = (String) propertyValues.get(node);
			} catch (Exception ex) {
			}
		}

		return val;
	}

	/**
	 * @return the label of the property
	 */
	public String getPropertyLabel() {
		return propertyLabel;
	}

	/**
	 * @return the map associating a value name of the property to its value
	 */
	public LinkedHashMap getPropertyValuesMap() {
		return valuesMap;
	}

	/**
	 * @return the map associating a value name of the property to its label
	 */
	public LinkedHashMap getPropertyValuesLabelMap() {
		return valuesLabelMap;
	}

	/**
	 * sets the value of the property, the value is taken from the value given
	 * by the widget
	 * 
	 * @param value
	 *            the new value for the property
	 */
	public void changePropertyValue(final String value) {

		String newValue = value;

		if ((newValue == null || newValue.equals("")
				&& (propertyConstraint != null && propertyConstraint
						.equals("required")))) {

			newValue = defaultPropertyValue;
		}

		// the current svg handle
		final SVGHandle handle = properties.getSVGEditor().getHandlesManager()
				.getCurrentHandle();

		if (handle != null && propertyType != null) {

			final LinkedHashMap oldPropertyValues = new LinkedHashMap(
					propertyValues);

			// creates a new values map associating a node to the current
			// widgetPropertyValue
			LinkedHashMap values = new LinkedHashMap();
			Node node = null;

			for (Iterator it = propertyValues.keySet().iterator(); it.hasNext();) {

				try {
					node = (Node) it.next();
				} catch (Exception ex) {
					node = null;
				}

				if (node != null) {

					values.put(node, newValue);
				}
			}

			// the maps that will be used for the undo/redo action
			final LinkedHashMap oldValues = new LinkedHashMap(propertyValues);
			final LinkedHashMap newValues = new LinkedHashMap(values);
			final LinkedHashMap fvalues = values;

			// getting the set of the selected elements
			final Set<Element> selectedElements = new HashSet<Element>(handle
					.getSelection().getSelectedElements());

			// creating the set of the elements that will be modified
			final Set<Element> elements = new HashSet<Element>();

			for (Object object : values.keySet()) {

				if (object != null && object instanceof Element) {

					elements.add((Element) object);
				}
			}

			// the refresh runnable
			final Runnable refreshRunnable = new Runnable() {

				public void run() {

					AbstractShape shape = null;

					for (Element el : elements) {

						shape = ShapeToolkit.getShapeModule(el);

						if (shape != null) {

							shape.refresh(el);
						}
					}
				}
			};

			Runnable executeRunnable = new Runnable() {

				public void run() {

					// sets the new value taken from the widget and gets the new
					// value once set from the node
					if (propertyType.equals("style")) {

						setStylePropertyValue(propertyName, fvalues);

					} else if (propertyType.equals("attribute")) {

						setAttributeValue(propertyName, fvalues);

					} else if (propertyType.equals("child")) {

						setChildValue(propertyName, fvalues);
					}

					refreshRunnable.run();

					handle.getScrollPane().getSVGCanvas().doRepaint(null);
					handle.getSelection().refreshSelection(false);
				}
			};

			Runnable undoRunnable = new Runnable() {

				public void run() {

					// setting the old value
					if (propertyType.equals("style")) {

						setStylePropertyValue(propertyName, oldValues);

					} else if (propertyType.equals("attribute")) {

						setAttributeValue(propertyName, oldValues);

					} else if (propertyType.equals("child")) {

						setChildValue(propertyName, oldValues);
					}

					refreshRunnable.run();

					// redraws the window
					handle.getSelection().handleSelection(selectedElements,
							false, true);
				}
			};

			Runnable redoRunnable = new Runnable() {

				public void run() {

					// sets the new value
					if (propertyType.equals("style")) {

						setStylePropertyValue(propertyName, newValues);

					} else if (propertyType.equals("attribute")) {

						setAttributeValue(propertyName, newValues);

					} else if (propertyType.equals("child")) {

						setChildValue(propertyName, newValues);
					}

					refreshRunnable.run();

					// redraws the window
					handle.getSelection().handleSelection(selectedElements,
							false, true);
				}
			};

			UndoRedoAction action = new UndoRedoAction(undoredoproperties,
					executeRunnable, undoRunnable, redoRunnable, elements);

			// creates the undo/redo list so that actions can be added to it
			UndoRedoActionList actionlist = new UndoRedoActionList(
					undoredoproperties, true);
			actionlist.add(action);

			handle.getUndoRedo().addActionList(actionlist, false);
		}
	}

	/**
	 * 添加设置Map类型的方法
	 * 
	 * @param value
	 *            the new value for the property
	 */
	public void changePropertyValue(final Map<String, String> valueMap) {

		Map<String, String> newValueMap = valueMap;

		// the current svg handle
		final SVGHandle handle = properties.getSVGEditor().getHandlesManager()
				.getCurrentHandle();

		if (handle != null && propertyType != null) {

			final LinkedHashMap oldPropertyValues = new LinkedHashMap(
					propertyValues);

			// creates a new values map associating a node to the current
			// widgetPropertyValue
			LinkedHashMap values = new LinkedHashMap();
			Node node = null;

			for (Iterator it = propertyValues.keySet().iterator(); it.hasNext();) {

				try {
					node = (Node) it.next();
				} catch (Exception ex) {
					node = null;
				}

				if (node != null) {

					values.put(node, newValueMap);
				}
			}

			// the maps that will be used for the undo/redo action
			final LinkedHashMap oldValues = new LinkedHashMap(propertyValues);
			final LinkedHashMap newValues = new LinkedHashMap(values);
			final LinkedHashMap fvalues = values;

			// getting the set of the selected elements
			final Set<Element> selectedElements = new HashSet<Element>(handle
					.getSelection().getSelectedElements());

			// creating the set of the elements that will be modified
			final Set<Element> elements = new HashSet<Element>();

			for (Object object : values.keySet()) {

				if (object != null && object instanceof Element) {

					elements.add((Element) object);
				}
			}

			// the refresh runnable
			final Runnable refreshRunnable = new Runnable() {

				public void run() {

					AbstractShape shape = null;

					for (Element el : elements) {

						shape = ShapeToolkit.getShapeModule(el);

						if (shape != null) {

							shape.refresh(el);
						}
					}
				}
			};

			Runnable executeRunnable = new Runnable() {

				public void run() {

					// sets the new value taken from the widget and gets the new
					// value once set from the node
					if (propertyType.equals("style")) {

						setStylePropertyValue(propertyName, fvalues);

					} else if (propertyType.equals("attribute")) {

						setAttributeValueMap(propertyName, fvalues);

					} else if (propertyType.equals("child")) {

						setChildValue(propertyName, fvalues);
					}

					refreshRunnable.run();

					handle.getScrollPane().getSVGCanvas().doRepaint(null);
					handle.getSelection().refreshSelection(false);
				}
			};

			Runnable undoRunnable = new Runnable() {

				public void run() {

					// setting the old value
					if (propertyType.equals("style")) {

						setStylePropertyValue(propertyName, oldValues);

					} else if (propertyType.equals("attribute")) {

						setAttributeValueMap(propertyName, oldValues);

					} else if (propertyType.equals("child")) {

						setChildValue(propertyName, oldValues);
					}

					refreshRunnable.run();

					// redraws the window
					handle.getSelection().handleSelection(selectedElements,
							false, true);
				}
			};

			Runnable redoRunnable = new Runnable() {

				public void run() {

					// sets the new value
					if (propertyType.equals("style")) {

						setStylePropertyValue(propertyName, newValues);

					} else if (propertyType.equals("attribute")) {

						setAttributeValueMap(propertyName, newValues);

					} else if (propertyType.equals("child")) {

						setChildValue(propertyName, newValues);
					}

					refreshRunnable.run();

					// redraws the window
					handle.getSelection().handleSelection(selectedElements,
							false, true);
				}
			};

			UndoRedoAction action = new UndoRedoAction(undoredoproperties,
					executeRunnable, undoRunnable, redoRunnable, elements);

			// creates the undo/redo list so that actions can be added to it
			UndoRedoActionList actionlist = new UndoRedoActionList(
					undoredoproperties, true);
			actionlist.add(action);

			handle.getUndoRedo().addActionList(actionlist, false);
		}
	}

	/**
	 * @param name
	 *            the name of the property in the style attribute
	 * @return the value of he property
	 */
	public String getStylePropertyValue(String name) {

		// clears the map associating a node to its value
		propertyValues.clear();

		if (nodeList != null) {

			String value = "";
			Element element = null;

			// for each node in the list
			for (Iterator it = nodeList.iterator(); it.hasNext();) {

				try {
					element = (Element) it.next();
				} catch (Exception ex) {
					element = null;
				}

				value = properties.getSVGEditor().getSVGToolkit()
						.getStyleProperty(element, name);

				if (value == null || (value != null && value.equals(""))) {

					value = defaultPropertyValue;
				}

				if (element != null) {

					propertyValues.put(element, value);
				}
			}
		}

		// the value that will be returned
		String returnedValue = "";

		// if the list contains a single element, its value will be returned,
		// otherwise the empty string is returned
		if (nodeList.size() == 1) {

			try {
				returnedValue = (String) propertyValues
						.get(nodeList.getFirst());
			} catch (Exception ex) {
				returnedValue = "";
			}
		}

		return returnedValue;
	}

	/**
	 * @param name
	 *            the name of the attribute
	 * @return the value of the attribute
	 */
	public String getAttributeValue(String name) {

		// clears the map associating a node to its value
		propertyValues.clear();

		String value = "";
		Element element = null;

		// for each node in the list
		for (Iterator it = nodeList.iterator(); it.hasNext();) {

			try {
				element = (Element) it.next();
			} catch (Exception ex) {
				element = null;
			}

			if (element != null && name != null && !name.equals("")) {

				value = element.getAttribute(name);
			}

			if (value == null || (value != null && value.equals(""))) {

				value = defaultPropertyValue;
			}

			if (element != null) {

				propertyValues.put(element, value);
			}
		}
		// the value that will be returned
		String returnedValue = "";

		// if the list contains a single element, its value will be returned,
		// otherwise the empty string is returned
		if (nodeList.size() == 1) {

			try {
				returnedValue = (String) propertyValues
						.get(nodeList.getFirst());
			} catch (Exception ex) {
				returnedValue = "";
			}
		}

		return returnedValue;
	}

	/**
	 * 得到Map值
	 * 
	 * @param name
	 *            the name of the attribute
	 * @return the value of the attribute
	 */
	public Map<String, String> getAttributeValueMap(String name) {

		// clears the map associating a node to its value
		propertyValues.clear();

		Map<String, String> valueMap = null;
		Element element = null;

		// for each node in the list
		for (Iterator it = nodeList.iterator(); it.hasNext();) {

			try {
				element = (Element) it.next();
			} catch (Exception ex) {
				element = null;
			}

			if (element != null && name != null && !name.equals("")) {

				if (name.equals("href")) {
					Element href = (Element) element.getParentNode();
					valueMap = new HashMap<String, String>();
					valueMap.put("xlink:href", href.getAttribute("xlink:href"));
					valueMap.put("target", href.getAttribute("target"));
				} else if (name.equals("metadata")) {
					Object obj=element.getNextSibling();
					if(!(obj instanceof org.apache.batik.dom.GenericText))
					{
//					Element metadata = (Element) element.getNextSibling();
						Element metadata =(Element) obj;
						if (metadata != null
								&& metadata.getNodeName().equals("metadata")) {
							Element ref = (Element) metadata.getFirstChild();
							if (ref != null && ref.getNodeName().equals("ref")) {
								valueMap = new HashMap<String, String>();
								valueMap.put("primary_meas",
										ref.getAttribute("primary_meas"));
								valueMap.put("primary_meas_text",
										ref.getAttribute("primary_meas_text"));
								valueMap.put("secondary_meas",
										ref.getAttribute("secondary_meas"));
								valueMap.put("secondary_meas_text",
										ref.getAttribute("secondary_meas_text"));
								valueMap.put("psr_id", ref.getAttribute("psr_id"));
							}
						}
					}
				}
			}

			if (valueMap == null || valueMap.isEmpty()) {

				valueMap = defaultPropertyValueMap;
			}

			if (element != null) {

				propertyValues.put(element, valueMap);
			}
		}
		// the value that will be returned
		Map<String, String> returnedValue = null;

		// if the list contains a single element, its value will be returned,
		// otherwise the empty string is returned
		if (nodeList.size() == 1) {

			try {
				returnedValue = (Map<String, String>) propertyValues
						.get(nodeList.getFirst());
			} catch (Exception ex) {
				returnedValue = null;
			}
		}

		return returnedValue;
	}

	/**
	 * @param name
	 *            the name of the child node
	 * @return the value of the child node
	 */
	public String getChildValue(String name) {

		// clears the map associating a node to its value
		propertyValues.clear();

		String value = "";
		Node node = null, cur = null;

		// for each node in the list
		for (Iterator it = nodeList.iterator(); it.hasNext();) {

			try {
				node = (Node) it.next();
			} catch (Exception ex) {
				node = null;
			}

			if (node != null && name != null && !name.equals("")) {

				// for each child of the given element, tests if the name of
				// these children is equals to the parameter string
				for (cur = node.getFirstChild(); cur != null; cur = cur
						.getNextSibling()) {

					if (cur.getNodeName().equals(name)) {

						value = cur.getNodeValue();
						break;
					}
				}
			}

			if (value == null || (value != null && value.equals(""))) {

				value = defaultPropertyValue;
			}

			if (node != null) {

				// value=getSVGEditor().getSVGToolkit().normalizeTextNodeValue(value);
				propertyValues.put(node, value);
			}
		}

		// the value that will be returned
		String returnedValue = "";

		// if the list contains a single element, its value will be returned,
		// otherwise the empty string is returned
		if (nodeList.size() == 1) {

			try {
				returnedValue = (String) propertyValues
						.get(nodeList.getFirst());
			} catch (Exception ex) {
				returnedValue = "";
			}
		}

		return returnedValue;
	}

	/**
	 * sets the value of a property in the style attribute
	 * 
	 * @param name
	 *            the name of the property
	 * @param values
	 *            the map associating a node to its value of the property
	 */
	public void setStylePropertyValue(String name, LinkedHashMap values) {

		if (nodeList != null) {

			Element element = null;
			String value = "", oldValue = "";

			// for each node in the list
			for (Iterator it = nodeList.iterator(); it.hasNext();) {

				try {
					element = (Element) it.next();
					value = (String) values.get(element);
					oldValue = (String) propertyValues.get(element);
				} catch (Exception ex) {
					element = null;
					value = null;
					oldValue = "";
				}

				if (element != null && name != null && !name.equals("")
						&& value != null && !value.equals(oldValue)) {

					properties.getSVGEditor().getSVGToolkit()
							.setStyleProperty(element, name, value);
				}
			}
		}

		generalPropertyValue = getStylePropertyValue(name);
	}

	/**
	 * sets the value of the given attribute
	 * 
	 * @param name
	 *            the name of the attribute
	 * @param values
	 *            the map associating a node to its value of the property
	 */
	public void setAttributeValue(String name, LinkedHashMap values) {

		if (nodeList != null) {

			Element element = null;
			String value = "", oldValue = "";

			// for each node in the list
			for (Iterator it = nodeList.iterator(); it.hasNext();) {

				try {
					element = (Element) it.next();
					value = (String) values.get(element);
					oldValue = (String) propertyValues.get(element);
				} catch (Exception ex) {
					element = null;
					value = null;
					oldValue = "";
				}

				if (element != null && name != null && !name.equals("")
						&& value != null && !value.equals(oldValue)) {
					element.setAttribute(name, value);
				}
			}
		}

		generalPropertyValue = getAttributeValue(name);
	}

	/**
	 * 添加设置map值的方法
	 * 
	 * @param name
	 *            the name of the attribute
	 * @param values
	 *            the map associating a node to its value of the property
	 */
	public void setAttributeValueMap(String name, LinkedHashMap values) {

		if (nodeList != null) {

			Element element = null;
			Map<String, String> value = null, oldValue = null;

			// for each node in the list
			for (Iterator it = nodeList.iterator(); it.hasNext();) {

				try {
					element = (Element) it.next();
					value = (Map<String, String>) values.get(element);
					if (propertyValues.get(element) instanceof Map)
						oldValue = (Map<String, String>) propertyValues
								.get(element);
				} catch (Exception ex) {
					element = null;
					value = null;
					oldValue = null;
				}

				if (element != null && name != null && !name.equals("")
						&& value != null && !value.isEmpty()) {

					if (name.equals("href")) {
						Element href = (Element) element;//.getParentNode();
						for (Map.Entry<String, String> entity : value
								.entrySet()) {
							href.setAttribute(entity.getKey(),
									entity.getValue());
						}
					} else if (name.equals("metadata")) {
						SVGHandle handle = properties.getSVGEditor()
								.getHandlesManager().getCurrentHandle();
						Document doc = handle.getCanvas().getDocument();
						Element metadata = (Element) element.getNextSibling();
						if (metadata == null
								|| !metadata.getNodeName().equals("metadata")) {
							metadata = doc.createElement("metadata");
						}
						Element ref = (Element) metadata.getFirstChild();
						if (ref == null || !ref.getNodeName().equals("ref")) {
							ref = doc.createElement("ref");
						}
						for (Map.Entry<String, String> entity : value
								.entrySet()) {
							ref.setAttribute(entity.getKey(), entity.getValue());
						}

						metadata.appendChild(ref);
						element.getParentNode().appendChild(metadata);
					}
				}
			}
		}

		generalPropertyValueMap = getAttributeValueMap(name);
	}

	/**
	 * sets the value of the child with the given name
	 * 
	 * @param name
	 *            the name of the child node
	 * @param values
	 *            the map associating a node to its value of the property
	 */
	public void setChildValue(String name, LinkedHashMap values) {

		if (nodeList != null) {

			Element element = null;
			String value = "", oldValue = "";

			// for each node in the list
			for (Iterator it = nodeList.iterator(); it.hasNext();) {

				try {
					element = (Element) it.next();
					value = (String) values.get(element);
					oldValue = (String) propertyValues.get(element);
				} catch (Exception ex) {
					element = null;
					value = null;
					oldValue = "";
				}

				if (element != null && name != null && !name.equals("")
						&& value != null && !value.equals(oldValue)) {

					// checks all the child nodes of the element to find the
					// text node, if it is found, sets its value
					for (Node cur = element.getFirstChild(); cur != null; cur = cur
							.getNextSibling()) {

						if (cur.getNodeName().equals(name)) {

							// sets the value of the node
							cur.setNodeValue(value);
							break;
						}
					}
				}
			}
		}

		generalPropertyValue = getChildValue(name);
	}
}
