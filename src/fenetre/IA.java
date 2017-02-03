package fenetre;

import java.util.LinkedList;
import java.util.List;

public class IA {
	
	private String position;
	private List<CarteIA> cartes;
	
	public IA(String position){
		this.position=position;
		cartes = new LinkedList<CarteIA>();
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public CarteIA getCarte(int num) {
		return cartes.get(num);
	}
	
	public int getNumberCartes(){
		return cartes.size();
	}
	
	public void addCarte(CarteIA carte){
		cartes.add(carte);
	}

	public void setCartes(int num, CarteIA carte) {
		cartes.set(num, carte);
	}
}
