package fenetre;

/**
 * <p> <b> Queues and graphs </b></p>
 * 
 * <p> An unbounded (LIFO) stack of integers 
 * 
 * <p> This package is part of the <em> CSC3101 </em> module <em> "Algorithmique et langage de programmation" </em>
 * at Telecom Sud Paris.
 * (See - <a href="http://moodle.tem-tsp.eu/course/view.php?id=707">http://moodle.tem-tsp.eu/course/view.php?id=707</a>)
 * </p>
 * 
 * <p> The code in this package <b> should not be </b>  edited by the students when completing their work.
 * </p>
 * 
 * @author jpaulgibson
 */
public interface StackSpecification {

	/**
	 * @return the number of elements that are currently stored on the stack
	 */
	public int getSize();
	
	
	/** 
	 * @return if the stack is empty
	 */
	public boolean is_empty();
	
	/**
	 * @param x is value being pushed onto stack, and it will become the top element which will be the next 
	 * to be popped off
	 */
	public void push (int x);
	
	
	/** 
	 * @return  the top element of stack without changing its state, 
	 * where the top element is the element that has been in the stack for the shortest time
	 * @throws IllegalStateException if we try to read the value of an empty stack
	 */
	public int head ();
	
	/**
	 *  remove the top element  of the stack, provided the stack  is not empty, 
	 *  where the  top element is the element that has been in the stack for the shortest time
	 *  @throws IllegalStateException if we try to pop from an empty stack
	 */
	public void pop ();
	
}