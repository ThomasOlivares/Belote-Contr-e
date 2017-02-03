package fenetre;

public class Instance extends Thread {
	
	private String phase;

  public Instance(String phase){
    super(phase);
    this.phase = phase;
  }

  public void run(){
	  new Fenetre(phase);
  }       
}