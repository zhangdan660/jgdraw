/*

   Copyright 1999-2003  The Apache Software Foundation 

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package org.apache.batik.dom;

import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.util.HashTable;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.stylesheets.LinkStyle;
import org.w3c.dom.stylesheets.StyleSheet;

/**
 * This class provides an implementation of the 'xml-stylesheet' processing
 * instructions.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: StyleSheetProcessingInstruction.java,v 1.1 2005/11/21 09:51:20 dev Exp $
 */
public class StyleSheetProcessingInstruction
    extends AbstractProcessingInstruction
    implements LinkStyle {

    /**
     * Is this node immutable?
     */
    protected boolean readonly;

    /**
     * The style sheet.
     */
    protected transient StyleSheet sheet;

    /**
     * The stylesheet factory.
     */
    protected StyleSheetFactory factory;

    /**
     * The pseudo attributes.
     */
    protected transient HashTable pseudoAttributes;

    /**
     * Creates a new ProcessingInstruction object.
     */
    protected StyleSheetProcessingInstruction() {
    }

    /**
     * Creates a new ProcessingInstruction object.
     */
    public StyleSheetProcessingInstruction(String            data,
					   AbstractDocument  owner,
					   StyleSheetFactory f) {
	ownerDocument = owner;
	setData(data);
	factory = f;
    }

    /**
     * Tests whether this node is readonly.
     */
    public boolean isReadonly() {
	return readonly;
    }

    /**
     * Sets this node readonly attribute.
     */
    public void setReadonly(boolean v) {
	readonly = v;
    }

    /**
     * Sets the node name.
     */
    public void setNodeName(String v) {
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.ProcessingInstruction#getTarget()}.
     * @return "xml-stylesheet".
     */
    public String getTarget() {
	return "xml-stylesheet";
    }

    /**
     *  The style sheet. 
     */
    public StyleSheet getSheet() {
	if (sheet == null) {
	    sheet = factory.createStyleSheet(this, getPseudoAttributes());
	}
	return sheet;
    }

    /**
     * Returns the pseudo attributes in a table.
     */
    public HashTable getPseudoAttributes() {
        if (pseudoAttributes == null) {
            pseudoAttributes = new HashTable();
            pseudoAttributes.put("alternate", "no");
            pseudoAttributes.put("media",     "all");
            DOMUtilities.parseStyleSheetPIData(data, pseudoAttributes);
        }
        return pseudoAttributes;
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.ProcessingInstruction#setData(String)}.
     */
    public void setData(String data) throws DOMException {
	super.setData(data);
	sheet = null;
        pseudoAttributes = null;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new StyleSheetProcessingInstruction();
    }

	public String getBaseURI() {
		// TODO Auto-generated method stub
		return null;
	}

	public short compareDocumentPosition(Node arg0) throws DOMException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getTextContent() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTextContent(String arg0) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public boolean isSameNode(Node arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public String lookupPrefix(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDefaultNamespace(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public String lookupNamespaceURI(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEqualNode(Node arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object getFeature(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object setUserData(String arg0, Object arg1, UserDataHandler arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getUserData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}