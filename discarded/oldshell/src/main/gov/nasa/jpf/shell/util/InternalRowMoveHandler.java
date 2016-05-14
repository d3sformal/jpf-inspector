//
// Copyright (C) 2011 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
//
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
//
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package gov.nasa.jpf.shell.util;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

/**
 * class that handles table internal row reordering by means of single row drag&drop
 */
public class InternalRowMoveHandler extends TransferHandler {
  
  // don't try to specify a mime type, as it most likely will cause the representation class to
  // change to InputStream
  private static final DataFlavor ROW_FLAVOR = new ActivationDataFlavor(Integer.class, "Integer Row Index");
  private JTable table = null;

  public InternalRowMoveHandler(JTable table) {
    this.table = table;
  }

  @Override
  protected Transferable createTransferable (JComponent c) {
    if (c == table){
      return new DataHandler(new Integer(table.getSelectedRow()), ROW_FLAVOR.getMimeType());
    } else {
      return null;
    }
  }

  @Override
  public int getSourceActions(JComponent c) {
    return TransferHandler.COPY_OR_MOVE;
  }
  
  @Override
  public boolean canImport (TransferHandler.TransferSupport tfs) {
    Component target = tfs.getComponent();
    
    if (target == table){
      if (tfs.isDataFlavorSupported(ROW_FLAVOR)){
        if (tfs.isDrop()){
          target.setCursor(DragSource.DefaultMoveDrop);
          return true;
        }
      }
    }
    
    target.setCursor(DragSource.DefaultMoveNoDrop);
    return false;
  }

  @Override
  public boolean importData(TransferHandler.TransferSupport tfs) {
    if (tfs.getComponent() == table) {
      try {
        JTable.DropLocation loc = (JTable.DropLocation) tfs.getDropLocation();
        int toIndex = loc.getRow();
        int fromIndex = (Integer) tfs.getTransferable().getTransferData(ROW_FLAVOR);
        
        if (fromIndex != toIndex) {
          if (toIndex > fromIndex) { // no idea why it is off by 1
            toIndex--;
          }
          
          ((Moveable) table.getModel()).move(fromIndex, toIndex - fromIndex);
          table.getSelectionModel().addSelectionInterval(toIndex, toIndex);
          return true;
        }
      } catch (Exception e) {
        System.err.println("exception during drop: " + e);
      } 
    }
    
    return false;
  }

  @Override
  protected void exportDone(JComponent c, Transferable t, int action) {
    if (action == TransferHandler.MOVE) {
      c.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }
}
