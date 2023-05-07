package main;

import java.awt.Image;


public class Espion {
	
	Image img;
	Case saCase;
	Map map ;
	
	Espion(Image img, Case saCase, Map map){
		
		this.img= img;
		this.saCase=saCase;
		this.map=map;
	}

public void seDeplacer (String direction) 		//Fonction déplacer de l'espion en fonction de la direction qu'il aura reçu
	{
	
	int deja_deplace=0;							//Obligation de créer une variable qui regarde s'il est déjà déplacé
    
	   for (int i=0; i<map.espion.saCase.listeAreteSortante.size();i++)				//Regarde la liste d'arrête sortante de la case de l'espion
	   {
		   if (map.espion.saCase.listeAreteSortante.get(i).direction == direction && deja_deplace==0)		//Si on lui donne une direction et qu'il n'est pas déplacé alors
			   {
		map.espion.saCase.espion=null;	   																	//On lui dit que son ancienne case ne comporte plus l'espion
	   	map.espion.saCase=map.espion.saCase.listeAreteSortante.get(i).caseSortante;							//On lui donne sa nouvelle case en fonction de son arrête sortante
	   	map.espion.saCase.espion=map.espion;																//On redéfini la case comportant l'espion
	   	
	   	deja_deplace=1;
			   }
	   }
	   
	   for(int i=0; i<map.listPointeurDeThread.size();i++) { // Vérification de Partie Gagné ou perdu par vérification de la position de la case de l'espion sur la même case que les gardes ou le trésor ?
		   
			if( map.espion.saCase == map.listDeGarde.get(i).saCase){
				map.ArretJeuPerdu(null);
			}
			if (map.espion.saCase.type == "tresor") {
				map.ArretJeuPerdu(null);
			}		
		}
	}
}
