/*
 * $Id: Header.java,v 1.1 2005/11/21 09:51:30 dev Exp $
 * $Name:  $
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text;

/**
 * This is an <CODE>Element</CODE> that contains
 * some userdefined meta information about the document.
 * <P>
 * <B>Example:</B>
 * <BLOCKQUOTE><PRE>
 * <STRONG>Header header = new Header("inspired by", "William Shakespeare");</STRONG>
 * </PRE></BLOCKQUOTE>
 *
 * @see		Element
 * @see		Meta
 */

public class Header extends Meta implements Element {
    
    // membervariables
    
/** This is the content of this chunk of text. */
    private StringBuffer name;
    
    // constructors
    
/**
 * Constructs a <CODE>Meta</CODE>.
 *
 * @param	name		the name of the meta-information
 * @param	content		the content
 */
    
    public Header(String name, String content) {
        super(Element.HEADER, content);
        this.name = new StringBuffer(name);
    }
    
    // methods to retrieve information
    
/**
 * Returns the name of the meta information.
 *
 * @return	a <CODE>String</CODE>
 */
    
    public String name() {
        return name.toString();
    }
}