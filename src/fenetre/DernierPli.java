package fenetre;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DernierPli extends JDialog {
	
	private static final long serialVersionUID = 1L;

public DernierPli(JFrame parent, String title, boolean modal, List<Carte> cartes){
    super(parent, title, modal);
    this.setSize(300, 150);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    
    JPanel container = new JPanel();
    for (int i = 0; i<4; i++){
    	CarteIA temp = new CarteIA(cartes.get(i));
    	temp.setPreferredSize(new Dimension(50,100));
    	container.add(temp);
    }
    JButton ok = new JButton("OK");
    container.add(ok);
    ok.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			dispose();
		}
	});
    this.setContentPane(container);
    this.setVisible(true);
  }

public DernierPli(JFrame parent, String title, boolean modal){
    super(parent, title, modal);
    this.setSize(300, 100);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    JPanel container = new JPanel();
    JLabel error = new JLabel("Aucun pli n'a encore été effectué");
    Font police = new Font("Tahoma", Font.BOLD, 15);
    error.setFont(police);
    container.add(error);
    JButton ok = new JButton("OK");
    container.add(ok);
    ok.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			dispose();
		}
	});
    this.setContentPane(container);
    this.setVisible(true);
  }
}