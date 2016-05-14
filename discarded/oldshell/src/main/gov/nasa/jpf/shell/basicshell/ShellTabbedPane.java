/* Copyright (C) 2007 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration
 * (NASA).  All Rights Reserved.
 *
 * This software is distributed under the NASA Open Source Agreement
 * (NOSA), version 1.3.  The NOSA has been approved by the Open Source
 * Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
 * directory tree for the complete NOSA document.
 *
 * THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
 * KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
 * LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
 * SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
 * THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
 * DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
 */
package gov.nasa.jpf.shell.basicshell;


import gov.nasa.jpf.shell.util.DraggableTabPane;
import gov.nasa.jpf.shell.*;
import gov.nasa.jpf.shell.util.tabtearing.TabTransferData;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Here is my feeble attempt at a tearable tabs based on:
 * http://stackoverflow.com/questions/60269/how-to-implement-draggable-panel-using-java-swing
 * This supports full 'panel tearing allowing for tabs to be broken off into
 * their own shells and then joined back together.
 */
public class ShellTabbedPane extends DraggableTabPane {

  private Shell shell;

  public ShellTabbedPane(final Shell shell) {
    super();
    this.shell = shell;

  }

  /**
    * Since just trying to use the tooltip feature that the TabComponent has
    * screws up the mouse listeners and renders the tab unselectable, we
    * have to do this manually.
    */
  @Override
  public String getToolTipText(MouseEvent me){
    int i = indexAtLocation(me.getX(), me.getY());
    if (i != -1) {
      return ((JComponent)getTabComponentAt(i)).getToolTipText();
    } else {
      return super.getToolTipText(me);
    }
  }

  @Override
  public void dragDropEnd(DragSourceDropEvent dsde){
    super.dragDropEnd(dsde); //Just incase there is something that
                             //need to be taken care of

    //If the drop ended but it didn't succeed then we should break the panel
    //into its own shell containing only this panel
    if (dsde.getDropSuccess() == false) {
      try {
        TabTransferData transferData = (TabTransferData) dsde.getDragSourceContext().getTransferable().getTransferData(TAB_FLAVOR);
        Component component = transferData.getComponent();
        if (component instanceof ShellPanel == false) { return; }
        ShellPanel panel = (ShellPanel) component;
        //This panel is being broken off into its own shell.
        Shell newShell = getShell().createChildShell();
        newShell.addShellPanel(panel);
        newShell.setLocation(dsde.getLocation());
        newShell.setVisible(true);
      } catch (UnsupportedFlavorException ex) {
        Logger.getLogger(ShellTabbedPane.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
        Logger.getLogger(ShellTabbedPane.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    //Now check if this shell needs to be closed up
    if (getTabCount() == 0) {
       getShell().dispose();
    }

  }

  public Shell getShell() {
    return shell;
  }

  public interface TabAcceptor {
    boolean isDropAcceptable(ShellTabbedPane c, int i);
  }

  public void insertShellPanel(ShellPanel panel, int index){
    insertTab(panel.getTitle(), panel.getIcon(), panel, panel.getTip(), index);
    ShellPanelTabComponent shellPanelTabComponent = new ShellPanelTabComponent(panel);
    ToolTipManager.sharedInstance().unregisterComponent(shellPanelTabComponent);
    setTabComponentAt(index, shellPanelTabComponent);
  }

  //From: http://java.sun.com/docs/books/tutorial/uiswing/examples/components/TabComponentsDemoProject/src/components/ButtonTabComponent.java
  class ShellPanelTabComponent extends JPanel implements ActionListener{

    private ShellPanel panel;
    private JLabel label;

    public ShellPanelTabComponent(final ShellPanel panel){
      super(new FlowLayout(FlowLayout.LEFT, 0, 0));
      this.panel = panel;

      setOpaque(false);
      label = new JLabel(panel.getTitle(), panel.getIcon(), JLabel.CENTER){
        @Override
        public void setToolTipText(String s){
          super.setToolTipText(s);
          /*
           * Whenever the tooltip gets set, the component is registered.
           * If this component is registered for tooltip then mouseevents are
           * consumed. So if you want to click on a tab to select it, you can't
           * because the tooltip is greedy and consumes the even instead.
           */
          ToolTipManager.sharedInstance().unregisterComponent(label);
        }
      };
      label.setFocusable(false);
      label.setBorder(BorderFactory.createEmptyBorder(2,0,0,5));
      label.setToolTipText(panel.getTip());
      add(label);

      //now the button
      CloseButton closeButton = new CloseButton(panel);
      add(closeButton);
      closeButton.addActionListener(this);
    }

    @Override
    public String getToolTipText(){
      return label.getToolTipText();
    }

    /**
     * Called when the close button is pressed on a tab component
     * @param ae
     */
    public void actionPerformed(ActionEvent ae) {
      panel.getShell().removeShellPanel(panel);
    }

    /**
     * @return the label
     */
    public JLabel getLabel() {
      return label;
    }
  }

  /**
   * Nothing more than a gray X button that turns red when you mouse over it.
   */
  private class CloseButton extends JButton{

    private ShellPanel panel;

    public CloseButton(ShellPanel panel){
      final int size = 10;
      setUI(new BasicButtonUI());
      setPreferredSize(new Dimension(size, size));
      setToolTipText("Close this tab");
      setContentAreaFilled(false);
      setFocusable(false);
      setBorderPainted(false);
      setRolloverEnabled(true);
      this.panel = panel;
    }

    @Override
    public void updateUI(){ }

    //paint the cross
    @Override
    protected void paintComponent(Graphics g) {
      //GraphicsUtil.drawButtonBackground(g, this);
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      g2.setStroke(new BasicStroke(1));

      //Draw a red circle
      int size = Math.min(getWidth(), getHeight());
      if (getModel().isPressed()) {
        g2.setColor(Color.RED.darker());
      }else if (getModel().isRollover()){
        g2.setColor(Color.RED);
      }else{
        g2.setColor(Color.GRAY.brighter());
      }
      g2.fillOval(0, 0, size, size);

      //Draw an X through it
      g2.setColor(Color.WHITE);
      //Using fancy geometry; for the lines to be drawn on the edge of the circle
      //the offset needs to be the (diameter of the circle) * 1/sqrt(2) - 0.5
      //which is approximatly 0.2, but since I want the X in the circle,
      //substract 2 for good measure.
      int offset = (int) ((float)size * 0.2) + 1;
      g2.drawLine(size-offset, size-offset, offset, offset); //criss
      g2.drawLine(offset, size-offset, size-offset, offset); //cross
      g2.dispose();
    }

    public void actionPerformed(ActionEvent ae) {
      panel.getShell().removeShellPanel(panel);
    }

  };
}
