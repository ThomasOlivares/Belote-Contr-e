package fenetre;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Carte{
	
	public String valeur;
	public boolean jouee = false;
	public String couleur;
	public int valeurTri = 0;  //Valeur utile uniquement pour trier les cartes
	public Image[] picture = new Image[2];
	
	public Carte(String valeur, String couleur){
		
		this.valeur = valeur;
		this.couleur=couleur;
		if (couleur == "pique")
			valeurTri+=10;
		else if (couleur == "carreau")
			valeurTri+=20;
		else if (couleur == "trefle")
			valeurTri+=30;
		if (valeur == "roi")
			valeurTri+=1;
		else if (valeur == "dame")
			valeurTri+=2;
		else if (valeur == "valet")
			valeurTri+=3;
		else if (valeur == "10")
			valeurTri+=4;
		else if (valeur == "9")
			valeurTri+=5;
		else if (valeur == "8")
			valeurTri+=6;
		else if (valeur == "7")
			valeurTri+=7;
		try {
			this.picture[0] = ImageIO.read(new File(valeur + "_" + couleur + ".jpeg")); 
			this.picture[1] = ImageIO.read(new File(valeur + "_" + couleur + "_bis.jpeg"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toString(){
		return valeur + " de " + couleur;
	}
}

