package fr.itris.glips.svgeditor;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class EUnit {
	private String name="DrawUnit";
	private String category;
	private String shortDescription = "unit"; // used for tooltips
	private String id;
	private Icon icon;
	private BufferedImage image;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	public Icon getIcon() {
		if (icon == null) 
		{
			if(image != null )
			{
				try {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					ImageIO.write(image, "png", out);
					icon = new ImageIcon(out.toByteArray());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return icon;
	}
	
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
