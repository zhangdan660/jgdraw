/*
 * $Id: JXLoginDialog.java,v 1.8 2006/05/14 08:12:16 dmouse Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import javax.swing.UIManager;
import org.jdesktop.swingx.auth.LoginService;
import org.jdesktop.swingx.auth.PasswordStore;
import org.jdesktop.swingx.auth.UserNameStore;

/**
 * A standard login dialog that provides a reasonable amount of flexibility
 * while also providing ease of use and a professional look.
 *
 * @author rbair
 */
public class JXLoginDialog extends JDialog {
    /**
     * The login panel containing the username & password fields, and handling
     * the login procedures.
     */
    private JXLoginPanel panel;
    
    /**
     * Creates a non-modal dialog without a title and without a specified
     * <code>Frame</code> owner.  A shared, hidden frame will be
     * set as the owner of the dialog.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     * 
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see javax.swing.JComponent#getDefaultLocale
     */
    public JXLoginDialog() throws HeadlessException {
        super();
        init();
    }

    /**
     * Creates a non-modal dialog without a title with the
     * specified <code>Frame</code> as its owner.  If <code>owner</code>
     * is <code>null</code>, a shared, hidden frame will be set as the
     * owner of the dialog.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param owner the <code>Frame</code> from which the dialog is displayed
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see javax.swing.JComponent#getDefaultLocale
     */
    public JXLoginDialog(Frame owner) throws HeadlessException {
        super(owner);
        init();
    }

    /**
     * Creates a modal or non-modal dialog without a title and
     * with the specified owner <code>Frame</code>.  If <code>owner</code>
     * is <code>null</code>, a shared, hidden frame will be set as the
     * owner of the dialog.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     *
     * @param owner the <code>Frame</code> from which the dialog is displayed
     * @param modal  true for a modal dialog, false for one that allows
     *               others windows to be active at the same time
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see javax.swing.JComponent#getDefaultLocale
     */
    public JXLoginDialog(Frame owner, boolean modal) throws HeadlessException {
        super(owner, modal);
        init();
    }

