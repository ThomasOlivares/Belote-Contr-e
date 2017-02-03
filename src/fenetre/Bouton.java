package fenetre;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class Bouton extends JButton implements MouseListener{
	
	private static final long serialVersionUID = 1L;
	private boolean echange = false;
	private Image picture;
	private Carte carteBouton;
	private Dimension dim = new Dimension(89,130);
	
	public Bouton (Carte carte){
		super();
		this.picture=carte.getPicture(0);
		this.carteBouton = carte;
		this.addMouseListener(this);
	}
	
	public void paintComponent(Graphics g){
		g.drawImage(this.picture, 0, 0, this);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		this.setSize(new Dimension(110, 165));
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		this.setSize(new Dimension(89,130));
		this.repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		echange=true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}

	public boolean isEchange() {
		return echange;
	}

	public void setEchange(boolean echange) {
		this.echange = echange;
	}

	public Image getPicture() {
		return picture;
	}

	public void setPicture(Image picture) {
		this.picture = picture;
	}

	public Carte getCarteBouton() {
		return carteBouton;
	}

	public void setCarteBouton(Carte carteBouton) {
		this.carteBouton = carteBouton;
	}

	public Dimension getDim() {
		return dim;
	}

	public void setDim(Dimension dim) {
		this.dim = dim;
	}
}
