package fr.itris.glips.svgeditor;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.RootGraphicsNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

public class SVG2BufferedImage {
	
	/**
	 * creates the image at the given dimension
	 * @param size the dimension of the created image
	 * @param encodeAlpha whether the image should use a alpha channel
	 */
	public static BufferedImage createImage(final Dimension size, Document document) {
		
		RootGraphicsNode gvtRoot = null;
		
		BufferedImage image = new BufferedImage(size.width, size.height,BufferedImage.TYPE_INT_RGB);
		
		try {
			UserAgentAdapter userAgent=new UserAgentAdapter();
			BridgeContext ctx=new BridgeContext(userAgent, null, new DocumentLoader(userAgent));
			GVTBuilder builder=new GVTBuilder();
			
			GraphicsNode gvt=builder.build(ctx, document);
			
            if(gvt!=null) {
            	gvtRoot=gvt.getRoot();
            }
			
			//getting the size of the canvas
			Point2D canvasSize=getGeometryCanvasSize(document);
			
			//computing the scale transformation for the svg doc content to fit the created image size
			AffineTransform af=AffineTransform.getScaleInstance(
					size.width/canvasSize.getX(), size.height/canvasSize.getY());
			
			//getting the graphics object of the image
			Graphics2D g2=image.createGraphics();
			
			//painting a white background
			g2.fillRect(0, 0, image.getWidth(), image.getHeight());
			
			handleGraphicsConfiguration(g2);
			g2.setTransform(af);
			
            //painting the image
            gvtRoot.paint(g2);
            g2.dispose();
            ctx.dispose();
            
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            ImageIO.write(image, "jpg", out);
//            
//			return out.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return image;
	}
	
	/**
	 * computes and returns the canvas' size of the given svg document
	 * @param document a svg document
	 * @return the canvas' size of the given svg document
	 */
	public static Point2D getGeometryCanvasSize(Document document){
		
		//gets the root element
		if(document!=null){
			
			Element root=document.getDocumentElement();
			
			if(root!=null){
				
				double w = getPixelledNumber(root.getAttributeNS(null, "width"));
				double h = getPixelledNumber(root.getAttributeNS(null, "height"));
				
				return new Point2D.Double(w, h);
			}
		}
		
		return new Point2D.Double(0,0);
	}
	
	 /**
     * computes the number corresponding to this string in pixel
     * @param str
     * @return the number corresponding to this string in pixel
     */
    public static double getPixelledNumber(String str){
        
        double i=0;
        
        if(str!=null && ! str.equals("")){
            
            str=str.trim();
            
            if(! Character.isDigit(str.charAt(str.length()-1))){
                
                String unit=str.substring(str.length()-2, str.length());
                String nb=str.substring(0, str.length()-2);
                
                try{
                    i=Double.parseDouble(nb);
                }catch (Exception ex){}
                
                if(unit.equals("pt")){
                    
                    i=i*1.25;
                    
                }else if(unit.equals("pc")){
                    
                    i=i*15;
                    
                }else if(unit.equals("mm")){
                    
                    i=i*3.543307;
                    
                }else if(unit.equals("cm")){
                    
                    i=i*35.43307;
                    
                }else if(unit.equals("in")){
                    
                    i=i*90;
                }
                
            }else{
                
                try{
                    i=Double.parseDouble(str);
                }catch (Exception ex){}
            }
        }
        
        return i;
    }
    
    /**
	 * sets the parameters of the given graphics object
	 * @param g2 a graphics object
	 */
	public static void handleGraphicsConfiguration(Graphics2D g2) {
		
		//setting the rendering hints
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
	}
	
	public  static void main(String[] args) throws Exception
	{
		File f = new File("D:\\t.svg");
		SAXSVGDocumentFactory factory=new SAXSVGDocumentFactory("");
		SVGDocument doc=factory.createSVGDocument(f.toURI().toASCIIString());
		if(doc!=null)
		{
			SVG2BufferedImage.createImage(new Dimension((int)320, (int)320), doc);
		}
	}
}
