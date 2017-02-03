package fenetre;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class BoutonAnnonces extends JButton {
	
	public Image picture;
	public Image barre;
	private static final long serialVersionUID = 1L;
	public boolean fond = false;
	private String texte;
	private int nbCaracter = 0;
	private int decalage;
	private String couleur;

	public BoutonAnnonces(String str, String couleur){
		super(str);
		this.setPreferredSize(new Dimension (100,100));
		this.texte = str;
		this.couleur = couleur;
		if (couleur != ""){
			if (this.couleur == "coeur" || this.couleur == "carreau")
				try {
					this.barre = ImageIO.read(new File("barre2.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			else if (this.couleur == "pique" || this.couleur == "trefle")
				try {
					this.barre = ImageIO.read(new File("barre.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			nbCaracter = str.length();
			fond = true;
			try {
				this.picture = ImageIO.read(new File(couleur + ".jpeg")); 
			}
			catch (IOException e) {
			}
		}
		else{
			try {
				this.picture = ImageIO.read(new File("blanc.jpeg")); 
			}
			catch (IOException e) {
			}
		}
		Font police = new Font("Tahoma", Font.BOLD, 12);
		this.setFont(police);
		if (nbCaracter == 2)
			decalage = 4;
		else if (nbCaracter == 3)
			decalage = 11;
		else if (nbCaracter == 5)
			decalage = 22;
		
	}
	
	public void paintComponent(Graphics g){
		g.drawImage(this.picture, 0, 0, this);
		g.setFont(new Font("Tahoma", Font.BOLD, 20));
		if (this.couleur == "coeur" || this.couleur == "carreau"){  //On met du rouge sur du noir et inversement (question de lisibilité)
			g.setColor(Color.BLACK);
		}
		else if (this.couleur == "pique" || this.couleur == "trefle")
			g.setColor(Color.RED);
		g.drawString(this.texte, this.getWidth() / 2 - (this.getWidth() / 2 /4) - decalage, (this.getHeight() / 2) + 5);
		if (!this.isEnabled())
			g.drawImage(this.barre, 0, 0, this);
	}

}