    /**
     * Creates a non-modal dialog with the specified title and
     * with the specified owner frame.  If <code>owner</code>
     * is <code>null</code>, a shared, hidden frame will be set as the
     * owner of the dialog.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     *
     * @param owner the <code>Frame</code> from which the dialog is displayed
     * @param title  the <code>String</code> to display in the dialog's
     *			title bar
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see javax.swing.JComponent#getDefaultLocale
     */
    public JXLoginDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);     
        init();
    }

    /**
     * Creates a modal or non-modal dialog with the specified title 
     * and the specified owner <code>Frame</code>.  If <code>owner</code>
     * is <code>null</code>, a shared, hidden frame will be set as the
     * owner of this dialog.  All constructors defer to this one.
     * <p>
     * NOTE: Any popup components (<code>JComboBox</code>,
     * <code>JPopupMenu</code>, <code>JMenuBar</code>)
     * created within a modal dialog will be forced to be lightweight.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     *
     * @param owner the <code>Frame</code> from which the dialog is displayed
     * @param title  the <code>String</code> to display in the dialog's
     *			title bar
     * @param modal  true for a modal dialog, false for one that allows
     *               other windows to be active at the same time
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see javax.swing.JComponent#getDefaultLocale
     */
    public JXLoginDialog(Frame owner, String title, boolean modal)
        throws HeadlessException {
        super(owner, title, modal);
        init();
    }

    /**
     * Creates a modal or non-modal dialog with the specified title, 
     * owner <code>Frame</code>, and <code>GraphicsConfiguration</code>.
     * 
     * <p>
     * NOTE: Any popup components (<code>JComboBox</code>,
     * <code>JPopupMenu</code>, <code>JMenuBar</code>)
     * created within a modal dialog will be forced to be lightweight.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     *
     * @param owner the <code>Frame</code> from which the dialog is displayed
     * @param title  the <code>String</code> to display in the dialog's
     *                  title bar
     * @param modal  true for a modal dialog, false for one that allows
     *               other windows to be active at the same time
     * @param gc the <code>GraphicsConfiguration</code> 
     * of the target screen device.  If <code>gc</code> is 
     * <code>null</code>, the same
     * <code>GraphicsConfiguration</code> as the owning Frame is used.    
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see javax.swing.JComponent#getDefaultLocale
     * @since 1.4
     */
    public JXLoginDialog(Frame owner, String title, boolean modal,
                   GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        init();
    }

    /**
     * Creates a non-modal dialog without a title with the
     * specified <code>Dialog</code> as its owner.
     * <p>
     * This constructor sets the component's locale property to the value 
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param owner the non-null <code>Dialog</code> from which the dialog is displayed
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see javax.swing.JComponent#getDefaultLocale
     */
    public JXLoginDialog(Dialog owner) throws HeadlessException {
        super(owner);
        init();
    }

    /**
     * Creates a modal or non-modal dialog without a title and
     * with the specified owner dialog.
     * <p>
     * This constructor sets the component's locale property to the value 
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param owner the non-null <code>Dialog</code> from which the dialog is displayed
     * @param modal  true for a modal dialog, false for one that allows
     *               other windows to be active at the same time
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see javax.swing.JComponent#getDefaultLocale
     */
    public JXLoginDialog(Dialog owner, boolean modal) throws HeadlessException {
        super(owner, modal);
        init();
    }

    /**
     * Creates a non-modal dialog with the specified title and
     * with the specified owner dialog.
     * <p>
     * This constructor sets the component's locale property to the value 
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param owner the non-null <code>Dialog</code> from which the dialog is displayed
     * @param title  the <code>String</code> to display in the dialog's
     *			title bar
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see javax.swing.JComponent#getDefaultLocale
     */
    public JXLoginDialog(Dialog owner, String title) throws HeadlessException {
        super(owner, title);     
        init();
    }

    /**
     * Creates a modal or non-modal dialog with the specified title 
     * and the specified owner frame. 
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     *
     * @param owner the non-null <code>Dialog</code> from which the dialog is displayed
     * @param title  the <code>String</code> to display in the dialog's
     *			title bar
     * @param modal  true for a modal dialog, false for one that allows
     *               other windows to be active at the same time
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see javax.swing.JComponent#getDefaultLocale
     */
    public JXLoginDialog(Dialog owner, String title, boolean modal)
        throws HeadlessException {
        super(owner, title, modal);
        init();
    }

    /**
     * Creates a modal or non-modal dialog with the specified title, 
     * owner <code>Dialog</code>, and <code>GraphicsConfiguration</code>.
     * 
     * <p>
     * NOTE: Any popup components (<code>JComboBox</code>,
     * <code>JPopupMenu</code>, <code>JMenuBar</code>)
     * created within a modal dialog will be forced to be lightweight.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     *
     * @param owner the <code>Dialog</code> from which the dialog is displayed
     * @param title  the <code>String</code> to display in the dialog's
     *			title bar
     * @param modal  true for a modal dialog, false for one that allows
     *               other windows to be active at the same time
     * @param gc the <code>GraphicsConfiguration</code> 
     * of the target screen device.  If <code>gc</code> is 
     * <code>null</code>, the same
     * <code>GraphicsConfiguration</code> as the owning Dialog is used.    
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see javax.swing.JComponent#getDefaultLocale
     * returns true.
     * @since 1.4
     */
    public JXLoginDialog(Dialog owner, String title, boolean modal,
                   GraphicsConfiguration gc) throws HeadlessException {

        super(owner, title, modal, gc);
        init();
    }

    /**
     */
    public JXLoginDialog(LoginService service, PasswordStore ps, UserNameStore us) {
        super();
        setTitle(UIManager.getString(JXLoginPanel.class.getCanonicalName() + ".loginString")); 
        setPanel(new JXLoginPanel(service, ps, us));
        JXLoginPanel.initWindow(this, getPanel());
    }
    
    protected void init() {
        setPanel(new JXLoginPanel());
        JXLoginPanel.initWindow(this, getPanel());
    }

    /**
     * @return the status of the login dialog
     */
    public JXLoginPanel.Status getStatus() {
        return getPanel().getStatus();
    }

    public JXLoginPanel getPanel() {
        return panel;
    }

    public void setPanel(JXLoginPanel panel) {
        this.panel = panel;
    }
}
