package fenetre;

import java.util.LinkedList;
import java.util.List;

public class IA {
	
	public String position;
	public List<CarteIA> Cartes = new LinkedList<CarteIA>();
	public Annonce DerniereAnnonce = new Annonce();
	
	public IA(String position){
		this.position=position;
	}
	

}
