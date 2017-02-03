package fenetre;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;

public class LabelAnnonce extends JLabel{
	
	private static final long serialVersionUID = 1L;
	public boolean gagne = false;  //indique si cette annonce a remporter les enchères
	
	public LabelAnnonce(String str){
		super(str);
		Font police = new Font("Tahoma", Font.BOLD, 20);
		this.setFont(police);
		this.setForeground(Color.RED);
	}

}
