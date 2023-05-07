package main;

import java.util.ArrayList;

public class Case {
	float positionX;
	float positionY;
	float tailleCase;
	String type;
	float NumeroDeCase;
	Case caseGauche;
	Case caseDroite;
	Case caseHaut;
	Case caseBas;
	Boolean CaseBord;
	ArrayList<Arete> listeAreteEntrante;
	ArrayList<Arete> listeAreteSortante;
	Espion espion;
	Garde garde;
	boolean estPotentielementVisible;
	boolean estReelementVisible;
	boolean estDansLeBrouillardDeGuerre;
	int distance;
	boolean vu;
	int lambda;
	int compteur;
	
	
	Case(float positionX,float positionY,float tailleCase ,String type , float NumeroDeCase ,Boolean CaseBord, Case caseGauche ,Case caseDroite ,Case caseHaut , Case caseBas ,  ArrayList<Arete> listeAreteEntrante , ArrayList<Arete> listeAreteSortante, Espion espion, Garde garde, boolean estPotentielementVisible, boolean estReelementVisible, boolean estDansLeBrouillardDeGuerre, int distance , boolean vu,int lambda,int compteur){
		this.positionX=positionX;
		this.positionY=positionY;
		this.tailleCase=tailleCase;
		this.type=type;
		this.NumeroDeCase = NumeroDeCase;
		this.CaseBord = CaseBord;
		this.caseGauche=caseGauche;
		this.caseDroite=caseDroite;
		this.caseHaut= caseHaut;
		this.caseBas = caseBas;
		this.listeAreteEntrante = listeAreteEntrante;
		this.listeAreteSortante = listeAreteSortante;
		this.espion=espion;
		this.garde=garde;
		this.estPotentielementVisible=estPotentielementVisible;
		this.estReelementVisible=estReelementVisible;
		this.estDansLeBrouillardDeGuerre=estDansLeBrouillardDeGuerre;
		this.distance=distance;
		this.vu=vu;
		this.lambda=lambda;
		this.compteur=compteur;
		}
}
