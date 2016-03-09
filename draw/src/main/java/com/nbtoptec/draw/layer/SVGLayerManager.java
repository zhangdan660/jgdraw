package com.nbtoptec.draw.layer;

import java.util.ArrayList;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

public class SVGLayerManager {
	
	/**
	 * 图层列表
	 */
	private ArrayList<SVGLayer> layerlist = new ArrayList<SVGLayer>();

	public ArrayList<SVGLayer> getLayerlist() {
		return layerlist;
	}

	public void setLayerlist(ArrayList<SVGLayer> layerlist) {
		this.layerlist = layerlist;
	}

	int id = 0;		//记录图层递增序号
	
	/**
	 * the related svg handle
	 */
	@SuppressWarnings("unused")
	private SVGHandle handle;
	
	public SVGLayerManager(SVGHandle handle) {
		this.handle=handle;
	}
	
	public SVGLayer getLayer(int index){
		if(layerlist.size()>index)
			return layerlist.get(index);
		return null;
	}
	
	public void removeLayer(int index){
		if(layerlist.size()>index)
			layerlist.remove(index);
	}
	
	public int getSize()
	{
		return layerlist.size();
	}

	public int getID()
	{
		return id;
	}
	
	public void addLayer(SVGLayer layer,int index)
	{
		layerlist.add(index, layer);
		id++;
	}
	
	public void bringLayerTop(int index)
	{
		layerlist.add(layerlist.remove(index));
	}
	
	public void bringLayerBottom(int index)
	{
		layerlist.add(0,layerlist.remove(index));
	}
	
	public void bringLayerUp(int index)
	{
		layerlist.add(index+1,layerlist.remove(index));
	}
	
	public void bringLayerDown(int index)
	{
		layerlist.add(index-1,layerlist.remove(index));
	}
	/**
	 * 获取当前编辑层
	 * @return
	 */
	public SVGLayer getEditingLayer(){
		for(SVGLayer layer : this.layerlist){
			if(layer.isEdit()){
				return layer;
			}
		}
		if(layerlist.size() == 0)
			layerlist.add(new SVGLayer());
		return layerlist.get(0);
	}
}
