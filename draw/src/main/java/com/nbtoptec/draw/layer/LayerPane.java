package com.nbtoptec.draw.layer;


public class LayerPane {
	//private JCheckBox lockbtn, editbtn, linkbtn, showbtn;
	//private JLabel namelabel;
	private SVGLayer layer;
	private LayersPane parent;

	public LayerPane(LayersPane parent, SVGLayer layer) {
		this.parent = parent;
		//build();
		//setLayer(layer);
	}

	/**
	 * 初始化
	 *//*
	private void build() {
		parent.
		lockbtn = new JCheckBox();
		editbtn = new JCheckBox();
		linkbtn = new JCheckBox();
		showbtn = new JCheckBox();
		namelabel = new JLabel();
		GridBagLayout grid = parent.getGridlayout();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 1;
		grid.setConstraints(lockbtn, constraints);
		parent.add(lockbtn);
		grid.setConstraints(showbtn, constraints);
		parent.add(showbtn);
		grid.setConstraints(editbtn, constraints);
		parent.add(editbtn);
		grid.setConstraints(linkbtn, constraints);
		parent.add(linkbtn);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		grid.setConstraints(namelabel, constraints);
		parent.add(namelabel);

		lockBtnEvent();
		editBtnEvent();
		linkBtnEvent();
		showBtnEvent();
	}

	private void lockBtnEvent() {
		lockbtn.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				if (e.getStateChange() == ItemEvent.SELECTED) {
					editbtn.setEnabled(false);
					linkbtn.setEnabled(false);
					linkbtn.setSelected(false);
					editbtn.setSelected(false);
					layer.setLock(true);
					layer.setEdit(false);
					layer.setLink(false);
				} else {
					editbtn.setEnabled(true);
					linkbtn.setEnabled(true);
					layer.setLock(false);
				}
			}
		});
	}
	
	private void editBtnEvent(){
		editbtn.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					linkbtn.setEnabled(false);
					linkbtn.setSelected(false);
					layer.setEdit(true);
					layer.setLink(false);
				} else {
					linkbtn.setEnabled(true);
					layer.setEdit(false);
				}
			}
			
		});
	}
	
	private void linkBtnEvent(){
		linkbtn.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					layer.setLink(true);
				} else {
					layer.setLink(false);
				}
			}
			
		});
	}
	
	private void showBtnEvent(){
		showbtn.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					layer.setVisible(true);
				} else {
					layer.setVisible(false);
				}
			}
		});
	}

	public SVGLayer getLayer() {
		return layer;
	}

	*//**
	 * 同步图层属性到界面
	 * 
	 * @param layer
	 *//*
	public void setLayer(SVGLayer layer) {
		this.layer = layer;
		lockbtn.setSelected(layer.isLock());
		editbtn.setSelected(layer.isEdit());
		linkbtn.setSelected(layer.isLink());
		showbtn.setSelected(layer.isVisible());
		namelabel.setText(layer.getName());
		if (layer.isLock()) {
			editbtn.setEnabled(false);
			linkbtn.setEnabled(false);
		} else if (layer.isEdit()) {
			editbtn.setEnabled(true);
			linkbtn.setEnabled(false);
		} else if (!layer.isVisible()) {
			editbtn.setEnabled(false);
			linkbtn.setEnabled(false);
		}
	}

	*//**
	 * 移除本面板的所有元素
	 *//*
	public void remove() {
		parent.remove(lockbtn);
		parent.remove(linkbtn);
		parent.remove(namelabel);
		parent.remove(editbtn);
		parent.remove(showbtn);
	}*/
}
