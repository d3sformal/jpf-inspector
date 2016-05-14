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

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * a JTable that only auto expands/shrinks the last column width
 */
public class RLCTable extends JTable {
  
  public RLCTable (){
    setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
  }
  
  @Override
  public void doLayout() {
    JTableHeader header = getTableHeader();
    
    if (header.getResizingColumn() == null){ // not a explicit column resize
      TableColumnModel columnModel = getColumnModel();
      int nCols = columnModel.getColumnCount();
      if (nCols > 0){
        header.setResizingColumn( columnModel.getColumn(nCols-1));
      }
    }
    
    super.doLayout();
  }
}
