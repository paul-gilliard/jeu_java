package main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;



public class Fenetre extends JFrame implements KeyListener, ActionListener {   //C'est la fen�tre qui �coute le clavier et le bouton de lacement de partie.
	
	public Map map;
	public JButton bouton;

    public Fenetre() {			//Cr�er la fen�tre avec ses dimensions 

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
	public void keyPressed(KeyEvent e) {		//Si des touches son appuy� alors on appele le d�placement de l'espion en fonction de la direction
		
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
		
		repaint();							//Apr�s chaque d�placement nous devons repaint
	}
				

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {    //Appeler quand on click sur le bouton "commencer la partie "
		Object source = e.getSource();
 
		if(source == bouton){						//Si on a appuy� sur le bouton 
			map.miseEnDeplacementGarde();			// On lance les gardes 	
			map.remove(bouton);						//On supprime le bouton
			this.addKeyListener(this);				//On �coute le clavier pour le d�placement de l'espion
			this.requestFocus();					//On request le focus sur le KeyListener
		}
	}
}
