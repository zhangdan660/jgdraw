/*

   Copyright 2001-2002  The Apache Software Foundation 

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
package org.apache.batik.gvt.renderer;

/**
 * Interface for a factory of ImageRenderers
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @version $Id: ImageRendererFactory.java,v 1.1 2005/11/21 09:51:40 dev Exp $
 */
public interface ImageRendererFactory extends RendererFactory{
    /**
     * Creates a new static renderer.
     */
    ImageRenderer createStaticImageRenderer();

    /**
     * Creates a new dynamic renderer.
     */
    ImageRenderer createDynamicImageRenderer();
}
