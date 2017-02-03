package fenetre;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class CarteIA extends JLabel{
	
	private static final long serialVersionUID = 1L;
	
	private Image picture;
	private Image dos;
	private Carte carte;
	
	public CarteIA (Carte carte){
		super();
		try {
			dos = ImageIO.read(new File("images/dos.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.picture = carte.getPicture(0);
		this.carte = carte;
	}
	public void paintComponent(Graphics g){
		if (Fenetre.getMasquer() && !carte.isJouee()){
			g.drawImage(dos, 0, 0, this);
		}
		else
			g.drawImage(this.picture, 0, 0, this);
	}

}