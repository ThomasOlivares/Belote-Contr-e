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
	public Image Bas;
	public boolean bas = false;
	public Image Droite;
	public boolean droite = false;
	public Image Haut;
	public boolean haut = false;
	public Image Gauche;
	public boolean gauche = false;
	public int entame;
	
	public Panneau(){
		super();
	}
	
	public Panneau(BorderLayout bord){
		super(bord);
	}
	
	public void paintComponent(Graphics g){
		Image img;
		Font police = new Font("Tahoma", Font.BOLD, 30);
		try {
			img = ImageIO.read(new File("Fond.jpeg"));
			g.drawImage(img, 0, 0, this);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		int i = entame;
		g.setFont(police);
		g.setColor(Color.ORANGE);
		g.drawString(Fenetre.noms[0], 990, this.getHeight()-50);
		g.drawString(Fenetre.noms[1], 0, this.getHeight()-140);
		g.drawString(Fenetre.noms[2], 990, 60);
		g.drawString(Fenetre.noms[3], 1020, this.getHeight()-140);
	
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
	} 
}

