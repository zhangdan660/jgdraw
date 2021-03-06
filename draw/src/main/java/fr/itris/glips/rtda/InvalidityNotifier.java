package fr.itris.glips.rtda;

import javax.swing.*;

import fr.itris.glips.rtda.resources.*;

import java.util.*;
import java.util.Timer;
import java.util.concurrent.*;
import java.awt.*;
import java.awt.geom.*;

import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.components.picture.Painter;

/**
 * the class used to visually notify that a tag is invalid
 * @author ITRIS, Jordi SUC
 */
public class InvalidityNotifier {

	/**
	 * the icon for the invalid state
	 */
	private static ImageIcon invalidIcon=RtdaResources.getIcon("invalidIcon", false);
	
	/**
	 * the set of the listeners that are in an invalid state
	 */
	private CopyOnWriteArraySet<DataChangedListener> listeners=
		new CopyOnWriteArraySet<DataChangedListener>();
	
	/**
	 * the current painters map
	 */
	private ConcurrentHashMap<SVGPicture, CopyOnWriteArrayList<Painter>> pictureToPaintersMap=
		new ConcurrentHashMap<SVGPicture, CopyOnWriteArrayList<Painter>>();

	/**
	 * whether the invalid markers are enabled or not
	 */
	private boolean invalidMarkersEnabled=true;
	
	/**
	 * whether the invalid markers are shown or not
	 */
	private boolean invalidMarkersShown=false;
	
	/**
	 * the timers
	 */
	private Timer updateTimer=new Timer(), invalidTimer=new Timer();
	
	/**
	 * whether the listeners are being updated
	 */
	private boolean isUpdating=false;
	
    /**
     * the constructor of the class
     * @param animationsHandler the handler of the animations
     */
    public InvalidityNotifier(AnimationsHandler animationsHandler){
		
		TimerTask task=new TimerTask(){
			
			@Override
			public void run() {

				if(invalidMarkersEnabled){
					
					paint();
				}
			}
		};
		
		invalidTimer.schedule(task, 0, 1000);
	}
    
    /**
     * registers a new data changed listener
     * @param listener a data changed listener
     */
    public void registerListener(DataChangedListener listener) {
    	
   		listeners.add(listener);
		
		if(! isUpdating){
			
			synchronized (this) {isUpdating=true;}
			
    		TimerTask task=new TimerTask(){
    			
    			@Override
    			public void run() {
    				
					requestUpdate();
    				
        			synchronized (InvalidityNotifier.this) {isUpdating=false;}
    			}
    		};
    		
    		updateTimer.schedule(task, 3000);
		}
    }
    
    /**
     * unregisters the given data changed listener
     * @param listener a data changed listener
     */
    public void unregisterListener(DataChangedListener listener) {

    	if(listener!=null) {

    		listeners.remove(listener);
    		
    		if(! isUpdating){
    			
    			synchronized (this) {isUpdating=true;}
    			
        		TimerTask task=new TimerTask(){
        			
        			@Override
        			public void run() {
        				
    					requestUpdate();
        				
            			synchronized (InvalidityNotifier.this) {isUpdating=false;}
        			}
        		};
        		
        		updateTimer.schedule(task, 3000);
    		}
    	}
    }
    
    /**
     * requests that the invalid panels should be updated
     */
    protected void requestUpdate(){

    	clearPainters();
    	pictureToPaintersMap.clear();
    	
   	   	CopyOnWriteArrayList<Painter> paintersList=null;
    	Painter painter=null;
    	
    	for(final DataChangedListener listener : listeners){
    		
    		if(listener.isInvalidTag()){
    			
    			final SVGPicture picture=listener.getPicture();
    			paintersList=pictureToPaintersMap.get(picture);
    			
    			if(paintersList==null){
    				
    				paintersList=new CopyOnWriteArrayList<Painter>();
    				pictureToPaintersMap.put(picture, paintersList);
    			}
    			
    			//creating the painter
    			painter=new Painter() {

    				public void paint(Graphics2D g) {
    						
						//getting the bounds of the element
						Rectangle2D bounds=AnimationsToolkit.getNodeBounds(picture, listener.getParentElement());

			            if(bounds!=null){
			            	
			            	//getting the location of the icon
			            	Point location=new Point(
			            		(int)(bounds.getX()+bounds.getWidth()/2-invalidIcon.getIconWidth()/2),
			            			(int)(bounds.getY()+bounds.getHeight()/2-invalidIcon.getIconHeight()/2));

			            	//painting the image
			            	g.drawImage(invalidIcon.getImage(), location.x, location.y, picture.getCanvas());
			            }
    				}
    			};
    			
    			paintersList.add(painter);

    		}else{
    			
    			listeners.remove(listener);
    		}
    	}
    }
    
    /**
     * paints the image
     */
    protected void paint(){
    	
    	for(final SVGPicture picture : pictureToPaintersMap.keySet()){
    		
			if(invalidMarkersShown){
				
				clearPainters();
				invalidMarkersShown=false;
				
			}else{
				
				picture.getCanvas().addPainters(pictureToPaintersMap.get(picture));
				invalidMarkersShown=true;
			}

    		try{
    			SwingUtilities.invokeAndWait(new Runnable() {

    				public void run() {
    					
    		    		picture.getCanvas().repaint();
    				}
    			});
    		}catch (Exception ex){ex.printStackTrace();}
    	}
    }
    
    /**
     * sets whether the invalid markers are enabled or not
     * @param enabled whether the invalid markers are enabled or not
     */
    public void setInvalidMarkersEnabled(boolean enabled) {
    	
    	invalidMarkersEnabled=enabled;
    	
    	if(! invalidMarkersEnabled){
    		
        	clearPainters();
    		
    	}else{
    		
    		requestUpdate();
    	}
    }

    /**
	 * @return the invalidMarkersEnabled
	 */
	public boolean isInvalidMarkersEnabled() {
		return invalidMarkersEnabled;
	}

	/**
	 * clears all the painters
	 */
	protected void clearPainters(){

		for(final SVGPicture picture : pictureToPaintersMap.keySet()){
			
			picture.getCanvas().clearPainters();
			
			SwingUtilities.invokeLater(new Runnable(){
				
				public void run() {
					
					picture.getCanvas().repaint();
				}
			});
		}
	}
}
