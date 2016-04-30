package gov.nasa.jpf.shell.util;

public class Range implements Comparable{
     int min,max;
     public Range(int min, int max){
       this.min = min;
       this.max = max;
     }

     public int end(){
       return max;
     }

     public int start(){
       return min;
     }

     public int length(){
       return end() - start();
     }

    public int compareTo(Object o) {
      if ( o == this || !(o instanceof Range)){
        return 0;
      }

      Range r = (Range) o;
      if (min < r.min)
        return -1;//We start earlier, so we go first
      else if (min > r.min){
        return 1;//We start later so ro goes first
      }else{
        if (max < r.max)
          return -1; //We finish first, so we go first
        else if (max == r.max)
          return 0; //Looks like we cover the exact same rage
        else
          return 1;
      }
    }

     public boolean containsInt(int i){
       return i >= min && i<=max;
     }

     public boolean overlaps(Range r){
       if (min == r.min || min == r.max || max == r.min || max == r.max){
         return true;
       }else{
         //Find out which range comes starts first. If the first range's max
         //is greater than the second range's min then they overlap.
         Range first = min < r.min ? this : r;
         Range second = first == r ? this : r;
         return first.max > second.min;
       }
     }

    @Override
     public String toString(){
       return "Range: min="+min+" max="+max;
     }

   }