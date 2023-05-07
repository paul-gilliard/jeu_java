package main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;



public class Fenetre extends JFrame implements KeyListener, ActionListener {   //C'est la fenêtre qui écoute le clavier et le bouton de lacement de partie.
	
	public Map map;
	public JButton bouton;

    public Fenetre() {			//Créer la fenêtre avec ses dimensions 

        this.setTitle("JeuLicence3");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        
        Dimension dimension = this.getSize(); 
        float height = (float) dimension.getHeight(); //1080
        float width = (float) dimension.getWidth(); //1920
        
        map = new Map(height,width);
        this.setContentPane(map);
        
        bouton = new JButton("lancer la partie");
        bouton.addActionListener(this);
		map.add(bouton);
    }

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {		//Si des touches son appuyé alors on appele le déplacement de l'espion en fonction de la direction
		
		int code=e.getKeyCode();
		
		switch (code){
	
		   case (KeyEvent.VK_DOWN): 
			   
			   map.espion.seDeplacer("bas");
		
		   break;
		   
		   case (KeyEvent.VK_UP): 
			   
			   map.espion.seDeplacer("haut");
		
		   break;
		   
		   case (KeyEvent.VK_LEFT): 
			   
			   map.espion.seDeplacer("gauche");
		   
			   break;
		   case (KeyEvent.VK_RIGHT): 
			   
			   map.espion.seDeplacer("droite");
		
			  break;
		}
		
		repaint();							//Après chaque déplacement nous devons repaint
	}
				

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {    //Appeler quand on click sur le bouton "commencer la partie "
		Object source = e.getSource();
 
		if(source == bouton){						//Si on a appuyé sur le bouton 
			map.miseEnDeplacementGarde();			// On lance les gardes 	
			map.remove(bouton);						//On supprime le bouton
			this.addKeyListener(this);				//On écoute le clavier pour le déplacement de l'espion
			this.requestFocus();					//On request le focus sur le KeyListener
		}
	}
}
