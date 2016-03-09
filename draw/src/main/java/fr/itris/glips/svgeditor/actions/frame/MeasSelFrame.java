package fr.itris.glips.svgeditor.actions.frame;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class MeasSelFrame extends JDialog implements WindowListener {

	private static final long serialVersionUID = 1L;

	// 树组件
	protected JTree jTree;

	private JLabel deviceLabel;
	private JTextField deviceTextField;
	private JLabel majorStateLabel;
	private JComboBox<MeasValue> majorStateBox;
	private JLabel deputyStateLabel;
	private JComboBox<MeasValue> deputyStateBox;

	private JTextField[] jf = null;
	private JButton btnOk;
	private JButton btnCancel;

	public MeasSelFrame(JTextField[] jf) {
		this.setModal(true);
		this.jf = jf;
		this.setTitle("属性设置");
		this.setLayout(null);
		this.initTree();
	}

	public void initTree() {

		deviceLabel = new JLabel("设备:");
		deviceLabel.setBounds(30, 20, 50, 30);
		this.add(deviceLabel);
		deviceTextField = new JTextField();
		deviceTextField.setBounds(80, 20, 200, 25);
		this.add(deviceTextField);
		deviceTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_ENTER) {
					String txt = deviceTextField.getText();
					if (txt != null && txt.trim().length() > 0) {
						findInTree(txt.trim());
					}
				}
			}
		});

		jTree = new JTree();

		// 树节点的相关数据
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("电力资源信息");
		List<Psr> psrList = MeasSelDb.getAllPsr();
		MeasSelDb.initTree(psrList, 0, root);
		// 树的数据模型
		DefaultTreeModel model = new DefaultTreeModel(root);
		// 设置数据模型
		jTree.setModel(model);
		jTree.getSelectionModel().setSelectionMode(
				DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
		// 展开所有树
		for (int i = 0; i < jTree.getRowCount(); i++)
			jTree.expandRow(i);
		// 添加事件
		jTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent se) {
				JTree tree = (JTree) se.getSource();
				DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				Psr p = (Psr) selNode.getUserObject();
				List<MeasValue> mvList = MeasSelDb.getMeasValueByPsrId(p
						.getId());
				if (mvList != null && !mvList.isEmpty()) {
					majorStateBox.removeAllItems();
					deputyStateBox.removeAllItems();
					for (MeasValue mv : mvList) {
						majorStateBox.addItem(mv);
						deputyStateBox.addItem(mv);
					}
				}
			}
		});
		// 滚动面板
		JScrollPane jScrollPane = new JScrollPane(jTree,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// 添加树到滚动面板
		jScrollPane.getViewport().add(jTree);
		jScrollPane.setBounds(30, 50, 300, 250);
		this.add(jScrollPane);

		majorStateLabel = new JLabel("主状态/值:");
		majorStateLabel.setBounds(30, 320, 60, 30);
		this.add(majorStateLabel);
		majorStateBox = new JComboBox<MeasValue>();
		majorStateBox.setBounds(90, 320, 200, 25);
		this.add(majorStateBox);
		deputyStateLabel = new JLabel("副状态/值:");
		deputyStateLabel.setBounds(30, 350, 60, 30);
		this.add(deputyStateLabel);
		deputyStateBox = new JComboBox<MeasValue>();
		deputyStateBox.setBounds(90, 350, 200, 25);
		this.add(deputyStateBox);
		btnOk = new JButton("确定");
		btnOk.setBounds(80, 400, 100, 25);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jf != null && jf.length > 0) {
					MeasValue mv = null;
					mv = (MeasValue) majorStateBox.getSelectedItem();
					if (mv != null) {
						jf[0].setText(String.valueOf(mv.getId()));
						jf[1].setText(mv.getName());
					} else {
						jf[0].setText("");
						jf[1].setText("");
					}
					mv = (MeasValue) deputyStateBox.getSelectedItem();
					if (mv != null) {
						jf[2].setText(String.valueOf(mv.getId()));
						jf[3].setText(mv.getName());
					} else {
						jf[2].setText("");
						jf[3].setText("");
					}
					DefaultMutableTreeNode note = (DefaultMutableTreeNode) jTree
							.getLastSelectedPathComponent();
					if (note != null) {
						Psr p = (Psr) note.getUserObject();
						jf[4].setText(String.valueOf(p.getId()));
					} else {
						jf[4].setText("");
					}
				}
				winHide();
			}
		});
		this.add(btnOk);
		btnCancel = new JButton("取消");
		btnCancel.setBounds(190, 400, 100, 25);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				winHide();
			}
		});
		this.add(btnCancel);

	}

	public void winHide() {
		this.setVisible(false);
	}

	private void findInTree(String str) {
		Object root = jTree.getModel().getRoot();
		TreePath treePath = new TreePath(root);
		treePath = findInPath(treePath, str);
		if (treePath != null) {
			jTree.setSelectionPath(treePath);
			jTree.scrollPathToVisible(treePath);
		}
	}

	private TreePath findInPath(TreePath treePath, String str) {
		Object object = treePath.getLastPathComponent();
		if (object == null) {
			return null;
		}

		String value = object.toString();
		if (str.equals(value)) {
			return treePath;
		} else {
			TreeModel model = jTree.getModel();
			int n = model.getChildCount(object);
			for (int i = 0; i < n; i++) {
				Object child = model.getChild(object, i);
				TreePath path = treePath.pathByAddingChild(child);

				path = findInPath(path, str);
				if (path != null) {
					return path;
				}
			}
			return null;
		}
	}

	// 布局居中方法
	public void centered(Container container) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int w = container.getWidth();
		int h = container.getHeight();
		container.setBounds((screenSize.width - w) / 2,
				(screenSize.height - h) / 2, w, h);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}