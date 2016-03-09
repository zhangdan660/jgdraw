/*

   Copyright 1999-2003  The Apache Software Foundation 

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

package org.apache.batik.dom.traversal;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.TreeWalker;

/**
 * This class provides support for traversal.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: TraversalSupport.java,v 1.1 2005/11/21 09:51:40 dev Exp $
 */
public class TraversalSupport {
    
    /**
     * The iterators list.
     */
    protected List iterators;

    /**
     * Creates a new TraversalSupport.
     */
    public TraversalSupport() {
    }

    /**
     * Creates a new tree walker.
     */
    public static TreeWalker createTreeWalker(AbstractDocument doc,
                                              Node root,
                                              int whatToShow, 
                                              NodeFilter filter, 
                                              boolean entityReferenceExpansion) {
        if (root == null) {
            throw doc.createDOMException
                (DOMException.NOT_SUPPORTED_ERR, "null.root",  null);
        }
        return new DOMTreeWalker(root, whatToShow, filter,
                                 entityReferenceExpansion);
    }

    /**
     * Creates a new node iterator.
     */
    public NodeIterator createNodeIterator(AbstractDocument doc,
                                           Node root,
                                           int whatToShow, 
                                           NodeFilter filter, 
                                           boolean entityReferenceExpansion)
        throws DOMException {
        if (root == null) {
            throw doc.createDOMException
                (DOMException.NOT_SUPPORTED_ERR, "null.root",  null);
        }
        NodeIterator result = new DOMNodeIterator(doc, root, whatToShow,
                                                  filter,
                                                  entityReferenceExpansion);
        if (iterators == null) {
            iterators = new LinkedList();
        }
        iterators.add(result);

        return result;
    }

    /**
     * Called by the DOM when a node will be removed from the current document.
     */
    public void nodeToBeRemoved(Node removedNode) {
        if (iterators != null) {
            Iterator it = iterators.iterator();
            while (it.hasNext()) {
                ((DOMNodeIterator)it.next()).nodeToBeRemoved(removedNode);
            }
        }
    }

    /**
     * Detaches the given node iterator.
     */
    public void detachNodeIterator(NodeIterator it) {
        iterators.remove(it);
    }
}
