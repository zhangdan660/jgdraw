package com.nbtoptec.draw.animation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.itris.glips.rtdaeditor.anim.ItemObject;
import fr.itris.glips.rtdaeditor.anim.components.AnimationPanel;
import fr.itris.glips.rtdaeditor.anim.components.HeaderPanel;
import fr.itris.glips.rtdaeditor.anim.components.StateRecord;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.actions.toolbar.ToolsFrame;
import fr.itris.glips.svgeditor.display.canvas.dom.SVGDOMListener;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionChangedListener;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * 
 * @author zhangdan
 *
 */
public class AnimationsModule extends ModuleAdapter {

	/**
	 * the ids of the module    &zd
	 */
	private final String idAnimations="Animations";
	
	/**
	 * the labels
	 */
	private String saveDialogTitle="", saveDialogMessage="";
	
	/**
	 * the nodes that are currently selected
	 */
	private LinkedList<Element> selectedNodes=new LinkedList<Element>();
	
	/**
	 * the header panel
	 */
	private HeaderPanel headerPanel;
	
	/**
	 * the panel in which the widget panel will be inserted
	 */
	private JPanel AnimationsPanel=new JPanel();
	
	/**
	 * the frame into which the animations panel will be inserted
	 */
	private ToolsFrame AnimationsFrame;
	
	/**
	 * the current dom listener
	 */
	private SVGDOMListener domListener;
	
	/**
	 * whether the graphic components of the dialog have already been reinitialized
	 */
	private boolean hasBeenReinitialized=false;
	
	/**
	 * the object used to record the state of this frame
	 */
	private static StateRecord stateRecord=new StateRecord();
	
	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public AnimationsModule(final Editor editor)
	{

		//a listener that listens to the changes of the svg handles
		final HandlesListener svgHandleListener=new HandlesListener(){
			
			/**
			 * a listener on the selection changes
			 */
			private SelectionChangedListener selectionListener;
			
			/**
			 * the last handle
			 */
			private SVGHandle lastHandle;
			
			@Override
			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {
				
				if(lastHandle!=null && selectionListener!=null && lastHandle.getSelection()!=null){
					
					//if a selection listener is already registered on a selection module, it is removed	
					lastHandle.getSelection().removeSelectionChangedListener(selectionListener);
				}
				
				//removing the dom listener
				if(domListener!=null) {
					
					domListener.removeListener();
					domListener=null;
				}
				
				//clearing the state record element
				stateRecord.setSelectedElement(null);

				//gets the current selection module	
				if(currentHandle!=null){

					manageSelection(new LinkedList<Element>(
							currentHandle.getSelection().getSelectedElements()), true);
					
				}else{
					
					manageSelection(new LinkedList<Element>(), true);
				}

				if(currentHandle!=null){
					
					//the listener of the selection changes
					selectionListener=new SelectionChangedListener(){
						
						@Override
						public void selectionChanged(Set<Element> selectedElements) {
							
							manageSelection(new LinkedList<Element>(selectedElements), false);
						}
					};
					
					//adds the selection listener
					if(selectionListener!=null){
						
						currentHandle.getSelection().addSelectionChangedListener(selectionListener);
					}
					
				}else{

					//clears the list of the selected items
					selectedNodes.clear();
					refreshAnimations(null, null);
					selectionListener=null;
				}
				
				lastHandle=currentHandle;
			}	
			
			/**
			 * updates the selected items and the state of the menu items
			 * @param selectedElements the list of the selected elements
			 * @param frameChanged whether the frame has changed
			 */
			protected void manageSelection(LinkedList<Element> selectedElements, boolean frameChanged){
				
				boolean selectionNotChanged=false;
				
				if(! frameChanged) {
					
					try {
						//comparing the old and new selected nodes to check if the selection has changed,
						//in order to know whether the frame should be refreshed or not
						HashSet<Element> oldSel=new HashSet<Element>(selectedNodes);
						HashSet<Element> newSel=new HashSet<Element>(selectedElements);
						
						if(oldSel.equals(newSel) && newSel.equals(oldSel)) {
							
							selectionNotChanged=true;
						}
					}catch (Exception ex) {}
				}

				if(frameChanged || ! selectionNotChanged) {
					
					selectedNodes.clear();
					
					//refresh the selected nodes list
					if(selectedElements!=null){
						
						selectedNodes.addAll(selectedElements);
					}
					
					if(selectedNodes.size()>=1 && AnimationsFrame.isVisible()){

						refreshAnimations(selectedNodes, null);
						
					}else if(AnimationsFrame.isVisible()){
						
						refreshAnimations(null, null);		
					}
				}
			}
		};
		
		//adds the svg handle change listener
		editor.getHandlesManager().addHandlesListener(svgHandleListener);

		//creating the animations panel
//		animationsPanel=new JGAnimationPanel(this);
		
		//filling the tabbed pane
		String frameLabel="", animationsPanelLabel="";
		
		try{
			saveDialogTitle=ResourcesManager.bundle.getString("label_rtdaanimationsandactionsSaveDialogTitle");
			saveDialogMessage=ResourcesManager.bundle.getString("label_rtdaanimationsandactionsSaveDialogMessage");
			frameLabel=ResourcesManager.bundle.getString("label_rtdaanimationsandactions");
			animationsPanelLabel=ResourcesManager.bundle.getString("label_rtdaanimations");
		}catch (Exception ex){}
		
		//creating the internal frame containing the animations panel
		AnimationsFrame=new ToolsFrame(
			editor, idAnimations, frameLabel, AnimationsPanel);
		
		//setting the visibility changes handler
		Runnable visibilityRunnable=new Runnable(){
			
			public void run() {
				
				if(! AnimationsFrame.isVisible() || selectedNodes.size()==0) {

					refreshAnimations(null, null);
					
				}else {
					
					refreshAnimations(selectedNodes, null);
				}
			}
		};
		
		AnimationsFrame.setVisibilityChangedRunnable(visibilityRunnable);
		
	}
	
