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

public class Menu extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private Boolean regles = false;
	
	public Menu(){
		super();
	}
	
	public Menu(Boolean regles){
		super();
		this.regles = regles;
	}
	
	public Menu(BorderLayout bord){
		super(bord);
	}
	
	public void paintComponent(Graphics g){
		Image img;
		try {
			img = ImageIO.read(new File("Fond.jpeg"));
			g.drawImage(img, 0, 0, this);
			Image gauche = ImageIO.read(new File("valet_trefle.jpeg"));
			g.drawImage(gauche, 50, 250, this);
			Image droite = ImageIO.read(new File("as_coeur.jpeg"));
			g.drawImage(droite, this.getWidth()-50-Carte.largeur, 250, this);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		if (regles){
			g.setColor(Color.CYAN);
			g.setFont(new Font("Tahoma", Font.BOLD, 25));
			g.drawString("La contrée est un jeu de cartes à enchères.", 250, 100);
			g.drawString("Dans un premier temps, les joueurs se mettent d'accord", 250, 150);
			g.drawString("sur la couleur qui sera l'atout au moyen d'enchères.", 250, 200);
			g.drawString("L'enchère la plus élevée définie l'atout mais aussi", 250, 250);
			g.drawString("le nombre de points que l'équipe doit effectuer pour", 250, 300);
			g.drawString("remplir son contrat.", 250, 350);
			g.drawString("Dans un second temps, la phase de jeu intervient, et ou", 250, 400);
			g.drawString("chaque équipe doit faire un maximum de plis afin de", 250, 450);
			g.drawString("remplir son contrat ou empêcher les adversaires de remplir le leur.", 250, 500);
		}
	} 
}

