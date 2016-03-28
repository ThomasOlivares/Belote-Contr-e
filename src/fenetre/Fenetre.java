package fenetre;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * N'hesitez pas à modifier/supprimer des choses si ça vous arrange
 * @authors Thomas, Meric, Paul, Martin
 */
public class Fenetre extends JFrame implements ActionListener, Observateur{
	
	private static final long serialVersionUID = 1L;
	
	//---------  Variables de la partie affichage-----------
	private JMenuBar menuBar = new JMenuBar();
	private JMenu partie = new JMenu("Partie");
	private JMenu aide = new JMenu("Aide");
	private JMenu aPropos = new JMenu("?");
	private JMenuItem lancer = new JMenuItem("Relancer une nouvelle partie");
	private JMenuItem menuPrincipal = new JMenuItem("Menu Principal");
	private JMenuItem quitter = new JMenuItem("Quitter le jeu");
	private JMenuItem item5 = new JMenuItem("Item");
	private JMenuItem item6 = new JMenuItem("Item");
	private static Instance PartieEnCours = new Instance("MenuPrincipal");
	
	//---------  Variables de la phase d'annonce -----------
	private List<BoutonAnnonces> annonceCoeur = new LinkedList<BoutonAnnonces>();   //Ces listes servent a ordonner les boutons d'annonces
	private List<BoutonAnnonces> annoncePique = new LinkedList<BoutonAnnonces>();
	private List<BoutonAnnonces> annonceCarreau = new LinkedList<BoutonAnnonces>();
	private List<BoutonAnnonces> annonceTrefle = new LinkedList<BoutonAnnonces>();
	private List<BoutonAnnonces> annonceSpecial = new LinkedList<BoutonAnnonces>();
	private int passe=0;   //Compte le nombre de joueurs qui passent leur tour
	private Annonce AnnonceGagnante = new Annonce();   //Indique la dernière annonce effectuée
	private JPanel annonc; // Panneau gérant tous les boutons d'annonces
	private LabelAnnonce AnnonceBas = new LabelAnnonce("---");  // Affichage des annonces (en rouge)
	private LabelAnnonce AnnonceGauche = new LabelAnnonce("---");
	private LabelAnnonce AnnonceHaut = new LabelAnnonce("---");
	private LabelAnnonce AnnonceDroite = new LabelAnnonce("---");
	
	//---------  Variables de la phase de jeu -----------
	private Carte[][] Joueurs = new Carte[4][8];  //Les cartes des joueur : la première ligne représente les cartes du joueur, les 3 autres celles des IA
	private Panneau container = new Panneau();  //Panneau général englobant tous les composants
	private JPanel bas = new JPanel();   // Panneaux contenants les cartes des joueurs
    private JPanel gauche = new JPanel();
    private JPanel droite = new JPanel();
    private JPanel haut = new JPanel();
    private List<Bouton> Boutons = new LinkedList<Bouton>();  // Liste des Cartes du joueur réel
    private List<Carte> Liste_Cartes = new LinkedList<Carte>();  //Liste des cartes utilisée (ce n'est pas le paquet)
    private Joueur Humain = new Joueur("humain");
    private IA IAgauche = new IA("gauche");
    private IA IAhaut = new IA("haut");
    private IA IAdroite = new IA("droite");
    private List<Carte> CartesMaitres = new LinkedList<Carte>();
    
    // ----------Variables mixtes ---------------
    private boolean ecouteAnnonce = false; //Indique si c'est au tour du joueur d'annoncer
    private boolean ecouteJoue = false; //Indique si c'est au tour du joueur de jouer
    private double vitesse = 1; //Indique la vitesse de jeu
    private int premier = 0;  // Indique qui commence a jouer, 0 : joueur, 1 : IAgauche, 2 : IAhaut, 3 : IAdroite
    private String etat = "MenuPrincipal"; //Indique la phase de jeu actuelle
    private JButton NvPartie;
    private JButton Options;
    private JButton Highscores;
    private String atout = "";
    
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
		this.setAlwaysOnTop(true);
    	
