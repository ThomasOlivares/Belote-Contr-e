package fenetre;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToolBar;

/**
 * @authors Thomas, Paul, Meric, Martin
 */
public class Fenetre extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	//---------  Variables de la partie affichage-----------
	private JPopupMenu menuContextuel= new JPopupMenu();
	private JToolBar outils = new JToolBar();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu partie = new JMenu("Partie");
	private JMenu jeu = new JMenu("Jeu");
	private JMenu aPropos = new JMenu("?");
	private JMenuItem lancer = new JMenuItem("Relancer une nouvelle partie");
	private JMenuItem menuPrincipal = new JMenuItem("Menu Principal");
	private JMenuItem quitter = new JMenuItem("Quitter le jeu");
	private JMenuItem dernierPli = new JMenuItem("Voir dernier pli");
	private JMenuItem score = new JMenuItem("Score général");
	private JMenuItem regles = new JMenuItem("Règles");
	private static Instance PartieEnCours = new Instance("MenuPrincipal");
	private Boolean go = false;
	private static String[] noms = {"Paul", "Martin", "Meric", "Thomas"};
	
	//---------  Variables de la phase d'annonce -----------
	private boolean ecouteAnnonce = false; //Indique si c'est au tour du joueur d'annoncer
	private List<BoutonAnnonces> annonceCoeur = new LinkedList<BoutonAnnonces>();   //Ces listes servent a ordonner les boutons d'annonces
	private List<BoutonAnnonces> annoncePique = new LinkedList<BoutonAnnonces>();
	private List<BoutonAnnonces> annonceCarreau = new LinkedList<BoutonAnnonces>();
	private List<BoutonAnnonces> annonceTrefle = new LinkedList<BoutonAnnonces>();
	private List<JButton> annonceSpecial = new LinkedList<JButton>();
	private int passe=0;   //Compte le nombre de joueurs qui passent leur tour
	private static Annonce AnnonceGagnante = new Annonce();   //Indique la dernière annonce effectuée
	private JPanel annonc; // Panneau gérant tous les boutons d'annonces
	private LabelAnnonce AnnonceBas = new LabelAnnonce("---");  // Affichage des annonces (en rouge)
	private LabelAnnonce AnnonceGauche = new LabelAnnonce("---");
	private LabelAnnonce AnnonceHaut = new LabelAnnonce("---");
	private LabelAnnonce AnnonceDroite = new LabelAnnonce("---");
	private List<Annonce> AnnonceJoueur = new LinkedList<Annonce>();
	private List<Annonce> AnnonceIA = new LinkedList<Annonce>();
	
	//---------  Variables de la phase de jeu -----------
	private boolean ecouteJoue = false; //Indique si c'est au tour du joueur de jouer
	//Les cartes des joueur : la première ligne représente les cartes du joueur, les 3 autres celles des IA
	private Carte[][] Joueurs = new Carte[4][8];
	private Panneau container = new Panneau();  //Panneau général englobant tous les composants
	private JPanel bas = new JPanel();   // Panneaux contenants les cartes des joueurs
    private JPanel gauche = new JPanel();
    private JPanel droite = new JPanel();
    private JPanel haut = new JPanel();
    private List<Bouton> Boutons = new LinkedList<Bouton>();  // Liste des Cartes du joueur réel
    //Liste des cartes utilisée (ce n'est pas le paquet)
    private List<Carte> Liste_Cartes = new LinkedList<Carte>();  
    private Joueur JoueurGauche = new Joueur("gauche");
    private Joueur JoueurHaut = new Joueur("haut");
    private Joueur JoueurDroite = new Joueur("droite");
    private IA IAgauche = new IA("gauche");
    private IA IAhaut = new IA("haut");
    private IA IAdroite = new IA("droite");
    private List<Carte> CartesMaitres = new LinkedList<Carte>();
    private Carte CarteAtoutMaitre;
    private int RangAtoutMaitre;	// Rang de la carte atout maitre du pli actuel
    private List<Carte> CartesTombees = new LinkedList<Carte>();
    private List<Carte> CartesPlateau = new LinkedList<Carte>();
    private String[] Couleurs = {"trefle", "carreau","coeur","pique"};
    private String[] OrdreCartesNonAtout = {"as","10","roi","dame","valet","9","8","7",""};
    private String[] OrdreCartesAtout = {"valet","9","as","10","roi","dame","8","7",""};
    private static int ScoreIAProvisoire = 0 ;
    private static int ScoreJoueurProvisoire = 0 ;
    private static int ScoreJoueur = 0 ;
    private static int ScoreIA = 0 ;
    private static int WinScore = 1000 ;  //Score nécessaire pour gagner
    private boolean pliIA = false ;
    private boolean pliJoueur = false ;
    
    // ----------Variables mixtes ---------------
    private double vitesse = 1; //Indique la vitesse de jeu : 1=normal
    // Indique qui commence a jouer, 0 : joueur, 1 : IAgauche, 2 : IAhaut, 3 : IAdroite
    private int premier = 0;
    private String etat = "MenuPrincipal"; //Indique la phase de jeu actuelle
    private JButton NvPartie;
    private JButton Regles;
    private JButton Options;
    private static String Atout = "aucun";  //Couleur de l'atout
    private static Boolean masquer = true; //indique si on masque les cartes des IA (jeu normal) ou non (tests)
    
    /**
     * Programme principal, 
     * comporte une phase d'initialisation puis l'appel de la fonction
     * correspondant à la phase de jeu dans laquelle on se trouve.
     */
    
    public Fenetre(String phase){
    	
    	ScoreJoueurProvisoire = 0;
    	ScoreIAProvisoire = 0;
    	etat = phase;
    	
    	this.setTitle("Plateau de jeu");
		this.setSize(1202, 719);
		this.setLocationRelativeTo(null);               
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	while (true){
    		if (etat == "MenuPrincipal")
    			MenuPrincipal();
    		else if(etat == "jeu")
    			PlateauJeu();
    		else if (etat == "options")
    			Options();
    		else if (etat == "regles")
    			Regles();
    		else if (etat == "fin")
    			FinPartie(ScoreJoueur>ScoreIA); //On lance l'écran victoire/défaite
    	}
   	}
    
    public void setMenu(){
    	lancer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {  //On change de thread pour un nouveau
    			dispose();
    			PartieEnCours = new Instance("jeu");
    			PartieEnCours.start();
    		}
    	});
    	partie.add(menuPrincipal);
    	menuPrincipal.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
    			dispose();
    			PartieEnCours = new Instance("MenuPrincipal");
    			PartieEnCours.start();
    		}
    	});
    	quitter.addActionListener(new ActionListener(){    // On arrete le programme
    		public void actionPerformed(ActionEvent arg0) {
    			System.exit(0);
    		}
    	});
    	dernierPli.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				int min = CartesTombees.size();
				if (min>=4){
					List<Carte> cartes = new LinkedList<Carte>();
					for (int i = 0; i<4; i++)
						cartes.add(CartesTombees.get(min-4+i));
					@SuppressWarnings("unused")
					DernierPli fen = new DernierPli(null, "Dernier pli", true, cartes);
				}
				else{
					@SuppressWarnings("unused")
					DernierPli fenErreur = new DernierPli(null, "Dernier pli", true);
				}
    		}
    	});
    	score.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Score Joueur : " + ScoreJoueur + "\nScore IA : " + ScoreIA, 
						"Score", JOptionPane.INFORMATION_MESSAGE);
    		}
    	});
    	String message = "La contrée est un jeu de cartes à enchères." + '\n' + 
				"Dans un premier temps, les joueurs se mettent d'accord" + '\n' + 
				"sur la couleur qui sera l'atout au moyen d'enchères." + '\n' +
				"L'enchère la plus élevée définie l'atout mais aussi" + '\n' +
				"le nombre de points que l'équipe doit effectuer pour" + '\n' +
				"remplir son contrat." + '\n' +
				"Dans un second temps, la phase de jeu intervient, et où" + '\n' +
				"chaque équipe doit faire un maximum de plis afin de" + '\n' +
				"remplir son contrat ou empêcher les adversaires de remplir le leur.";
    	regles.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, message, "Regles", JOptionPane.INFORMATION_MESSAGE);
    		}
    	});
    	partie.add(lancer);
    	partie.add(quitter);
    	jeu.add(dernierPli);
    	jeu.add(score);
    	aPropos.add(regles);
    	this.menuBar.add(partie);
        this.menuBar.add(jeu);
        this.menuBar.add(aPropos);
        this.setJMenuBar(menuBar);
        
        //   Menu Contextuel
        
        container.addMouseListener(new MouseAdapter(){
            public void mouseReleased(MouseEvent event){
            	if(event.isPopupTrigger()){
            		menuContextuel.add(partie);
            		menuContextuel.add(jeu);
            		menuContextuel.add(aPropos);
            		menuContextuel.show(container, (event).getX(), (event).getY());
            	}
            }
          });
        
        //   Menu Outils
        
        JButton b1 = new JButton("lancer");
        b1.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent event){
        		lancer.doClick();
        	}
        });
        JButton b2 = new JButton("quitter");
        b2.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent event){
        		quitter.doClick();
        	}
        });
        JButton b3 = new JButton(new ImageIcon("images/mini_carte.jpeg"));
        b3.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent event){
        		dernierPli.doClick();
        	}
        });
        outils.add(b1);
        outils.add(b2);
        outils.add(b3);
        menuBar.add(outils);
    }
    
    public void MenuPrincipal(){
    	this.setTitle("Menu Principal");
    	
    	Font police = new Font("Tahoma", Font.BOLD, 30);
    	Font police2 = new Font("Tahoma", Font.BOLD, 50);
    	
    	JLabel Bienvenue = new JLabel("Bienvenue sur le plateau de contrée");
    	NvPartie = new JButton("Nouvelle Partie");
    	Regles = new JButton("Règles du jeu");
    	Options = new JButton("Options");
    	
    	Bienvenue.setFont(police2);
    	Bienvenue.setForeground(Color.RED);
    	Bienvenue.setHorizontalAlignment(JLabel.CENTER);
    	NvPartie.setFont(police);
    	Regles.setFont(police);
    	Options.setFont(police);
    	
    	NvPartie.setPreferredSize(new Dimension(500,80));
    	Regles.setPreferredSize(new Dimension(500,80));
    	Options.setPreferredSize(new Dimension(500,80));
    	
    	NvPartie.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) { 
				etat = "jeu";
    		}
    	});
    	
    	Regles.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) { 
				etat = "regles";
    		}
    	});
    	
    	Options.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) { 
				etat = "options";
    		}
    	});
    	
    	JPanel pan1 = new JPanel(new BorderLayout());
        pan1.setOpaque(false);
        pan1.add(new JLabel(), BorderLayout.NORTH);
        pan1.add(Bienvenue, BorderLayout.SOUTH);

        JPanel pan2 = new JPanel();
        pan2.setOpaque(false);
        pan2.add(NvPartie);

        JPanel pan3 = new JPanel();
        pan3.setOpaque(false);
        pan3.add(Regles);
        
        JPanel pan4 = new JPanel();
        pan4.setOpaque(false);
        pan4.add(Options);
        
        Menu principal = new Menu();
        principal.setLayout(new GridLayout(4,1,20,20));

        principal.add(pan1);
        principal.add(pan2);
        principal.add(pan3);
        principal.add(pan4);
    	
    	this.setContentPane(principal);
    	this.setVisible(true);
    	while(etat == "MenuPrincipal"){pause(10);} //On attend l'utilisateur
    }
    
    public void Options(){
    	
    	Menu opt = new Menu();
    	opt.setLayout(new GridLayout(6,1,5,5));
    	Font police = new Font("Tahoma", Font.BOLD, 30);
    	JLabel[] texte = {new JLabel("Nom joueur : "), new JLabel("Nom IA gauche : "), //Champs de textes
    			new JLabel("Nom IA haut : "), new JLabel("Nom IA droite : "), new JLabel("Score pour gagner : ")};
    	JTextField[] text = new JTextField[4];
    	JComboBox<Integer> win = new JComboBox<Integer>();   //Choix multiples
    	JPanel[] pan = new JPanel[5];
    	for (int i = 0; i<4; i++){
    		texte[i].setFont(police);
    		texte[i].setForeground(Color.YELLOW);
    		text[i] = new JTextField(noms[i]);
    		text[i].setPreferredSize(new Dimension(400,40));
    		pan[i] = new JPanel();
    		pan[i].setOpaque(false);
    		pan[i].add(texte[i]);
        	pan[i].add(text[i]);
        	opt.add(pan[i]);
    	}
    	win.setPreferredSize(new Dimension(200,40));
    	win.addItem(1000);
    	win.addItem(2000);
    	win.addItem(3000);
    	win.addItemListener(new ItemState());
    	texte[4].setFont(police);
		texte[4].setForeground(Color.YELLOW);
    	pan[4] = new JPanel();
		pan[4].setOpaque(false);
		pan[4].add(texte[4]);
    	pan[4].add(win);
    	opt.add(pan[4]);
    	
    	JPanel valider = new JPanel();
    	valider.setOpaque(false);
    	JButton ok = new JButton("OK");
    	ok.setFont(police);
    	ok.setPreferredSize(new Dimension(100,50));
    	ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				etat = "MenuPrincipal";
				go = true;
			}
    	});
    	valider.add(ok);
    	
    	opt.add(valider);
    	
    	opt.setOpaque(false);
    	
    	this.setContentPane(opt);
    	this.setVisible(true);
    	
    	while(!go){  //On attend le joueur
    		pause(5);
    	}
    	
    	for (int i = 0; i<4; i++){
    		String nvNom = text[i].getText();
    		if (nvNom != null){
    			noms[i] = nvNom;
    		}
    	}
    	go =false;
    }
    
    class ItemState implements ItemListener{  //Prise en compte du choix du joueur sur le score maximum pour gagner
    	
	    public void itemStateChanged(ItemEvent e) {
	        WinScore = (int)e.getItem();
	    }               

	  }
    
    public void Regles(){
    	Menu regles = new Menu(true);
    	regles.setLayout(new BorderLayout());
    	JPanel pan1 = new JPanel();
    	pan1.setOpaque(false);
    	JButton ok = new JButton("OK");
    	ok.setPreferredSize(new Dimension(100,50));
    	ok.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e){
    			etat = "MenuPrincipal";
    		}
    	});
    	pan1.add(ok);
    	regles.add(pan1, BorderLayout.SOUTH);
    	this.setContentPane(regles);
    	this.setVisible(true);
    	while(etat == "regles"){ pause(5); } //On attend le joueur
    }
    
    public void PlateauJeu(){  //Sous fonction correspondant à la phase de jeu
    	
    	initialise();  //initialise les 32 cartes
    	
    	container = new Panneau(new BorderLayout());
		
		bas.setOpaque(false);
		gauche.setOpaque(false);
		haut.setOpaque(false);
		droite.setOpaque(false);
		bas.setPreferredSize(new Dimension(this.getWidth(), 165));
		gauche.setPreferredSize(new Dimension(130, this.getHeight()));
		haut.setPreferredSize(new Dimension(this.getWidth(), 130));
		droite.setPreferredSize(new Dimension(130, this.getHeight()));
		
		setMenu();
		    
		shuffle();
		distribue();
		trier();
		
		initialiseAnnonces();  //Place tous les boutons liés aux annonces
		    
		container.add(droite, BorderLayout.EAST);
		container.add(gauche, BorderLayout.WEST);
		container.add(bas, BorderLayout.SOUTH);
		container.add(haut, BorderLayout.NORTH);
		    
		container.repaint();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(container);
		this.setTitle("Plateau de jeu");
		this.requestFocus();
		this.setVisible(true);
		container.repaint();
		    
		go();  // Gestion de la partie
    }
    
    public void go(){
		
		while(ScoreJoueur < WinScore && ScoreIA < WinScore){  // WinScore par defaut a 1000
			this.setSize(1202, 720); //Ces deux lignes servent a forcer le programme à réactualiser la fenêtre
			this.setSize(1202, 719);
			Atout = "aucun";
			Phase_annonces();
			Atout = AnnonceGagnante.getCouleur();
			this.getContentPane().repaint();
			InitialiseCartesMaitres();
			int entame = premier;
			int carte_jouer = 0;  //indique le nombre de carte déjà jouées
			pause((int)(1000/vitesse));
			while(carte_jouer<8){  //Boucle gérant une partie en phase de jeu
				//On précise au plateau quelle carte il dessine en dessous des autres
				container.setEntame(entame);  
				int i=entame;  //i est le numero du joueur qui joue 
				for (int j = 0; j<4 ; j++){  //boucle gérant un tour
					if (i==0)
						play_joueur();
					else 
						play_IA(i);
					i=(i+1)%4;
				}
				for (Carte j: CartesPlateau){
					if ((checkGagnant() + entame)%4 == 0 || (checkGagnant() + entame)%4 == 2){
						ScoreJoueurProvisoire += value(j) ;
						pliJoueur = true ;
					}
					else {
						ScoreIAProvisoire += value(j) ;
						pliIA = true ;
					}
					CartesTombees.add(j);
				}
				int GagnantFinal = (checkGagnant() + entame)%4;
				entame = (checkGagnant() + entame)%4; //On regarde qui remporte le pli
				CartesPlateau = new LinkedList<Carte>();
				ActualiseCartesMaitres();
				pause((int)(500/vitesse));
				container.setBas(false);  //On efface les cartes sur le tapis de jeu
				container.setGauche(false);
				container.setHaut(false);
				container.setDroite(false);
				container.repaint();
				carte_jouer++;
				pause((int)(500/vitesse));
//				10 de der
				if (carte_jouer == 7 && (GagnantFinal == 0 || GagnantFinal == 2)) ScoreJoueurProvisoire += 10 ;
				if (carte_jouer == 7 && (GagnantFinal == 1 || GagnantFinal == 3)) ScoreIAProvisoire += 10 ;
			}
			pause((int)(2000/vitesse));
//			On compte les points de la manche
			if ((AnnonceGagnante.getJoueur() == 0 || AnnonceGagnante.getJoueur() == 2) && 
					AnnonceGagnante.getValeur() != 250){
				if (ScoreJoueurProvisoire >= AnnonceGagnante.getValeur() && AnnonceGagnante.getContre()){
					ScoreJoueur += 2*AnnonceGagnante.getValeur();
				}
				else if(ScoreJoueurProvisoire >= AnnonceGagnante.getValeur())
					ScoreJoueur += AnnonceGagnante.getValeur() ;
				else {
					ScoreIA += 160 ;
				}
			}
			if ((AnnonceGagnante.getJoueur() == 1 || AnnonceGagnante.getJoueur() == 3) 
					&& AnnonceGagnante.getValeur() != 250){
				if (ScoreIAProvisoire >= AnnonceGagnante.getValeur() && AnnonceGagnante.getContre()){
					ScoreIA += 2*AnnonceGagnante.getValeur();
				}
				else if (ScoreIAProvisoire >= AnnonceGagnante.getValeur()){
					ScoreIA += AnnonceGagnante.getValeur();
				}
				else {
					ScoreJoueur += 160 ;
				}
			}
			if(AnnonceGagnante.getValeur() == 250){
				if(AnnonceGagnante.getJoueur() == 1 || AnnonceGagnante.getJoueur() == 3){
					if(pliJoueur == false && AnnonceGagnante.getContre()){
						ScoreIA += 500 ;
					}
					else if(pliJoueur == false){
						ScoreIA += 250 ;
					}
					else ScoreJoueur += 160 ;
				}
				if(AnnonceGagnante.getJoueur() == 0 || AnnonceGagnante.getJoueur() == 2){
					if(pliIA == false && AnnonceGagnante.getContre()){
						ScoreJoueur+= 500 ;
					}
					else if(pliIA == false){
						ScoreJoueur+= 250 ;
					}
					else ScoreIA += 160 ;
				}
			}
			pliIA = false;
			pliJoueur = false ;
			ScoreIAProvisoire = 0 ;
			ScoreJoueurProvisoire = 0 ;
			premier = (premier+1)%4;  //On change le joueur qui commence
			Liste_Cartes = CartesTombees;
			CartesTombees = new LinkedList<Carte>();
			cut();
			distribue();
			trier();
			reboot();   //Rend toutes les cartes de nouveau visibles et réaffiche les boutons d'annonces
			pause((int)(1000/vitesse));
		}
		etat = "fin";
	}
    
    public void FinPartie(Boolean victoire){  //Ecran victoire/défaite
    	Menu fin = new Menu();
    	Font police = new Font("Tahoma", Font.BOLD, 30);
    	Font police2 = new Font("Tahoma", Font.BOLD, 50);
    	fin.setLayout(new GridLayout(3,1,20,20));
    	JLabel msg = new JLabel();
    	msg.setFont(police2);
    	msg.setForeground(Color.RED);
    	if (victoire)
    		msg.setText("Félicitations, vous avez gagné");
    	else
    		msg.setText("Dommage vous avez perdu");
    	
    	JPanel pan1 = new JPanel();
    	pan1.add(msg);
    	fin.add(pan1);
    	pan1.setOpaque(false);
    	
    	JPanel pan2 = new JPanel();
    	pan2.add(NvPartie);
    	NvPartie.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) { 
				etat = "jeu";
    		}
    	});
    	fin.add(pan2);
    	pan2.setOpaque(false);
    	
    	JPanel pan3 = new JPanel();
    	JButton quit = new JButton("Quitter la partie");
    	quit.setFont(police);
    	quit.setPreferredSize(new Dimension(500,80));
    	quit.addActionListener(new ActionListener(){    // On arrete le programme
    		public void actionPerformed(ActionEvent arg0) {
    			System.exit(0);
    		}
    	});
    	pan3.add(quit);
    	fin.add(pan3);
    	pan3.setOpaque(false);
    	
    	this.setContentPane(fin);
    	this.setVisible(true);
    	
    	while(etat == "fin"){  // On attend que le joueur appui sur un bouton
    		pause(5);
    	}
    }
    
    public void InitialiseCartesMaitres(){
    	if (Atout != "coeur")
    		CartesMaitres.add(new Carte("as", "coeur"));
    	if (Atout != "carreau")
    		CartesMaitres.add(new Carte("as", "carreau"));
    	if (Atout != "trefle")
    		CartesMaitres.add(new Carte("as", "trefle"));
    	if (Atout != "pique")
    		CartesMaitres.add(new Carte("as", "pique"));
    	CarteAtoutMaitre = new Carte("valet", Atout);
    }
    
    public void ActualiseCartesMaitres(){
    	CartesMaitres = new LinkedList<Carte>();
    	for (String i : Couleurs){
    		if (i != Atout){
       	    	int indice = 0;
       	    	Carte Temp = new Carte(OrdreCartesNonAtout[indice],i);
    			while (indice<9 && contient(CartesTombees, Temp)){
    				indice ++;
    				Temp = new Carte(OrdreCartesNonAtout[indice],i);
    			}
       	    	CartesMaitres.add(Temp);
    		}
    		else{
       	    	int indice = 0;
    			Carte Temp = new Carte(OrdreCartesAtout[indice],i);
    			while (indice<9 && contient(CartesTombees, Temp)){
    				indice ++;
    				Temp = new Carte(OrdreCartesAtout[indice],i);
    				CarteAtoutMaitre = Temp;
    			}
    			
    		}
    	}
    }
    
    public boolean contient(List<Carte> liste, Carte carte){
    	for (Carte temp : liste){
    		if (temp.equals(carte))
    			return true;
    	}
    	return false;
    }
    
    public boolean contient(Carte[] liste, Carte carte){
    	for (Carte temp : liste){
    		if (temp.equals(carte))
    			return true;
    	}
    	return false;
    }
    
		public void pause(int t){  // Permet de faire une pause de t ms
			  try{
				  Thread.sleep(t);
				}catch(InterruptedException e) {
				  e.printStackTrace();
			  }
		}
		
		public void actionPerformed(ActionEvent arg0) {  //Gestion de l'appui sur les les cartes du joueur
			Bouton b=(Bouton)arg0.getSource();
			if (ecouteJoue){  //On a cliquer sur un bouton
				container.setBas(b.getPicture());
				container.setBas(true);
				b.setVisible(false);
				container.repaint();
				ecouteJoue=false;
				CartesPlateau.add(b.getCarteBouton());
				ActualiseRangAtoutMaitre();
				Joueurs[0][CarteToIndice(b.getCarteBouton(), 0)].setJouee(true);
			}
			else if (ecouteAnnonce){
				int ind1 = -1;
				int ind2 = -1;
				for (int i = 0; i<8; i++){
					if (Boutons.get(i).isEchange() && ind1==-1)
						ind1=i;
					else if (Boutons.get(i).isEchange())
						ind2=i;
				}
				if (ind1!=-1 && ind2!=-1){
					Carte temp = Joueurs[0][ind1];
					Joueurs[0][ind1] = Joueurs[0][ind2];
					Joueurs[0][ind2] = temp;
					List<Bouton> temp2 = new LinkedList<Bouton>();
					for (int i = 0; i<8; i++){	
						temp2.add(Boutons.get(i));
					}
					Boutons = new LinkedList<Bouton>();
					for (int i = 0; i<8; i++){	
						if (i == ind1){
							Boutons.add(temp2.get(ind2));
						}
						else if (i == ind2){
							Boutons.add(temp2.get(ind1));
						}
						else
							Boutons.add(temp2.get(i));
					}
					for (int i = 0; i<Boutons.size(); i++){
						Boutons.get(i).setEchange(false);
					}
				}
				for (int i = 0; i<Boutons.size(); i++){
					bas.remove(1);
				}
				for (int i = 0; i<Boutons.size(); i++){
					bas.add(Boutons.get(i));
					Boutons.get(i).repaint();
				}
				bas.repaint();
				//Ces deux lignes servent a forcer le programme à réactualiser la fenêtre
				this.setSize(1202, 720); 
				this.setSize(1202, 719);
			}
		} 
		/**
		 * Gestion de l'appui sur les boutons d'annonces
		 */
		class classique implements ActionListener{  //Annonces numériques
			private int valeur;
			private String couleur;
			public classique(int val, String coul){
				this.valeur=val;
				this.couleur = coul;
			}
		    public void actionPerformed(ActionEvent e) {
		    	AnnonceBas.setText(((valeur == 250) ? "Capot" : valeur) + " " + couleur);
		    	ecouteAnnonce=false;
		    	Annonce primaire = null;
		    	if (AnnonceJoueur.size()!=0){
		    		primaire = AnnonceJoueur.get(AnnonceJoueur.size()-1);
		    	}
		    	if (primaire == null || primaire.getJoueur() == 0 || couleur != primaire.getCouleur()){
		    		AnnonceGagnante=new Annonce(valeur, couleur, 0, 1);
		    		AnnonceJoueur.add(new Annonce(valeur, couleur, 0, 1));
		    	}
		    	else if (primaire.getNumero() == 1){
		    		AnnonceGagnante=new Annonce(valeur, couleur, 0, 2);
		    		AnnonceJoueur.add(new Annonce(valeur, couleur, 0, 2));
		    	}
		    	else{
		    		AnnonceGagnante=new Annonce(valeur, couleur, 0, 3);
		    		AnnonceJoueur.add(new Annonce(valeur, couleur, 0, 3));
		    	}
		    }
		 }
		
		class special implements ActionListener{  //Autres annonces (passe, contrée et surcontrée)
			private String valeur;
			public special(String val){
				this.valeur=val;
			}
		    public void actionPerformed(ActionEvent e) {
		    	AnnonceBas.setText(valeur);
		    	ecouteAnnonce=false;
		    	if (this.valeur == "Contrée!"){
		    		passe=0;
		    		AnnonceGagnante.setContre(true);
		    	}
		    	else if (this.valeur == "Sur-contreé!"){
		    		passe=0;
		    		AnnonceGagnante.setSurContre(true);
		    	}
		    	else{
		    		passe++;
		    	}
		    		
		    }
		 }
			
		public void Phase_annonces(){ //Fonction gérant les annonces entre le joueur et les IA
			passe = 0;
			int i=premier;  // Sert à gerer qui joue dans quel ordre
			AnnonceGagnante=new Annonce();
			annonceSpecial.get(1).setEnabled(false);  // On desactive Contré/Surcontré
			annonceSpecial.get(2).setEnabled(false);
			initialiseMainJoueur();  //Place les cartes dans les mains du joueurs et des IA
			initialiseMainIA();
			setCartes();
			while(passe<3 || AnnonceGagnante.getValeur() == 0){
				if (passe==4){   //Si tout le monde passe on redistribue
					passe=0;
					AnnonceBas.setText("---");
					AnnonceGauche.setText("---");
					AnnonceHaut.setText("---");
					AnnonceDroite.setText("---");
					distribue();
					premier=(premier+1)%4;
					i=premier;
				}
				if (i==0)
					annonceJoueur();
				else{
					Annonce(i);
					pause((int)(500/vitesse));
				}
				i=(i+1)%4;
			}
			AnnonceBas.setVisible(false); //On efface les annonces (l'annonce gagnante s'affiche alors en dessous des score)
			AnnonceGauche.setVisible(false);
			AnnonceHaut.setVisible(false);
			AnnonceDroite.setVisible(false);
			annonc.setVisible(false);  //On efface les boutons d'annonces
		}
		
		public void setCartes(){
			for (Carte c : Joueurs[1]){
				JoueurGauche.addCarte(c);
			}
			for (Carte c : Joueurs[2]){
				JoueurHaut.addCarte(c);
			}
			for (Carte c : Joueurs[3]){
				JoueurDroite.addCarte(c);
			}
		}
		
		public void Annonce(int numJoueur){  //Gestion des annonces des IA
			Annonce temp = AnnoncePrimaire(numJoueur);  //Annonce en fonction des cartes en main
			Annonce temp2 = AnnonceSecondaire(numJoueur); //Annonce qui répond à une annonce primaire du partenaire
			Annonce temp3 = AnnonceTertiaire(numJoueur); //Annonce qui prend en compte la réponse du partenaire
			Annonce finale = Max(temp, temp2, temp3,numJoueur);  //Meilleure annonce possible
			if (finale.getValeur()>=160)  // cas du capot
				finale.setValeur(250);
			if (numJoueur == 2){
				if (finale.getValeur() == 0){
					AnnonceHaut.setText("Passe");
					passe++;
				}
				else if (Integer.valueOf(finale.getValeur()) > Integer.valueOf(AnnonceGagnante.getValeur())){
					//On regarde si on peut monter par rapport à la meilleure annonce actuelle
					AnnonceJoueur.add(finale);
					AnnonceGagnante = finale;
					AnnonceHaut.setText((finale.getValeur() == 250) ? "Capot" : finale.getValeur() + 
							" " + finale.getCouleur());
					passe = 0;
				}
				else{
					AnnonceHaut.setText("Passe");
					passe++;
				}
			}
			else{
				if (numJoueur == 1){
					if (finale.getValeur() == 0){
						AnnonceGauche.setText("Passe");
						passe++;
					}
					else if (Integer.valueOf(finale.getValeur()) > 
							Integer.valueOf(AnnonceGagnante.getValeur())){
						AnnonceIA.add(finale);
						AnnonceGagnante = finale;
						AnnonceGauche.setText((finale.getValeur() == 250) ? "Capot" : 
							finale.getValeur() + " " + finale.getCouleur());
						passe = 0;
					}
					else{
						AnnonceGauche.setText("Passe");
						passe++;
					}
				}
				else{
					if (finale.getValeur() == 0){
						AnnonceDroite.setText("Passe");
						passe++;
					}
					else if (Integer.valueOf(finale.getValeur()) > 
							Integer.valueOf(AnnonceGagnante.getValeur())){
						AnnonceIA.add(finale);
						AnnonceGagnante = finale;
						AnnonceDroite.setText((finale.getValeur() == 250) ? "Capot" : finale.getValeur() + 
								" " + finale.getCouleur());
						passe = 0;
					}
					else{
						AnnonceDroite.setText("Passe");
						passe++;
					}
				}
			}
		}
		
		public Annonce Max(Annonce a1, Annonce a2, Annonce a3, int numJoueur){
			Annonce a = new Annonce(0, "", 0, 0);
			if (a1.getValeur()==250||a2.getValeur()==250||a3.getValeur()==250){
				return new Annonce(250, a1.getCouleur(), numJoueur, 1);
			}
			if (a1.getValeur() != 0){
				a=a1;
			}
			if (a2.getValeur() !=0){
				if (Integer.valueOf(a2.getValeur())>Integer.valueOf(a.getValeur())){
					a=a2;
				}
			}
			if (a3.getValeur() !=0){
				if (Integer.valueOf(a3.getValeur())>Integer.valueOf(a.getValeur())){
					a=a3;
				}
			}
			if (a.getValeur() == 0)
				return new Annonce(0, "", 0, 0);
			else{
				return a;
			}
		}
		
		// ----- Annonce Primaire -----
		
		public Annonce AnnoncePrimaire(int numJoueur){
			String[] couleur = {"coeur", "carreau", "pique", "trefle"};
			Annonce[] annonces = new Annonce[4];
			Carte[] main = new Carte[8];
			for (int i = 0; i<8; i++){
				main[i] = Joueurs[numJoueur][i];
			}
			for (int i = 0; i<4; i++){
				annonces[i] = CalculAnnonce(couleur[i], main, numJoueur);
			}
			Annonce finale = MeilleureAnnonce(annonces, numJoueur, main);
			return finale;
		}
		
		public Annonce CalculAnnonce(String couleur, Carte[] main, int numJoueur){
			List<Carte> mainAtout = new LinkedList<Carte>();
			for (int i = 0; i<8; i++){
				if (main[i].getCouleur() == couleur)
					mainAtout.add(main[i]);
			}
			Boolean cond80 = ((contient(mainAtout, new Carte("valet", couleur)) || 
					(contient(mainAtout, new Carte("9", couleur))))&& mainAtout.size() >=3);
			Boolean cond90 = (contient(mainAtout, new Carte("valet", couleur))) && 
					(contient(mainAtout, new Carte("9", couleur)));
			int nbAs = CompteAs(main);
			if (cond80){
				if(cond90){
					return new Annonce(90+10*nbAs, couleur, numJoueur, 1);
				}
				else
					return new Annonce(80, couleur, numJoueur, 1);
			}
			else
				return new Annonce(0, "", numJoueur, 1);
		}
		
		public int CompteAs(Carte[] main){
			int nbAs = 0;
			for (int i = 0; i<8; i++){
				if (main[i].getValeur() == "as"){
					nbAs++;
				}
			}
			return nbAs;
		}
		
		public Annonce MeilleureAnnonce(Annonce[] annonces, int numJoueur, Carte[] main){
			int valeurMax = 0;
			for (Annonce a : annonces){
				if (a.getValeur() == 250)
					return a;
				else if (a.getValeur() != 0){
					if (Integer.valueOf(a.getValeur()) > valeurMax){
						valeurMax = a.getValeur();
					}
				}
			}
			if (valeurMax == 0){
				return new Annonce(0, "", numJoueur, 1);
			}
			List<Annonce> annoncesMax = new LinkedList<Annonce>();
			List<Annonce> annoncesMax2 = new LinkedList<Annonce>();
			for (Annonce a : annonces){
				if (a.getValeur() == valeurMax){
					annoncesMax.add(a);
				}
			}
			if (annoncesMax.size() == 1){
				return annoncesMax.get(0);
			}
			else{
				int size = annoncesMax.size();
				for (int i = 0; i<size; i++){
					if(contient(main, new Carte("valet", annoncesMax.get(i).getCouleur()))){
						annoncesMax2.add(annoncesMax.get(i));
					}
				}
				if (annoncesMax2.size()==1)
					return annoncesMax2.get(0);
				else{
					if (annoncesMax2.size() != 0){
						if(contient(main, new Carte("as", annoncesMax2.get(0).getCouleur()))){
							return annoncesMax2.get(1);
						}
						else{
							return annoncesMax2.get(0);
						}
					}	
					else{
						if(contient(main, new Carte("as", annoncesMax.get(0).getCouleur()))){
							return annoncesMax.get(1);
						}
						else{
							return annoncesMax.get(0);
						}
					}
				}
			}
		}
		
		// ----- Annonce Secondaire -----
		
		public Annonce AnnonceSecondaire(int numJoueur){
			Carte[] main = new Carte[8];
			for (int i = 0; i<8; i++){
				main[i] = Joueurs[numJoueur][i];
			}
			if (numJoueur == 2){
				if (AnnonceJoueur.size() == 0){
					return new Annonce(0, "", 0, 0);
				}
				else if ((AnnonceJoueur.get(AnnonceJoueur.size()-1).getJoueur() != numJoueur )
						&& (AnnonceJoueur.get(AnnonceJoueur.size()-1).getNumero() == 1)){
							return Ajout(AnnonceJoueur.get(AnnonceJoueur.size()-1), main, numJoueur);
						}
				else{
					return new Annonce(0, "", 0, 0);
				}
			}
			else{
				if (AnnonceIA.size() == 0){
					return new Annonce(0, "", 0, 0);
				}
				else if ((AnnonceIA.get(AnnonceIA.size()-1).getJoueur() != 2 )
						&& (AnnonceIA.get(AnnonceIA.size()-1).getNumero() == 1)){
							return Ajout(AnnonceIA.get(AnnonceIA.size()-1), main, numJoueur);
						}
				else{
					return new Annonce(0, "", 0, 0);
				}
			}
		}
		
		public Annonce Ajout(Annonce primaire, Carte[] main, int numJoueur){
			Boolean complementaire = false;
			int ajout = 0;
			List<Carte> mainAtout = new LinkedList<Carte>();
			for (int i = 0; i<8; i++){
				if (main[i].getCouleur() == primaire.getCouleur())
					mainAtout.add(main[i]);
			}
			if (contient(main, new Carte("valet", primaire.getCouleur())) 
					|| (contient(main, new Carte("9", primaire.getCouleur())) && mainAtout.size()>=2)){
				complementaire = true;
			}
			int nbAs = 0;
			for (Carte c : main){
				if (c.getValeur() == "as" && c.getCouleur()!=primaire.getCouleur()){
					nbAs++;
				}
			}
			if (complementaire){
				ajout+=20+10*nbAs;
			}
			else if(Integer.valueOf(primaire.getValeur())>=90){
				ajout+=10*nbAs;
			}
			else{
				ajout+=(nbAs>=1) ? 10 : 0;
			}
			return new Annonce(AnnonceGagnante.getValeur() + 
					ajout, primaire.getCouleur(), numJoueur, 2, ajout);
		}
		
		// ----- Annonce Tertiaire -----
		
		public Annonce AnnonceTertiaire(int numJoueur){
			Carte[] main = new Carte[8];
			for (int i = 0; i<8; i++){
				main[i] = Joueurs[numJoueur][i];
			}
			if (numJoueur == 2){
				if (AnnonceJoueur.size() == 0){
					return new Annonce(0, "", 0, 0);
				}
				else if ((AnnonceJoueur.get(AnnonceJoueur.size()-1).getJoueur() != numJoueur )
						&& (AnnonceJoueur.get(AnnonceJoueur.size()-1).getNumero() == 1)){
							return Annonce3(AnnonceJoueur.get(AnnonceJoueur.size()-1), main, numJoueur);
						}
				else{
					return new Annonce(0, "", 0, 0);
				}
			}
			else{
				if (AnnonceIA.size() == 0){
					return new Annonce(0, "", 0, 0);
				}
				else if ((AnnonceIA.get(AnnonceIA.size()-1).getJoueur() != 2 )
						&& (AnnonceIA.get(AnnonceIA.size()-1).getNumero() == 1)){
							return Ajout(AnnonceIA.get(AnnonceIA.size()-1), main, numJoueur);
						}
				else{
					return new Annonce(0, "", 0, 0);
				}
			}
		}
		
		public Annonce Annonce3(Annonce derniere, Carte[] main, int numJoueur){
			Boolean maitrise = false;
			Annonce primaire;
			if (AnnonceJoueur.size()<=1){
				return new Annonce(0, "", 0, 0);
			}
			else{
				if (numJoueur == 2){
					primaire = AnnonceJoueur.get(AnnonceJoueur.size()-2);
				}
				else{
					primaire = AnnonceIA.get(AnnonceIA.size()-2);
				}
				if (Integer.valueOf(primaire.getValeur()) >= 90){
					maitrise = true;
				}
				else if(derniere.getAjout()>10){
					maitrise = true;
				}
				if (maitrise){
					int nbPli = 0;
					for (int i = 0; i<8; i++){
						if (main[i].getCouleur() == derniere.getCouleur()){
							nbPli+=1;
						}
						String[] couleur = {"coeur", "carreau", "pique", "trefle"};
						for (String c : couleur){
							if (c != derniere.getCouleur()){
								int rang = 0;
								while (contient(main, new Carte(OrdreCartesNonAtout[rang], c))){
									nbPli++;
									rang++;
								}
							}
						}
					}
					if (nbPli == 8)
						return new Annonce(250, derniere.getCouleur(), numJoueur, 3);
					else
						return new Annonce(20*nbPli, derniere.getCouleur(), numJoueur, 3);
				}
				else
					return new Annonce(0, "", 0, 0);
			}
		}
				
		public void initialiseAnnonces(){ //Initialise tous les boutons d'annonces
			
			JPanel centre = new JPanel();
			GridLayout grille = new GridLayout(4,12,5,5);
			centre.setLayout(grille);
			
			Font police = new Font("Tahoma", Font.BOLD, 11); //Font pour les CAPOT
			
			// ----Coeur-----
			for (int i = 0; i<11; i++){
				annonceCoeur.add(new BoutonAnnonces(Integer.toString(80+10*i), "coeur"));
				annonceCoeur.get(i).addActionListener(new classique(80+i*10, "coeur"));
				centre.add(annonceCoeur.get(i));
			}
			BoutonAnnonces coeurCAPOT = new BoutonAnnonces("CAPOT", "coeur");
			coeurCAPOT.setFont(police);
			centre.add(coeurCAPOT);
			coeurCAPOT.addActionListener(new classique(250, "coeur"));
			annonceCoeur.add(coeurCAPOT);
			
			// ----Pique-----
			for (int i = 0; i<11; i++){
				annoncePique.add(new BoutonAnnonces(Integer.toString(80+10*i), "pique"));
				annoncePique.get(i).addActionListener(new classique(80+i*10, "pique"));
				centre.add(annoncePique.get(i));
			}
			BoutonAnnonces piqueCAPOT = new BoutonAnnonces("CAPOT", "pique");
			piqueCAPOT.setFont(police);
			centre.add(piqueCAPOT);
			piqueCAPOT.addActionListener(new classique(250, "pique"));
			annoncePique.add(piqueCAPOT);
			
			// ----Carreau-----
			for (int i = 0; i<11; i++){
				annonceCarreau.add(new BoutonAnnonces(Integer.toString(80+10*i), "carreau"));
				annonceCarreau.get(i).addActionListener(new classique(80+i*10, "carreau"));
				centre.add(annonceCarreau.get(i));
			}
			BoutonAnnonces carreauCAPOT = new BoutonAnnonces("CAPOT", "carreau");
			carreauCAPOT.setFont(police);
			centre.add(carreauCAPOT);
			carreauCAPOT.addActionListener(new classique(250, "carreau"));
			annonceCarreau.add(carreauCAPOT);
			
			// ----Trefle-----
			for (int i = 0; i<11; i++){
				annonceTrefle.add(new BoutonAnnonces(Integer.toString(80+10*i), "trefle"));
				annonceTrefle.get(i).addActionListener(new classique(80+i*10, "trefle"));
				centre.add(annonceTrefle.get(i));
			}
			BoutonAnnonces trefleCAPOT = new BoutonAnnonces("CAPOT", "trefle");
			trefleCAPOT.setFont(police);
			centre.add(trefleCAPOT);
			trefleCAPOT.addActionListener(new classique(250, "trefle"));
			annonceTrefle.add(trefleCAPOT);

			Font police2 = new Font("Tahoma", Font.BOLD, 30); //Font pour les annonces speciales
			
			// ----Autres-----
			JPanel special = new JPanel();
			special.setLayout(new GridLayout(1,3,5,5));
			JButton passe = new JButton("Passe");
			JButton Contre = new JButton("Contrée !");
			JButton Surcontre = new JButton("Surcontrée !");
			passe.setPreferredSize(new Dimension(100,100));
			Contre.setPreferredSize(new Dimension(100,100));
			Surcontre.setPreferredSize(new Dimension(100,100));
			annonceSpecial.add(passe);
			annonceSpecial.add(Contre);
			annonceSpecial.add(Surcontre);
			passe.setFont(police2);
			Contre.setFont(police2);
			Surcontre.setFont(police2);
			
			special.add(passe);
			special.add(Contre);
			special.add(Surcontre);
			passe.addActionListener(new special("Passe"));
			Contre.addActionListener(new special("Contrée!"));
			Surcontre.addActionListener(new special("Sur-contreé!"));
			
			centre.setOpaque(false);
			special.setOpaque(false);
			
			annonc = new JPanel();
			annonc.setLayout(new BorderLayout());
			annonc.add(centre, BorderLayout.CENTER);
			annonc.add(special, BorderLayout.SOUTH);
			annonc.setOpaque(false);
			
			bas.add(AnnonceBas, 0);
			gauche.add(AnnonceGauche, 0);
			haut.add(AnnonceHaut, 0);
			droite.add(AnnonceDroite, 0);
			
			container.add(annonc, BorderLayout.CENTER);
			
			for (int i = 0; i<12; i++){          //On désactive tous les boutons      
				annonceCoeur.get(i).setEnabled(false);
				annoncePique.get(i).setEnabled(false);
				annonceCarreau.get(i).setEnabled(false);
				annonceTrefle.get(i).setEnabled(false);
			}
			annonceSpecial.get(0).setEnabled(false);
			annonceSpecial.get(1).setEnabled(false);
			annonceSpecial.get(2).setEnabled(false);
			
			annonc.getComponent(0).setVisible(false);
			JPanel temp = (JPanel)annonc.getComponent(1);
			temp.getComponent(0).setVisible(false);
			
			this.setContentPane(container);
		    this.setVisible(true);
		}
		
		public void annonceJoueur(){
			ecouteAnnonce = true;
			annonc.getComponent(0).setVisible(true);
			JPanel temp = (JPanel)annonc.getComponent(1);
			temp.getComponent(0).setVisible(true);
			if (!AnnonceGagnante.getContre()){        // On active les boutons
				int j;
				if (AnnonceGagnante.getValeur() == 0)
					j=0;
				else
					j = (AnnonceGagnante.getValeur() - 80)/10+1;
				for (int i = j; i<12; i++){              
					annonceCoeur.get(i).setEnabled(true);
					annoncePique.get(i).setEnabled(true);
					annonceCarreau.get(i).setEnabled(true);
					annonceTrefle.get(i).setEnabled(true);
				}
			}
			annonceSpecial.get(0).setEnabled(true);
			annonceSpecial.get(1).setEnabled(true);
			annonceSpecial.get(2).setEnabled(true);
			if ((AnnonceGagnante.getJoueur() == 0 || AnnonceGagnante.getJoueur() == 2) || 
					AnnonceGagnante.getContre() || AnnonceGagnante.getValeur() == 0)
				annonceSpecial.get(1).setEnabled(false);
			if ((AnnonceGagnante.getJoueur() == 1 || AnnonceGagnante.getJoueur() == 3) || 
					!AnnonceGagnante.getContre() || AnnonceGagnante.getSurContre())
				annonceSpecial.get(2).setEnabled(false);
			
			while(ecouteAnnonce){  // On attend l'appui sur un bouton
				pause(10);
			}
			
			for (int i = 0; i<12; i++){   // On désactive les boutons
				annonceCoeur.get(i).setEnabled(false);
				annoncePique.get(i).setEnabled(false);
				annonceCarreau.get(i).setEnabled(false);
				annonceTrefle.get(i).setEnabled(false);
			}
			annonceSpecial.get(0).setEnabled(false);
			if ((AnnonceGagnante.getJoueur() == 0 || AnnonceGagnante.getJoueur() == 2) || 
					AnnonceGagnante.getValeur() == 0 || AnnonceGagnante.getContre())
				annonceSpecial.get(1).setEnabled(false);
			else
				annonceSpecial.get(1).setEnabled(true);
			if ((AnnonceGagnante.getJoueur() == 1 || AnnonceGagnante.getJoueur() == 3) || 
					!AnnonceGagnante.getContre() || AnnonceGagnante.getSurContre())
				annonceSpecial.get(2).setEnabled(false);
			else
				annonceSpecial.get(2).setEnabled(true);
			annonc.getComponent(0).setVisible(false);
			temp.getComponent(0).setVisible(false);
			pause((int)(500/vitesse));
		}
		
		public void initialise(){  //Création de toutes les cartes
			Liste_Cartes.add(new Carte("7", "coeur"));
			Liste_Cartes.add(new Carte("8", "coeur"));
			Liste_Cartes.add(new Carte("9", "coeur"));
			Liste_Cartes.add(new Carte("10", "coeur"));
			Liste_Cartes.add(new Carte("valet", "coeur"));
			Liste_Cartes.add(new Carte("dame", "coeur"));
			Liste_Cartes.add(new Carte("roi", "coeur"));
			Liste_Cartes.add(new Carte("as", "coeur"));
			Liste_Cartes.add(new Carte("7", "pique"));
			Liste_Cartes.add(new Carte("8", "pique"));
			Liste_Cartes.add(new Carte("9", "pique"));
			Liste_Cartes.add(new Carte("10", "pique"));
			Liste_Cartes.add(new Carte("valet", "pique"));
			Liste_Cartes.add(new Carte("dame", "pique"));
			Liste_Cartes.add(new Carte("roi", "pique"));
			Liste_Cartes.add(new Carte("as", "pique"));
			Liste_Cartes.add(new Carte("7", "carreau"));
			Liste_Cartes.add(new Carte("8", "carreau"));
			Liste_Cartes.add(new Carte("9", "carreau"));
			Liste_Cartes.add(new Carte("10", "carreau"));
			Liste_Cartes.add(new Carte("valet", "carreau"));
			Liste_Cartes.add(new Carte("dame", "carreau"));
			Liste_Cartes.add(new Carte("roi", "carreau"));
			Liste_Cartes.add(new Carte("as", "carreau"));
			Liste_Cartes.add(new Carte("7", "trefle"));
			Liste_Cartes.add(new Carte("8", "trefle"));
			Liste_Cartes.add(new Carte("9", "trefle"));
			Liste_Cartes.add(new Carte("10", "trefle"));
			Liste_Cartes.add(new Carte("valet", "trefle"));
			Liste_Cartes.add(new Carte("dame", "trefle"));
			Liste_Cartes.add(new Carte("roi", "trefle"));
			Liste_Cartes.add(new Carte("as", "trefle"));
		}
		
		public void distribue(){
			
			int quiRecoit = premier ;
			int nbCartes = 2 ;
			int nbDonne = 0 ;
			for (int j = 0; j<12 ; j++){
				for (int i = 0 ; i<nbCartes ; i++){
					Joueurs[quiRecoit][i+nbDonne] = Liste_Cartes.get(0) ;
					Liste_Cartes.remove(0) ;
				}
				quiRecoit = (quiRecoit+1)%4 ;
				if (j == 3 || j == 7){ nbDonne = nbDonne + nbCartes ; nbCartes = 3 ; }
			}
		}
		
		public void shuffle(){
			int taille = Liste_Cartes.size();
			List<Carte> provisoire = new LinkedList<Carte>();
			for (int j = 0; j<32 ; j++){
				int num = (int)(Math.random()*(double)taille);
				provisoire.add(Liste_Cartes.get(num));
				Liste_Cartes.remove(num);
				taille--;
			}
			Liste_Cartes = provisoire ;
		}
				
		public void cut(){
			List<Carte> provisoire1 = new LinkedList<Carte>();
			List<Carte> provisoire2 = new LinkedList<Carte>();
			int num = (int)(Math.random()*32);
			for (int i = 0; i < num ; i++){
				provisoire1.add(Liste_Cartes.get(0));
				Liste_Cartes.remove(0);
			}
			for (int i = num+1; i < 32 ; i++){
				provisoire2.add(Liste_Cartes.get(0));
				Liste_Cartes.remove(0);
			}
			for (int i = 0; i < provisoire1.size(); i++){
				Liste_Cartes.add(provisoire1.get(i));
			}
			for (int i = 0; i < provisoire2.size() ; i++){
				Liste_Cartes.add(provisoire2.get(i));
			}
		}
		
		public int value(Carte test){
			Atout = AnnonceGagnante.getCouleur();
			if (test.getValeur() == "as" )
				return 11 ;
			if (test.getValeur() == "roi")
				return 4 ;
			if (test.getValeur() == "dame")
				return 3 ;
			if (test.getValeur() == "10")
				return 10 ;
			if (test.getValeur() == "valet" && Atout == test.getCouleur())
				return 20 ;
			if (test.getValeur() == "9" && Atout == test.getCouleur())
				return 14 ;
			if (test.getValeur() == "valet" && Atout != test.getCouleur())
				return 2 ; 
			return 0 ;
		}
		
		public void trier(){  //Fonctionne sur la base du tri rapide aussi appelé tri à bulles
			for (int i = 0; i<4; i++){
				boolean trie  =false; //indique si les cartes sont triées
				while (!trie){
					trie = true;
					for (int j = 0; j<7; j++){
						if (Joueurs[i][j].getValeurTri()>Joueurs[i][j+1].getValeurTri()){
							Carte temp = Joueurs[i][j];
							Joueurs[i][j] = Joueurs[i][j+1];
							Joueurs[i][j+1] = temp;
							trie = false;
						}
					}
				}
			}
		}
		
		public void initialiseMainJoueur(){ //place les cartes dans la main du joueur
			
			for (int i = 0; i<Boutons.size(); i++){
				bas.remove(1);
			}
				
				Boutons = new LinkedList<Bouton>();
				
				for (int i = 0; i<8; i++){
					Bouton b = new Bouton(Joueurs[0][i]);
					Boutons.add(b);
				}
				for (int i = 0; i<Boutons.size(); i++){
					bas.add(Boutons.get(i));
					Boutons.get(i).addActionListener(this);
					Boutons.get(i).setPreferredSize(new Dimension(89, 130));
				}   
				
				bas.repaint();
		}
		
		public void initialiseMainIA(){ //place les cartes dans les mains des IA
			//On supprime tous les boutons liés aux cartes de la partie précédente
			for (int i = 0; i<IAgauche.getNumberCartes(); i++){  
				haut.remove(1);
				gauche.remove(1);
				droite.remove(1);
			}
			
			gauche.setLayout(new GridLayout(9, 1));
			droite.setLayout(new GridLayout(9, 1));
			
			for (int i =0; i<8; i++){
				IAgauche.addCarte(new CarteIA(Joueurs[1][i]));
				gauche.add(IAgauche.getCarte(i));
				IAgauche.getCarte(i).setPreferredSize(new Dimension(130,89));
				IAhaut.addCarte(new CarteIA(Joueurs[2][i]));
				haut.add(IAhaut.getCarte(i));
				IAhaut.getCarte(i).setPreferredSize(new Dimension(89,130));
				IAdroite.addCarte(new CarteIA(Joueurs[3][i]));
				droite.add(IAdroite.getCarte(i));
			    IAdroite.getCarte(i).setPreferredSize(new Dimension(130,89));
			}
			gauche.repaint();
			haut.repaint();
			droite.repaint();
		}
		
		public void play_joueur(){
			setCartesJouables();
			ecouteJoue = true;
		    while(ecouteJoue){    //On attend que le joueur joue
		    	pause(5);
		    }
			pause((int)(500/vitesse));
		}
		
		public void setCartesJouables(){  //On interdit l'appui sur les cartes interdites
			for (Bouton b : Boutons)  //On ré-autorise tout dans un premier temps
				b.setEnabled(true);
			List<Carte> cartesJouables = new LinkedList<Carte>();
			for (Carte EnMain : Joueurs[0])
				if (!EnMain.isJouee())
					cartesJouables.add(EnMain);
			if (CartesPlateau.size() != 0){  //Si on est le premier a jouer, on joue ce qu'on veut
				Carte actuelle = CartesPlateau.get(0);
				if (actuelle.getCouleur() == Atout){  //On demande de l'atout
					boolean jePeuxJouer = false;
					boolean jaiDeLAtout = false;  //Indique si le jouer a de l'atout
					for (Carte EnMain : cartesJouables){
						if (EnMain.getCouleur().equals(Atout)){
							jaiDeLAtout = true;
						}
					}
					if (jaiDeLAtout){  //Si le joueur n'as pas d'atout il joue ce qu'il veut
						Carte PlusForte = CartesPlateau.get(0); //Meilleur atout en jeu
						for (Carte plat : CartesPlateau)   //On détermine le meilleur atout
							if (plat.getRangAtout() < PlusForte.getRangAtout() && plat.getCouleur() == Atout)
								PlusForte = plat;
						for (Carte EnMain : cartesJouables)
							//Peut-on monter à l'atout ?
							if (EnMain.getCouleur() == Atout && (EnMain.getRangAtout() 
									< PlusForte.getRangAtout())){
								jePeuxJouer = true;
							}
						//On désactive (enfin) les boutons des cartes interdites
						for (int i = 0; i<cartesJouables.size(); i++){  
							if (cartesJouables.get(i).getCouleur() != Atout || 
									(jePeuxJouer && cartesJouables.get(i).getRangAtout() 
									> actuelle.getRangAtout())){
								Boutons.get(CarteToIndice(cartesJouables.get(i), 0)).setEnabled(false);
							}
						}
					}
				}
				else{  //On ne demande pas d'atout
					boolean jaiLaCouleur = false;
					for (Carte EnMain : cartesJouables){
						if (EnMain.getCouleur() == actuelle.getCouleur())
							jaiLaCouleur = true;
					}
					if (jaiLaCouleur){
						//On désactive les boutons des cartes interdites
						for (int i = 0; i<cartesJouables.size(); i++){  
							if (cartesJouables.get(i).getCouleur() != actuelle.getCouleur()){
								Boutons.get(CarteToIndice(cartesJouables.get(i), 0)).setEnabled(false);
							}
						}
					}
					else{
						boolean jePeuxJouer = false;
						boolean jaiDeLAtout = false;  //Indique si le jouer a de l'atout
						for (Carte EnMain : cartesJouables)
							if (EnMain.getCouleur().equals(Atout))
								jaiDeLAtout = true;
						if (jaiDeLAtout){  //Si le joueur n'as pas d'atout il joue ce qu'il veut
							Carte PlusForte = new Carte("temp", "temp"); //Meilleur atout en jeu
							PlusForte.setRangAtout(50);
							for (Carte plat : CartesPlateau)   //On détermine le meilleur atout
								if (plat.getRangAtout() < PlusForte.getRangAtout() 
										&& plat.getCouleur().equals(Atout)){
									PlusForte = plat;
								}
							if (PlusForte.getRangAtout() == 50){
								//On désactive les boutons des cartes interdites
								for (int i = 0; i<cartesJouables.size(); i++){  
									if (cartesJouables.get(i).getCouleur() != Atout){
										Boutons.get(CarteToIndice(cartesJouables.get(i), 0)).setEnabled(false);
									}
								}
							}
							else{
								for (Carte EnMain : cartesJouables)
									//Peut-on monter à l'atout ?
									if (EnMain.getCouleur() == Atout && (EnMain.getRangAtout() 
											< PlusForte.getRangAtout())){
										jePeuxJouer = true;
									}
								//On désactive (enfin) les boutons des cartes interdites
								for (int i = 0; i<cartesJouables.size(); i++){  
									if (Joueurs[0][i].getCouleur() != Atout || 
											(jePeuxJouer && Joueurs[0][i].getRangAtout() 
											> actuelle.getRangAtout())){
										Boutons.get(CarteToIndice(cartesJouables.get(i), 0)).setEnabled(false);
									}
								}
							}
						}
					}
				}
			}
		}
		
		public int checkGagnant(){ // Renvoie le numero du gagnant du pli
			boolean atout = false;
			int gagnante = 0;
			String couleurDemandee = CartesPlateau.get(0).getCouleur();
			for (int i = 0; i<4; i++){
				if (CartesPlateau.get(i).getCouleur() == Atout || atout){
					atout = true;
					if ((CartesPlateau.get(i).getRangAtout() < CartesPlateau.get(gagnante).getRangAtout()
							&& CartesPlateau.get(i).getCouleur() == Atout)
								|| (CartesPlateau.get(i).getCouleur() == Atout
									&&  CartesPlateau.get(gagnante).getCouleur() != Atout))
						gagnante = i;
				}
				else{  //Pas d'atout sur le plateau
					if (CartesPlateau.get(i).getCouleur() == couleurDemandee &&
							CartesPlateau.get(i).getRangNonAtout() 
							< CartesPlateau.get(gagnante).getRangNonAtout()
							&& !atout)
					{
						gagnante = i;
					}
						
				}
					
			}
			return gagnante;
		}
		
		public int CarteToIndice(Carte Carte, int NumJoueur){ // Nous donne l'indice d'une carte dans une certaine main d'un joueur
			int indice = 0 ;
			for (int i =0 ; i < 8 ; i++){
				if (Joueurs[NumJoueur][i].isequal(Carte)){
					indice = i;
				}
			}
			return indice;
		}
		
		public void play_IA(int q){  
			// Parmis les cartes en main, les cartes que l'on peu jouer dans les règles
			List<Carte> CartesJouables = new LinkedList<Carte>();  
			int choix = -1;	// Indice de la carte que l'on va jouer						
			// Carte que l'on va bientot jouer 
			Carte CarteBientotJouee = new Carte("ProblemeValeur","ProblemeCouleur");  
			if (CartesPlateau.size() != 0){							// Est-on le premier à jouer?
				String CouleurDemandee = CartesPlateau.get(0).getCouleur();	// Couleur demandée du pli
				if (CouleurDemandee == Atout){
					for (int k = 0 ; k < 8 ; k++){		
						/* Si atout demandé, les cartes jouables sont les cartes 
						 * d'atout meilleures que la meilleure carte atout  jouée */
						if (Joueurs[q][k].getCouleur() == CouleurDemandee && 
								Joueurs[q][k].getRangAtout() < RangAtoutMaitre && Joueurs[q][k].isJouee() == false){
							CartesJouables.add(Joueurs[q][k]);
						}
					}
					// Si on peut monter à l'atout et que l'on a la carte maitre à l'atout
					if (CartesJouables.size() !=0) {	
						for (Carte Carte : CartesJouables){
							if (CarteAtoutMaitre.isequal(Carte)){
								choix = CarteToIndice(Carte,q);
							}
							
						}
						// Si on peut monter mais que l'on n'a pas la carte maitre à l'atout
						if (choix == -1){			
							int IndiceJouable = 0;
							for (int i = 0 ; i < CartesJouables.size(); i++){
								if (CartesJouables.get(i).getRangAtout() > 
										CartesJouables.get(IndiceJouable).getRangAtout()){
									IndiceJouable = i ;
								}
									
							}
							choix = CarteToIndice(CartesJouables.get(IndiceJouable),q) ;
						}
					}
					else {	// Si on ne peut pas monter à l'atout, on joue le plus petit 
						for (int k = 0 ; k < 8 ; k++){
							if (Joueurs[q][k].getCouleur() == CouleurDemandee && Joueurs[q][k].isJouee() == false){
								CartesJouables.add(Joueurs[q][k]);
							}
						}
						// Si on a de l'atout mais que l'on ne peut pas monter, on joue le plus petit
						if (CartesJouables.size() !=0) { 
							int IndiceJouable = 0;
							for (int i = 0 ; i < CartesJouables.size(); i++){
								if (CartesJouables.get(i).getRangAtout() > CartesJouables.get(IndiceJouable).getRangAtout()){
									IndiceJouable = i ;
								}
								
							}
							choix = CarteToIndice(CartesJouables.get(IndiceJouable),q) ;
						}
						else {							// On n'a pas d'atout 
							for (int k = 0 ; k < 8 ; k++){
								if (Joueurs[q][k].isJouee() == false){
									CartesJouables.add(Joueurs[q][k]);
								}
							}
							int IndiceJouable = 0;		// On joue la carte la plus faible 
							for (int i = 0 ; i < CartesJouables.size(); i++){
								if (CartesJouables.get(i).getRangNonAtout() > CartesJouables.get(IndiceJouable).getRangNonAtout()) {
									IndiceJouable = i ;
								}
								
							}
							choix = CarteToIndice(CartesJouables.get(IndiceJouable),q) ;
							
						}
						
					}
				}
				else {								// Non Atout demandé 
					for (int k = 0 ; k < 8 ; k++){	// Cartes jouables sont les cartes de la couleur demandée
						if (Joueurs[q][k].getCouleur() == CouleurDemandee && Joueurs[q][k].isJouee() == false){
							CartesJouables.add(Joueurs[q][k]);
						}
					}
					if (CartesJouables.size() != 0) { // On pose la carte maittre si on l'atout
						for (Carte carte : CartesMaitres){
							if (contient(CartesJouables, carte)){
								CarteBientotJouee = carte;
							}
						}
						if (CarteBientotJouee.getValeur() != "ProblemeValeur"){
							choix = CarteToIndice(CarteBientotJouee,q);
						}
						else {						// On met la carte la ple faible de la couleur demandée
							int IndiceJouable = 0;
							for (int i = 0 ; i < CartesJouables.size(); i++){
								if (CartesJouables.get(i).getRangNonAtout() > 
										CartesJouables.get(IndiceJouable).getRangNonAtout()) {
									IndiceJouable = i ;
								}
							}
							CarteBientotJouee = CartesJouables.get(IndiceJouable);
							choix = CarteToIndice(CarteBientotJouee,q);
						}
					}
					/* Si on n's pas de la couleur demande, on doit couper, avec un atout 
					 * plus grag que tous les atouts dans le pli
					 */
					else{						
						for (int k = 0 ; k < 8 ; k++){
							if (Joueurs[q][k].getCouleur() == Atout && Joueurs[q][k].getRangAtout() < RangAtoutMaitre 
									&& Joueurs[q][k].isJouee() == false){
								CartesJouables.add(Joueurs[q][k]);
							}
						}
						if (CartesJouables.size() != 0){ 
							int IndiceJouable = 0;
							for (int i = 0 ; i < CartesJouables.size(); i++){
								if (CartesJouables.get(i).getRangAtout() > 
										CartesJouables.get(IndiceJouable).getRangAtout()) {
									IndiceJouable = i ;
								}
							}
							CarteBientotJouee = CartesJouables.get(IndiceJouable);
							choix = CarteToIndice(CarteBientotJouee,q);
						}
						else {				// Sinon on sous-coupe 
							for (int k = 0 ; k < 8 ; k++){
								if (Joueurs[q][k].getCouleur() == Atout && Joueurs[q][k].isJouee() == false){
									CartesJouables.add(Joueurs[q][k]);
								}
							}
							if (CartesJouables.size() != 0){
								int IndiceJouable = 0;
								for (int i = 0 ; i < CartesJouables.size(); i++){
									if (CartesJouables.get(i).getRangAtout() > CartesJouables.get(IndiceJouable).getRangAtout()) {
										IndiceJouable = i ;
									}
								}
								CarteBientotJouee = CartesJouables.get(IndiceJouable);
								choix = CarteToIndice(CarteBientotJouee,q);
							}
							else { 				// Si on n'a pas d'aout, on pose une petite carte
								for (int k = 0 ; k < 8 ; k++){
									if (Joueurs[q][k].isJouee() == false){
										CartesJouables.add(Joueurs[q][k]);
									}
								}
								int IndiceJouable = 0;
								for (int i = 0 ; i < CartesJouables.size(); i++){
									if (CartesJouables.get(i).getRangNonAtout() > 
											CartesJouables.get(IndiceJouable).getRangNonAtout()) {
										IndiceJouable = i ;
									}
								}
								CarteBientotJouee = CartesJouables.get(IndiceJouable);
								choix = CarteToIndice(CarteBientotJouee,q);
							}
							
						}
					}

				}

			}
			else { 											// Premier à jouer
				int nbAtout = CompteAtout(q);    //atouts joues + en main
				for (int k = 0 ; k < 8 ; k++){
					if (Joueurs[q][k].isJouee() == false){
						CartesJouables.add(Joueurs[q][k]);
					}
				}
				// Si tous les atouts autres que ceux que nous avons ne sont pas tombés
				if (nbAtout != 8){			
					for (Carte Carte : CartesJouables){ // On met sa carte maitre d'atout 
						if (CarteAtoutMaitre.isequal(Carte)){
							choix = CarteToIndice(Carte,q);
						}
					}
					if (choix == -1){		// On met ses autres cartes maitres
						for (Carte carte : CartesMaitres){
							if (contient(CartesJouables, carte)){
								CarteBientotJouee = carte;
								choix = CarteToIndice(CarteBientotJouee,q);
							}
						}
					}
					if (choix == -1) { 		// On met des petites cartes
						int IndiceJouable = 0;
						for (int i = 0 ; i < CartesJouables.size(); i++){
							if (CartesJouables.get(i).getRangNonAtout() > 
									CartesJouables.get(IndiceJouable).getRangNonAtout()) {
								IndiceJouable = i ;
							}
						}
						CarteBientotJouee = CartesJouables.get(IndiceJouable);
						choix = CarteToIndice(CarteBientotJouee,q);
					}

				}
				else {			// Tous les atouts qui restent sont dans notre main
					if (choix == -1){
						for (Carte carte : CartesMaitres){ // On met nos cartes maitres
							if (contient(CartesJouables, carte)){
								CarteBientotJouee = carte;
								choix = CarteToIndice(CarteBientotJouee,q);
							}
						}
					}
					if (choix == -1) {		// On met des peites cartes
						int IndiceJouable = 0;
						for (int i = 0 ; i < CartesJouables.size(); i++){
							if (CartesJouables.get(i).getRangNonAtout() > 
									CartesJouables.get(IndiceJouable).getRangNonAtout()) {
								IndiceJouable = i ;
							}
						}
						CarteBientotJouee = CartesJouables.get(IndiceJouable);
						choix = CarteToIndice(CarteBientotJouee,q);
					}
					if (choix == -1) { 		// On met nos atous 
						for (Carte Carte : CartesJouables){
							if (CarteAtoutMaitre.isequal(Carte)){
								choix = CarteToIndice(Carte,q);
							}
						}
					}
				}
			}
			if (Joueurs[q][choix].isJouee() == true){  // Test
				throw new IllegalStateException("La carte " + Joueurs[q][choix].toString() + " a déjà étée jouée pour le joueur " + q);
			}
			Joueurs[q][choix].setJouee(true);

			if (q==1){ // On met a jour les affichages en fonction du joueur
				container.setGauche(Joueurs[q][choix].getPicture(1));
				CartesPlateau.add(Joueurs[q][choix]);
				container.setGauche(true);
				IAgauche.getCarte(choix).setVisible(false);
			}
			
			else if (q==2){
				container.setHaut(Joueurs[q][choix].getPicture(0));
				CartesPlateau.add(Joueurs[q][choix]);
				container.setHaut(true);
				IAhaut.getCarte(choix).setVisible(false);
			}
			else if (q==3){
				container.setDroite(Joueurs[q][choix].getPicture(1));
				CartesPlateau.add(Joueurs[q][choix]);
				container.setDroite(true);
				IAdroite.getCarte(choix).setVisible(false);
			}
			gauche.repaint();
			container.repaint();
			pause((int)(500/vitesse));
		}
		
		public int CompteAtout(int numJoueur){ // Compte les atouts tombées + ceux en main
			int nb = 0;
			for (Carte c : CartesTombees){
				if (c.getCouleur() == Atout){
					nb++;
				}
			}
			for (Carte c : Joueurs[numJoueur]){
				if (c.getCouleur() == Atout && c.isJouee() == false){
					nb++;
				}
			}
			return nb;
		}
		
		private void ActualiseRangAtoutMaitre() {	// Le rang du meilleur atout du pli actuel
			RangAtoutMaitre = 8;
			for (Carte i : CartesPlateau){
				if (i.getCouleur() == Atout && i.getRangAtout() < RangAtoutMaitre)
					RangAtoutMaitre = i.getRangAtout();
			}
		}
		
		public void reboot(){
			for (int i = 0; i<8; i++){
				IAgauche.getCarte(i).setVisible(true);
				IAhaut.getCarte(i).setVisible(true);
				IAdroite.getCarte(i).setVisible(true);
			}
			for (Bouton b : Boutons)
				b.setVisible(true);
			
			for (int i = 0; i<4; i++)
				for (int j = 0; j<8; j++)
					Joueurs[i][j].setJouee(false);
			
			AnnonceBas.setText("---");
			AnnonceGauche.setText("---");
			AnnonceHaut.setText("---");
			AnnonceDroite.setText("---");
			
			AnnonceBas.setVisible(true);
			AnnonceGauche.setVisible(true);
			AnnonceHaut.setVisible(true);
			AnnonceDroite.setVisible(true);
			
			annonc.setVisible(true);
		}

		public static Instance getPartieEnCours() {
			return PartieEnCours;
		}

		public static void setPartieEnCours(Instance partieEnCours) {
			PartieEnCours = partieEnCours;
		}

		public static String getNom(int num) {
			return noms[num];
		}

		public static void setNom(int num, String nouveau) {
			Fenetre.noms[num] = nouveau;
		}

		public static Annonce getAnnonceGagnante() {
			return AnnonceGagnante;
		}

		public static void setAnnonceGagnante(Annonce annonceGagnante) {
			AnnonceGagnante = annonceGagnante;
		}

		public static int getScoreIAProvisoire() {
			return ScoreIAProvisoire;
		}

		public static void setScoreIAProvisoire(int scoreIAProvisoire) {
			ScoreIAProvisoire = scoreIAProvisoire;
		}

		public static int getScoreJoueurProvisoire() {
			return ScoreJoueurProvisoire;
		}

		public static void setScoreJoueurProvisoire(int scoreJoueurProvisoire) {
			ScoreJoueurProvisoire = scoreJoueurProvisoire;
		}

		public static int getScoreJoueur() {
			return ScoreJoueur;
		}

		public static void setScoreJoueur(int scoreJoueur) {
			ScoreJoueur = scoreJoueur;
		}

		public static int getScoreIA() {
			return ScoreIA;
		}

		public static void setScoreIA(int scoreIA) {
			ScoreIA = scoreIA;
		}

		public static int getWinScore() {
			return WinScore;
		}

		public static void setWinScore(int winScore) {
			WinScore = winScore;
		}

		public static String getAtout() {
			return Atout;
		}

		public static void setAtout(String atout) {
			Atout = atout;
		}

		public static Boolean getMasquer() {
			return masquer;
		}

		public static void setMasquer(Boolean masquer) {
			Fenetre.masquer = masquer;
		}
		
		
		// Le MAIN
		public static void main(String[] args) {
			PartieEnCours.start();  //On lance un thread Instance de partie
		}
}
