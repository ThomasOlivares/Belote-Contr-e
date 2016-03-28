package fenetre;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.imageio.ImageIO; 
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class Panneau extends JPanel {
	
	private static final long serialVersionUID = 1L;
	public Image Bas;
	public boolean bas = false;
	public Image Droite;
	public boolean droite = false;
	public Image Haut;
	public boolean haut = false;
	public Image Gauche;
	public boolean gauche = false;
	
	public Panneau(){
		super();
	}
	
	public Panneau(BorderLayout bord){
		super(bord);
	}
	
	public void paintComponent(Graphics g){
		Image img;
		try {
			img = ImageIO.read(new File("Fond.jpeg"));
			g.drawImage(img, 0, 0, this);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		if(bas){
			g.drawImage(Bas, 525, 345, this);
		}
		if(gauche){
			g.drawImage(Gauche, 400, 270, this);
		}
		if(haut){
			g.drawImage(Haut, 525, 145, this);
		}
		if(droite){
			g.drawImage(Droite, 600, 270, this);
		}
		
		
	} 
	
	

}
