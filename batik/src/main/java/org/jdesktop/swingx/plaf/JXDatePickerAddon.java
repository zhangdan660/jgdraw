/*
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.plaf;

import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.IconUIResource;
import org.jdesktop.swingx.JXDatePicker;

/**
 * @author Joshua Outwater
 */
public class JXDatePickerAddon extends AbstractComponentAddon {
    public JXDatePickerAddon() {
        super("JXDatePicker");
    }

    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, List<Object> defaults) {
        super.addBasicDefaults(addon, defaults);
        defaults.addAll(Arrays.asList(new Object[]{
                defaults.add(JXDatePicker.uiClassID),
                defaults.add("org.jdesktop.swingx.plaf.basic.BasicDatePickerUI"),
                "JXDatePicker.border",
                new BorderUIResource(BorderFactory.createCompoundBorder(
                        LineBorder.createGrayLineBorder(),
                        BorderFactory.createEmptyBorder(3, 3, 3, 3)))
        }));
    }

    @Override
    protected void addWindowsDefaults(LookAndFeelAddons addon, List<Object> defaults) {
        super.addWindowsDefaults(addon, defaults);
        defaults.addAll(Arrays.asList(new Object[] {
                "JXDatePicker.arrowDown.image",
                new IconUIResource(new ImageIcon(getClass().getResource("resources/combo-xp.png")))
        }));
    }

    @Override
    protected void addMacDefaults(LookAndFeelAddons addon, List<Object> defaults) {
        super.addMacDefaults(addon, defaults);
        defaults.addAll(Arrays.asList(new Object[] {
                "JXDatePicker.arrowDown.image",
                new IconUIResource(new ImageIcon(getClass().getResource("resources/combo-osx.png")))
        }));
    }
}