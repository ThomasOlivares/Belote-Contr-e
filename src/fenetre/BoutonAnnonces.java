package fenetre;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

public class BoutonAnnonces extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BoutonAnnonces(String str){
		super(str);
		this.setPreferredSize(new Dimension (100,100));
		Font police = new Font("Tahoma", Font.BOLD, 12);
		this.setFont(police);
	}

}
