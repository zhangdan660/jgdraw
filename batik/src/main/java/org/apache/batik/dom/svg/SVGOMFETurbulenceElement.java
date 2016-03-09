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
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedInteger;
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGFETurbulenceElement;

/**
 * This class implements {@link SVGFETurbulenceElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVGOMFETurbulenceElement.java,v 1.1 2005/11/21 09:51:30 dev Exp $
 */
public class SVGOMFETurbulenceElement
    extends    SVGOMFilterPrimitiveStandardAttributes
    implements SVGFETurbulenceElement {

    /**
     * The 'stitchTiles' attribute values.
     */
    protected final static String[] STITCH_TILES_VALUES = {
        "",
        SVG_STITCH_VALUE,
        SVG_NO_STITCH_VALUE
    };

    /**
     * The 'type' attribute values.
     */
    protected final static String[] TYPE_VALUES = {
        "",
        SVG_FRACTAL_NOISE_VALUE,
        SVG_TURBULENCE_VALUE
    };

    /**
     * Creates a new SVGOMFETurbulence object.
     */
    protected SVGOMFETurbulenceElement() {
    }

    /**
     * Creates a new SVGOMFETurbulenceElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMFETurbulenceElement(String prefix,
                                    AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    public String getLocalName() {
        return SVG_FE_TURBULENCE_TAG;
    }

    /**
     * <b>DOM</b>: Implements {@link
     * SVGFETurbulenceElement#getBaseFrequencyX()}.
     */
    public SVGAnimatedNumber getBaseFrequencyX() {
        throw new RuntimeException("!!! TODO getBaseFrequencyX()");
    }

    /**
     * <b>DOM</b>: Implements {@link
     * SVGFETurbulenceElement#getBaseFrequencyY()}.
     */
    public SVGAnimatedNumber getBaseFrequencyY() {
        throw new RuntimeException("!!! TODO getBaseFrequencyY()");
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFETurbulenceElement#getNumOctaves()}.
     */
    public SVGAnimatedInteger getNumOctaves() {
        return getAnimatedIntegerAttribute(null, SVG_NUM_OCTAVES_ATTRIBUTE, 1);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFETurbulenceElement#getSeed()}.
     */
    public SVGAnimatedNumber getSeed() {
        return getAnimatedNumberAttribute(null, SVG_SEED_ATTRIBUTE, 0f);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFETurbulenceElement#getStitchTiles()}.
     */
    public SVGAnimatedEnumeration getStitchTiles() {
        return getAnimatedEnumerationAttribute
            (null, SVG_STITCH_TILES_ATTRIBUTE, STITCH_TILES_VALUES, (short)2);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFETurbulenceElement#getType()}.
     */
    public SVGAnimatedEnumeration getType() {
        return getAnimatedEnumerationAttribute
            (null, SVG_TYPE_ATTRIBUTE, TYPE_VALUES, (short)2);
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new SVGOMFETurbulenceElement();
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
