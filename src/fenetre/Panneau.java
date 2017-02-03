package fenetre;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import javax.imageio.ImageIO; 
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class Panneau extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private Image Bas;
	private boolean bas = false;
	private Image Droite;
	private boolean droite = false;
	private Image Haut;
	private boolean haut = false;
	private Image Gauche;
	private boolean gauche = false;
	private int entame;
	
	public Panneau(){
		super();
	}
	
	public Panneau(BorderLayout bord){
		super(bord);
	}
	
	public void paintComponent(Graphics g){
		Image img;
		Font police = new Font("Tahoma", Font.BOLD, 25);
		try {
			img = ImageIO.read(new File("images/Fond.jpeg"));
			g.drawImage(img, 0, 0, this);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		int i = entame;
		g.setFont(police);
		g.setColor(Color.CYAN);
		g.drawString(Fenetre.getNom(0), 1020, this.getHeight()-50);
		g.drawString(Fenetre.getNom(1), 0, this.getHeight()-140);
		g.drawString(Fenetre.getNom(2), 1020, 60);
		g.drawString(Fenetre.getNom(3), 1050, this.getHeight()-140);
	
		for (int j = 0; j<4; j++){
			if (i == 0 && bas)
				g.drawImage(Bas, 525, 345, this);
			if (i == 1 && gauche)
				g.drawImage(Gauche, 400, 270, this);
			if (i == 2 && haut)
				g.drawImage(Haut, 525, 145, this);
			if (i == 3 && droite)
				g.drawImage(Droite, 600, 270, this);
			i = (i+1)%4;
		}
		g.drawString("Score Joueur : " + Fenetre.getScoreJoueurProvisoire(), 0, 30);
		g.drawString("Score IA : " + Fenetre.getScoreIAProvisoire(), 0, 60);
		if (Fenetre.getAtout() !="aucun")
			g.drawString(((Fenetre.getAnnonceGagnante().getValeur() == 250) ? 
					"Capot" : Fenetre.getAnnonceGagnante().getValeur() )+ " " 
					+ Fenetre.getAnnonceGagnante().getCouleur(), 0, 100);
	}

	public Image getBas() {
		return Bas;
	}

	public void setBas(Image bas) {
		Bas = bas;
	}

	public boolean isBas() {
		return bas;
	}

	public void setBas(boolean bas) {
		this.bas = bas;
	}

	public Image getDroite() {
		return Droite;
	}

	public void setDroite(Image droite) {
		Droite = droite;
	}

	public boolean isDroite() {
		return droite;
	}

	public void setDroite(boolean droite) {
		this.droite = droite;
	}

	public Image getHaut() {
		return Haut;
	}

	public void setHaut(Image haut) {
		Haut = haut;
	}

	public boolean isHaut() {
		return haut;
	}

	public void setHaut(boolean haut) {
		this.haut = haut;
	}

	public Image getGauche() {
		return Gauche;
	}

	public void setGauche(Image gauche) {
		Gauche = gauche;
	}

	public boolean isGauche() {
		return gauche;
	}

	public void setGauche(boolean gauche) {
		this.gauche = gauche;
	}

	public int getEntame() {
		return entame;
	}

	public void setEntame(int entame) {
		this.entame = entame;
	} 
}

