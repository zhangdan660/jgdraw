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
package org.apache.batik.ext.awt.image.rendered;

import  java.awt.image.Raster;


/**
 * This the generic interface for a TileStore.  This is used to
 * store and retrieve tiles from the cache.
 */
public interface TileStore {
    
    public void setTile(int x, int y, Raster ras);

    public Raster getTile(int x, int y);

    // This is return the tile if it is available otherwise
    // returns null.  It will not compute the tile if it is
    // not present.
    public Raster getTileNoCompute(int x, int y);
}
