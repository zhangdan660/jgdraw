/*

   Copyright 2000  The Apache Software Foundation 

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

import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

/**
 * This class implements the {@link org.w3c.dom.CDATASection} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: GenericCDATASection.java,v 1.1 2005/11/21 09:51:20 dev Exp $
 */
public class GenericCDATASection extends AbstractText implements CDATASection {
    /**
     * Is this element immutable?
     */
    protected boolean readonly;

    /**
     * Creates a new CDATASection object.
     */
    protected GenericCDATASection() {
    }

    /**
     * Creates a new CDATASection object.
     */
    public GenericCDATASection(String value, AbstractDocument owner) {
	ownerDocument = owner;
	setNodeValue(value);
    }

    /**
     * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNodeName()}.
     * @return "#cdata-section".
     */
    public String getNodeName() {
	return "#cdata-section";
    }

    /**
     * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNodeType()}.
     * @return {@link org.w3c.dom.Node#CDATA_SECTION_NODE}
     */
    public short getNodeType() {
	return CDATA_SECTION_NODE;
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
     * Creates a text node of the current type.
     */
    protected Text createTextNode(String text) {
	return getOwnerDocument().createCDATASection(text);
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new GenericCDATASection();
    }

	public boolean isElementContentWhitespace() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getWholeText() {
		// TODO Auto-generated method stub
		return null;
	}

	public Text replaceWholeText(String arg0) throws DOMException {
		// TODO Auto-generated method stub
		return null;
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
