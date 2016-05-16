package fenetre;

import java.util.LinkedList;
import java.util.List;

public class Joueur {
	
	public String position;
	public List<Carte> Cartes = new LinkedList<Carte>();
	public Annonce DerniereAnnonce = new Annonce();
	public boolean AnnonceFaite = false;
	public boolean AlwaysPassed = true;
	
	public Joueur(String position){
		this.position=position;
	}
	
	public String toString(){
		return "Je suis le joueur de " + position;
	}
}
