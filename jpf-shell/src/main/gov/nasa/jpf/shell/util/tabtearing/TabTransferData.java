package gov.nasa.jpf.shell.util.tabtearing;

import java.awt.Component;
import javax.swing.Icon;

public class TabTransferData {
  private String title = null;
  private Icon icon = null;
  private String tooltip = null;
  private Component tabComponent = null;
  private Component comp = null;

  public TabTransferData(String title, Icon icon, String tooltip, Component tabComponent, Component st) {
    this.title        = title;
    this.icon         = icon;
    this.tooltip      = tooltip;
    this.tabComponent = tabComponent;
    this.comp = st;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @return the icon
   */
  public Icon getIcon() {
    return icon;
  }

  /**
   * @return the tooltip
   */
  public String getTooltip() {
    return tooltip;
  }

  /**
   * @return the tabComponent
   */
  public Component getTabComponent() {
    return tabComponent;
  }

  /**
   * @return the comp
   */
  public Component getComponent() {
    return comp;
  }

}