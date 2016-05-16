package fenetre;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.imageio.ImageIO; 
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class Menu extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	public Menu(){
		super();
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
	} 
}

