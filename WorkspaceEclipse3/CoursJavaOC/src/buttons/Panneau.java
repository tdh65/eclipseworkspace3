package buttons;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Panneau extends JPanel {
	private int posX = -50;
	private int posY = -50;
	private int drawSize = 50;
	private String forme = "ROND";
	// un bool pour le morphing , un autre pour savoir
	// si la taille doit etre reduite
	private boolean morph = false, reduce = false;
	// le compteur de rafraichissement
	private int increment = 0;

	public int getDrawSize() {
		return drawSize;
	}

	public void setDrawSize(int drawSize) {
		this.drawSize = drawSize;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posy) {
		this.posY = posy;
	}

	public void paintComponent(Graphics g) {
		// on choisit une couleur de fonds du rectangle
		g.setColor(Color.white);
		// on remplit le cadre de la couleur
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		// on redefinit la couleur du rond
		g.setColor(Color.RED);
		// si le mode morphing est active
		if (this.morph)
			this.drawMorph(g);
		else
			this.draw(g);

	}

	public void draw(Graphics g) {
		if (this.forme.equals("ROND")) {
			g.fillOval(posX, posY, 50, 50);
		}
		if (this.forme.equals("CARRE")) {
			g.fillRect(posX, posY, 50, 50);

		}
		if (this.forme.equals("TRIANGLE")) {
			int s1X = posX + 50 / 2;

			int s1Y = posY;

			int s2X = posX + 50;

			int s2Y = posY + 50;

			int s3X = posX;

			int s3Y = posY + 50;

			int[] ptsX = { s1X, s2X, s3X };

			int[] ptsY = { s1Y, s2Y, s3Y };

			g.fillPolygon(ptsX, ptsY, 3);

		}

		if (this.forme.equals("ETOILE")) {

			int s1X = posX + 50 / 2;

			int s1Y = posY;

			int s2X = posX + 50;

			int s2Y = posY + 50;

			g.drawLine(s1X, s1Y, s2X, s2Y);

			int s3X = posX;

			int s3Y = posY + 50 / 3;

			g.drawLine(s2X, s2Y, s3X, s3Y);

			int s4X = posX + 50;

			int s4Y = posY + 50 / 3;

			g.drawLine(s3X, s3Y, s4X, s4Y);

			int s5X = posX;

			int s5Y = posY + 50;

			g.drawLine(s4X, s4Y, s5X, s5Y);

			g.drawLine(s5X, s5Y, s1X, s1Y);

		}
	}

	// methode qui peint le morphing
	public void drawMorph(Graphics g) {
		// on incremente
		increment++;
		// on regarde si on doit reduire ou nom
		if (drawSize >= 50)
			reduce = true;
		if (drawSize <= 10)
			reduce = false;
		if (reduce) {
			drawSize = drawSize - getUsedSize();
		} else {
			drawSize = drawSize + getUsedSize();
		}
		
		if (this.forme.equals("ROND")) {
			g.fillOval(posX, posY, drawSize, drawSize);
		}
		if (this.forme.equals("CARRE")) {
			g.fillRect(posX, posY, drawSize, drawSize);
		}
		  if(this.forme.equals("TRIANGLE")){        
		      int s1X = posX + drawSize/2;
		      int s1Y = posY;
		      int s2X = posX + drawSize;
		      int s2Y = posY + drawSize;
		      int s3X = posX;
		      int s3Y = posY + drawSize;      
		      int[] ptsX = {s1X, s2X, s3X};
		      int[] ptsY = {s1Y, s2Y, s3Y};      
		      g.fillPolygon(ptsX, ptsY, 3);
		    }
		    if(this.forme.equals("ETOILE")){      
		      int s1X = posX + drawSize/2;
		      int s1Y = posY;
		      int s2X = posX + drawSize;
		      int s2Y = posY + drawSize;      
		      g.drawLine(s1X, s1Y, s2X, s2Y);      
		      int s3X = posX;
		      int s3Y = posY + drawSize/3;
		      g.drawLine(s2X, s2Y, s3X, s3Y);      
		      int s4X = posX + drawSize;
		      int s4Y = posY + drawSize/3;
		      g.drawLine(s3X, s3Y, s4X, s4Y);                   
		      int s5X = posX;
		      int s5Y = posY + drawSize;
		      g.drawLine(s4X, s4Y, s5X, s5Y);       
		      g.drawLine(s5X, s5Y, s1X, s1Y);  
		    }    
	}

	private int getUsedSize() {
		int res = 0 ;
		// si le nombre de tours est de 10 on reinitialise l increment et on retourne a 1
		if (increment /10 == 1) {
			increment = 0 ;
			res = 1;
		}
		return res;
		
	}

	public void setMorph(boolean bool) {

		this.morph = bool;

		// On réinitialise la taille

		drawSize = 50;

	}

	public void setForme(String form) {

		this.forme = form;

	}

	public Boolean getMorph() {
		return morph;
	}

	public void setMorph(Boolean morph) {
		this.morph = morph;
	}

	public Boolean getReduce() {
		return reduce;
	}

	public void setReduce(Boolean reduce) {
		this.reduce = reduce;
	}

	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}

	public String getForme() {
		return forme;
	}

}
