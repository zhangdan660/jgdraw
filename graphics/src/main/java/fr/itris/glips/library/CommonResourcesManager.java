package fr.itris.glips.library;

import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;

import sun.reflect.Reflection;

public class CommonResourcesManager {
	   /**
     * the HashMap associating a an icon's name to an icon
     */
    private static final HashMap<String, ImageIcon> icons=
    	new HashMap<String, ImageIcon>();
    
    /**
     * the HashMap associating a an icon's name to a gray icon
     */
    private static final HashMap<String, ImageIcon> grayIcons=
    	new HashMap<String, ImageIcon>();
    
    /**
     * computes the path of a resource given its name
     * @param resource the name of a resource 
     * @return the full path of a resource
     */
    public static String getPath(String resource){
        
        String path="";
        
        try{
        	if(resource.contains("icon"))		//to find icon 
        	{
        		String tmppath =Class.class.getClass().getResource("/").getPath();
        		path = "file:" + tmppath.replaceAll("/draw", "/graphics")+resource;
        	} else {
        		path =Class.class.getClass().getResource("/").getPath() + resource;
        	}
//            path=Reflection.getCallerClass().getClassLoader().getResource(resource).toExternalForm();
        }catch (Exception ex){ path="";}
 
        return path;
    }
    
	   /**
     * gives an ImageIcon object given the name of it as it is witten in the SVGEditorIcons.properties file
     * @param name the name of an icon
     * @param isGrayIcon true if the icon should be used for a disabled widget
     * @return an image icon 
     */
    public static ImageIcon getIcon(String name, boolean isGrayIcon){
        
        ImageIcon icon=null;
        
        if(name!=null && ! name.equals("")){
            
            if(icons.containsKey(name)){
                
                if(isGrayIcon){
                    
                    icon=grayIcons.get(name);
                    
                }else{
                    
                    icon=icons.get(name);
                }
                
            }else{
                
                //gets the name of the icons from the resources
                ResourceBundle iconsBundle=null;
                
                try{
                    iconsBundle=ResourceBundle.getBundle(
                    		"properties/SVGEditorIcons");
                }catch (Exception ex){}
                
                String path="";
                
                if(iconsBundle!=null){
                    
                    try{path=iconsBundle.getString(name);}catch (Exception ex){path="";}
                    
                    if(path!=null && ! path.equals("")){
                        
                        try{
                            icon=new ImageIcon(new URL(getPath("icons/"+path)));
                        }catch (Exception ex){}

                        if(icon!=null){
                            
                            icons.put(name, icon);
                            Image image=icon.getImage();
                            
                            ImageIcon grayIcon=new ImageIcon(GrayFilter.createDisabledImage(image));
                            grayIcons.put(name, grayIcon);
                            
                            if(isGrayIcon){
                                
                                icon=grayIcon;
                            }
                        }
                    }
                }
            }
        }
        
        return icon;
    }
}
