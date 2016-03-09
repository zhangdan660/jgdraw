/*

   Copyright 2002-2003  The Apache Software Foundation 

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
package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.SVGStylableElement;
import org.apache.batik.util.SVG12Constants;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

/**
 * This class implements a multiImage extension to SVG.
 *
 * The 'multiImage' element is similar to the 'image' element (supports
 * all the same attributes and properties) except.
 * <ol>
 *    <li>It has two addtional attributes: 'pixel-width' and
 *        'pixel-height' which are the maximum width and height of the
 *        image referenced by the xlink:href attribute.</li>
 *    <li>It can contain a child element 'subImage' which has 
 *        two attributes, pixel-width, pixel-height.
 *        It holds SVG content to be rendered.</li>
 *    <li>It can contain a child element 'subImageRef' which has only
 *        three attributes, pixel-width, pixel-height and xlink:href.
 *        The image displayed is the smallest image such that
 *        pixel-width and pixel-height are greater than or equal to the
 *        required image size for display.</li>
 * </ol>
 *
 * @author <a href="mailto:thomas.deweese@kodak.com">Thomas DeWeese</a>
 * @version $Id: SVGOMMultiImageElement.java,v 1.1 2005/11/21 09:51:40 dev Exp $ */
public class SVGOMMultiImageElement
    extends    SVGStylableElement {

    /**
     * Creates a new SVG MultiImageElement object.
     */
    protected SVGOMMultiImageElement() {
    }

    /**
     * Creates a new SVG MultiImageElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMMultiImageElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getLocalName()}.
     */
    public String getLocalName() {
        return SVG12Constants.SVG_MULTI_IMAGE_TAG;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new SVGOMMultiImageElement();
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
