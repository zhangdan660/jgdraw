/*
 * JXMultiThumbSliderAddon.java
 *
 * Created on May 9, 2006, 4:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx.plaf;

import java.util.Arrays;
import java.util.List;
import org.jdesktop.swingx.JXMultiThumbSlider;

/**
 *
 * @author jm158417
 */
public class JXMultiThumbSliderAddon extends AbstractComponentAddon {
    
    /** Creates a new instance of JXMultiThumbSliderAddon */
    public JXMultiThumbSliderAddon() {
        super("JXMultiThumbSliderAddon");
    }
    
    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, List<Object> defaults) {
        defaults.addAll(Arrays.asList(new Object[] {
                defaults.add(JXMultiThumbSlider.uiClassID),
                defaults.add("org.jdesktop.swingx.plaf.basic.BasicMultiThumbSliderUI")
        }));
    }
    
}
