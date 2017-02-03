package fenetre;

public class Annonce{
	
	private int valeur = 0;
	private String couleur = "";
	private int joueur;
	private Boolean contre = false; // Indicate if the announce has been counter/overcounter
	private Boolean surcontre = false;
	private int numero = 0; // 1-Annonce primaire, 2-secondaire, 3-tertiaire
	private int ajout = 0; // montant ajout� � une annonce primaire (propre aux annonces secondaires)
	
	public Annonce(int val, String coul, int joueur, int numero){
		this.valeur = val;
		this.couleur = coul;
		this.joueur = joueur;
		this.numero = numero;
	}
	
	public Annonce(int val, String coul, int joueur, int numero, int ajout){ //Constructeur propre aux annonces secondaires
		this.valeur = val;
		this.couleur = coul;
		this.joueur = joueur;
		this.numero = numero;
		this.ajout = ajout;
	}
	
	public Annonce(){
	}
	
	public String toString(){
		String val = "";
		if (valeur == 250)
			val = "Capot";
		else if (valeur == 0)
			val = "Passe";
		else
			val = Integer.toString(valeur);
		return "Annonce de " + val + " " + couleur + " par " + Fenetre.getNom(joueur) + 
				((contre) ? " contrée " : "") + ((surcontre) ? "et surcontrée" : "");
	}
	
	public int getNumero(){
		return numero;
	}
	
	public int getAjout(){
		return ajout;
	}
	
	public String getCouleur(){
		return couleur;
	}
	
	public int getValeur(){
		return valeur;
	}
	
	public int getJoueur(){
		return joueur;
	}
	
	public boolean getContre(){
		return contre;
	}
	
	public void setContre(boolean nouveau){
		contre = nouveau;
	}
	
	public boolean getSurContre(){
		return surcontre;
	}
	
	public void setSurContre(boolean nouveau){
		surcontre = nouveau;
	}

	public Boolean getSurcontre() {
		return surcontre;
	}

	public void setSurcontre(Boolean surcontre) {
		this.surcontre = surcontre;
	}

	public void setValeur(int valeur) {
		this.valeur = valeur;
	}

	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}

	public void setJoueur(int joueur) {
		this.joueur = joueur;
	}

	public void setContre(Boolean contre) {
		this.contre = contre;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public void setAjout(int ajout) {
		this.ajout = ajout;
	}

}
