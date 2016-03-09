package com.nbtoptec.symbol.status;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import fr.itris.glips.svgeditor.SymbolMap;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;

public class PopMenu{
	
	
	private JPopupMenu popupMenu;
	
	final Component c;
	
	final SVGHandle handle;
	
	
	public PopMenu(Component c, JPopupMenu popupMenu, SVGHandle handle)
	{
		this.c = c;
		this.popupMenu = popupMenu;
		this.handle = handle;
		
		init();
	}
	
	public void init()
	{
		c.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent evt) {
				
				handle.getSymbolStatusManager().getSnapshot().c = c;
				
				//用于删除元件状态时显示
				SymbolMap.symbolid = handle.getSymbolStatusManager().getSymboId();
				SymbolMap.statusname = handle.getSymbolStatusManager().getStatusAction().getTarget().getName(); 

				if( 3 == evt.getButton())
				{
					//记录鼠标右键的对应的组件和时刻
					
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
