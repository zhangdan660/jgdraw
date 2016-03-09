/*

   Copyright 2000-2003  The Apache Software Foundation 

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
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFEGaussianBlurElement;

/**
 * This class implements {@link SVGFEGaussianBlurElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVGOMFEGaussianBlurElement.java,v 1.1 2005/11/21 09:51:24 dev Exp $
 */
public class SVGOMFEGaussianBlurElement
    extends    SVGOMFilterPrimitiveStandardAttributes
    implements SVGFEGaussianBlurElement {

    /**
     * Creates a new SVGOMFEGaussianBlurElement object.
     */
    protected SVGOMFEGaussianBlurElement() {
    }

    /**
     * Creates a new SVGOMFEGaussianBlurElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMFEGaussianBlurElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    public String getLocalName() {
        return SVG_FE_GAUSSIAN_BLUR_TAG;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFEGaussianBlurElement#getIn1()}.
     */
    public SVGAnimatedString getIn1() {
        return getAnimatedStringAttribute(null, SVG_IN_ATTRIBUTE);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * SVGFEGaussianBlurElement#getStdDeviationX()}.
     */
    public SVGAnimatedNumber getStdDeviationX() {
        throw new RuntimeException("!!! TODO: getStdDeviationX");
    }

    /**
     * <b>DOM</b>: Implements {@link
     * SVGFEGaussianBlurElement#getStdDeviationY()}.
     */
    public SVGAnimatedNumber getStdDeviationY() {
        throw new RuntimeException("!!! TODO: getStdDeviationY");
    }

    /**
     * <b>DOM</b>: Implements {@link
     * SVGFEGaussianBlurElement#setStdDeviation(float,float)}.
     */
    public void setStdDeviation (float devX, float devY) {
        setAttributeNS(null, SVG_STD_DEVIATION_ATTRIBUTE,
                       Float.toString(devX) + " " + Float.toString(devY));
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new SVGOMFEGaussianBlurElement();
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
