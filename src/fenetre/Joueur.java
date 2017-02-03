package fenetre;

import java.util.LinkedList;
import java.util.List;

public class Joueur {
	
	private String position;
	private List<Carte> cartes = new LinkedList<Carte>();
	
	public Joueur(String position){
		this.position=position;
	}
	
	public String toString(){
		return "Je suis le joueur de " + position;
	}
	
	public void addCarte(Carte nouvelle){
		cartes.add(nouvelle);
	}

	public Carte getCarte(int num) {
		return cartes.get(num);
	}

	public void setCarte(int num, Carte carte) {
		cartes.set(num, carte);
	}
}
