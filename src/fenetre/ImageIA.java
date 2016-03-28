package fenetre;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;

public class ImageIA extends JLabel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image[] picture = new Image[2];
	
	public ImageIA (Carte carte){
		picture[0]=carte.picture[0];
		picture[1]=carte.picture[1];
	}
	public void paintComponent(Graphics g){
		g.drawImage(this.picture[0], 0, 0, this);
	}

}

