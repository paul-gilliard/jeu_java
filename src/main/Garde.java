package main;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Garde { 
	
	Image img;
	Case saCase;
	String direction;
	Map map;
	boolean fautStopTonWhileMaintenant;
	ArrayList<Case> listDeCaseVisible;
	int longueurVision;//longueurVision *2 = nombre d'ieration possible
	int compteur = 0;
	boolean plusAGauche;
	boolean plusADroite;
	boolean plusEnHaut;
	boolean plusEnBas;
	ArrayList<Case> listDeCasePotentiellementVisible;
	public List<Case> F= new ArrayList<Case>();
	public List<Case> S= new ArrayList<Case>();
	public List<Case> listDeVerif= new ArrayList<Case>();
	List<Case> listcheminopti= new ArrayList<Case>();
	

	Garde(Image img, Case saCase, String direction,Map map, boolean fautStopTonWhileMaintenant, ArrayList<Case> listDeCaseVisible, int longueurVision,ArrayList<Case> listDeCasePotentiellementVisible,List<Case> listcheminopti ){
			
			this.img= img;
			this.saCase=saCase;
			this.direction=direction;
			this.map=map;
			this.fautStopTonWhileMaintenant=fautStopTonWhileMaintenant;
			this.listDeCaseVisible=listDeCaseVisible;
			this.longueurVision=longueurVision;
			this.listDeCasePotentiellementVisible=listDeCasePotentiellementVisible;
			this.listcheminopti=listcheminopti;
			
		}
	
 public boolean deplacementRandom() /* M�thode Appel� � l'initialisation dans chaque Thread (lui-m�me propre � un Garde) qui lui permet de se d�placer en ligne droite jusqu'� un obstacle
 								    Et il change de direction al�atoirement une fois avoir rencontr� un mur. Apr�s chaque d�placement, le Garde recalcule son champ de vision avec la m�thode remplirListPotentiellementVisible()
	 								Cette m�thode retourne false quand nous voulons que le garde s'arr�te. Car une fois qu'il a return, on arrive � la ligne map.ArretJeuPerdu(this); dans monThread.Java, qui arr�te le jeu ainsi
	 								Que tous les Threads.*/
 {
	 if (this.saCase==map.espion.saCase){
		 return false;
		
	 }

	 int Max=3;
	 int Min= 0;
	 int nombreAleatoire1 = Min + (int)(Math.random() * ((Max - Min) + 1));
	 
	 switch (nombreAleatoire1) { // Le garde choisit une direction al�atoire pour se d�placer.
		case 0:
			this.direction="droite";
			break;
		case 1:
			this.direction="gauche";
			break;
		case 2:
			this.direction="haut";
			break;
		case 3:
			this.direction="bas";
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + direction);
		}

	 if(this.direction=="droite"){
		 if (this.saCase.caseDroite ==null ) {
			
			deplacementRandom(); // S'il est sur un bord de notre map au d�but de son d�placement, et qu'il veut aller dans le "vide", on recommence pour choisir une autre direction al�atoire
		 }
		 else {

			 while(this.saCase.caseDroite.type!="mur") // Sinon, tant qu'il ne voit pas de mur et qu'il ne verra pas l'espion, il avancera dans la direction choisie
			 {
				 int SleepTime=0;
				 if(this.fautStopTonWhileMaintenant==true){  //Un autre object demande au garde de s'arreter
					 return false;
				 }
				 
				 try {

					 for(int i=0 ; i<this.saCase.listeAreteSortante.size() ; i++){
						 
							if(this.saCase.listeAreteSortante.get(i).direction=="droite"){
								
								SleepTime= this.saCase.listeAreteSortante.get(i).Valuation*1000; // On associe un SleepTime en fonction de la case sur laquelle il est et ses valuations d'ar�tes sortantes
							}
						}
					
					 Thread.sleep(SleepTime); // On attend de pouvoir bouger
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				 
				
				 this.saCase.caseDroite.garde=this; // On dit � nouvelle case qu'elle a d�sormais le garde sur elle.
				 this.saCase=saCase.caseDroite; 	// On dit � l'espion quelle est sa nouvelle case.
				 this.saCase.caseGauche.garde=null;	// On dit � l'ancienne case qu'elle n'a plus le garde sur elle.
				 
				 this.remplirListPotentiellementVisible(this.saCase); //On commence � construire notre boruillard de guerre visible
				 
				 this.map.paintImmediately(0,0,2000,2000); // On repaint apr�s avoir cr�e notre brouillard de guerre visible
				 
				 for (Case case1 : this.listDeCaseVisible) { // on r�initialise la list des cases visibles pour sa prochaine utilisation
					 
					 case1.estReelementVisible=false;
					 case1.estDansLeBrouillardDeGuerre=true;
				}
				 
				 if (this.saCase==map.espion.saCase){
					 
					 return false; // Si le garde touche l'espion avec ses d�placements random -> On arr�te tout 
					
				 }
				 
				 
				 if(this.saCase.caseDroite == null) { // S'il est sur un bord de notre map � la fin de son d�placement, et qu'il veut aller dans le "vide", on recommence pour choisir une autre direction al�atoire
					 break;
					 }
			 }
			 
			 
			 if( this.saCase.caseDroite == null || this.saCase.caseDroite.type == "mur"  ) { // S'l est sur un bord de notre map ou contre un mur � la fin de son d�placement, et qu'il veut aller dans le "vide", on recommence pour choisir une autre direction al�atoire
			
				 deplacementRandom();
			 }		
			 }
		 }

	 else if(this.direction=="gauche") // m�me commentaire  que la direction droite
	 {
		 if (this.saCase.caseGauche ==null ) {
				deplacementRandom();
			 }
			 else {

		 
		 while(this.saCase.caseGauche.type!="mur")
		 {
			 int SleepTime=0;
			 if(this.fautStopTonWhileMaintenant==true){
				 
				 return false;
			 }
			 
			 try {
				 
				 for(int i=0 ; i<this.saCase.listeAreteSortante.size() ; i++){
					 
						if(this.saCase.listeAreteSortante.get(i).direction=="gauche"){
							 
							SleepTime= this.saCase.listeAreteSortante.get(i).Valuation*1000;
						}
					}
				 Thread.sleep(SleepTime);
							
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
			 this.saCase.caseGauche.garde=this;
			 this.saCase=saCase.caseGauche;
			 this.saCase.caseDroite.garde=null;
			 
			 this.remplirListPotentiellementVisible(this.saCase);
			 
			 this.map.paintImmediately(0,0,2000,2000); 
			 
			 for (Case case1 : this.listDeCaseVisible) { 
				 
				 case1.estReelementVisible=false;
				 case1.estDansLeBrouillardDeGuerre=true;
			}
			 
			 if (this.saCase==map.espion.saCase) {
				 
				 return false;
			 }
			  
			 if(this.saCase.caseGauche == null) {
				 break;
			 }
			 }
		 if(this.saCase.caseGauche == null || this.saCase.caseGauche.type=="mur") {
			deplacementRandom();
			}	
		 }
		 }

	 
	 else if(this.direction=="haut") // m�me commentaire que direction droite
	 {
		 if (this.saCase.caseHaut ==null ) {
				deplacementRandom();
			 }
		 else {
		 
			 while(this.saCase.caseHaut.type!="mur" ){
				 
				 int SleepTime=0;
				 if(this.fautStopTonWhileMaintenant==true){
					 
					 return false;
				 }
				 
				 try {
					 for(int i=0 ; i<this.saCase.listeAreteSortante.size() ; i++){
						 
							if(this.saCase.listeAreteSortante.get(i).direction=="haut"){
								
								SleepTime= this.saCase.listeAreteSortante.get(i).Valuation*1000;
							}
					 	}
					 
					 Thread.sleep(SleepTime);
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				 this.saCase.caseHaut.garde=this;
				 this.saCase=saCase.caseHaut;
				 this.saCase.caseBas.garde=null;
				
				 
				 this.remplirListPotentiellementVisible(this.saCase);
				 
				 this.map.paintImmediately(0,0,2000,2000);
				 
					 for (Case case1 : this.listDeCaseVisible) { 
									 
									 case1.estReelementVisible=false;
									 case1.estDansLeBrouillardDeGuerre=true;
								}

				 if (this.saCase==map.espion.saCase) {
					 
					 return false; 
				 }
				 
				 
				 if(this.saCase.caseHaut == null) {
					 break;
				 }
			 }
			 if(this.saCase.caseHaut == null || this.saCase.caseHaut.type=="mur") {
				 deplacementRandom();
			 }
			 }	
	 }

	 else if(this.direction=="bas") // m�me commentaire que direction droite
	 {
		 if (this.saCase.caseBas ==null ) {
				deplacementRandom();			
			 }
			 else {
	
		 while(this.saCase.caseBas.type!="mur" )
		 {
			 int SleepTime=0;
			 if(this.fautStopTonWhileMaintenant==true)
			 {
				 return false;
				 
			 }
			 
		 	try {
		 		for(int i=0 ; i<this.saCase.listeAreteSortante.size() ; i++)
				{
					if(this.saCase.listeAreteSortante.get(i).direction=="bas")
					{
						SleepTime= this.saCase.listeAreteSortante.get(i).Valuation*1000;
						
					}
				}
		 		Thread.sleep(SleepTime);
			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			 this.saCase.caseBas.garde=this;
			 this.saCase=saCase.caseBas;
			 this.saCase.caseHaut.garde=null;
			 
			 this.remplirListPotentiellementVisible(this.saCase);
			 this.map.paintImmediately(0,0,2000,2000); 
			  
			 for (Case case1 : this.listDeCaseVisible) { 
				 
				 case1.estReelementVisible=false;
				 case1.estDansLeBrouillardDeGuerre=true;
				
			}
			 if (this.saCase==map.espion.saCase)
			 {
				
				 return false;
				 
			 }
			 
			 
			 if(this.saCase.caseBas == null) {break;
			 }
		}
		 
		 if(this.saCase.caseBas == null || this.saCase.caseBas.type=="mur") {
			 deplacementRandom();
		 }
			 }
			
		 }
	return false;
	 
	 }

private void remplirListPotentiellementVisible(Case CaseActuelle) { /*M�thode appel�e dans d�placementrandom(), premi�re �tape de notre Algorithme de vision, celui ci ne prend pas en compte des murs
 																		Une fois que cette m�thode est appliqu�e, elle appel "voiEnLargeur()"*/
	
	ArrayList<Case> listDeCaseAPasAssombrir = new ArrayList<Case>();
	 
	float positionX = CaseActuelle.positionX;
	float positionY = CaseActuelle.positionY;
	
	float CarreHautGauchepositionX = positionX - longueurVision*map.tailleCase;
	float CarreHautGauchepositionY = positionY - longueurVision*map.tailleCase;
	
	float CarreHautDroitepositionX = positionX + longueurVision*map.tailleCase;
	float CarreHautDroitepositionY = positionY - longueurVision*map.tailleCase;
	
	float CarreBasDroitepositionX = positionX + longueurVision*map.tailleCase;
	float CarreBasDroitepositionY = positionY + longueurVision*map.tailleCase;
	
	float CarreBasGauchepositionX = positionX - longueurVision*map.tailleCase;
	float CarreBasGauchepositionY = positionY + longueurVision*map.tailleCase;
	
	//Calcul en fonction de x y le carr� � �clairer, dans un premier temps
	
		
		for (int k = 0; k < this.listDeCasePotentiellementVisible.size(); k++) { // Tout ce for sert � r�initialiser les casesPotentiellementVisible d'avant, en v�rifiant que les cases que l'on veut re assombrir ne doivent pas �tre �clairer par un autre garde
			
			Case case1 = this.listDeCasePotentiellementVisible.get(k);
		
			compteur=0;
		for (int i = 0; i < map.listDeGarde.size(); i++) {
			Garde garde = map.listDeGarde.get(i);
			
			
			for (int j = 0; j < garde.listDeCasePotentiellementVisible.size(); j++) {
				
				if(garde.listDeCasePotentiellementVisible.get(j) == case1 && compteur==0)
				{
					compteur=1;
					
				}
				else if (garde.listDeCasePotentiellementVisible.get(j) == case1 && compteur==1) {
					
					listDeCaseAPasAssombrir.add(case1);
					case1.estDansLeBrouillardDeGuerre=true;
				}
				else {
					case1.estDansLeBrouillardDeGuerre=true;
				}
			}
			
		}
		
		
		
	}
		
		for (Case case1 : listDeCaseAPasAssombrir) { // Si le garde veut l'effacer en se d�placent mais que un garde voit cette m�me case, on la laisse hors du BrouillardDeGuerre
			case1.estDansLeBrouillardDeGuerre=false;
		}
		listDeCaseAPasAssombrir.clear(); // on clear pour la prochaine utilisation
	
		this.listDeCasePotentiellementVisible.clear();
	
	
	for(int i = 0 ; i< map.listDeCase.size(); i++) // Tout ce for sert � �clairer un carr� de case autour du garde en fonction de sa vision, ce seront des cases potentiellement visibles du garde
	{	
		Case Case = map.listDeCase.get(i);
		
		if(Case.positionX >= CarreHautGauchepositionX && Case.positionY >= CarreHautGauchepositionY  && Case.positionX <= CarreBasDroitepositionX && Case.positionY <= CarreBasDroitepositionY && Case.positionX<= CarreHautDroitepositionX && Case.positionY >= CarreHautDroitepositionY && Case.positionX >= CarreBasGauchepositionX && Case.positionY<= CarreBasGauchepositionY)
		{
			
			Case.estPotentielementVisible=true;
			this.listDeCasePotentiellementVisible.add(Case);
		}
	}
	 voirEnLargeur(this.listDeCasePotentiellementVisible, this.saCase); // Une fois qu'on a nos cases potentilles, on explore les cases qui seront vraiment visibles


	 for (int i = 0; i < this.listDeCasePotentiellementVisible.size(); i++) {
		this.listDeCasePotentiellementVisible.get(i).estPotentielementVisible=false; // On clear pour prochaine utilisation
		
	}
	 
	
}

private void voirEnLargeur(List<Case> X, Case saCase) /* Simple algo de parcours en largeur, en fonction de la vision du garde, et de ses cases potentillemenVisible donn� par la fonction pr�c�dente*/
	{
	
	List<Case> f = new ArrayList<Case>();
	Case s;
	f.add(saCase);
	F.add(saCase);
	
	for (Case case1 : X) {
		
		case1.estPotentielementVisible=false;
		case1.distance=10000;
	}
	saCase.estPotentielementVisible=true;
	saCase.distance=0;
	
	while (f.size()>0) {
		s=f.get(f.size()-1);
		f.remove(f.size()-1);
		
		for (int i = 0; i <s.listeAreteSortante.size(); i++) {
			Case x = s.listeAreteSortante.get(i).caseSortante;
			
			
			
			if(x.estPotentielementVisible==false && s.distance<this.longueurVision)
			{
				f.add(x);
				F.add(x);
				x.estPotentielementVisible=true;
				
				x.distance=s.distance+1;
				x.estDansLeBrouillardDeGuerre=false;
				
			}
		}
	
		
	}
	
	for (int i = 0; i < F.size(); i++) { //Je regarde dans toutes mes cases que je vois en largeur.
		
		Case case1=  F.get(i);
		
	
		if(case1== map.espion.saCase)
		{
			charger(F,case1); // S'il y a l'espion je le charge
			F.clear(); //Je clear pour prochaine utilisation
		}
	
		
	}
	F.clear(); //Je clear pour prochaine utilisation
	}

public void charger(List<Case> listDeCasesVisibles, Case caseEspion) /* Cette m�thode ex�cute Algoriithme de Dijkstra pour mettre les lambda optimaux, Une fois ex�cuter, on cherche le chemin optimal qui est un algo
 																		d�riv� du parcours en largeur */
{	
	 this.map.paintImmediately(0,0,2000,2000);
	
	 //***************D�but Algoriithme de Dijkstra******************
	 
	List<Case> listMemoire= new ArrayList<>(listDeCasesVisibles);
	
	Case caseLambdaMini=this.saCase;
	
	
	List<Case> S = new ArrayList<Case>();
	S.clear();
	
	
	for (int i = 0; i < listDeCasesVisibles.size(); i++) {
		Case case1= listDeCasesVisibles.get(i);
		case1.lambda=10000;
		case1.compteur=0;
	}
	
	for (int i = 0; i <this.saCase.listeAreteSortante.size(); i++) {
		Case x = this.saCase.listeAreteSortante.get(i).caseSortante;
		x.lambda= this.saCase.listeAreteSortante.get(i).Valuation;
	}

	this.saCase.lambda=0;
	listDeVerif.clear();
	while (listDeVerif.size()<=listMemoire.size()) {
		
				Iterator<Case> IteratorTest = listDeCasesVisibles.iterator();
			Case case11 = null;
			while (IteratorTest.hasNext()) {
				Case case1 = (Case) IteratorTest.next();
				
				if(case1.equals(caseLambdaMini))
				{
					;
					IteratorTest.remove();
				}
				}
					if(listDeCasesVisibles.size()!=0)
					{
						caseLambdaMini=listDeCasesVisibles.get(0);
					}
					
					for (int i = 0; i <listDeCasesVisibles.size(); i++) {
						case11 = listDeCasesVisibles.get(i);
						if(case11.lambda<caseLambdaMini.lambda)
						{
							
							caseLambdaMini=case11;
						}
						
						
					}					
		
	
		S.add(caseLambdaMini);
		
	for (int i = 0; i < S.size(); i++) {
		Case caseTest;

		
	}
		
	
		
		for (int i = 0; i <caseLambdaMini.listeAreteSortante.size(); i++) {
			Case x = caseLambdaMini.listeAreteSortante.get(i).caseSortante;
		
			if(x.lambda > caseLambdaMini.lambda + caseLambdaMini.listeAreteSortante.get(i).Valuation)
			{
				x.lambda =  caseLambdaMini.lambda + caseLambdaMini.listeAreteSortante.get(i).Valuation;
			}
			
		}
		
		listDeVerif.clear();
		for (int i = 0; i < listMemoire.size(); i++) {
			Case case1=listMemoire.get(i);
			for (int j = 0; j <S.size(); j++) {
				Case case2=S.get(j);
				if (case1==case2)
				{
					listDeVerif.add(case2);
				}
				
				
			}
		}
	}
	
	//****************Fin Algoriithme de Dijkstra******************
	// Les lambda sont donc optimaux
	
	
	//***************D�but Algo recherche plus court chemin en fonction des lambdas******************
	// On calcule � partir de la case Espion le chemin le plus optimal pour aller sur le garde, on retournera ce chemin � la fin pour cr�er la charge du Garde
	
	Case caseDansLalgo = map.espion.saCase;
	int potentielPLusPetit=100;
	this.saCase.estDansLeBrouillardDeGuerre=false;
	map.espion.saCase.estDansLeBrouillardDeGuerre=false;
	this.listcheminopti.clear();
	this.listcheminopti.add(caseDansLalgo);
	Case x=null;
	
	
	
	while (caseDansLalgo!=this.saCase) {
		
	
	
	for (int i = 0; i <caseDansLalgo.listeAreteSortante.size(); i++) {
		
		 x = caseDansLalgo.listeAreteSortante.get(i).caseSortante; //  Case adjacente de case dans l'algo
		
		if(x.lambda<potentielPLusPetit && x.estDansLeBrouillardDeGuerre==false && x.type!="mur"){
		potentielPLusPetit=x.lambda;
		
		}
		
	}
	
	// potentielPlusPetit qui est bon, donc on a trouv� le lambda le plus petit autour de la caseDansLalgo
	Case p = null;
	List<Case> listConcurente = new ArrayList<Case>();
	for (int i = 0; i <caseDansLalgo.listeAreteSortante.size(); i++) { // On regarde dans ce for s'il n'y a pas une �galit� dans les lambdas autour de la case dans l'algo
		 p = caseDansLalgo.listeAreteSortante.get(i).caseSortante;
			if(p.lambda == potentielPLusPetit)
			{
				
				listConcurente.add(p);
				
			}
	}
	
	Case caseConcurente = null;
	if (listConcurente.size()==0) {
		caseDansLalgo=p;
	}
	else {
		
	

	for (Case case1 : listConcurente) { // S'il y a une h�sitation entre 2 lambdas, on regarde gr�ce celle qui nous � apport� sont lambda le plus optimaux donc la case dans l'algo a son lamba optimal en regardant les valuations
	}
	
	for (int i = 0; i < listConcurente.size(); i++) {
	
		caseConcurente=listConcurente.get(i);
		
		
		int ValuationAssocie;
		int ValuationEau = 3;
		int ValuationHerbe = 1;
		int ValuationBoue = 2; 
		
		switch (caseConcurente.type) {
		case "eau": 
			ValuationAssocie = ValuationEau;
			break;
		case "herbe":
			ValuationAssocie =ValuationHerbe;
			break;
		case "boue":
			ValuationAssocie =ValuationBoue;
			break;
		case "tresor":
			ValuationAssocie =ValuationHerbe;
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + caseConcurente.type);
		}
	
		if(caseDansLalgo.lambda-ValuationAssocie==caseConcurente.lambda)
		{
			caseDansLalgo=caseConcurente; // S'il y a concurrence, alors ce sera la case qui a rendu la caseDansL'algo optimal, c'est donc par elle que l'on dois passer pour le chemin optimal
		}
		

	}
		
	
	this.listcheminopti.add(caseDansLalgo); // On passe � la case suivante tant que l'on ne tombe pas sur la case du garde (ce qui nous ferrait sortir du while)
	}
	

}


for (Case case1 : this.listcheminopti) { // on r�initialise vite pour ne pas avoir de probl�me si un autre garde veut faire des calculs, les lambdas ne seront pas fauss�
	case1.lambda=10000;
	case1.compteur=10; // Mais on met un attribut ici qui servira � paint les cases en roses, ce qui nous montrera visuellement le chemin optimal que va prendre le garde.
	this.map.paintImmediately(0,0,2000,2000);
	
}
for (Case case1 : listMemoire) { // on r�initialise vite pour ne pas avoir de probl�me si un autre garde veut faire des calculs, les lambdas ne seront pas fauss�
	case1.lambda=10000;
	this.map.paintImmediately(0,0,2000,2000);
}

Collections.reverse(this.listcheminopti); // On retourne la list du chemin opti pour que �a commence du garde vers l'espion
	for(int i = 0; i < this.listcheminopti.size(); i++ )
	{
		Case x1 = this.listcheminopti.get(i);
try {
	Thread.sleep(x1.listeAreteEntrante.get(0).Valuation*500);
} catch (InterruptedException e) {

	e.printStackTrace();
}

this.saCase= x1; // On le fait se d�placer 2 fois + vite qu'� la normal quand il charge, c'est d� � la ligne 749, on divise le temps d'attente sur les cases par deux par rapport au d�placement dans dd�placementRandom()

this.map.paintImmediately(0,0,2000,2000);
if (this.saCase==map.espion.saCase) {
	map.ArretJeuPerdu(null); // On arr�te le jeu s'il touche l'espion
}

}

for (Case case1 : this.listcheminopti) {
	case1.compteur=0;
	this.map.paintImmediately(0,0,2000,2000);
	
}

}

}