    	while (true){
    		if (etat == "MenuPrincipal")
    			MenuPrincipal();
    		else if(etat == "jeu")
    			PlateauJeu();
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
    	partie.add(quitter);
    	aide.add(item5);
    	aPropos.add(item6);
    	this.menuBar.add(partie);
        this.menuBar.add(aide);
        this.menuBar.add(aPropos);
        this.setJMenuBar(menuBar);
    }
    
    public void MenuPrincipal(){
    	this.setTitle("Menu Principal");
    	Font police = new Font("Tahoma", Font.BOLD, 30);
    	Font police2 = new Font("Tahoma", Font.BOLD, 50);
    	container = new Panneau();
    	container.setLayout(new GridLayout(4,1,20,20));
    	JLabel Bienvenue = new JLabel("Bienvenue sur le plateau de contrée");
    	NvPartie = new JButton("Nouvelle Partie");
    	Options = new JButton("Options");
    	Highscores = new JButton("Highscores");
    	
    	Bienvenue.setFont(police2);
    	Bienvenue.setForeground(Color.BLUE);
    	Bienvenue.setHorizontalAlignment(JLabel.CENTER);
    	NvPartie.setFont(police);
    	Options.setFont(police);
    	Highscores.setFont(police);
    	
    	NvPartie.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) { 
				etat = "jeu";
    		}
    	});
    	Options.addActionListener(this);
    	Highscores.addActionListener(this);
    	
    	NvPartie.setPreferredSize(new Dimension(500,200));
    	container.add(Bienvenue);
    	container.add(NvPartie);
    	container.add(Options);
    	container.add(Highscores);
    	this.setContentPane(container);
    	this.setVisible(true);
    	while(etat == "MenuPrincipal"){pause(10);} //On attend l'utilisateur
    }
    
    public void PlateauJeu(){
    	setMenu();
    	
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
			initialiseMainJoueur();  //Place les cartes dans les mains du joueurs et des IA
			initialiseMainIA();
			this.setSize(1202, 720); //Ces deux lignes servent a forcer le programme à réactualiser la fenêtre (j'ai pas trouver mieux)
			this.setSize(1202, 719);
			Phase_annonces();
			atout = AnnonceGagnante.couleur;
			initialiseCarteMaitres();
			int carte_jouer = 0;  //indique le nombre de carte déjà jouées
			while(carte_jouer<8){  //Boucle gérant une partie en phase de jeu
				int i=premier;  //i est le numero du joueur qui joue 
				for (int j = 0; j<4; j++){  //boucle gérant un tour
					if (i==0)
						play_joueur();
					else if (i==1)
						play_IA_gauche(carte_jouer);
					else if (i==2)
						play_IA_haut(carte_jouer);
					else if (i==3)
						play_IA_droite(carte_jouer);
					i=(i+1)%4;
				}
				pause((int)(500/vitesse));
				container.bas = false;  //On efface les cartes sur le tapis de jeu
				container.gauche = false;
				container.haut = false;
				container.droite = false;
				container.repaint();
				carte_jouer++;
			}
			premier = (premier+1)%4;  //On change le joueur qui commence
			initialise();
			distribue();
			trier();
			reboot();   //Rend toutes les cartes de nouveau visibles et réaffiche les boutons d'annonces
			pause((int)(1000/vitesse));
		}
	}
    
    public void initialiseCarteMaitres(){
    	if (atout != "coeur")
    		CartesMaitres.add(new Carte("as", "coeur"));
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
			System.out.println(b.echange1);
			System.out.println(b.echange2);
			if (b.echange1 && b.echange2 && ecouteJoue){  //On a cliquer sur un bouton
				container.Bas = b.picture;
				container.bas = true;
				b.setVisible(false);
				container.repaint();
				ecouteJoue=false;
			}
			else if (!b.echange1 && b.echange2){     //On a relacher la souris sur un bouton different de celui sur lequel on avait cliquer (pour échanger les cartes)
				int ind1 = -1;
				int ind2 = -1;
				for (int i = 0; i<8; i++){
					if (Boutons.get(i).echange1)
						ind1=i;
					if (Boutons.get(i).echange2)
						ind2=i;
				}
				Carte temp = Joueurs[0][ind1];
				Joueurs[0][ind1] = Joueurs[0][ind2];
				Joueurs[0][ind2] = temp;
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
		    	AnnonceBas.gagne=true;
		    	AnnonceGauche.gagne=false;
		    	AnnonceHaut.gagne=false;
		    	AnnonceDroite.gagne=false;
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
		    		Humain.DerniereAnnonce = new Annonce("passe", "", "joueur");
		    		passe++;
		    	}
		    		
		    }
		 }
			
		public void Phase_annonces(){
			passe = 0;
			int i=premier;  // Sert à gerer qui joue dans quel ordre
			AnnonceGagnante=new Annonce();
			annonceSpecial.get(1).setEnabled(false);  // On desactive Contré/Surcontré
			annonceSpecial.get(2).setEnabled(false);
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
				else if (i==1)
					annonceGauche();
				else if (i==2)
					annonceHaut();
				else if (i==3)
					annonceDroite();
				i=(i+1)%4;
			}
			AnnonceBas.setVisible(AnnonceBas.gagne); //On ne garde visible que les annonces "gagnantes" (annonce la plus élevé + éventuellemnt Contré/Surcontré)
			AnnonceGauche.setVisible(AnnonceGauche.gagne);
			AnnonceHaut.setVisible(AnnonceHaut.gagne);
			AnnonceDroite.setVisible(AnnonceDroite.gagne);
			annonc.setVisible(false);
		}
		
		public void initialiseAnnonces(){ //Initialise tous les boutons d'annonces (fonction encore un peu brouillone)
			
			JPanel centre = new JPanel();
			GridLayout grille = new GridLayout(4,12,5,5);
			centre.setLayout(grille);
			
			Font police = new Font("Tahoma", Font.BOLD, 11); //Font pour les CAPOT
			
			// ----Coeur-----
			for (int i = 0; i<11; i++){
				annonceCoeur.add(new BoutonAnnonces(Integer.toString(80+10*i) + "co"));
				annonceCoeur.get(i).addActionListener(new classique(Integer.toString(80+i*10), "coeur"));
				centre.add(annonceCoeur.get(i));
			}
			BoutonAnnonces coeurCAPOT = new BoutonAnnonces("CAPOT");
			coeurCAPOT.setFont(police);
			centre.add(coeurCAPOT);
			coeurCAPOT.addActionListener(new classique("CAPOT", "coeur"));
			annonceCoeur.add(coeurCAPOT);
			
			// ----Pique-----
			for (int i = 0; i<11; i++){
				annoncePique.add(new BoutonAnnonces(Integer.toString(80+10*i) + "pi"));
				annoncePique.get(i).addActionListener(new classique(Integer.toString(80+i*10), "pique"));
				centre.add(annoncePique.get(i));
			}
			BoutonAnnonces piqueCAPOT = new BoutonAnnonces("CAPOT");
			piqueCAPOT.setFont(police);
			centre.add(piqueCAPOT);
			piqueCAPOT.addActionListener(new classique("CAPOT", "pique"));
			annoncePique.add(piqueCAPOT);
			
			// ----Carreau-----
			for (int i = 0; i<11; i++){
				annonceCarreau.add(new BoutonAnnonces(Integer.toString(80+10*i) + "ca"));
				annonceCarreau.get(i).addActionListener(new classique(Integer.toString(80+i*10), "carreau"));
				centre.add(annonceCarreau.get(i));
			}
			BoutonAnnonces carreauCAPOT = new BoutonAnnonces("CAPOT");
			carreauCAPOT.setFont(police);
			centre.add(carreauCAPOT);
			carreauCAPOT.addActionListener(new classique("CAPOT", "carreau"));
			annonceCarreau.add(carreauCAPOT);
			
			// ----Trefle-----
			for (int i = 0; i<11; i++){
				annonceTrefle.add(new BoutonAnnonces(Integer.toString(80+10*i) + "tr"));
				annonceTrefle.get(i).addActionListener(new classique(Integer.toString(80+i*10), "trefle"));
				centre.add(annonceTrefle.get(i));
			}
			BoutonAnnonces trefleCAPOT = new BoutonAnnonces("CAPOT");
			trefleCAPOT.setFont(police);
			centre.add(trefleCAPOT);
			trefleCAPOT.addActionListener(new classique("CAPOT", "trefle"));
			annonceTrefle.add(trefleCAPOT);

			Font police2 = new Font("Tahoma", Font.BOLD, 30); //Font pour les annonces speciales
			
			// ----Autres-----
			JPanel special = new JPanel();
			special.setLayout(new GridLayout(1,3,5,5));
			BoutonAnnonces passe = new BoutonAnnonces("PASSE");
			BoutonAnnonces Contre = new BoutonAnnonces("Contrée!");
			BoutonAnnonces Surcontre = new BoutonAnnonces("Sur-Contrée!");
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
			pause((int)(100/vitesse));
			annonc.getComponent(0).setVisible(false);
			temp.getComponent(0).setVisible(false);
			pause((int)(400/vitesse));
		}
		
		public void annonceGauche(){  // Pour l'instant les IA ne font que passer
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
			for (int j = 0; j<4; j++){
				for (int c = 0; c<8; c++){
					int num = (int)(Math.random()*taille);
					provisoire.add(Liste_Cartes.get(num));
					Liste_Cartes.remove(num);
					taille--;
					Liste_Cartes = provisoire ;
				}
			}
		}
		
		public void cut(){
			List<Carte> provisoire1 = new LinkedList<Carte>();
			List<Carte> provisoire2 = new LinkedList<Carte>();
			int num = (int)(Math.random()*52);
			for (int i = 0; i <= num ; i++){
				provisoire1.add(Liste_Cartes.get(i));
				Liste_Cartes.remove(i);
			}
			for (int i = num+1; i <= 53 ; i++){
				provisoire2.add(Liste_Cartes.get(i));
				Liste_Cartes.remove(i);
			}
			for (int i = 0; i < provisoire1.size(); i++){
				Liste_Cartes.add(provisoire1.get(i));
			}
			for (int i = 0; i < provisoire2.size() ; i++){
				Liste_Cartes.add(provisoire2.get(i));
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
			
				Bouton b1 = new Bouton(Joueurs[0][0]);
				Bouton b2 = new Bouton(Joueurs[0][1]);
				Bouton b3 = new Bouton(Joueurs[0][2]);
				Bouton b4 = new Bouton(Joueurs[0][3]);
				Bouton b5 = new Bouton(Joueurs[0][4]);
				Bouton b6 = new Bouton(Joueurs[0][5]);
				Bouton b7 = new Bouton(Joueurs[0][6]);
				Bouton b8 = new Bouton(Joueurs[0][7]);
				
				Boutons.add(b1);
				Boutons.add(b2);
				Boutons.add(b3);
				Boutons.add(b4);
				Boutons.add(b5);
				Boutons.add(b6);
				Boutons.add(b7);
				Boutons.add(b8);
				
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
			ecouteJoue = true;
		    while(ecouteJoue){    //On attend que le joueur joue
		    	pause(5);
		    }
			pause((int)(500/vitesse));
		}
		
		public void play_IA_gauche(int choix){  // Joue systematiquement la carte de gauche
			container.Gauche = Joueurs[1][choix].picture[1];
			container.gauche = true;
			IAgauche.Cartes.get(choix).setVisible(false);
			gauche.repaint();
			container.repaint();
			pause((int)(500/vitesse));
		}
		
		public void play_IA_haut(int choix){
			container.Haut = Joueurs[2][choix].picture[0];
			container.haut = true;
			IAhaut.Cartes.get(choix).setVisible(false);
			container.repaint();
			pause((int)(500/vitesse));
		}
		
		public void play_IA_droite(int choix){
			container.Droite = Joueurs[3][choix].picture[1];
			container.droite = true;
			IAdroite.Cartes.get(choix).setVisible(false);
			container.repaint();
			pause((int)(500/vitesse));
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
			
			annonc.setVisible(true);
		}

		
		
		public static void main(String[] args) {
			PartieEnCours.start();  //On lance un thread Instance de partie
		}

		@Override
		public void checkEchange() {
			// TODO Auto-generated method stub
			
		}
}

