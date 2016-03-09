/*

   Copyright 2000-2004  The Apache Software Foundation 

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

import org.apache.batik.css.engine.CSSImportNode;
import org.apache.batik.css.engine.CSSImportedElementRoot;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.dom.util.XMLSupport;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGElementInstance;
import org.w3c.dom.svg.SVGUseElement;

/**
 * This class implements {@link org.w3c.dom.svg.SVGUseElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVGOMUseElement.java,v 1.1 2005/11/21 09:51:30 dev Exp $
 */
public class SVGOMUseElement
    extends    SVGURIReferenceGraphicsElement
    implements SVGUseElement,
               CSSImportNode {

    /**
     * The attribute initializer.
     */
    protected final static AttributeInitializer attributeInitializer;
    static {
        attributeInitializer = new AttributeInitializer(4);
        attributeInitializer.addAttribute(XMLSupport.XMLNS_NAMESPACE_URI,
                                          null, "xmlns:xlink",
                                          XLinkSupport.XLINK_NAMESPACE_URI);
        attributeInitializer.addAttribute(XLinkSupport.XLINK_NAMESPACE_URI,
                                          "xlink", "type", "simple");
        attributeInitializer.addAttribute(XLinkSupport.XLINK_NAMESPACE_URI,
                                          "xlink", "show", "embed");
        attributeInitializer.addAttribute(XLinkSupport.XLINK_NAMESPACE_URI,
                                          "xlink", "actuate", "onLoad");
    }

    /**
     * Store the imported element.
     */
    protected CSSImportedElementRoot cssImportedElementRoot;

    /**
     * Creates a new SVGOMUseElement object.
     */
    protected SVGOMUseElement() {
    }

    /**
     * Creates a new SVGOMUseElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMUseElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    public String getLocalName() {
        return SVG_USE_TAG;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGUseElement#getX()}.
     */
    public SVGAnimatedLength getX() {
        return getAnimatedLengthAttribute
            (null, SVG_X_ATTRIBUTE, SVG_USE_X_DEFAULT_VALUE,
             SVGOMAnimatedLength.HORIZONTAL_LENGTH);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGUseElement#getY()}.
     */
    public SVGAnimatedLength getY() {
        return getAnimatedLengthAttribute
            (null, SVG_Y_ATTRIBUTE, SVG_USE_Y_DEFAULT_VALUE,
             SVGOMAnimatedLength.VERTICAL_LENGTH);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGUseElement#getWidth()}.
     */
    public SVGAnimatedLength getWidth() {
        return getAnimatedLengthAttribute
            (null, SVG_WIDTH_ATTRIBUTE, SVG_USE_WIDTH_DEFAULT_VALUE,
             SVGOMAnimatedLength.HORIZONTAL_LENGTH);
    } 

    /**
     * <b>DOM</b>: Implements {@link SVGUseElement#getHeight()}.
     */
    public SVGAnimatedLength getHeight() {
        return getAnimatedLengthAttribute
            (null, SVG_HEIGHT_ATTRIBUTE, SVG_USE_HEIGHT_DEFAULT_VALUE,
             SVGOMAnimatedLength.VERTICAL_LENGTH);
    } 

    /**
     * <b>DOM</b>: Implements {@link SVGUseElement#getInstanceRoot()}.
     */
    public SVGElementInstance getInstanceRoot() {
	throw new RuntimeException(" !!! TODO: getInstanceRoot()");
    }
 
    /**
     * <b>DOM</b>: Implements {@link SVGUseElement#getAnimatedInstanceRoot()}.
     */
    public SVGElementInstance getAnimatedInstanceRoot() {
	throw new RuntimeException(" !!! TODO: getAnimatedInstanceRoot()");
    }

    // CSSImportNode //////////////////////////////////////////////////

    /**
     * The CSSImportedElementRoot.
     */
    public CSSImportedElementRoot getCSSImportedElementRoot() {
        return cssImportedElementRoot;
    }

    /**
     * Sets the CSSImportedElementRoot.
     */
    public void setCSSImportedElementRoot(CSSImportedElementRoot r) {
        cssImportedElementRoot = r;
    }

    /**
     * Returns the AttributeInitializer for this element type.
     * @return null if this element has no attribute with a default value.
     */
    protected AttributeInitializer getAttributeInitializer() {
        return attributeInitializer;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new SVGOMUseElement();
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
