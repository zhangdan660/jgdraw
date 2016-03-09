package com.nbtoptec.symbol.manager;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import fr.itris.glips.library.widgets.TitledDialog;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.SymbolManager;
import fr.itris.glips.svgeditor.SymbolMap;

public class DownLoadDialog extends TitledDialog{

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 设置服务器地址
	 */
	private JTextField serviceAddrText;
	
	/**
	 * 用户名
	 */
	private JTextField userText;
	
	/**
	 * 密码
	 */
	private JTextField passwdText;
	
	/**
	 * 同步进度
	 */
	private JProgressBar progressBar;
	
	/**
	 * 同步过程
	 */
	private JLabel currentName;
	
	private final int  port = 21;
	
	/** 本地字符编码 */
	private String LOCAL_CHARSET = "GBK";
	 
	/**
	 * FTP协议里面，规定文件名编码为iso-8859-1
	 */
	private String SERVER_CHARSET = "ISO-8859-1";
	
	public DownLoadDialog() {
		super((Frame)Editor.getParent(), true, true);
	}

	@Override
	protected JPanel buildContentPanel() {
		// TODO Auto-generated method stub
		
		
		final String titleDialogMessageLabel = "从指定服务器下载元件库";
		
		//setting the title
		setTitleMessage("同步元件库");
		
		//setting the information message
		setMessage(titleDialogMessageLabel, INFORMATION_TYPE);

		//createing the jlabes for jg requirement
		JLabel serviceAddr = new JLabel("服务器地址：");
		serviceAddr.setHorizontalAlignment(SwingConstants.RIGHT);
		serviceAddrText = new JTextField(40);
		serviceAddrText.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel user = new JLabel("用户名：");
		user.setHorizontalAlignment(SwingConstants.RIGHT);
		userText = new JTextField(40);
		userText.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel passwd = new JLabel("密码：");
		passwd.setHorizontalAlignment(SwingConstants.RIGHT);
		passwdText = new JTextField(40);
		passwdText.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel progress = new JLabel("当前进度：");
		progress.setHorizontalAlignment(SwingConstants.RIGHT);
		progressBar=new JProgressBar();
		progressBar.setStringPainted(true);
//		progressBar.setValue(4);
		
		JLabel current = new JLabel("当前操作：");
		current.setHorizontalAlignment(SwingConstants.RIGHT);
		currentName = new JLabel();
		currentName.setHorizontalAlignment(SwingConstants.LEFT);
				
		//creating and filling the panel that will contain the widgets
		JPanel widgetsPanel=new JPanel();
		widgetsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout layout=new GridBagLayout();
		widgetsPanel.setLayout(layout);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(2, 2, 2, 2);
		
		
		c.anchor=GridBagConstraints.EAST;
		c.gridx=0;
		c.gridy=0;
		layout.setConstraints(serviceAddr, c);
//		widgetsPanel.add(serviceAddr);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.gridy=0;
		c.weightx=50;
		layout.setConstraints(serviceAddrText, c);
//		widgetsPanel.add(serviceAddrText);
		
		c.anchor=GridBagConstraints.EAST;
		c.gridx=0;
		c.gridy=1;
		layout.setConstraints(user, c);
//		widgetsPanel.add(user);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.gridy=1;
		c.weightx=50;
		layout.setConstraints(userText, c);
//		widgetsPanel.add(userText);
		
		c.anchor=GridBagConstraints.EAST;
		c.gridx=0;
		c.gridy=2;
		layout.setConstraints(passwd, c);
//		widgetsPanel.add(passwd);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.gridy=2;
		c.weightx=50;
		layout.setConstraints(passwdText, c);
//		widgetsPanel.add(passwdText);
		
		c.anchor=GridBagConstraints.EAST;
		c.gridx=0;
		c.gridy=3;
		layout.setConstraints(progress, c);
		widgetsPanel.add(progress);
		
		c.anchor = GridBagConstraints.WEST;
		c.gridx=1;
		c.gridy=3;
		c.weightx=100;
		layout.setConstraints(progressBar, c);
		widgetsPanel.add(progressBar);
		
		c.anchor = GridBagConstraints.EAST;
		c.gridx=0;
		c.gridy=4;
		layout.setConstraints(current, c);
		widgetsPanel.add(current);
		
		c.anchor = GridBagConstraints.WEST;
		c.gridx=1;
		c.gridy=4;
		layout.setConstraints(currentName, c);
		widgetsPanel.add(currentName);
		
		//creating the ok and cancel listener
		ActionListener buttonsListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				if(evt.getSource().equals(okButton)){
					//执行同步操作
					doOperator(SymbolConfig.addr, SymbolConfig.user, SymbolConfig.passwd);
				} else {
					
					setVisible(false);
				}
				
			}
		};
		
		okButton.setText("同步"); 
		cancelButton.setText("关闭");
		okButtonListener=buttonsListener;
		cancelButtonListener=buttonsListener;
		
		okButton.addActionListener(buttonsListener);
		cancelButton.addActionListener(buttonsListener);
		
		return widgetsPanel;
	}
	
	@Override
	public void showDialog(JComponent relativeComponent) {

		super.showDialog(relativeComponent);
	}
	
	@Override
	public void disposeDialog() {
		
		super.disposeDialog();
	}
	
	private String[] Operator={
			"正在连接服务器...",
			"连接服务器成功",
			"正在下载库配置...", 
			"正在下载元件库", 
			"正在下载资源库", 
			"正在下载资源文件",
			"连接服务器失败！", 
			"下载元件库失败！", 
			"下载资源库失败！", 
			"下载资源文件失败！", 
			"下载完成",
			"用户名或密码错误！",
			"验证用户名密码通过"};

	public void doOperator(final String  addr, final String username, final String password)
	{
		Runnable runA = new Runnable(){
			public void run(){
				//启动连接
				currentName.setText("正在连接服务器...");
				progressBar.setValue(0);
				pause(1);
				
				FTPClient ftpClient = new FTPClient();
				
				try {
					ftpClient.connect(addr, port);
					ftpClient.setDataTimeout(60 * 1000);
					ftpClient.setDefaultTimeout(60 * 1000);
					
					currentName.setText("连接服务器成功");
					progressBar.setValue(10);
					pause(1);
					
					ftpClient.login(username, password);// 登录
					// 设置文件传输类型为二进制
					ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
					// 获取ftp登录应答代码
					int reply = ftpClient.getReplyCode();
					
					// 验证是否登陆成功
					if (!FTPReply.isPositiveCompletion(reply)) {
						ftpClient.disconnect();
						currentName.setText("用户名或密码错误");
						progressBar.setValue(100);
						return ;
					}
					//验证成功
					currentName.setText("登录服务器成功");
					progressBar.setValue(15);
					pause(1);
					
					if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON")))
					{
						// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
						LOCAL_CHARSET = "UTF-8";
					}
					System.out.println(LOCAL_CHARSET);
					ftpClient.setControlEncoding(LOCAL_CHARSET);
					ftpClient.enterLocalPassiveMode();// 设置被动模式
					
					//下载文件
					currentName.setText("准备下载元件库");
					progressBar.setValue(20);
					pause(1);
					
					// 转移到FTP服务器目录至指定的目录下
					ftpClient.changeWorkingDirectory(new String(SymbolConfig.remotePath
		                    .getBytes(LOCAL_CHARSET), SERVER_CHARSET));
					System.err.println(ftpClient.getReplyString());
					
					File dir = new File(SymbolConfig.localPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					
					currentName.setText("下载元件库...");
					progressBar.setValue(30);
					pause(1);
					String fileName = "symbol.svg";
					File localFile = new File(SymbolConfig.localPath+File.separator+fileName);
					OutputStream is = new FileOutputStream(localFile);
					ftpClient.retrieveFile(new String(fileName.getBytes(LOCAL_CHARSET), SERVER_CHARSET), is);
					is.close();
					
					currentName.setText("下载元件库完成");
					progressBar.setValue(50);
					pause(1);
					
					ftpClient.logout();
					ftpClient.disconnect();
					currentName.setText(Operator[10]);
					progressBar.setValue(100);
					
					//转移临时文件到实际位置
					File srcFile = new File("download"+File.separator+fileName);
					File desFile = new File(fileName);
					FileUtils.copyFile(srcFile,desFile);
					
					//重新读取配置symbol.svg
					SymbolMap.loadSymbol();
					SymbolMap.menumap.clear();
					Editor.getEditor().getSymbolManager().update();
					
				} catch (Exception e) {
					e.printStackTrace();
					currentName.setText(Operator[6]);
					progressBar.setValue(100);
					return;
				}
			}
		}; 
		
		 Thread ta = new Thread(runA,"ThreadA");
		 ta.start();
	}
	
	
	public void pause(int t)
	{
		try {
			Thread.sleep(t * 1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
