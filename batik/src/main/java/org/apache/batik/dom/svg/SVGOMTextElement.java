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

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.svg.SVGAnimatedLengthList;
import org.w3c.dom.svg.SVGAnimatedTransformList;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.svg.SVGTextElement;

/**
 * This class implements {@link SVGTextElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVGOMTextElement.java,v 1.1 2005/11/21 09:51:24 dev Exp $
 */
public class SVGOMTextElement
    extends    SVGOMTextPositioningElement
    implements SVGTextElement {

    // Default values for attributes on a text element
    public static final String X_DEFAULT_VALUE = "0";
    public static final String Y_DEFAULT_VALUE = "0";

    /**
     * Creates a new SVGOMTextElement object.
     */
    protected SVGOMTextElement() {
    }

    /**
     * Creates a new SVGOMTextElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMTextElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    public String getLocalName() {
        return SVG_TEXT_TAG;
    }

    // SVGLocatable support /////////////////////////////////////////////

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGLocatable#getNearestViewportElement()}.
     */
    public SVGElement getNearestViewportElement() {
	return SVGLocatableSupport.getNearestViewportElement(this);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGLocatable#getFarthestViewportElement()}.
     */
    public SVGElement getFarthestViewportElement() {
	return SVGLocatableSupport.getFarthestViewportElement(this);
    }

    /**
     * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGLocatable#getBBox()}.
     */
    public SVGRect getBBox() {
	return SVGLocatableSupport.getBBox(this);
    }

    /**
     * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGLocatable#getCTM()}.
     */
    public SVGMatrix getCTM() {
	return SVGLocatableSupport.getCTM(this);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGLocatable#getScreenCTM()}.
     */
    public SVGMatrix getScreenCTM() {
	return SVGLocatableSupport.getScreenCTM(this);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGLocatable#getTransformToElement(SVGElement)}.
     */
    public SVGMatrix getTransformToElement(SVGElement element)
	throws SVGException {
	return SVGLocatableSupport.getTransformToElement(this, element);
    }

    // SVGTransformable support /////////////////////////////////////////////

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGTransformable#getTransform()}.
     */
    public SVGAnimatedTransformList getTransform() {
	return SVGTransformableSupport.getTransform(this);
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new SVGOMTextElement();
    }

    // SVGTextPositioningElement support ////////////////////////////////////

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGTextPositioningElement#getX()}.
     */
    public SVGAnimatedLengthList getX() {
        SVGOMAnimatedLengthList result = (SVGOMAnimatedLengthList)
            getLiveAttributeValue(null, SVGConstants.SVG_X_ATTRIBUTE);
        if (result == null) {
            result = new SVGOMAnimatedLengthList(this, null,
                                                 SVGConstants.SVG_X_ATTRIBUTE,
                                                 X_DEFAULT_VALUE,
                                                 AbstractSVGLength.HORIZONTAL_LENGTH);
            putLiveAttributeValue(null,
                                  SVGConstants.SVG_X_ATTRIBUTE, result);
        }
        return result;
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGTextPositioningElement#getY()}.
     */
    public SVGAnimatedLengthList getY() {
        SVGOMAnimatedLengthList result = (SVGOMAnimatedLengthList)
            getLiveAttributeValue(null, SVGConstants.SVG_Y_ATTRIBUTE);
        if (result == null) {
            result = new SVGOMAnimatedLengthList(this, null,
                                                 SVGConstants.SVG_Y_ATTRIBUTE,
                                                 Y_DEFAULT_VALUE,
                                                 AbstractSVGLength.VERTICAL_LENGTH);
            putLiveAttributeValue(null,
                                  SVGConstants.SVG_Y_ATTRIBUTE, result);
        }
        return result;
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
