package fenetre;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Carte{
	
	public static int largeur = 137;
	public String valeur;
	public boolean jouee = false;
	public String couleur;
	public int valeurTri = 0;  //Valeur utile uniquement pour trier les cartes
	public Image[] picture = new Image[2];
	public int RangAtout;
	public int RangNonAtout;
	
	public Carte(String valeur, String couleur){
		
		this.valeur = valeur;
		this.couleur=couleur;
		setRangs();
		setValeurTri();
		try {
			this.picture[0] = ImageIO.read(new File(valeur + "_" + couleur + ".jpeg"));
			this.picture[1] = ImageIO.read(new File(valeur + "_" + couleur + "_bis.jpeg"));
		}
		catch (IOException e) {
		}

	}
	public String toString(){
		return valeur + " de " + couleur;
	}
	
	public boolean equals(Carte carte){
		if (carte.valeur == this.valeur && carte.couleur == this.couleur){
			return true;
		}
		return false;
	}
	
	public boolean isequal(Carte c){
		if (this.couleur == c.couleur && this.valeur == c.valeur)
			return true;
		else
			return false;
	}
	
	public void setRangs(){
		if (this.valeur == "valet"){
			RangAtout = 1;
			RangNonAtout = 5;
		}
		if (this.valeur == "9"){
			RangAtout = 2;
			RangNonAtout = 6;
		}
		if (this.valeur == "as"){
			RangAtout = 3;
			RangNonAtout = 1;
		}
		if (this.valeur == "10"){
			RangAtout = 4;
			RangNonAtout = 2;
		}
		if (this.valeur == "roi"){
			RangAtout = 5;
			RangNonAtout = 3;
		}
		if (this.valeur == "dame"){
			RangAtout = 6;
			RangNonAtout = 4;
		}
		if (this.valeur == "8"){
			RangAtout = 7;
			RangNonAtout = 7;
		}
		if (this.valeur == "7"){
			RangAtout = 8;
			RangNonAtout = 8;
		}
	}
	
	public void setValeurTri(){
		if (couleur == "pique")
			valeurTri+=10;
		else if (couleur == "carreau")
			valeurTri+=20;
		else if (couleur == "trefle")
			valeurTri+=30;
		if (valeur == "10")
			valeurTri+=1;
		else if (valeur == "roi")
			valeurTri+=2;
		else if (valeur == "dame")
			valeurTri+=3;
		else if (valeur == "valet")
			valeurTri+=4;
		else if (valeur == "9")
			valeurTri+=5;
		else if (valeur == "8")
			valeurTri+=6;
		else if (valeur == "7")
			valeurTri+=7;
	}
}

