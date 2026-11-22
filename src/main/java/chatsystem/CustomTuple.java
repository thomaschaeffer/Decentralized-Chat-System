package chatsystem;


/**
 * CustomTuple is a class that stores two values of potentially different types.
 * This class is used by the UserList class
 * 
 * @param <A>   the type of the first element
 * @param <B>   the type of the second element
 */

public class CustomTuple<A, B> {
   private A first;
   private B second;
   
   /**
    * Creates a tuple containing two elements.
    * 
    * @param first
    * @param second
    */
   public CustomTuple(A first, B second) {
       this.first = first;
       this.second = second;
   }

   /**
    * Returns the first element.
    */
   public A getFirst() {
       return first;
   }

/**
 * Updates the first element 
 * 
 * @param first the new first element
 */
   public void setFirst(A first) {
       this.first = first;
   }

   /**
    *  return the second element
    */
   public B getSecond() {
       return second;
   }

   /**
    *Updates the second element

    * @param second the new second element
    */
   public void setSecond(B second) {
       this.second = second;
   }

   /**   
    * Return a string representation of a tuple.
    */
   @Override
   public String toString() {
       return "(" + first + ", " + second + ")";
   }
}