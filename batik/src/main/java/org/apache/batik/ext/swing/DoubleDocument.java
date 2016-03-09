/*

   Copyright 2001  The Apache Software Foundation 

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
package org.apache.batik.ext.swing;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Helper class. Only allows an Double value in the document.
 * 
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @version $Id: DoubleDocument.java,v 1.1 2005/11/21 09:51:38 dev Exp $
 */
public class DoubleDocument extends PlainDocument {

    /** 
     * Strip all non digit characters.  The first character must be '-' or '+'.
     * Only one '.' is allowed.
     */
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException { 

        if (str == null) {
            return;
        }

        // Get current value
        String curVal = getText(0, getLength());
        boolean hasDot = curVal.indexOf(".")!=-1;

        // Strip non digit characters
        char[] buffer = str.toCharArray();
        char[] digit = new char[buffer.length];
        int j = 0;

        if(offs==0 && buffer!=null && buffer.length>0 && buffer[0]=='-')
            digit[j++] = buffer[0];

        for (int i = 0; i < buffer.length; i++) {
            if(Character.isDigit(buffer[i]))
                digit[j++] = buffer[i];
            if(!hasDot && buffer[i]=='.'){
                digit[j++] = '.';
                hasDot = true;
            }
        }

        // Now, test that new value is within range.
        String added = new String(digit, 0, j);
        try{
            StringBuffer val = new StringBuffer(curVal);
            val.insert(offs, added);
            if(val.toString().equals(".") || val.toString().equals("-") || val.toString().equals("-."))
                super.insertString(offs, added, a);
            else{
                Double.valueOf(val.toString());
                super.insertString(offs, added, a);
            }
        }catch(NumberFormatException e){
            // Ignore insertion, as it results in an out of range value
        }
    }

    public void setValue(double d){
        try{
            remove(0, getLength());
            insertString(0, (new Double(d)).toString(), null);
        }catch(BadLocationException e){
            // Will not happen because we are sure
            // we use the proper range
        }
    }

    public double getValue(){
        try{
            String t = getText(0, getLength());
            if(t != null && t.length() > 0){
                return Double.parseDouble(t);
            }
            else{
                return 0;
            }
        }catch(BadLocationException e){
            // Will not happen because we are sure
            // we use the proper range
            throw new Error();
        }
    }
}


