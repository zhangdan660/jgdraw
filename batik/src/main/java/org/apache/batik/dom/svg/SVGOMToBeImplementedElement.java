/*

   Copyright 2000-2001  The Apache Software Foundation 

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
package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

/**
 * This is a development only class. It is used temporarily in the
 * SVG DOM implementation for SVG elements whose DOM support has not
 * been put in
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @version $Id: SVGOMToBeImplementedElement.java,v 1.1 2005/11/21 09:51:24 dev Exp $
 */
public class SVGOMToBeImplementedElement
    extends SVGGraphicsElement {
    
    /**
     * This element's local name
     */
    protected String localName;

    /**
     * Creates a new SVGOMToBeImplementedElement object.
     */
    protected SVGOMToBeImplementedElement() {
    }

    /**
     * Creates a new SVGOMToBeImplementedElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     * @param localName the local name for the element.
     */
    public SVGOMToBeImplementedElement(String prefix, AbstractDocument owner,
                                       String localName) {
        super(prefix, owner);
        this.localName = localName;
    }

    /**
     * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getLocalName()}.
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new SVGOMToBeImplementedElement();
    }

    /**
     * Exports this node to the given document.
     */
    protected Node export(Node n, AbstractDocument d) {
	super.export(n, d);
	SVGOMToBeImplementedElement ae = (SVGOMToBeImplementedElement)n;
	ae.localName = localName;
	return n;
    }

    /**
     * Deeply exports this node to the given document.
     */
    protected Node deepExport(Node n, AbstractDocument d) {
	super.deepExport(n, d);
	SVGOMToBeImplementedElement ae = (SVGOMToBeImplementedElement)n;
	ae.localName = localName;
	return n;
    }

    /**
     * Copy the fields of the current node into the given node.
     * @param n a node of the type of this.
     */
    protected Node copyInto(Node n) {
	super.copyInto(n);
	SVGOMToBeImplementedElement ae = (SVGOMToBeImplementedElement)n;
	ae.localName = localName;
	return n;
    }

    /**
     * Deeply copy the fields of the current node into the given node.
     * @param n a node of the type of this.
     */
    protected Node deepCopyInto(Node n) {
	super.deepCopyInto(n);
	SVGOMToBeImplementedElement ae = (SVGOMToBeImplementedElement)n;
	ae.localName = localName;
        return n;
    }

	public TypeInfo getSchemaTypeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setIdAttribute(String arg0, boolean arg1) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public void setIdAttributeNS(String arg0, String arg1, boolean arg2) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public void setIdAttributeNode(Attr arg0, boolean arg1) throws DOMException {
		// TODO Auto-generated method stub
		
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
