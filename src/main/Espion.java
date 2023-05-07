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

public void seDeplacer (String direction) 		//Fonction d�placer de l'espion en fonction de la direction qu'il aura re�u
	{
	
	int deja_deplace=0;							//Obligation de cr�er une variable qui regarde s'il est d�j� d�plac�
    
	   for (int i=0; i<map.espion.saCase.listeAreteSortante.size();i++)				//Regarde la liste d'arr�te sortante de la case de l'espion
	   {
		   if (map.espion.saCase.listeAreteSortante.get(i).direction == direction && deja_deplace==0)		//Si on lui donne une direction et qu'il n'est pas d�plac� alors
			   {
		map.espion.saCase.espion=null;	   																	//On lui dit que son ancienne case ne comporte plus l'espion
	   	map.espion.saCase=map.espion.saCase.listeAreteSortante.get(i).caseSortante;							//On lui donne sa nouvelle case en fonction de son arr�te sortante
	   	map.espion.saCase.espion=map.espion;																//On red�fini la case comportant l'espion
	   	
	   	deja_deplace=1;
			   }
	   }
	   
	   for(int i=0; i<map.listPointeurDeThread.size();i++) { // V�rification de Partie Gagn� ou perdu par v�rification de la position de la case de l'espion sur la m�me case que les gardes ou le tr�sor ?
		   
			if( map.espion.saCase == map.listDeGarde.get(i).saCase){
				map.ArretJeuPerdu(null);
			}
			if (map.espion.saCase.type == "tresor") {
				map.ArretJeuPerdu(null);
			}		
		}
	}
}
