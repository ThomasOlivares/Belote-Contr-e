package fenetre;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToolBar;

/**
 * Cette version est un prototype.
 * Les cartes des adversaires sont visible pour l'instant
 * car elles nous sont necessaires pour tester notre code, 
 * elles ne seront bien sur pas visibles pour l'utilisateur
 * sur la version définitive.
 * @authors Thomas, Meric, Paul, Martin
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
	private JMenuItem item6 = new JMenuItem("Item");
	private static Instance PartieEnCours = new Instance("MenuPrincipal");
	private Boolean go = false;
	public static String[] noms = {"Paul", "Martin", "Méric", "Thomas"};
	
	//---------  Variables de la phase d'annonce -----------
	private boolean ecouteAnnonce = false; //Indique si c'est au tour du joueur d'annoncer
	private List<BoutonAnnonces> annonceCoeur = new LinkedList<BoutonAnnonces>();   //Ces listes servent a ordonner les boutons d'annonces
	private List<BoutonAnnonces> annoncePique = new LinkedList<BoutonAnnonces>();
	private List<BoutonAnnonces> annonceCarreau = new LinkedList<BoutonAnnonces>();
	private List<BoutonAnnonces> annonceTrefle = new LinkedList<BoutonAnnonces>();
	private List<JButton> annonceSpecial = new LinkedList<JButton>();
	private int passe=0;   //Compte le nombre de joueurs qui passent leur tour
	private Annonce AnnonceGagnante = new Annonce();   //Indique la dernière annonce effectuée
	private JPanel annonc; // Panneau gérant tous les boutons d'annonces
	private LabelAnnonce AnnonceBas = new LabelAnnonce("---");  // Affichage des annonces (en rouge)
	private LabelAnnonce AnnonceGauche = new LabelAnnonce("---");
	private LabelAnnonce AnnonceHaut = new LabelAnnonce("---");
	private LabelAnnonce AnnonceDroite = new LabelAnnonce("---");
	
	//---------  Variables de la phase de jeu -----------
	private boolean ecouteJoue = false; //Indique si c'est au tour du joueur de jouer
	private Carte[][] Joueurs = new Carte[4][8];  //Les cartes des joueur : la première ligne représente les cartes du joueur, les 3 autres celles des IA
	private Panneau container = new Panneau();  //Panneau général englobant tous les composants
	private JPanel bas = new JPanel();   // Panneaux contenants les cartes des joueurs
    private JPanel gauche = new JPanel();
    private JPanel droite = new JPanel();
    private JPanel haut = new JPanel();
    private List<Bouton> Boutons = new LinkedList<Bouton>();  // Liste des Cartes du joueur réel
    private List<Carte> Liste_Cartes = new LinkedList<Carte>();  //Liste des cartes utilisée (ce n'est pas le paquet)
    private Joueur Humain = new Joueur("humain");
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
    
    // ----------Variables mixtes ---------------
    private double vitesse = 1; //Indique la vitesse de jeu
    private int premier = 0;  // Indique qui commence a jouer, 0 : joueur, 1 : IAgauche, 2 : IAhaut, 3 : IAdroite
    private String etat = "MenuPrincipal"; //Indique la phase de jeu actuelle
    private JButton NvPartie;
    private JButton ChargerPartie;
    private JButton Options;
    private String Atout = "";
    
    /**
     * Programme principal, 
     * comporte une phase d'initialisation puis l'appel de la fonction
     * go() qui gère le déroulement du jeu
     */
    
    public Fenetre(String phase){
    	
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
    	}
   	}
    
    public void setMenu(){
    	partie.add(lancer);
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
    	partie.add(quitter);
    	jeu.add(dernierPli);
    	aPropos.add(item6);
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
        JButton b3 = new JButton(new ImageIcon("mini_carte.jpeg"));
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
    	ChargerPartie = new JButton("Charger Partie");
    	Options = new JButton("Options");
    	
    	Bienvenue.setFont(police2);
    	Bienvenue.setForeground(Color.RED);
    	Bienvenue.setHorizontalAlignment(JLabel.CENTER);
    	NvPartie.setFont(police);
    	ChargerPartie.setFont(police);
    	Options.setFont(police);
    	
    	NvPartie.setPreferredSize(new Dimension(500,80));
    	ChargerPartie.setPreferredSize(new Dimension(500,80));
    	Options.setPreferredSize(new Dimension(500,80));
    	
    	NvPartie.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) { 
				etat = "jeu";
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
        pan3.add(ChargerPartie);
        
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
    	opt.setLayout(new GridLayout(5,1,5,5));
    	Font police = new Font("Tahoma", Font.BOLD, 30);
    	JLabel[] texte = {new JLabel("Nom joueur : "), new JLabel("Nom IA gauche : "), 
    			new JLabel("Nom IA haut : "), new JLabel("Nom IA droite : ")};
    	JTextField[] text = new JTextField[4];
    	JPanel[] pan = new JPanel[4];
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
    	
    	while(!go){
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
    
    public void PlateauJeu(){
    	
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
		
		while(true){  //Boucle principale gérant la partie, il faudra remplacer true par une condition sur le score dans le futur
			this.setSize(1202, 720); //Ces deux lignes servent a forcer le programme à réactualiser la fenêtre (j'ai pas trouver mieux)
			this.setSize(1202, 719);
			Phase_annonces();
			Atout = AnnonceGagnante.couleur;
			InitialiseCartesMaitres();
			int entame = premier;
			int carte_jouer = 0;  //indique le nombre de carte déjà jouées
			pause((int)(1000/vitesse));
			while(carte_jouer<8){  //Boucle gérant une partie en phase de jeu
				container.entame = entame;  //On précise au plateau quelle carte il dessine en dessous des autres
				int i=entame;  //i est le numero du joueur qui joue 
				for (int j = 0; j<4; j++){  //boucle gérant un tour
					if (i==0)
						play_joueur();
					else play_IA(i);
					i=(i+1)%4;
				}
				entame = (checkGagnant() + entame)%4; //On regarde qui remporte le pli
				for (Carte j: CartesPlateau)
					CartesTombees.add(j);
				CartesPlateau = new LinkedList<Carte>();
				ActualiseCartesMaitres();
				pause((int)(500/vitesse));
				container.bas = false;  //On efface les cartes sur le tapis de jeu
				container.gauche = false;
				container.haut = false;
				container.droite = false;
				container.repaint();
				carte_jouer++;
				pause((int)(500/vitesse));
			}
			premier = (premier+1)%4;  //On change le joueur qui commence
			initialise();
			distribue();
			trier();
			reboot();   //Rend toutes les cartes de nouveau visibles et réaffiche les boutons d'annonces
			pause((int)(1000/vitesse));
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
    
		public void pause(int t){  // Permet de faire une pause de t ms
			  try{
				  Thread.sleep(t);

				}catch(InterruptedException e) {

				  e.printStackTrace();
			  }
		}
		
		public void actionPerformed(ActionEvent arg0) {  //Gestion de l'appui sur les boutons (les cartes du joueur)
			Bouton b=(Bouton)arg0.getSource();
			if (ecouteJoue){  //On a cliquer sur un bouton
				container.Bas = b.picture;
				container.bas = true;
				b.setVisible(false);
				container.repaint();
				ecouteJoue=false;
				CartesPlateau.add(b.carteBouton);
				ActualiseRangAtoutMaitre();
				Joueurs[0][CarteToIndice(b.carteBouton, 0)].jouee = true;
			}
			else if (ecouteAnnonce){
				int ind1 = -1;
				int ind2 = -1;
				for (int i = 0; i<8; i++){
					if (Boutons.get(i).echange && ind1==-1)
						ind1=i;
					else if (Boutons.get(i).echange)
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
						Boutons.get(i).echange = false;
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
				this.setSize(1202, 720); //Ces deux lignes servent a forcer le programme à réactualiser la fenêtre (j'ai pas trouver mieux)
				this.setSize(1202, 719);
			}
		} 
		/**
		 * Gestion de l'appui sur les boutons d'annonces (seulement l'affichage)
		 */
		class classique implements ActionListener{
			private String valeur;
			private String couleur;
			public classique(String val, String coul){
				this.valeur=val;
				this.couleur = coul;
			}
		    public void actionPerformed(ActionEvent e) {
		    	AnnonceBas.setText(valeur + " " + couleur);
		    	ecouteAnnonce=false;
		    	setGagne("bas");
		    	AnnonceGagnante=new Annonce(valeur, couleur, "joueur");
		    	Humain.DerniereAnnonce=new Annonce(valeur, couleur, "joueur");
		    }
		 }
		
		class special implements ActionListener{
			private String valeur;
			public special(String val){
				this.valeur=val;
			}
		    public void actionPerformed(ActionEvent e) {
		    	AnnonceBas.setText(valeur);
		    	ecouteAnnonce=false;
		    	if (this.valeur == "Contrée!"){
		    		AnnonceBas.gagne=true;
		    		passe=0;
		    		AnnonceGagnante.contre = true;
		    	}
		    	else if (this.valeur == "Sur-contreé!"){
		    		AnnonceBas.gagne=true;
		    		passe=0;
		    		AnnonceGagnante.surcontre = true;
		    	}
		    	else{
		    		Humain.DerniereAnnonce = new Annonce("Passe", "", "joueur");
		    		passe++;
		    	}
		    		
		    }
		 }
		
		public void setGagne(String position){
			AnnonceBas.gagne=false;
	    	AnnonceGauche.gagne=false;
	    	AnnonceHaut.gagne=false;
	    	AnnonceDroite.gagne=false;
	    	if (position == "bas")
	    		AnnonceBas.gagne=true;
	    	else if (position == "gauche")
	    		AnnonceGauche.gagne=true;
	    	else if (position == "haut")
	    		AnnonceHaut.gagne=true;
	    	else if (position == "droite")
	    		AnnonceDroite.gagne=true;
		}
			
		public void Phase_annonces(){
			passe = 0;
			int i=premier;  // Sert à gerer qui joue dans quel ordre
			AnnonceGagnante=new Annonce();
			annonceSpecial.get(1).setEnabled(false);  // On desactive Contré/Surcontré
			annonceSpecial.get(2).setEnabled(false);
			initialiseMainJoueur();  //Place les cartes dans les mains du joueurs et des IA
			initialiseMainIA();
			setCartes();
			while(passe<3 || AnnonceGagnante.valeur == "0"){
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
				else if (i==1){
					AnnonceChoix(JoueurGauche);
					pause((int)(500/vitesse));
				}
				else if (i==2){
					AnnonceChoix(JoueurHaut);
					pause((int)(500/vitesse));
				}
				else if (i==3){
					AnnonceChoix(JoueurDroite);
					pause((int)(500/vitesse));
				}
				i=(i+1)%4;
			}
			AnnonceBas.setVisible(AnnonceBas.gagne); //On ne garde visible que les annonces "gagnantes" (annonce la plus élevé + éventuellemnt Contré/Surcontré)
			AnnonceGauche.setVisible(AnnonceGauche.gagne);
			AnnonceHaut.setVisible(AnnonceHaut.gagne);
			AnnonceDroite.setVisible(AnnonceDroite.gagne);
			annonc.setVisible(false);
		}
		
		public void setCartes(){
			for (Carte c : Joueurs[1])
				JoueurGauche.Cartes.add(c);
			for (Carte c : Joueurs[2])
				JoueurHaut.Cartes.add(c);
			for (Carte c : Joueurs[3])
				JoueurDroite.Cartes.add(c);
		}
		
		public void AnnonceChoix(Joueur Actuel){
			Actuel.AnnonceFaite=false;
			if (AnnonceGagnante.contre==true || AnnonceGagnante.valeur=="CAPOT"){
				Actuel.DerniereAnnonce= new Annonce("Passe", "", "");
				Actuel.AnnonceFaite=true;
				if (Actuel == JoueurGauche) {AnnonceGauche.setText("Passe");passe++;}
				else if (Actuel == JoueurHaut) {AnnonceHaut.setText("Passe");passe++;}
				else if (Actuel == JoueurDroite) {AnnonceDroite.setText("Passe");passe++;}
			}
			else if (Integer.valueOf(AnnonceGagnante.valeur)>=100) {
				Actuel.DerniereAnnonce= new Annonce("Passe", "", "");
				Actuel.AnnonceFaite=true;
				if (Actuel == JoueurGauche) {AnnonceGauche.setText("Passe");passe++;}
				else if (Actuel == JoueurHaut) {AnnonceHaut.setText("Passe");passe++;}
				else if (Actuel == JoueurDroite) {AnnonceDroite.setText("Passe");passe++;}
			}
			else if (Actuel == JoueurGauche){
				if ( JoueurDroite.DerniereAnnonce.valeur == "0" 
						|| (JoueurDroite.DerniereAnnonce.valeur == "Passe" && JoueurDroite.AlwaysPassed==true)) {
					AnnonceInit(Actuel.Cartes, Actuel);
				}
				else {
					AnnonceSuite(JoueurDroite.DerniereAnnonce, Actuel.Cartes, Actuel);
				}
			}
			else if (Actuel == JoueurDroite) {
				if ( JoueurGauche.DerniereAnnonce.valeur == "0" 
						|| (JoueurGauche.DerniereAnnonce.valeur == "Passe" && JoueurGauche.AlwaysPassed==true)) {
					AnnonceInit(Actuel.Cartes, Actuel);
				}
				else {
					AnnonceSuite(JoueurGauche.DerniereAnnonce, Actuel.Cartes, Actuel);
				}
			}
			else if (Actuel == JoueurHaut) {
				if ( Humain.DerniereAnnonce.valeur == "0" 
						|| (Humain.DerniereAnnonce.valeur == "Passe" && JoueurHaut.AlwaysPassed==true)) {
					AnnonceInit(Actuel.Cartes, Actuel);
				}
				else {
					AnnonceSuite(Humain.DerniereAnnonce, Actuel.Cartes, Actuel);
				}
			}

		}
		
		public void AnnonceInit(List<Carte> Cartes, Joueur Actuel){
			int MainCoeur = 0;
			int MainCarreau = 0;
			int MainTrefle = 0;
			int MainPique = 0;
			Carte temp = new Carte("","");
			
			for (int i=0 ; i<8 ; i++) {
				temp = Actuel.Cartes.get(i);
				
				if (temp.couleur == "coeur"){  //test main atout coeur
					if(temp.valeur == "valet"){
						MainCoeur+=50000000 ;}
					else if(temp.valeur == "9"){
						MainCoeur+=5000000 ;}
					else if(temp.valeur == "as"){
						MainCoeur+=500000 ;
						MainCarreau+=10000000;
						MainTrefle+=10000000;
						MainPique+=10000000;}
					else if(temp.valeur == "10"){
						MainCoeur+=50000 ;
						MainCarreau+=1000000;
						MainTrefle+=1000000;
						MainPique+=1000000;}
					else if(temp.valeur == "roi"){
						MainCoeur+=5000 ;}
					else if(temp.valeur == "dame"){
						MainCoeur+=500 ;}
					else if(temp.valeur == "8"){
						MainCoeur+=5 ;}
					else if(temp.valeur == "7"){
						MainCoeur+=5 ;}
				}
				else if (temp.couleur == "carreau"){  //test main atout carreau
					if(temp.valeur == "valet"){
						MainCarreau+=50000000 ;}
					else if(temp.valeur == "9"){
						MainCarreau+=5000000 ;}
					else if(temp.valeur == "as"){
						MainCarreau+=500000 ;
						MainCoeur+=10000000;
						MainTrefle+=10000000;
						MainPique+=10000000;}
					else if(temp.valeur == "10"){
						MainCarreau+=50000 ;
						MainCoeur+=1000000;
						MainTrefle+=1000000;
						MainPique+=1000000;}
					else if(temp.valeur == "roi"){
						MainCarreau+=5000 ;}
					else if(temp.valeur == "dame"){
						MainCarreau+=500 ;}
					else if(temp.valeur == "8"){
						MainCarreau+=50 ;}
					else if(temp.valeur == "7"){
						MainCarreau+=5 ;}
				}
				else if (temp.couleur == "trefle"){  //test main atout trefle
					if(temp.valeur == "valet"){
						MainTrefle+=50000000 ;}
					else if(temp.valeur == "9"){
						MainTrefle+=5000000 ;}
					else if(temp.valeur == "as"){
						MainTrefle+=500000 ;
						MainCarreau+=10000000;
						MainCoeur+=10000000;
						MainPique+=10000000;}
					else if(temp.valeur == "10"){
						MainTrefle+=50000 ;
						MainCarreau+=1000000;
						MainCoeur+=1000000;
						MainPique+=1000000;}
					else if(temp.valeur == "roi"){
						MainTrefle+=5000 ;}
					else if(temp.valeur == "dame"){
						MainTrefle+=500 ;}
					else if(temp.valeur == "8"){
						MainTrefle+=50 ;}
					else if(temp.valeur == "7"){
						MainTrefle+=5 ;}
				}
				else if (temp.couleur == "pique"){  //test main atout pique
					if(temp.valeur == "valet"){
						MainPique+=50000000 ;}
					else if(temp.valeur == "9"){
						MainPique+=5000000 ;}
					else if(temp.valeur == "as"){
						MainPique+=500000 ;
						MainCarreau+=10000000;
						MainTrefle+=10000000;
						MainCoeur+=10000000;}
					else if(temp.valeur == "10"){
						MainPique+=50000 ;
						MainCarreau+=1000000;
						MainTrefle+=1000000;
						MainCoeur+=1000000;}
					else if(temp.valeur == "roi"){
						MainPique+=5000 ;}
					else if(temp.valeur == "dame"){
						MainPique+=500 ;}
					else if(temp.valeur == "8"){
						MainPique+=50 ;}
					else if(temp.valeur == "7"){
						MainPique+=5 ;}
				}
			}

			if (MainCoeur<5000000 && MainCarreau<5000000 && MainTrefle<5000000 && MainPique<5000000){ //Test Mauvaise main
				Actuel.DerniereAnnonce= new Annonce("Passe", "", "");
				Actuel.AnnonceFaite=true;
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("Passe");
					passe++;
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("Passe");
					passe++;
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("Passe");
					passe++;
					}
			}
			
			else if(Integer.valueOf(AnnonceGagnante.valeur)<80){	//Test 80
				if ((MainCoeur>5000000 && MainCoeur<9000000)||(MainCoeur>50000000 && MainCoeur<54000000)){	
				String str = "coeur";
				Actuel.DerniereAnnonce= new Annonce("80", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("80 " + str);
					AnnonceGagnante = new Annonce("80", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurDroite");
				}
				}	
			else if ((MainCarreau>5000000 && MainCarreau<9000000)||(MainCarreau>50000000 && MainCarreau<54000000)){						//Test à 80
				String str = "carreau";
				Actuel.DerniereAnnonce= new Annonce("80", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("80 " + str);
					AnnonceGagnante = new Annonce("80", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurDroite");
					}
				}	
			else if ((MainTrefle>5000000 && MainTrefle<9000000)||(MainTrefle>50000000 && MainTrefle<54000000)){						//Test à 80
				String str = "trefle";
				Actuel.DerniereAnnonce= new Annonce("80", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false; 
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("80 " + str);
					AnnonceGagnante = new Annonce("80", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurDroite");
					}
				}	
			else if ((MainPique>5000000 && MainPique<9000000)||(MainPique>50000000 && MainPique<54000000)){						//Test à 80
				String str = "pique";
				Actuel.DerniereAnnonce= new Annonce("80", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("80 " + str);
					AnnonceGagnante = new Annonce("80", str, "JoueurGauche");
					}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurDroite");
					}
				}
			}
			
			else if(Integer.valueOf(AnnonceGagnante.valeur)<90){	//Test 90
				if (MainCoeur>55000000){  							
				String str = "coeur";
				Actuel.DerniereAnnonce= new Annonce("90", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("90 " + str);
					AnnonceGagnante = new Annonce("90", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurDroite");
					}
				}	
			else if (MainCarreau>55000000){
				String str = "carreau";
				Actuel.DerniereAnnonce= new Annonce("90", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("90 " + str);
					AnnonceGagnante = new Annonce("90", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurDroite");
					}
				}	
			else if (MainTrefle>55000000){
				String str = "trefle";
				Actuel.DerniereAnnonce= new Annonce("90", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("90 " + str);
					AnnonceGagnante = new Annonce("90", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurDroite");
					}
				}	
			else if (MainPique>55000000){
				String str = "pique";
				Actuel.DerniereAnnonce= new Annonce("90", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("90 " + str);
					AnnonceGagnante = new Annonce("90", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurDroite");
					}
				}
			}
						
			else if(Integer.valueOf(AnnonceGagnante.valeur)<100){	//Test 100
				if (MainCoeur>55500000){						
				String str = "coeur";
				Actuel.DerniereAnnonce= new Annonce("100", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {							
					AnnonceGauche.setText("100 " + str);
					AnnonceGagnante = new Annonce("100", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("100 "+ str);
					AnnonceGagnante = new Annonce("100", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("100 "+ str);
					AnnonceGagnante = new Annonce("100", str, "JoueurDroite");
					}
				
				}	
			else if (MainCarreau>55500000){
				String str = "carreau";
				Actuel.DerniereAnnonce= new Annonce("100", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("100 " + str);
					AnnonceGagnante = new Annonce("100", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("100 "+ str);
					AnnonceGagnante = new Annonce("100", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("100 "+ str);
					AnnonceGagnante = new Annonce("100", str, "JoueurDroite");
					}
				}	
			else if (MainTrefle>55500000){
				String str = "Trefle";
				Actuel.DerniereAnnonce= new Annonce("100", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("100 " + str);
					AnnonceGagnante = new Annonce("100", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("100 "+ str);
					AnnonceGagnante = new Annonce("100", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("100 "+ str);
					AnnonceGagnante = new Annonce("100", str, "JoueurDroite");
					}
				}	
			else if (MainPique>55500000){
				String str = "pique";
				Actuel.DerniereAnnonce= new Annonce("100", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("100 " + str);
					AnnonceGagnante = new Annonce("100", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("100 "+ str);
					AnnonceGagnante = new Annonce("100", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("100 "+ str);
					AnnonceGagnante = new Annonce("100", str, "JoueurDroite");
					}
				}
			}
			
			else if(Actuel.AnnonceFaite=false){
				Actuel.DerniereAnnonce= new Annonce("Passe", "", "");
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("Passe");
					passe++;
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("Passe");
					passe++;
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("Passe");
					passe++;
					}
				
		}		
		}
			
		public void AnnonceSuite(Annonce AnnonceCoequipier, List<Carte> Cartes, Joueur Actuel){
			int MainCoeur = 0;
			int MainCarreau = 0;
			int MainTrefle = 0;
			int MainPique = 0;
			Carte temp = new Carte("","");
			
			for (int i=0 ; i<8 ; i++) {
				temp = Actuel.Cartes.get(i);
				
				if (temp.couleur == "coeur"){  //test main atout coeur
					if(temp.valeur == "valet"){
						MainCoeur+=50000000 ;}
					else if(temp.valeur == "9"){
						MainCoeur+=5000000 ;}
					else if(temp.valeur == "as"){
						MainCoeur+=500000 ;
						MainCarreau+=10000000;
						MainTrefle+=10000000;
						MainPique+=10000000;}
					else if(temp.valeur == "10"){
						MainCoeur+=50000 ;
						MainCarreau+=1000000;
						MainTrefle+=1000000;
						MainPique+=1000000;}
					else if(temp.valeur == "roi"){
						MainCoeur+=5000 ;}
					else if(temp.valeur == "dame"){
						MainCoeur+=500 ;}
					else if(temp.valeur == "8"){
						MainCoeur+=5 ;}
					else if(temp.valeur == "7"){
						MainCoeur+=5 ;}
				}
				else if (temp.couleur == "carreau"){  //test main atout carreau
					if(temp.valeur == "valet"){
						MainCarreau+=50000000 ;}
					else if(temp.valeur == "9"){
						MainCarreau+=5000000 ;}
					else if(temp.valeur == "as"){
						MainCarreau+=500000 ;
						MainCoeur+=10000000;
						MainTrefle+=10000000;
						MainPique+=10000000;}
					else if(temp.valeur == "10"){
						MainCarreau+=50000 ;
						MainCoeur+=1000000;
						MainTrefle+=1000000;
						MainPique+=1000000;}
					else if(temp.valeur == "roi"){
						MainCarreau+=5000 ;}
					else if(temp.valeur == "dame"){
						MainCarreau+=500 ;}
					else if(temp.valeur == "8"){
						MainCarreau+=50 ;}
					else if(temp.valeur == "7"){
						MainCarreau+=5 ;}
				}
				else if (temp.couleur == "trefle"){  //test main atout trefle
					if(temp.valeur == "valet"){
						MainTrefle+=50000000 ;}
					else if(temp.valeur == "9"){
						MainTrefle+=5000000 ;}
					else if(temp.valeur == "as"){
						MainTrefle+=500000 ;
						MainCarreau+=10000000;
						MainCoeur+=10000000;
						MainPique+=10000000;}
					else if(temp.valeur == "10"){
						MainTrefle+=50000 ;
						MainCarreau+=1000000;
						MainCoeur+=1000000;
						MainPique+=1000000;}
					else if(temp.valeur == "roi"){
						MainTrefle+=5000 ;}
					else if(temp.valeur == "dame"){
						MainTrefle+=500 ;}
					else if(temp.valeur == "8"){
						MainTrefle+=50 ;}
					else if(temp.valeur == "7"){
						MainTrefle+=5 ;}
				}
				else if (temp.couleur == "pique"){  //test main atout pique
					if(temp.valeur == "valet"){
						MainPique+=50000000 ;}
					else if(temp.valeur == "9"){
						MainPique+=5000000 ;}
					else if(temp.valeur == "as"){
						MainPique+=500000 ;
						MainCarreau+=10000000;
						MainTrefle+=10000000;
						MainCoeur+=10000000;}
					else if(temp.valeur == "10"){
						MainPique+=50000 ;
						MainCarreau+=1000000;
						MainTrefle+=1000000;
						MainCoeur+=1000000;}
					else if(temp.valeur == "roi"){
						MainPique+=5000 ;}
					else if(temp.valeur == "dame"){
						MainPique+=500 ;}
					else if(temp.valeur == "8"){
						MainPique+=50 ;}
					else if(temp.valeur == "7"){
						MainPique+=5 ;}
				}
			}

			
			if (MainCoeur<5000000 && MainCarreau<5000000 && MainTrefle<5000000 && MainPique<5000000){ //Test Mauvaise main
				Actuel.DerniereAnnonce= new Annonce("Passe", "", "");
				Actuel.AnnonceFaite=true;
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("Passe");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("Passe");
				}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("Passe");
				}
			}
			
			else if(Integer.valueOf(AnnonceGagnante.valeur)<80){	//Test 80
				if ((AnnonceCoequipier.couleur=="coeur" && MainCoeur>5000000 && MainCoeur<9000000)||(AnnonceCoequipier.couleur=="coeur" && MainCoeur>50000000 && MainCoeur<54000000)){	
				String str = "coeur";
				Actuel.DerniereAnnonce= new Annonce("80", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {							
					AnnonceGauche.setText("80 " + str);
					AnnonceGagnante = new Annonce("80", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurDroite");
					}
				}	
			else if ((AnnonceCoequipier.couleur=="carreau" && MainCarreau>5000000 && MainCarreau<9000000)||(AnnonceCoequipier.couleur=="carreau" && MainCarreau>50000000 && MainCarreau<54000000)){						//Test à 80
				String str = "carreau";
				Actuel.DerniereAnnonce= new Annonce("80", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {							
					AnnonceGauche.setText("80 " + str);
					AnnonceGagnante = new Annonce("80", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurDroite");
					}
				}	
			else if ((AnnonceCoequipier.couleur=="trefle" && MainTrefle>5000000 && MainTrefle<9000000)||(AnnonceCoequipier.couleur=="trefle" && MainTrefle>50000000 && MainTrefle<54000000)){						//Test à 80
				String str = "trefle";
				Actuel.DerniereAnnonce= new Annonce("80", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {							
					AnnonceGauche.setText("80 " + str);
					AnnonceGagnante = new Annonce("80", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurDroite");
					}
				}	
			else if ((AnnonceCoequipier.couleur=="pique" && MainPique>5000000 && MainPique<9000000)||(AnnonceCoequipier.couleur=="pique" && MainPique>50000000 && MainPique<54000000)){						//Test à 80
				String str = "pique";
				Actuel.DerniereAnnonce= new Annonce("80", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {							
					AnnonceGauche.setText("80 " + str);
					AnnonceGagnante = new Annonce("80", str, "JoueurGauche");
					}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("80 "+ str);
					AnnonceGagnante = new Annonce("80", str, "JoueurDroite");
					}
				}
			}
			
			else if(Integer.valueOf(AnnonceGagnante.valeur)<90){	//Test 90
				if (AnnonceCoequipier.couleur=="coeur" && MainCoeur>55000000){  							
				String str = "coeur";
				Actuel.DerniereAnnonce= new Annonce("90", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {							
					AnnonceGauche.setText("90 " + str);
					AnnonceGagnante = new Annonce("90", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurDroite");
					}
				}	
			else if (AnnonceCoequipier.couleur=="carreau" && MainCarreau>55000000){
				String str = "carreau";
				Actuel.DerniereAnnonce= new Annonce("90", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {							
					AnnonceGauche.setText("90 " + str);
					AnnonceGagnante = new Annonce("90", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurDroite");
					}
				}	
			else if (AnnonceCoequipier.couleur=="trefle" && MainTrefle>55000000){
				String str = "trefle";
				Actuel.DerniereAnnonce= new Annonce("90", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {							
					AnnonceGauche.setText("90 " + str);
					AnnonceGagnante = new Annonce("90", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurDroite");
					}
				}	
			else if (AnnonceCoequipier.couleur=="pique" && MainPique>55000000){
				String str = "pique";
				Actuel.DerniereAnnonce= new Annonce("90", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {							
					AnnonceGauche.setText("90 " + str);
					AnnonceGagnante = new Annonce("90", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("90 "+ str);
					AnnonceGagnante = new Annonce("90", str, "JoueurDroite");
					}
				}
			}
			
			else if(Integer.valueOf(AnnonceGagnante.valeur)<100){	//Test 100
				if (AnnonceCoequipier.couleur=="coeur" && MainCoeur>55500000){						
					String str = "coeur";
					Actuel.DerniereAnnonce= new Annonce("100", str, "");
					Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
					if (Actuel==JoueurGauche) {							
						AnnonceGauche.setText("100 " + str);
						AnnonceGagnante = new Annonce("100", str, "JoueurGauche");
					}
					else if (Actuel==JoueurHaut) {
						AnnonceHaut.setText("100 "+ str);
						AnnonceGagnante = new Annonce("100", str, "JoueurHaut");
						}
					else if (Actuel==JoueurDroite) {
						AnnonceDroite.setText("100 "+ str);
						AnnonceGagnante = new Annonce("100", str, "JoueurDroite");
						}
					}	
				else if (AnnonceCoequipier.couleur=="carreau" && MainCarreau>55500000){
				String str = "carreau";
				Actuel.DerniereAnnonce= new Annonce("100", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {							
					AnnonceGauche.setText("100 " + str);
					AnnonceGagnante = new Annonce("100", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("100 "+ str);
					AnnonceGagnante = new Annonce("100", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("100 "+ str);
					AnnonceGagnante = new Annonce("100", str, "JoueurDroite");
					}
				}	
			else if (AnnonceCoequipier.couleur=="trefle" && MainTrefle>55500000){
				String str = "Trefle";
				Actuel.DerniereAnnonce= new Annonce("100", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {							
					AnnonceGauche.setText("100 " + str);
					AnnonceGagnante = new Annonce("100", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("100 "+ str);
					AnnonceGagnante = new Annonce("100", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("100 "+ str);
					AnnonceGagnante = new Annonce("100", str, "JoueurDroite");
					}
				}	
			else if (AnnonceCoequipier.couleur=="pique" && MainPique>55500000){
				String str = "pique";
				Actuel.DerniereAnnonce= new Annonce("100", str, "");
				Actuel.AnnonceFaite=true; Actuel.AlwaysPassed=false;
				if (Actuel==JoueurGauche) {							
					AnnonceGauche.setText("100 " + str);
					AnnonceGagnante = new Annonce("100", str, "JoueurGauche");
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("100 "+ str);
					AnnonceGagnante = new Annonce("100", str, "JoueurHaut");
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("100 "+ str);
					AnnonceGagnante = new Annonce("100", str, "JoueurDroite");
					}
				}
			}	
						
			else if(Actuel.AnnonceFaite=false){
				Actuel.DerniereAnnonce= new Annonce("Passe", "", "");
				if (Actuel==JoueurGauche) {
					AnnonceGauche.setText("Passe");
					passe++;
				}
				else if (Actuel==JoueurHaut) {
					AnnonceHaut.setText("Passe");
					passe++;
					}
				else if (Actuel==JoueurDroite) {
					AnnonceDroite.setText("Passe");
					passe++;
					}
			}		
		}
			
				
		
				
		public void initialiseAnnonces(){ //Initialise tous les boutons d'annonces (fonction encore un peu brouillone)
			
			JPanel centre = new JPanel();
			GridLayout grille = new GridLayout(4,12,5,5);
			centre.setLayout(grille);
			
			Font police = new Font("Tahoma", Font.BOLD, 11); //Font pour les CAPOT
			
			// ----Coeur-----
			for (int i = 0; i<11; i++){
				annonceCoeur.add(new BoutonAnnonces(Integer.toString(80+10*i), "coeur"));
				annonceCoeur.get(i).addActionListener(new classique(Integer.toString(80+i*10), "coeur"));
				centre.add(annonceCoeur.get(i));
			}
			BoutonAnnonces coeurCAPOT = new BoutonAnnonces("CAPOT", "coeur");
			coeurCAPOT.setFont(police);
			centre.add(coeurCAPOT);
			coeurCAPOT.addActionListener(new classique("CAPOT", "coeur"));
			annonceCoeur.add(coeurCAPOT);
			
			// ----Pique-----
			for (int i = 0; i<11; i++){
				annoncePique.add(new BoutonAnnonces(Integer.toString(80+10*i), "pique"));
				annoncePique.get(i).addActionListener(new classique(Integer.toString(80+i*10), "pique"));
				centre.add(annoncePique.get(i));
			}
			BoutonAnnonces piqueCAPOT = new BoutonAnnonces("CAPOT", "pique");
			piqueCAPOT.setFont(police);
			centre.add(piqueCAPOT);
			piqueCAPOT.addActionListener(new classique("CAPOT", "pique"));
			annoncePique.add(piqueCAPOT);
			
			// ----Carreau-----
			for (int i = 0; i<11; i++){
				annonceCarreau.add(new BoutonAnnonces(Integer.toString(80+10*i), "carreau"));
				annonceCarreau.get(i).addActionListener(new classique(Integer.toString(80+i*10), "carreau"));
				centre.add(annonceCarreau.get(i));
			}
			BoutonAnnonces carreauCAPOT = new BoutonAnnonces("CAPOT", "carreau");
			carreauCAPOT.setFont(police);
			centre.add(carreauCAPOT);
			carreauCAPOT.addActionListener(new classique("CAPOT", "carreau"));
			annonceCarreau.add(carreauCAPOT);
			
			// ----Trefle-----
			for (int i = 0; i<11; i++){
				annonceTrefle.add(new BoutonAnnonces(Integer.toString(80+10*i), "trefle"));
				annonceTrefle.get(i).addActionListener(new classique(Integer.toString(80+i*10), "trefle"));
				centre.add(annonceTrefle.get(i));
			}
			BoutonAnnonces trefleCAPOT = new BoutonAnnonces("CAPOT", "trefle");
			trefleCAPOT.setFont(police);
			centre.add(trefleCAPOT);
			trefleCAPOT.addActionListener(new classique("CAPOT", "trefle"));
			annonceTrefle.add(trefleCAPOT);

			Font police2 = new Font("Tahoma", Font.BOLD, 30); //Font pour les annonces speciales
			
			// ----Autres-----
			JPanel special = new JPanel();
			special.setLayout(new GridLayout(1,3,5,5));
			JButton passe = new JButton("PASSE");
			JButton Contre = new JButton("Contrée!");
			JButton Surcontre = new JButton("Sur-Contrée!");
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
			if (!AnnonceGagnante.contre){        // On active les boutons
				int j;
				if (AnnonceGagnante.valeur == "0")
					j=0;
				else
					j = (Integer.valueOf(AnnonceGagnante.valeur) - 80)/10+1;
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
			if (AnnonceGagnante.joueur == "joueur" || AnnonceGagnante.contre || AnnonceGagnante.valeur == "0")
				annonceSpecial.get(1).setEnabled(false);
			if (AnnonceGagnante.joueur == "IA" || !AnnonceGagnante.contre || AnnonceGagnante.surcontre)
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
			if (AnnonceGagnante.joueur == "joueur" || AnnonceGagnante.valeur == "0" || AnnonceGagnante.contre)
				annonceSpecial.get(1).setEnabled(false);
			else
				annonceSpecial.get(1).setEnabled(true);
			if (AnnonceGagnante.joueur == "IA" || !AnnonceGagnante.contre || AnnonceGagnante.surcontre)
				annonceSpecial.get(2).setEnabled(false);
			else
				annonceSpecial.get(2).setEnabled(true);
			annonc.getComponent(0).setVisible(false);
			temp.getComponent(0).setVisible(false);
			pause((int)(500/vitesse));
		}
		
		public void annonceGauche(){ 
			if (AnnonceGagnante.valeur == "CAPOT"){
				AnnonceGauche.setText("Passe");
				AnnonceGauche.gagne = true;
				passe++;
			}
			else if (Integer.valueOf(AnnonceGagnante.valeur)<80){
				AnnonceGauche.setText("80 coeur");
				AnnonceGagnante = new Annonce ("80", "coeur", "IA");
				passe=0;
			}
			else{
				AnnonceGauche.setText("Passe");
				passe++;
			}
			if (AnnonceGagnante.joueur == "joueur" || AnnonceGagnante.contre)
				annonceSpecial.get(1).setEnabled(false);
			else
				annonceSpecial.get(1).setEnabled(true);
			if (AnnonceGagnante.joueur == "IA" || !AnnonceGagnante.contre || AnnonceGagnante.surcontre)
				annonceSpecial.get(2).setEnabled(false);
			else
				annonceSpecial.get(2).setEnabled(true);
			pause((int)(500/vitesse));
		}
		
		public void annonceHaut(){
			AnnonceHaut.setText("Passe");
			passe++;
			pause((int)(500/vitesse));
		}
		
		public void annonceDroite(){
			if (AnnonceGagnante.valeur == "CAPOT" || AnnonceGagnante.contre){
				AnnonceDroite.setText("Passe");
				passe++;
			}
			else if (Integer.valueOf(AnnonceGagnante.valeur)<100){
				AnnonceDroite.setText("100 coeur");
				AnnonceDroite.gagne = true;
				AnnonceGagnante = new Annonce ("100", "coeur", "IA");
				passe=0;
			}
			else{
				AnnonceDroite.setText("Passe");
				passe++;
			}
			if (AnnonceGagnante.joueur == "joueur" || AnnonceGagnante.contre)  //Actualisation des boutons contré/surcontré
				annonceSpecial.get(1).setEnabled(false);
			else
				annonceSpecial.get(1).setEnabled(true);
			if (AnnonceGagnante.joueur == "IA" || !AnnonceGagnante.contre || AnnonceGagnante.surcontre)
				annonceSpecial.get(2).setEnabled(false);
			else
				annonceSpecial.get(2).setEnabled(true);
			pause((int)(500/vitesse));
		}
		
		public void initialise(){
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
			int taille = Liste_Cartes.size();
			for (int j = 0; j<4; j++){
				for (int c = 0; c<8; c++){
					int num = (int)(Math.random()*taille);
					Joueurs[j][c]=Liste_Cartes.get(num);
					Liste_Cartes.remove(num);
					taille--;
				}
			}
		}
		
		public void trier(){  //Fonctionne sur la base du tri rapide aussi appelé tri à bulles
			for (int i = 0; i<4; i++){
				boolean trie  =false; //indique si les cartes sont triées
				while (!trie){
					trie = true;
					for (int j = 0; j<7; j++){
						if (Joueurs[i][j].valeurTri>Joueurs[i][j+1].valeurTri){
							Carte temp = Joueurs[i][j];
							Joueurs[i][j] = Joueurs[i][j+1];
							Joueurs[i][j+1] = temp;
							trie = false;
						}
					}
				}
			}
		}
		
		public void initialiseMainJoueur(){
			
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
		
		public void initialiseMainIA(){
			
			for (int i = 0; i<IAgauche.Cartes.size(); i++){  //On supprime tous les boutons liés aux cartes de la partie précédente
				haut.remove(1);
				gauche.remove(1);
				droite.remove(1);
			}
			
			IAgauche.Cartes = new LinkedList<CarteIA>();
			IAhaut.Cartes = new LinkedList<CarteIA>();
			IAdroite.Cartes = new LinkedList<CarteIA>();
			
			gauche.setLayout(new GridLayout(9, 1));
			droite.setLayout(new GridLayout(9, 1));
			
			for (int i =0; i<8; i++){
				IAgauche.Cartes.add(new CarteIA(Joueurs[1][i]));
				gauche.add(IAgauche.Cartes.get(i));
				IAgauche.Cartes.get(i).setPreferredSize(new Dimension(130,89));
				IAhaut.Cartes.add(new CarteIA(Joueurs[2][i]));
				haut.add(IAhaut.Cartes.get(i));
				IAhaut.Cartes.get(i).setPreferredSize(new Dimension(89,130));
				IAdroite.Cartes.add(new CarteIA(Joueurs[3][i]));
				droite.add(IAdroite.Cartes.get(i));
			    IAdroite.Cartes.get(i).setPreferredSize(new Dimension(130,89));
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
				if (!EnMain.jouee)
					cartesJouables.add(EnMain);
			if (CartesPlateau.size() != 0){  //Si on est le premier a jouer, on joue ce qu'on veut
				Carte actuelle = CartesPlateau.get(0);
				if (actuelle.couleur == Atout){  //On demande de l'atout
					boolean jePeuxJouer = false;
					boolean jaiDeLAtout = false;  //Indique si le jouer a de l'atout
					for (Carte EnMain : cartesJouables){
						if (EnMain.couleur == Atout)
							jaiDeLAtout = true;
					}
					if (jaiDeLAtout){  //Si le joueur n'as pas d'atout il joue ce qu'il veut
						Carte PlusForte = CartesPlateau.get(0); //Meilleur atout en jeu
						for (Carte plat : CartesPlateau)   //On détermine le meilleur atout
							if (plat.RangAtout < PlusForte.RangAtout && plat.couleur == Atout)
								PlusForte = plat;
						for (Carte EnMain : cartesJouables)
							if (EnMain.couleur == Atout && (EnMain.RangAtout < PlusForte.RangAtout)) //Peut-on monter à l'atout ?
								jePeuxJouer = true;
						for (int i = 0; i<cartesJouables.size(); i++){  //On désactive (enfin) les boutons des cartes interdites
							if (cartesJouables.get(i).couleur != Atout || (jePeuxJouer && cartesJouables.get(i).RangAtout > actuelle.RangAtout)){
								Boutons.get(CarteToIndice(cartesJouables.get(i), 0)).setEnabled(false);
							}
						}
					}
				}
				else{  //On ne demande pas d'atout
					boolean jaiLaCouleur = false;
					for (Carte EnMain : cartesJouables){
						if (EnMain.couleur == actuelle.couleur)
							jaiLaCouleur = true;
					}
					if (jaiLaCouleur){
						for (int i = 0; i<cartesJouables.size(); i++){  //On désactive les boutons des cartes interdites
							if (cartesJouables.get(i).couleur != actuelle.couleur){
								Boutons.get(CarteToIndice(cartesJouables.get(i), 0)).setEnabled(false);
							}
						}
					}
					else{
						boolean jePeuxJouer = false;
						boolean jaiDeLAtout = false;  //Indique si le jouer a de l'atout
						for (Carte EnMain : cartesJouables)
							if (EnMain.couleur == Atout)
								jaiDeLAtout = true;
						if (jaiDeLAtout){  //Si le joueur n'as pas d'atout il joue ce qu'il veut
							Carte PlusForte = new Carte("temp", "temp"); //Meilleur atout en jeu
							PlusForte.RangAtout = 50;
							for (Carte plat : CartesPlateau)   //On détermine le meilleur atout
								if (plat.RangAtout < PlusForte.RangAtout && plat.couleur == Atout)
									PlusForte = plat;
							if (PlusForte.RangAtout == 50){
								for (int i = 0; i<cartesJouables.size(); i++){  //On désactive les boutons des cartes interdites
									if (cartesJouables.get(i).couleur != Atout){
										Boutons.get(CarteToIndice(cartesJouables.get(i), 0)).setEnabled(false);
									}
								}
							}
							else{
								for (Carte EnMain : cartesJouables)
									if (EnMain.couleur == Atout && (EnMain.RangAtout < PlusForte.RangAtout)) //Peut-on monter à l'atout ?
										jePeuxJouer = true;
								for (int i = 0; i<cartesJouables.size(); i++){  //On désactive (enfin) les boutons des cartes interdites
									if (Joueurs[0][i].couleur != Atout || (jePeuxJouer && Joueurs[0][i].RangAtout > actuelle.RangAtout)){
										Boutons.get(CarteToIndice(cartesJouables.get(i), 0)).setEnabled(false);
									}
								}
							}
						}
					}
				}
			}
		}
		
		public int checkGagnant(){
			boolean atout = false;
			int gagnante = 0;
			String couleurDemandee = CartesPlateau.get(0).couleur;
			for (int i = 0; i<4; i++){
				if (CartesPlateau.get(i).couleur == Atout || atout){
					atout = true;
					if ((CartesPlateau.get(i).RangAtout < CartesPlateau.get(gagnante).RangAtout
							&& CartesPlateau.get(i).couleur == Atout)
								|| (CartesPlateau.get(i).couleur == Atout
									&&  CartesPlateau.get(gagnante).couleur != Atout))
						gagnante = i;
				}
				else{  //Pas d'atout sur le plateau
					if (CartesPlateau.get(i).couleur == couleurDemandee &&
							CartesPlateau.get(i).RangNonAtout < CartesPlateau.get(gagnante).RangNonAtout)
						gagnante = i;
				}
					
			}
			return gagnante;
		}
		
		public int CarteToIndice(Carte Carte, int NumJoueur){
			int indice = 0 ;
			for (int i =0 ; i < 8 ; i++){
				if (Joueurs[NumJoueur][i].isequal(Carte)){  //Paul : j'ai remplacé ton == par isequal
					indice = i;
				}
			}
			return indice;
		}
		
		public void play_IA(int q){  
			List<Carte> CartesJouables = new LinkedList<Carte>();  // Parmis les cartes en main, les cartes que l'on peu jouer dans les règles
			int choix = -1;											// Indice de la carte que l'on va jouer
			Carte CarteBientotJouee = new Carte("ProblemeValeur","ProblemeCouleur");  // Carte que l'on va bientot jouer 
			if (CartesPlateau.size() != 0){							// Est-on le premier à jouer?
				String CouleurDemandee = CartesPlateau.get(0).couleur;	// Couleur demandée du pli
				if (CouleurDemandee == Atout){
					for (int k = 0 ; k < 8 ; k++){		// Si atout demandé, les cartes jouables sont les cartes d'atout meilleures que la meilleure carte atout  jouée 
						if (Joueurs[q][k].couleur == CouleurDemandee && Joueurs[q][k].RangAtout < RangAtoutMaitre && Joueurs[q][k].jouee == false){
							CartesJouables.add(Joueurs[q][k]);
						}
					}

					if (CartesJouables.size() !=0) {	// Si on peut monter à l'atout et que l'on a la carte maitre à l'atout
						for (Carte Carte : CartesJouables){
							if (CarteAtoutMaitre.isequal(Carte)){
								choix = CarteToIndice(Carte,q);
							}
							
						}
						if (choix == -1){			// Si on peut monter mais que l'on n'a pas la carte maitre à l'atout
							int IndiceJouable = 0;
							for (int i = 0 ; i < CartesJouables.size(); i++){
								if (CartesJouables.get(i).RangAtout > CartesJouables.get(IndiceJouable).RangAtout) {
									IndiceJouable = i ;
								}
									
							}
							choix = CarteToIndice(CartesJouables.get(IndiceJouable),q) ;
						}
					}
					else {						// Si on ne peut pas monter à l'atout, on joue le plus petit 
						for (int k = 0 ; k < 8 ; k++){
							if (Joueurs[q][k].couleur == CouleurDemandee && Joueurs[q][k].jouee == false){
								CartesJouables.add(Joueurs[q][k]);
							}
						}
						
						if (CartesJouables.size() !=0) { // Si on a de l'atout mais que l'on ne peut pas monter, on joue le plus petit
							int IndiceJouable = 0;
							for (int i = 0 ; i < CartesJouables.size(); i++){
								if (CartesJouables.get(i).RangAtout > CartesJouables.get(IndiceJouable).RangAtout) {
									IndiceJouable = i ;
								}
								
							}
							choix = CarteToIndice(CartesJouables.get(IndiceJouable),q) ;
						}
						else {							// On n'a pas d'atout 
							for (int k = 0 ; k < 8 ; k++){
								if (Joueurs[q][k].jouee == false){
									CartesJouables.add(Joueurs[q][k]);
								}
							}
							int IndiceJouable = 0;		// On joue la carte la plus faible 
							for (int i = 0 ; i < CartesJouables.size(); i++){
								if (CartesJouables.get(i).RangNonAtout > CartesJouables.get(IndiceJouable).RangNonAtout) {
									IndiceJouable = i ;
								}
								
							}
							choix = CarteToIndice(CartesJouables.get(IndiceJouable),q) ;
							
						}
						
					}
				}
				else {								// Non Atout demandé 
					for (int k = 0 ; k < 8 ; k++){	// Cartes jouables sont les cartes de la couleur demandée
						if (Joueurs[q][k].couleur == CouleurDemandee && Joueurs[q][k].jouee == false){
							CartesJouables.add(Joueurs[q][k]);
						}
					}
					if (CartesJouables.size() != 0) {
						for (Carte carte : CartesMaitres){
							if (contient(CartesJouables, carte)){
								CarteBientotJouee = carte;
							}
						}
						if (CarteBientotJouee.valeur != "ProblemeValeur"){
							choix = CarteToIndice(CarteBientotJouee,q);
						}
						else {
							int IndiceJouable = 0;
							for (int i = 0 ; i < CartesJouables.size(); i++){
								if (CartesJouables.get(i).RangNonAtout > CartesJouables.get(IndiceJouable).RangNonAtout) {
									IndiceJouable = i ;
								}
							}
							CarteBientotJouee = CartesJouables.get(IndiceJouable);
							choix = CarteToIndice(CarteBientotJouee,q);
						}
					}
					else{
						for (int k = 0 ; k < 8 ; k++){
							if (Joueurs[q][k].couleur == Atout && Joueurs[q][k].RangAtout < RangAtoutMaitre && Joueurs[q][k].jouee == false){
								CartesJouables.add(Joueurs[q][k]);
							}
						}
						if (CartesJouables.size() != 0){
							int IndiceJouable = 0;
							for (int i = 0 ; i < CartesJouables.size(); i++){
								if (CartesJouables.get(i).RangAtout > CartesJouables.get(IndiceJouable).RangAtout) {
									IndiceJouable = i ;
								}
							}
							CarteBientotJouee = CartesJouables.get(IndiceJouable);
							choix = CarteToIndice(CarteBientotJouee,q);
						}
						else {
							for (int k = 0 ; k < 8 ; k++){
								if (Joueurs[q][k].couleur == Atout && Joueurs[q][k].jouee == false){
									CartesJouables.add(Joueurs[q][k]);
								}
							}
							if (CartesJouables.size() != 0){
								int IndiceJouable = 0;
								for (int i = 0 ; i < CartesJouables.size(); i++){
									if (CartesJouables.get(i).RangAtout > CartesJouables.get(IndiceJouable).RangAtout) {
										IndiceJouable = i ;
									}
								}
								CarteBientotJouee = CartesJouables.get(IndiceJouable);
								choix = CarteToIndice(CarteBientotJouee,q);
							}
							else {
								for (int k = 0 ; k < 8 ; k++){
									if (Joueurs[q][k].jouee == false){
										CartesJouables.add(Joueurs[q][k]);
									}
								}
								int IndiceJouable = 0;
								for (int i = 0 ; i < CartesJouables.size(); i++){
									if (CartesJouables.get(i).RangNonAtout > CartesJouables.get(IndiceJouable).RangNonAtout) {
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
				for (int k = 0 ; k < 8 ; k++){
					if (Joueurs[q][k].jouee == false){
						CartesJouables.add(Joueurs[q][k]);
					}
				}
				for (Carte Carte : CartesJouables){
					if (CarteAtoutMaitre.isequal(Carte)){
						choix = CarteToIndice(Carte,q);
					}
				}
				if (choix == -1){
					for (Carte carte : CartesMaitres){
						if (contient(CartesJouables, carte)){
							CarteBientotJouee = carte;
						}
					}
					choix = CarteToIndice(CarteBientotJouee,q);
				}
				else {
					int IndiceJouable = 0;
					for (int i = 0 ; i < CartesJouables.size(); i++){
						if (CartesJouables.get(i).RangNonAtout > CartesJouables.get(IndiceJouable).RangNonAtout) {
							IndiceJouable = i ;
						}
					}
					CarteBientotJouee = CartesJouables.get(IndiceJouable);
					choix = CarteToIndice(CarteBientotJouee,q);
				}
			}
			
			Joueurs[q][choix].jouee = true ;

			if (q==1){
				container.Gauche = Joueurs[q][choix].picture[1];
				CartesPlateau.add(Joueurs[q][choix]);
				container.gauche = true;
				IAgauche.Cartes.get(choix).setVisible(false);
			}
			
			else if (q==2){
				container.Haut = Joueurs[q][choix].picture[0];
				CartesPlateau.add(Joueurs[q][choix]);
				container.haut = true;
				IAhaut.Cartes.get(choix).setVisible(false);
			}
			else if (q==3){
				container.Droite = Joueurs[q][choix].picture[1];
				CartesPlateau.add(Joueurs[q][choix]);
				container.droite = true;
				IAdroite.Cartes.get(choix).setVisible(false);
			}
			gauche.repaint();
			container.repaint();
			pause((int)(500/vitesse));
		}
		
		private void ActualiseRangAtoutMaitre() {
			RangAtoutMaitre = 8;
			for (Carte i : CartesPlateau){
				if (i.couleur == Atout && i.RangAtout < RangAtoutMaitre)
					RangAtoutMaitre = i.RangAtout;
			}
		}
		
		public void reboot(){
			for (int i = 0; i<8; i++){
				IAgauche.Cartes.get(i).setVisible(true);
				IAhaut.Cartes.get(i).setVisible(true);
				IAdroite.Cartes.get(i).setVisible(true);
			}
			for (Bouton b : Boutons)
				b.setVisible(true);
			
			AnnonceBas.setText("---");
			AnnonceGauche.setText("---");
			AnnonceHaut.setText("---");
			AnnonceDroite.setText("---");
			
			AnnonceBas.setVisible(true);
			AnnonceGauche.setVisible(true);
			AnnonceHaut.setVisible(true);
			AnnonceDroite.setVisible(true);
			
			JoueurGauche.AlwaysPassed=true;
			JoueurHaut.AlwaysPassed=true;
			JoueurDroite.AlwaysPassed=true;
			Humain.AlwaysPassed=true;
			
			annonc.setVisible(true);
		}

		public static void main(String[] args) {
			PartieEnCours.start();  //On lance un thread Instance de partie
		}
}