	/**
	 * refreshes the animations and actions widgets
	 * @param nodes the list of nodes
	 * @param animationOrActionToSelect the animation or action 
	 * element that should be selected in the animations or actions chooser
	 */
	protected void refreshAnimations(
			LinkedList<Element> nodes, Element animationToSelect){

		//getting the current svg handle
		SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();

		//if the frame has already been reinitialized and is not visible, nothing is done
		if(hasBeenReinitialized && ((handle==null && nodes==null) || 
				! AnimationsFrame.isVisible())) {

			return;
		}
		
		//if the frame is not visible and has not been reinitialized, the reinitalization is processed
		if(! AnimationsFrame.isVisible()) {
			
			nodes=null;
		}
		
		//if the list of nodes is null, the frame is reinitialized
		hasBeenReinitialized=(handle==null && nodes==null);
		
		//removing the previous dom listener
		if(domListener!=null && handle!=null) {
			
			handle.getSvgDOMListenerManager().
				removeDOMListener(domListener);
		}

		if(handle!=null && (nodes==null || nodes.size()==0)){

			//adding the root element to the list of the nodes that should be animated
			nodes=new LinkedList<Element>();
			nodes.add(handle.getCanvas().getDocument().getDocumentElement());
		}
		
		
		if(nodes!=null && nodes.size()==1){
			
			Element selectedNode=nodes.getFirst();
			Element lastElement=stateRecord.getSelectedElement();

			if(lastElement!=null && ! lastElement.equals(selectedNode)) {
				
				stateRecord.reinitialize();
			}

			stateRecord.setSelectedElement(selectedNode);
			
			//creating the new dom listener
			domListener=new SVGDOMListener(selectedNode) {

				@Override
				public void nodeChanged() {}

				@Override
				public void nodeInserted(Node insertedNode) {

					refreshAnimations(selectedNodes, (Element)insertedNode);
				}

				@Override
				public void nodeRemoved(Node removedNode) {

					refreshAnimations(selectedNodes, (Element)removedNode);
				}

				@Override
				public void structureChanged(Node lastModifiedNode) {}
			};
			
			//adding the dom listener
			Editor.getEditor().getHandlesManager().getCurrentHandle().
				getSvgDOMListenerManager().addDOMListener(domListener);
			
		}else {
			
			stateRecord.reinitialize();
		}

		//getting the selected element
		Element element=null;
		
		if(nodes!=null && nodes.size()==1){

			element=nodes.getFirst();
		}

		//setting the element for the header panel
		if(element != null)
		{
			headerPanel.setCurrentElement(element);
		}
		
		//removing the tabbed pane
		AnimationsPanel.removeAll();

		//adding the header panel in the main panel
		AnimationsPanel.setLayout(
				new BoxLayout(AnimationsPanel, BoxLayout.X_AXIS));
		AnimationsPanel.add(headerPanel);
//		animationsPanel.setAnimations(null);
		stateRecord.reinitialize();
		AnimationsFrame.revalidate();
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

        HashMap<String, JMenuItem> map=new HashMap<String, JMenuItem>();
        map.put("ToolFrame_"+this.idAnimations, 
        		AnimationsFrame.getMenuItem());
        return map;
	}
}
