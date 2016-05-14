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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * annotation driven table model
 */
public abstract class AnnotationTableModel<T> extends AbstractTableModel implements Moveable {

  static class Column {
    int ordinal;
    String name;
    Field field;
    boolean isEditable;

    Column(int ordinal, String name, Field field, boolean isEditable) {
      this.ordinal = ordinal;
      this.name = name;
      this.field = field;
      this.isEditable = isEditable;
    }
    
    public String toString(){
      return "Column[ordinal="+ordinal+",name="+name+",field="+field + ",isEditable="+isEditable+"]";
    }
  }
  
  Class<T> rowClass;
  ArrayList<Column> columns;
  ArrayList<T> data;

  public AnnotationTableModel() {
    rowClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    columns = new ArrayList<Column>();
    data = new ArrayList();

    int maxOrd = -1;

    for (Field f : rowClass.getDeclaredFields()) {
      ColumnInfo tca = f.getAnnotation(ColumnInfo.class);
      if (tca != null) {
        int idx = tca.ordinal();
        int ord;
        if (idx < 0) {
          ord = maxOrd = maxOrd + 1;
        } else {
          ord = idx;
          maxOrd = Math.max(columns.size(), idx);
        }

        f.setAccessible(true);
        Column col = new Column(ord, tca.name(), f, tca.isEditable());
        
        if (idx < 0) {
          columns.add(col);
        } else {
          sortIn(col);
        }
      }
    }
  }

  public void setData (ArrayList<T> newData){
    data = newData;
    fireTableRowsUpdated(0, data.size()-1);
  }
  
  public ArrayList<T> getData(){
    return data;
  }
  
  protected void sortIn(Column column) {
    int ord = column.ordinal;

    for (int i = 0; i < columns.size(); i++) {
      Column c = columns.get(i);
      if (c.ordinal > ord) {
        columns.add(i, column);
        return;
      }
    }

    columns.add(column);
  }

  @Override
  public int getColumnCount() {
    return columns.size();
  }

  @Override
  public int getRowCount() {
    return data.size();
  }

  @Override
  public String getColumnName(int col) {
    return columns.get(col).name;
  }

  @Override
  public Object getValueAt(int row, int col) {
    try {
      T rData = data.get(row);
      Column cData = columns.get(col);
      return cData.field.get(rData);
    } catch (Throwable x){
      return null;
    }
  }

  @Override
  public void setValueAt (Object newValue, int row, int col){
    try {
      T rData = data.get(row);
      Column cData = columns.get(col);
      cData.field.set(rData, newValue);
      fireTableCellUpdated(row,col);
      
    } catch (Throwable x){
      x.printStackTrace();
    }
  }
  
  @Override
  public Class getColumnClass(int col) {
    return columns.get(col).field.getType();
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    return columns.get(col).isEditable;
  }

  public void append(T e) {
    int rowIdx = data.size();
    data.add(e);
    fireTableRowsInserted(rowIdx, rowIdx);
  }

  public void remove(int rowIdx) {
    data.remove(rowIdx);
    fireTableRowsDeleted(rowIdx, rowIdx);
  }

  public int move(int rowIdx, int delta) {
    T e = data.get(rowIdx);
    int newIdx;

    if (delta < 0) {
      newIdx = Math.max(rowIdx + delta, 0);
    } else {
      newIdx = Math.min(data.size() - 1, rowIdx + delta);
    }

    data.remove(rowIdx);
    data.add(newIdx, e);

    fireTableRowsDeleted(rowIdx, rowIdx);
    fireTableRowsInserted(newIdx, newIdx);

    return newIdx;
  }
}
