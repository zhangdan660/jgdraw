/*

   Copyright 2002  The Apache Software Foundation 

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
package org.apache.batik.css.engine;

/**
 * This interface must be implemented by the DOM nodes which represent
 * CSS style-sheets.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: CSSStyleSheetNode.java,v 1.1 2005/11/21 09:51:34 dev Exp $
 */
public interface CSSStyleSheetNode {

    /**
     * Returns the StyleSheet object this node represents. The result
     * is null if no style-sheet is available.
     */
    StyleSheet getCSSStyleSheet();
}