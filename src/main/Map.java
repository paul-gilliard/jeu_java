package main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.State;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Map extends JPanel  {
	
	public Espion espion;
	public Ligne ligne;
	public List<Ligne> listDeLigne = new ArrayList<Ligne>();
	public List<Case> listDeCase = new ArrayList<Case>();
	public List<Garde> listDeGarde = new ArrayList<Garde>();
	public List<Thread> listDeThread = new ArrayList<Thread>();
	public ArrayList<monThread> listPointeurDeThread = new ArrayList<monThread>();
	public Image img;
	public Image imgGarde;
	public Image imgTresor;
	public Case caseTresor;
	private int compteur=0;
	public int tailleCase;
	public boolean DejaAfficherPopUp=false;
	
	public Map(float height, float width) {			// Classe centrale qui appele la plupart de nos fonction
		 int NombreDeCase = 15;
		
		
		 int nombreDeGarde=4;										// Défini le nombre de gardes
		 int bordCase = 100;
		tailleCase = (int)  ((height- 2* bordCase)/	NombreDeCase);				// Calcule en "responsive" la map en fonction de la fenetre  (Si on divise par 100 on aura plus de cases )
		
		List<Ligne> ListDeLigne= creationLigne(height - 2* bordCase,tailleCase);
		List<Case> listDeCase=creationCase(ListDeLigne, height - 2* bordCase ,tailleCase);
		Case caseActuelle = (Case) listDeCase.get(0);
		
			
		relationEntreLesCases1(caseActuelle, listDeCase,ligne.nombreDeCase); 		
		relationEntreLesCases2(listDeCase);
		AreteEntreLesCases1(listDeCase);
		
		espion = creationEspion(tailleCase, listDeCase);
		CreationTresor();
		
		creationGarde(tailleCase,nombreDeGarde, this);
		
	}
	private List<Ligne> creationLigne(float height, float tailleCase){ 			// Fonction qui créer des lignes de ligne , qui serviront à bien placer nos futures cases sur la fenêtre
		for( float i=tailleCase; i <= height; i=i+tailleCase) {
			listDeLigne.add(new Ligne(i,1)); 
		}
		
		for (int i =0; i<listDeLigne.size() ; i++ )
		{
			ligne = (Ligne) listDeLigne.get(i);
			ligne.nombreDeCase = listDeLigne.size();
		}
		return listDeLigne;
	}
	
	private List<Case> creationCase(List<Ligne> listDeLigne, float height, float tailleCase) { // Fonction qui crée nos cases en fonction des lignes et de leur taille pour un beau carré de case
		int NumeroDeCase = 1;
		for(int i=0;i<listDeLigne.size();i++){			
			
			for (float j=tailleCase ; j <=height; j=j+tailleCase , NumeroDeCase++){
				ligne = (Ligne) listDeLigne.get(i);
				if (NumeroDeCase % ligne.nombreDeCase ==0 )
				{
				listDeCase.add(new Case(j, ligne.positionY, tailleCase, null, NumeroDeCase, true, null, null , null, null , new ArrayList<>() ,  new ArrayList<>(), null,null,false,false,true, 0, false,10000,0));
				}
				else
				{
				listDeCase.add(new Case(j, ligne.positionY, tailleCase, null, NumeroDeCase, null, null, null , null, null , new ArrayList<>() ,  new ArrayList<>(), null,null,false,false,true,0, false,10000,0));
				}
			}
		}
			for(int i=0;i<listDeCase.size();i++){							// Je choisis aléatoirement un type de case ( eau , boue , herbe , mur )

				Case CaseEnModification = listDeCase.get(i);
				CaseEnModification.type="mur";
				double randomNumber = Math.random()*12; 
				
				if((int)randomNumber<=5)
				{
					CaseEnModification.type="herbe";
				}
				else if ((int)randomNumber>5 && (int)randomNumber<=8 )
				{
					CaseEnModification.type="mur";
				}
				else if ((int)randomNumber>8 && (int)randomNumber<11 )
				{
					CaseEnModification.type="boue";
				}
				else 
				{
					CaseEnModification.type="eau";
				}
				
			}
			
		return listDeCase;
	}
	
	public void relationEntreLesCases1(Case caseActuelle, List listDeCase, int NombreDeCaseParLigne) {  // Fonction permet de créer la relation entre les cases bas et droite recursivement 
	
		
		if (caseActuelle.NumeroDeCase > listDeCase.size())  {
        	return;
        }	
	    else if (caseActuelle.caseBas != null || caseActuelle.caseDroite !=null ) {
	        	return;			
		}
	       
	    else {
	    	if (caseActuelle.CaseBord == null) {
	    		
	    			caseActuelle.caseDroite = (Case) listDeCase.get((int) caseActuelle.NumeroDeCase);
	    			relationEntreLesCases1(caseActuelle.caseDroite, listDeCase, NombreDeCaseParLigne);
	        		
	        	   } 
	          	
	        if (caseActuelle.NumeroDeCase <= listDeCase.size() - NombreDeCaseParLigne) {
	        	
	            	caseActuelle.caseBas =(Case) listDeCase.get((int)caseActuelle.NumeroDeCase + NombreDeCaseParLigne -1 ); 
	                relationEntreLesCases1(caseActuelle.caseBas, listDeCase, NombreDeCaseParLigne);
	            }
	        }
	}
	
	public void relationEntreLesCases2(List ListeCase) {						 // Fonction réciproque de (relationentrelescase1) on associe haut et gauche 

        for (int w = 0; w < ListeCase.size(); w++) {

            Case relation = (Case) ListeCase.get(w);

            if (relation.caseBas != null) {
                relation.caseBas.caseHaut = relation;
            }
        }

        for (int i = 0; i < ListeCase.size(); i++) {

            Case relation1 = (Case) ListeCase.get(i);

            if (relation1.caseDroite != null) {
                relation1.caseDroite.caseGauche = relation1;
            }
        }
    }
	
	public void AreteEntreLesCases1(List ListeCase) {							//  Fonction qui créer les arrêtes sortante en fonction des relations entre les cases fait auparavant recursivement
		
		for(int i = 0 ; i < listDeCase.size() ; i ++){
			
			int Valuation;
			int ValuationEau = 3;
			int ValuationHerbe = 1;
			int ValuationBoue = 2; 
			int ValuationMur=0 ;
			
			Case CaseActuel = (Case) ListeCase.get(i);						// On ira dans cet ordre de plus à moins vite : herbe -> boue -> eau 
			switch (CaseActuel.type) {										// On ne créer pas d'arrête pour aller vers les murs
			case "eau": 
				Valuation = ValuationEau;
				break;
			case "herbe":
				Valuation =ValuationHerbe;
				break;
			case "boue":
				Valuation =ValuationBoue;
				break;
			case "mur":
				Valuation =ValuationMur;
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + CaseActuel.type);
			}
			
			
			 if (CaseActuel.caseDroite != null && CaseActuel.caseDroite.type != "mur" && CaseActuel.type != "mur"){
				 
				CaseActuel.listeAreteSortante.add(new Arete(Valuation, CaseActuel, CaseActuel.caseDroite, "droite"));
			 }
			 if (CaseActuel.caseGauche != null && CaseActuel.caseGauche.type != "mur" && CaseActuel.type != "mur"){
				
				CaseActuel.listeAreteSortante.add(new Arete(Valuation, CaseActuel, CaseActuel.caseGauche, "gauche"));
			 }
			 
			 if (CaseActuel.caseHaut != null && CaseActuel.caseHaut.type != "mur" && CaseActuel.type != "mur"){
				
				CaseActuel.listeAreteSortante.add(new Arete(Valuation, CaseActuel, CaseActuel.caseHaut, "haut"));
			 }
			 if (CaseActuel.caseBas != null && CaseActuel.caseBas.type != "mur" && CaseActuel.type != "mur"){
				
				CaseActuel.listeAreteSortante.add(new Arete(Valuation, CaseActuel, CaseActuel.caseBas, "bas"));
			 }
		}
		for(int j = 0 ; j < listDeCase.size() ; j ++){
			
			Case CaseActuel = (Case) ListeCase.get(j);
			for (int k = 0; k < CaseActuel.listeAreteSortante.size(); k++) {
				
				Arete areteAcutelle = CaseActuel.listeAreteSortante.get(k);
				Case caseSortante = areteAcutelle.caseSortante;
				caseSortante.listeAreteEntrante.add(areteAcutelle); // Ici , on fait la réciproque pour définir les cases entrantes (une arrête sortante vers une case et une arrête entrante pour celle-ci)
			}
		}
	}
	
	public void CreationTresor()												// Fonction qui place un trésor à une case atteignable par l'espion (Algo de  parcours en largeur) 
	{	
		String cheminImage = new File("").getAbsolutePath()+"\\tresor.png";		// Prends le nom du chemin de la photo donnée dans le repertoir du projet
		File file = new File(cheminImage);				
		
		try {
			 imgTresor = ImageIO.read(file);																// Lire l'image
			 imgTresor = imgTresor.getScaledInstance(tailleCase-1, tailleCase-1, imgTresor.SCALE_SMOOTH);	// Redimensionne la photo en fonction d'une case.
			}
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		//******** Debut parcours en largeur ********//
		
		List<Case> f = new ArrayList<Case>();
		List<Case> casesPotentiellesTresors = new ArrayList<Case>();
		
		Case s;
		f.add(espion.saCase);
		
		for (Case case1 : listDeCase) {
			
			case1.vu=false;
		}
		
		espion.saCase.vu=true;
		
		while (f.size()>0) {
			s=f.get(f.size()-1);
			f.remove(f.size()-1);
			
			for (int i = 0; i <s.listeAreteSortante.size(); i++) {
				Case x = s.listeAreteSortante.get(i).caseSortante;
				
				if(x.vu==false)
				{
					f.add(x);
					x.vu=true;
					casesPotentiellesTresors.add(x);	
				}
			}
		}
		
		//******** Fin parcours en largeur ********//
		
		
		 int Max= casesPotentiellesTresors.size(); 								// Le trésor peut se trouver au plus loin possible de l'espion mais avec un chemin existant
		 int Min= 1; 															// Le trésor se trouvera au minimum à une distance de 1 case
		 int randomNumber = Min + (int)(Math.random() * ((Max - Min) + 1));
		 
		 Case saCase = casesPotentiellesTresors.get((int)randomNumber-1);      
		 saCase.type="tresor";													// Ici on associe un type trésor à une case
		}
	
	private void creationGarde(int tailleCase, int nombreDeGarde, Map map)		// Fonction qui permet de créer les gardes , qui leur associe un champ de vision random , une photo
	{
		String cheminImage = new File("").getAbsolutePath()+"\\illustration-dessin-anime-garde-du-corps_11460-2284.jpg";
		File file = new File(cheminImage);
		try {
			 imgGarde = ImageIO.read(file);
			 imgGarde = imgGarde.getScaledInstance(tailleCase-1, tailleCase-1, imgGarde.SCALE_SMOOTH);
			 
			 
			for(int i=0; i<nombreDeGarde;i++) {
				 double randomNumber = Math.random()*listDeCase.size(); 
				 while (listDeCase.get((int)randomNumber).type == "mur") {
				 
					 randomNumber = Math.random()*listDeCase.size(); 
				 }
	
				 int MaxLongueurVisionGarde= 6;									//Max possible du champ de vision ( en aleatoire )
				 int MinLongueurVisionGarde= 2;									//Minpossible du champ de vision ( en aleatoire )
				 int longueurVisionDuGarde = MinLongueurVisionGarde + (int)(Math.random() * ((MaxLongueurVisionGarde - MinLongueurVisionGarde) + 1));
	
				 Case saCase = listDeCase.get((int)randomNumber-1);
				 listDeGarde.add(new Garde(imgGarde,saCase,null, map,false, new ArrayList<>(),longueurVisionDuGarde, new ArrayList<Case>(), new ArrayList<Case>()));
				 saCase.garde=listDeGarde.get(i);
			} 
		} 

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			 
	}
	
	private Espion creationEspion (int tailleCase, List<Case> listDeCase) { 	//  Fonction qui créer l'affichage de l'image espion et lui attribut sa position de départ
		
		String cheminImage = new File("").getAbsolutePath()+"\\unknown.png";
		File file = new File(cheminImage);
		try {
			
			 img = ImageIO.read(file);
			 img = img.getScaledInstance(tailleCase-1, tailleCase-1, img.SCALE_SMOOTH);
			 
			 double randomNumber = Math.random()*listDeCase.size(); 
			 
			 while (listDeCase.get((int)randomNumber-1).type == "mur") {
			 
				 randomNumber = Math.random()*listDeCase.size(); 
			 
			 }
			 
			 Case saCase = listDeCase.get((int)randomNumber-1);
			 espion= new Espion(img,saCase,this);
			 saCase.espion=espion;
			 return espion;
		
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
		return espion;
		
	}

	public void miseEnDeplacementGarde() {										//Fonction qui permet de Lancer un thread par garde , et commencer son déplacement random (dans mon thread.java)
		 
		for ( int iterationThread = 0; iterationThread < listDeGarde.size();iterationThread++) {
		
			monThread pointeurThread=new monThread(iterationThread,listDeGarde, true, this);
			listPointeurDeThread.add(pointeurThread);
			Thread thread = new Thread(pointeurThread);
			thread.start();
			listDeThread.add( thread);
		}			
}

	public void ArretJeuPerdu(monThread monThread) {							// Fonction qui permet l'arrêt du jeu et qui permet l'arrêt de tous les threads 
    	for (int i = 0; i < listDeGarde.size(); i++) {
    		listDeGarde.get(i).fautStopTonWhileMaintenant=true;				// Je dis à tous les threads de s'arrêter.
			}
    	
    	for (int i = 0; i < listDeThread.size(); i++) {
    	
    		if(listPointeurDeThread.get(i)== monThread){
    			
    		Thread thread = listDeThread.get(i);				
    		thread.interrupt();												// Le thread arrivé ici s'interrompt lui-même une fois qu'il a arrêté  tout traitement
    		}
    	}
    		
    	int compteurSecurite=0; 											
    	
		for (int i = 0; i < listDeThread.size(); i++) {					//  Vérification de l'arrêt de tous les threads 
			
			if(listDeThread.get(i).getState()==State.TERMINATED)
			{
				compteurSecurite=compteurSecurite+1;
			}
			
			if(compteurSecurite==listDeThread.size()-1) {					//  Si tous arrêter alors on envoie un message pop-up , en fonction de ce qu'il s'est passé
               
				if(DejaAfficherPopUp==false) {								//  Pour afficher que un pop up de fin
				
                if(compteur!=1 && listDeThread.size()>1) {
                
                	
                	
                    if(this.espion.saCase== this.caseTresor){				// Si la case espion est sur la case trésor
                        JOptionPane.showMessageDialog(null, 
                                "Bien joué tu as gagné", 
                                "Bien joué tu as gagné ", 
                                JOptionPane.INFORMATION_MESSAGE);
                        DejaAfficherPopUp=true;
                    }
                    else {
                        JOptionPane.showMessageDialog(null, 			// Sinon il est sur le garde (c'est la seul fois que l'on peut perdre)
                                "GAME OVER", 
                                "Game Over", 
                                JOptionPane.INFORMATION_MESSAGE);
                        DejaAfficherPopUp=true;
                        }
                    compteur=compteur+1;
                }

                else {
                    
                    if(this.espion.saCase == this.caseTresor){			//  Si case espion est sur la case du trésor
                        JOptionPane.showMessageDialog(null, 
                                "Bien joué tu as gagné", 
                                "Bien joué tu as gagné ", 
                                JOptionPane.INFORMATION_MESSAGE);
                        DejaAfficherPopUp=true;
                    }
                    else {												//  Sinon il est sur le garde (c'est la seul fois que l'on peut perdre)
                        JOptionPane.showMessageDialog(null, 
                                "GAME OVER", 
                                "Game Over", 
                                JOptionPane.INFORMATION_MESSAGE);
                        DejaAfficherPopUp=true;
                        }
                    }
                }}
			}
		}
    
	public void paintComponent(Graphics g) {									// Fonction qui dessine tout :)=

			for (int j=0; j<listDeCase.size();j++) {
				Case Case = listDeCase.get(j);
				
				if (Case.type == "herbe" && Case.estDansLeBrouillardDeGuerre==true) {			// Dessin pour brouillard de guerre
					g.setColor(new Color(43, 175, 27).darker());
				}
				if (Case.type == "herbe" && Case.estDansLeBrouillardDeGuerre==false){	
					g.setColor(new Color(43, 175, 27).brighter());	
				}
				if (Case.type == "mur" ) {
					g.setColor(Color.black);
				}
				
				if (Case.type == "boue"  && Case.estDansLeBrouillardDeGuerre==true) {			// Dessin pour brouillard de guerre
					g.setColor(new Color(89, 52, 32).darker());
				}
				if (Case.type == "boue" && Case.estDansLeBrouillardDeGuerre==false) {
					g.setColor(new Color(89, 52, 32).brighter());
				}
				if (Case.type == "eau"  && Case.estDansLeBrouillardDeGuerre==true) {			// Dessin pour brouillard de guerre
					g.setColor(Color.cyan.darker());
				}
				if (Case.type == "eau" && Case.estDansLeBrouillardDeGuerre==false){
					g.setColor(Color.cyan.brighter());
				}
				
				if (Case.type == "tresor"){
					caseTresor = Case;
				}
				
				if(Case.compteur==10){
					g.setColor(Color.pink);
				}
		        
				g.fillRect((int)Case.positionX, (int)Case.positionY,(int) Case.tailleCase ,(int) Case.tailleCase );		// Dessine la case
		     
				}
			
			g.drawImage(img, (int )espion.saCase.positionX, (int )espion.saCase.positionY, null);			// Dessin espion
			g.drawImage(imgTresor, (int) caseTresor.positionX, (int )caseTresor.positionY, null);	 		// Dessin trésor
			
			for (int i=0; i<listDeGarde.size();i++){
				g.drawImage(imgGarde, (int )listDeGarde.get(i).saCase.positionX, (int )listDeGarde.get(i).saCase.positionY, null);		// Dessine chaque garde
				
			}
		}
	}

