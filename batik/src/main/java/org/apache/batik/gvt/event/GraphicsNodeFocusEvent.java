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
package org.apache.batik.gvt.event;

import org.apache.batik.gvt.GraphicsNode;

/**
 * A low-level event which indicates that a graphics node has gained or
 * lost the keyboard focus.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @version $Id: GraphicsNodeFocusEvent.java,v 1.1 2005/11/21 09:51:37 dev Exp $
 */
public class GraphicsNodeFocusEvent extends GraphicsNodeEvent {

    /**
     * The first number in the range of ids used for focus events.
     */
    static final int FOCUS_FIRST = 1004;

    /**
     * The id for the "focusGained" event. This event indicates that
     * the component gained the keyboard focus.
     */
    public static final int FOCUS_GAINED = FOCUS_FIRST;

    /**
     * The id for the "focusLoses" event. This event indicates that
     * the component lost the keyboard focus.
     */
    public static final int FOCUS_LOST = FOCUS_FIRST + 1;

    /**
     * Constructs a new graphics node focus event.
     * @param source the graphics node where the event originated
     * @param id the id of this event
     */
    public GraphicsNodeFocusEvent(GraphicsNode source, int id) {
        super(source, id);
    }
}
