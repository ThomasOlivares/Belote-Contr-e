package fenetre;

public class Instance extends Thread {
	
	public String phase;

  public Instance(String phase){
    super(phase);
    this.phase = phase;
  }

  public void run(){
	  @SuppressWarnings("unused")
	  Fenetre Letsgo = new Fenetre(phase);
  }       
}