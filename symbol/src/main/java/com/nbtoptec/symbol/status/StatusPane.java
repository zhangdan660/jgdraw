package com.nbtoptec.symbol.status;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.VerticalLayout;
import org.w3c.dom.Document;

import fr.itris.glips.svgeditor.SymbolMap;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * 元件状态面板
 * @author zhangdan
 *
 */
@SuppressWarnings("serial")
public class StatusPane extends JPanel{
	
	private SymbolStatus symbolstatus;
	
	public HashMap<String, HashMap<String, Document>> docmap;
	
	private JXTaskPaneContainer container = null;
	
	private JPopupMenu popupMenu;
	
	private Dimension dim = new Dimension(64, 64);
	
	public StatusPane(SymbolStatus status)
	{
		this.symbolstatus = status;
		build();
	}
	
	public void build()
	{
		popupMenu = symbolstatus.getPopupMenu();
		
		container = new JXTaskPaneContainer(){
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public int getScrollableBlockIncrement(Rectangle visibleRect,
					int orientation, int direction) {
				
				return SwingConstants.VERTICAL == orientation ? visibleRect.height : visibleRect.width;
			}
		};
		
		container.setLayout(new VerticalLayout(0));
		container.setBorder(BorderFactory.createEmptyBorder());
		add(container);
		
	}
	
	public JXTaskPaneContainer getContainer() {
		return container;
	}

	/**
	 * 加载当前元件的所有状态图形
	 * @param handle 
	 */
	public void addStatus(SVGHandle handle)
	{
		System.out.println("current handle name: " + handle.getName());
		
		SymbolStatusManager ssm = handle.getSymbolStatusManager();
		
		ssm.getTaskpane().removeAll();
		
		ssm.getTaskpane().revalidate();
		
		int count = 0;
		
		HashMap<String, StatusAction> seqmap = ssm.genertseq();
		
		for(int i=0; count<ssm.getStatuslist().size(); i++)
		{
			if(seqmap.containsKey(""+i))
			{
				StatusAction sa = seqmap.get(""+i);
				
				sa.getTarget().setIcon(SymbolMap.Doc2Icon(sa.getLocaldoc(), dim));
				
				Component c = ssm.getTaskpane().add(sa);
				
				ssm.getComp2Idmap().put(c, sa.getTarget().getId());
				
				new PopMenu(c, popupMenu, handle);		//绑定鼠标右键事件
				
				count ++;
			}
		}
		
		/*for(StatusAction sa : ssm.getStatuslist() )
		{
			sa.getTarget().setIcon(SymbolMap.Doc2Icon(sa.getLocaldoc(), dim));
			
			Component c = ssm.getTaskpane().add(sa);
			
			ssm.getComp2Idmap().put(c, sa.getTarget().getId());
			
			new PopMenu(c, popupMenu, handle);		//绑定鼠标右键事件
		
		}*/		
		
		container.add(ssm.getTaskpane());
		
		container.revalidate();
		
		container.repaint();
	}

	/**
	 * 生成icon
	 * @param image
	 * @return
	 */
	public Icon getIcon(BufferedImage image)
	{
		
		Icon icon = null;
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
		
		return icon;
	}
	
	
	/**
	 * 清除所有元件状态图形
	 */
	public void removeAllStatus()
	{
		container.removeAll();
		
		container.revalidate();
		
		container.repaint();
	}
}
