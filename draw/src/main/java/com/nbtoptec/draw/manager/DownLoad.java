package com.nbtoptec.draw.manager;


import fr.itris.glips.svgeditor.io.IOManager;

/**
 * 下载元件库
 * @author zhangdan
 * 2016-2-2
 */
public class DownLoad {

	
	/**
	 * the io manager
	 */
	private IOManager ioManager;
	
	public DownLoad(IOManager ioManager) {
		// TODO Auto-generated constructor stub
		this.ioManager = ioManager;
	}
	
	public void doDownLoad()
	{
		System.out.println("download operator");
		DownLoadDialog dlg = new DownLoadDialog();
		dlg.showDialog(null);
	}

}
