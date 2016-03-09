package fr.itris.glips.svgeditor;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

public class SymbolPopMenu{
	
	
	private JPopupMenu popupMenu;
	
	final Component c;
	
	
	
	public SymbolPopMenu(Component c, JPopupMenu popupMenu)
	{
		this.c = c;
		this.popupMenu = popupMenu;
		
		init();
	}
	
	public void init()
	{
		c.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent evt) {
				
				if( 3 == evt.getButton())
				{
					//记录鼠标右键的对应的组件和时刻
					
					if(SymbolMap.menumap.containsKey(c))
					{
						
						SymbolMap.currentMenu = SymbolMap.menumap.get(c);
					} else {
						
						SymbolMap.currentMenu = null;
					}
					
//					System.out.println("右键点击");
				} else if( 1== evt.getButton())
				{
					
//					System.out.println("左键点击");
				}
				if(SwingUtilities.isRightMouseButton(evt)){		//实现右键弹出式菜单
					
					popupMenu.show(c, evt.getPoint().x, evt.getPoint().y);
				}
				
				super.mousePressed(evt);
			}
			
		});
	}
}
