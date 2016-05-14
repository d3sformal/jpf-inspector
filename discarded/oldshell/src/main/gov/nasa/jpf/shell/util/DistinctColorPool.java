package gov.nasa.jpf.shell.util;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Implemented using the technique from:
 * http://stackoverflow.com/questions/470690/how-to-automatically-generate-n-distinct-colors/470747#470747
 */
public class DistinctColorPool<E> {

  ArrayList<E> arrayList = new ArrayList<E>();
  private int num_of_colors;

  public DistinctColorPool(int num_of_colors){
    this.num_of_colors = num_of_colors;
  }

  public Color getColor(int i){
    float hue = (float)i/num_of_colors;
    return new Color(Color.HSBtoRGB(hue, 140, 100));
  }

  public void addItem(E item){
    arrayList.add(item);
  }
}
