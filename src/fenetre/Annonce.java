package fenetre;

public class Annonce{
	
	public String valeur = "0";
	public String couleur = "";
	public String joueur = "";
	public Boolean contre = false; //Indique si l'annonce a �t�e contr�e/surcontr�e
	public Boolean surcontre = false;
	
	public Annonce(String val, String coul, String joueur){
		this.valeur = val;
		this.couleur = coul;
		this.joueur = joueur;
	}
	
	public Annonce(){
	}
	
	public String toString(){
		return "Annonce de " + valeur + " " + couleur + " par " + joueur + 
				((contre) ? " contr�e " : "") + ((surcontre) ? "et surcontr�e" : "");
	}

}
