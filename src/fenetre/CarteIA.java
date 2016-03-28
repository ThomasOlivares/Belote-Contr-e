package fenetre;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JLabel;

public class CarteIA extends JLabel{
	
	private static final long serialVersionUID = 1L;
	
	public Image picture;
	
	public CarteIA (Carte carte){
		super();
		this.picture=carte.picture[0];
	}
	public void paintComponent(Graphics g){
		g.drawImage(this.picture, 0, 0, this);
	}

}