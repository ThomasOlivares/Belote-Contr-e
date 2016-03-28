package fenetre;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;

public class Bouton extends JButton implements MouseListener, Observable{
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Observateur> listObservateur = new ArrayList<Observateur>();
	
	public boolean echange1 = false;
	public boolean echange2 = false;

	public Image picture;
	
	public Dimension dim = new Dimension(89,130);
	
	public Bouton (Carte carte){
		super();
		this.picture=carte.picture[0];
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
		echange1=true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		echange2=true;
	}
	@Override
	public void addObservateur(Observateur obs) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateObservateur() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void delObservateur() {
		// TODO Auto-generated method stub
		
	}

}
