/*
 * $Id: GradientPreviewPanel.java,v 1.6 2006/07/21 08:27:22 joshy Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
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

package org.jdesktop.swingx.color;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;
import javax.swing.event.MouseInputAdapter;
import org.apache.batik.ext.awt.LinearGradientPaint;
import org.apache.batik.ext.awt.MultipleGradientPaint;
import org.apache.batik.ext.awt.RadialGradientPaint;
import org.jdesktop.swingx.JXGradientChooser;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.multislider.Thumb;

/**
 *
 * @author jm158417
 */
public class GradientPreviewPanel extends JXPanel {
    private Paint checker_texture = null;
    private Point2D start, end;
    public JXGradientChooser picker;
    boolean moving_start = false;
    boolean moving_end = false;
    
    public GradientPreviewPanel() {
        start = new Point2D.Float(10,10);
        end = new Point2D.Float(80,10);
        checker_texture = ColorUtil.getCheckerPaint();
        MouseInputAdapter ma = new GradientMouseHandler();
        this.addMouseListener(ma);
        this.addMouseMotionListener(ma);
    }
    
    public void setGradient() {
        repaint();
    }
    
    public void setGradient(MultipleGradientPaint grad) {
        if(grad instanceof LinearGradientPaint) {
            LinearGradientPaint paint = (LinearGradientPaint)grad;
            this.start = paint.getStartPoint();
            this.end = paint.getEndPoint();
        } else {
            RadialGradientPaint paint = (RadialGradientPaint)grad;
            this.start = paint.getCenterPoint();
            this.end = new Point2D.Double(start.getX(),start.getY()+paint.getRadius());
        }
        repaint();
    }
    
    public MultipleGradientPaint getGradient() {
        // calculate the color stops
        List<Thumb<Color>> stops = picker.getSlider().getModel().getSortedThumbs();
        int len = stops.size();
        
        // set up the data for the gradient
        float[] fractions = new float[len];
        Color[] colors = new Color[len];
        int i = 0;
        for (Thumb<Color> thumb : stops) {
            colors[i] = (Color)thumb.getObject();
            fractions[i] = thumb.getPosition();
            i++;
        }
        
        // get the final gradient
        MultipleGradientPaint paint = calculateGradient(fractions, colors);
        return paint;
    }
    
    protected void paintComponent(Graphics g) {
        try {
            Graphics2D g2 = (Graphics2D)g;
            
            // fill the background with checker first
            g2.setPaint(checker_texture);
            g.fillRect(0,0,getWidth(),getHeight());
            
            
            Paint paint = getGradient();
            // fill the area
            if(paint != null) {
                g2.setPaint(paint);
            } else {
                g2.setPaint(Color.black);
            }
            
            g.fillRect(0,0,getWidth(),getHeight());
            
            drawHandles(g2);
        } catch (Exception ex) {
            System.out.println("ex: " + ex);
        }
    }
    
    private MultipleGradientPaint calculateGradient(final float[] fractions, final Color[] colors) {
        // set up the end points
        Point2D start = this.start;
        Point2D end = this.end;
        if(picker.reversedCheck.isSelected()) {
            start = this.end;
            end = this.start;
        }
        
        // set up the cycle type
        MultipleGradientPaint.CycleMethodEnum cycle = MultipleGradientPaint.NO_CYCLE;
        if(picker.repeatedRadio.isSelected()) {
            cycle = MultipleGradientPaint.REPEAT;
        }
        if(picker.reflectedRadio.isSelected()) {
            cycle = MultipleGradientPaint.REFLECT;
        }
        
        // create the underlying gradient paint
        MultipleGradientPaint paint = null;
        if(picker.styleCombo.getSelectedItem().toString().equals("Linear")) {
            paint = new org.apache.batik.ext.awt.LinearGradientPaint(
                    (float)start.getX(),
                    (float)start.getY(),
                    (float)end.getX(),
                    (float)end.getY(),
                    fractions,colors,cycle);
        }
        if(picker.styleCombo.getSelectedItem().toString().equals("Radial")) {
            paint = new org.apache.batik.ext.awt.RadialGradientPaint(
                    start, (float)start.distance(end),start,
                    fractions, colors, cycle, MultipleGradientPaint.SRGB
                    );
        }
        return paint;
    }
    
    private void drawHandles(final Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        // draw the points and gradient line
        g2.setColor(Color.black);
        g2.drawOval((int)start.getX()-5,(int)start.getY()-5,10,10);
        g2.setColor(Color.white);
        g2.drawOval((int)start.getX()-4,(int)start.getY()-4,8,8);
        
        g2.setColor(Color.black);
        g2.drawOval((int)end.getX()-5,(int)end.getY()-5,10,10);
        g2.setColor(Color.white);
        g2.drawOval((int)end.getX()-4,(int)end.getY()-4,8,8);
        
        g2.setColor(Color.darkGray);
        g2.drawLine((int)start.getX(),(int)start.getY(),
                (int)end.getX(),(int)end.getY());
        g2.setColor(Color.gray);
        g2.drawLine((int)start.getX()-1,(int)start.getY()-1,
                (int)end.getX()-1,(int)end.getY()-1);
    }
    
    private class GradientMouseHandler extends MouseInputAdapter {
        
        public void mousePressed(MouseEvent evt) {
            moving_start = false;
            moving_end = false;
            if (evt.getPoint().distance(start) < 5) {
                moving_start = true;
                start = evt.getPoint();
                return;
            }
            
            if (evt.getPoint().distance(end) < 5) {
                moving_end = true;
                end = evt.getPoint();
                return;
            }
            
            start = evt.getPoint();
        }
        
        public void mouseDragged(MouseEvent evt) {
            if (moving_start) {
                start = evt.getPoint();
            } else {
                end = evt.getPoint();
            }
            firePropertyChange("gradient",null,getGradient());
            repaint();
        }
    }
}

