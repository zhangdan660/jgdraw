package com.nbtoptec.draw.manager;


import fr.itris.glips.svgeditor.io.IOManager;

/**
 * 上传元件库
 * @author zhangdan
 * 2016-2-2
 */
public class UpLoad {
	
	/**
	 * the io manager
	 */
	private IOManager ioManager;

	public UpLoad(IOManager ioManager) {
		// TODO Auto-generated constructor stub
		this.ioManager = ioManager;
	}
	
	public void doUpLoad()
	{
		System.out.println("upload operator");
		UploadDialog dlg = new UploadDialog();
		dlg.showDialog(null);
	}

}
