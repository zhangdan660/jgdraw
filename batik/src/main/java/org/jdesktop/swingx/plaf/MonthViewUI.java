package org.jdesktop.swingx.plaf;

import javax.swing.plaf.ComponentUI;

public abstract class MonthViewUI extends ComponentUI {
    /**
     * Return a long representing the date at the specified x/y position.
     * It is expected that the date returned will have a valid day, month
     * and year.  All other fields such as hour, minute, second and milli-
     * second will be set to 0.
     * @param x X position
     * @param y Y position
     * @return long The date, -1 if position does not contain a date.
     */
    public abstract long getDayAt(int x, int y);

    /**
     * Calculate the last possible date that can be displayed.
     * This is implemented by the UI since it is in control of layout
     * and may possibly yeild different results based on implementation.
     * @return long The date.
     */
    public abstract long calculateLastDisplayedDate();
}
