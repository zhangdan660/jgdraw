package fr.itris.glips.svgeditor;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXDialog;
import org.jdesktop.swingx.JXFindPanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.Searchable;

@SuppressWarnings("serial")
public class JGFindBar extends JXFindPanel{
    // PENDING: need to read from UIManager
    protected Color notFoundBackgroundColor = Color.decode("#FF6666");

    protected Color notFoundForegroundColor = Color.white;
    
    protected HashMap<JXTaskPane, ArrayList<DrawAction>> taskmap;
    
    protected JXTaskPaneContainer container;
    
    public JGFindBar(HashMap<JXTaskPane, ArrayList<DrawAction>> taskmap, JXTaskPaneContainer container) {
    	this.taskmap = taskmap;
    	this.container = container;
    }

    public JGFindBar(Searchable searchable) {
        super(searchable);
        getPatternModel().setIncremental(true);
        getPatternModel().setWrapping(true);
    }
    
    @Override
    public void setSearchable(Searchable searchable) {
        super.setSearchable(searchable);
        match();
    }
    

    /**
     * here: set textfield colors to not-found colors.
     */
    @Override
    protected void showNotFoundMessage() {
        searchField.setForeground(notFoundForegroundColor);
        searchField.setBackground(notFoundBackgroundColor);
    }

    public void cancel() {
    }
    
 // -------------------- init

    @Override
    protected void initExecutables() {
        getActionMap().put(JXDialog.CLOSE_ACTION_COMMAND,
                createBoundAction(JXDialog.CLOSE_ACTION_COMMAND, "cancel"));
        super.initExecutables();
    }
    
    @Override
    protected void bind() {
        super.bind();
        searchField.addActionListener(getAction(JXDialog.EXECUTE_ACTION_COMMAND));
        KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke,
                JXDialog.CLOSE_ACTION_COMMAND);
    }
    

    @Override
    protected void build() {
        setLayout(new FlowLayout(SwingConstants.LEADING));
        add(searchLabel);
        add(new JLabel(":"));
//        add(new JLabel(" "));
        add(searchField);
    }
    
    @Override
    protected void initComponents() {
        super.initComponents();
    }
    @Override
    protected void refreshModelFromDocument() {
        getPatternModel().setRawText(searchField.getText());
//        System.out.println(getPatternModel().getRawText());
        String text = getPatternModel().getRawText();
        
        container.removeAll();
//        container.revalidate(); //刷新菜单栏
        if(text != null && !text.isEmpty())
        {
        	for(Entry<JXTaskPane, ArrayList<DrawAction>> entry : taskmap.entrySet())
    		{
        		boolean matched = false;
    			JXTaskPane taskpane = entry.getKey();
    			taskpane.removeAll();
    			ArrayList<DrawAction> list = entry.getValue();
    			for(DrawAction action : list)
    			{
    				if(action.isMatch(text))	//包含关键词
    				{
    					taskpane.add(action);
    					matched = true;
    				}
    			}
    			if(matched)
    			{
    				container.add(taskpane);
    			}
    			taskpane.revalidate();
//    			container.revalidate(); //刷新菜单栏
    		}
        } else {
        	
        	for(Entry<JXTaskPane, ArrayList<DrawAction>> entry : taskmap.entrySet())
    		{
    			JXTaskPane taskpane = entry.getKey();
    			taskpane.removeAll();
    			ArrayList<DrawAction> list = entry.getValue();
    			for(DrawAction action : list)
    			{
    				taskpane.add(action);
    			}
    			container.add(taskpane);
    			taskpane.revalidate();
//    			container.revalidate(); //刷新菜单栏
    		}
        }
        container.repaint();
    }
}
