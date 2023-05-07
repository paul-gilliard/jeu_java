package main;

import java.util.List;

public class monThread implements Runnable{
	
	private int parameter;
	public List<Garde> listDeGardes;
	public boolean running;
	private Map map;
	
     monThread(int parameter, List<Garde> listDeGarde, boolean running, Map map) {
       this.parameter = parameter;
       this.listDeGardes=listDeGarde;
       this.running= running;
       this.map=map;
    }

    
    public void run() {  //Quand on créer un Thread il prend un garde à l'indice "parameter" dans listedegarde et lance dans le Thread du garde la méthode de deplacementrandom()
    	
    	this.running=listDeGardes.get(parameter).deplacementRandom();
    	map.ArretJeuPerdu(this); //On arrive sur cette instruction quand deplacementrandom()  return , elle arrête le jeu 
    }
}


