package fenetre;


public class Stack implements StackSpecification {
	
	private class intLink {
	int value;
	intLink tail;
	intLink(int h, intLink t){value = h;  tail = t;}
	}


	private int numberOfElements;

	private intLink head_link;
	
	public Stack () throws IllegalArgumentException{
 
		head_link =null; 
		numberOfElements=0;
	}
	
	@Override
	public int getSize() {
		
		return numberOfElements;
	}


	@Override
	public void push (int x){

		intLink il = new intLink(x,head_link);
		head_link = il;
		numberOfElements++;
	}

	@Override
	public int head (){
		if (head_link==null) throw new IllegalStateException("Cannot read the value of a stack that is empty");
		else return head_link.value;
	}

	@Override
	public void pop (){
		if (head_link==null) throw new IllegalStateException("Cannot pop the value of a stack that is empty");
		
		else{ 
			head_link = head_link.tail; 
			numberOfElements--;
		}
	}


	@Override
	public boolean is_empty() {
		
		return head_link==null;
	}

	public String toString(){
		String temp  ="Stack: Head - ";
		intLink temp_link =head_link;
		while (temp_link!=null){temp = temp+ temp_link.value+ " - "; 
		temp_link = temp_link.tail;}
		return temp;
	}
	
}