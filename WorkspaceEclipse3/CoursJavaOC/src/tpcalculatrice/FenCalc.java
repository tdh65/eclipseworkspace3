package tpcalculatrice;

import java.math.*;
import java.text.DecimalFormat;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FenCalc extends JFrame {

	private Panneau panGlob; // le panneau global qui remplit le contentPane
	private Panneau panChiffre; // contient les chiffres
	private Panneau panOperateur;
	private Panneau panAfficheur;
	private JLabel afficheur;
	private String operation; // l'operateur selectionne
	private double valEnCours = 0; // le resultat intermediaire
	private double saisie = 0;
	private String partieEntiereSaisie = ""; // la partie entiere de la saisie en cours
	private String partieDecimaleSaisie = ""; // la partie decimale sans le "."
	private String signe = "";
	private String saisieDoubleText = ""; //
	private boolean efface = false;
	boolean decimal = false; // indique si le signe "." est presse et donc on concatene apres la virgule
	private JButton bouton = new JButton("Mon premier bouton");
	private JButton bouton2 = new JButton("Mon II bouton");
	private String nameFenetre = "Bouton";

	/*
	 * constructeur par defaut definit les 3 panneaux chiffre -> gridlayout
	 * operateur ->boxlayout vertical agencement des trois par borderlayout centre
	 * est nord
	 */
	public FenCalc() {
		System.out.println("fenetre ; ");

		this.setTitle("Calculatrice");
		this.setLocationRelativeTo(null);
		this.setSize(300, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// construit le panneau chiffre
		this.construitPanneauChiffre();
		// construit le panneau afficheur
		this.construitPanneauAfficheur();
		// construit le panneau operateur
		this.construitPanneauOperateur();
		// ajoute les panneaux au panneau ppal
		this.getContentPane().add(this.panChiffre, BorderLayout.CENTER);
		this.getContentPane().add(panAfficheur, BorderLayout.NORTH);
		this.getContentPane().add(this.panOperateur, BorderLayout.EAST);
		this.pack();
		this.setVisible(true);

	}

	/*
	 * construit le panneau d'operateurs
	 */
	private void construitPanneauOperateur() {
		int vHeight = 0;
		int vWidth = 0;
		this.panOperateur = new Panneau();
		this.panOperateur.setLayout(new BoxLayout(panOperateur, BoxLayout.PAGE_AXIS));
		String[] t = { "C", "+", "-", "*", "/" };
		for (String v : t) {
			JButton b = new JButton(v);
			if (v == "C") {
				b.setForeground(Color.RED);
				/*
				 * on affecte par defaut la valeur de taille du btn C
				 */
				// vWidth = b.getWidth() ;
				// vHeight = b.getHeight() ;
			}
			// b.setPreferredSize(new Dimension(vWidth,vHeight));
			b.setName(v);
			b.setMnemonic(v.charAt(0));
			b.addActionListener(new ChiffrePannListener());
			this.panOperateur.add(b);
			//System.out.println(b.getText() + " h " + b.getHeight() + " w " + b.getWidth());

		}

	}

	/*
	 * construit le panneau afficheur sur un seul panel pas de layout particulier
	 * 
	 */
	private void construitPanneauAfficheur() {
		this.panAfficheur = new Panneau();
		this.afficheur = new JLabel();
		// this.afficheur.setBackground(Color.LIGHT_GRAY);
		this.afficheur.setPreferredSize(new Dimension(250, 50));
		// this.afficheur.setForeground(Color.orange);
		Font ft = new Font("Tahoma", Font.LAYOUT_RIGHT_TO_LEFT, 16);
		this.afficheur.setFont(ft);
		this.afficheur.setHorizontalAlignment(JLabel.RIGHT);
		raz();
		String t = String.valueOf(valEnCours);

		this.afficheur.setText(t);
		this.panAfficheur.add(afficheur);

	}

	/*
	 * construit le panneau contenant les chiffres et le gridlayout
	 */
	private void construitPanneauChiffre() {
		this.panChiffre = new Panneau();

		GridLayout gl = new GridLayout();
		// on definit les colonnes et lignes
		gl.setColumns(3);
		gl.setRows(4);
		// on definit lespacement entre les colonnes et les lignes
		gl.setHgap(5); // 5 pixels H orizontal
		gl.setVgap(3); // 3 pixels V ertical

		this.panChiffre.setLayout(gl);

		String[] t = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".", "=" };
		for (String v : t) {
			affiche("v " + v);
			JButton b = new JButton(v);
			b.setName(v);
			b.setMnemonic(v.charAt(0));
			b.addActionListener(new ChiffrePannListener());
			affiche("nom bouton " + b.getName());
			this.panChiffre.add(b);

		}
		
	}

	/*
	 * on constuit la classe anonyme ecoutant le panneau chiffre
	 * 
	 */
	class ChiffrePannListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// nom du bouton qui est egal a un chiffre ou un operateur
			String sourceArg = arg0.getSource().toString().substring(20, 21);
			affiche("arg0 " + arg0.getSource() + " Name : ");
			// recupere la derniere valeur de la saisie
		//	affiche("arg0 + modif source " + sourceArg);
			gereOperation(sourceArg);

		}
	}

	/*
	 * affecte les operations
	 */
	protected void gereOperation(String operateur) {
		String res = "";
		if (operateur.equals("C")) {
			raz();
			res = "0";

		} else if (((operateur.equals("+")) || (operateur.equals(".")) || (operateur.equals("-"))
				|| (operateur.equals("*")) || (operateur.equals("/")) || (operateur.equals("=")))) {
			res = ajouteOperateur(operateur);

		} else {
			// ce sont des chiffres a concatener a la saisie ou une virgule 

			try {
				saisie = Double.valueOf(afficheur.getText());
			} catch (NumberFormatException ex) {
				affiche("exception " + ex.getMessage());
				// ex.printStackTrace();
				saisie = 0;

			}
		//	affiche("Saisie = " + saisie);
			// ce sont des chiffres on les concatene
			// affiche("ajout chiffre nom bouton " + arg0.getSource());
			res = concateneChiffre(afficheur.getText(), operateur);
		//	affiche("listener res = " + res);

			// afficheur.setText(res);
		}
		// met a jour les chiffres en string
	//	affiche("Avant mettreaJour "+ res );
		this.mettreAJourAfficheur( res);
	//	affiche("Avant afficheur "+ res );
		this.afficheur.setText(res);

	}
	/*
	 * protected void go() { Scanner sc = new Scanner(System.in) ; while()
	 * 
	 * }
	 */

	/*
	 * effectue les calculs demande
	 */
	private double calcule(double valEncours, double saisie, String operation) {
		double res = 0;
		if (operation.equals("/")) {
			try {
				res = valEncours / saisie;

			} catch (ArithmeticException e) {
				e.printStackTrace();
			} finally {
				// si res = 0 && valencours != 0
				// fait rien
				// sinon

			}

		} else if (operation.equals("+")) {
			res = valEncours + saisie;
		} else if (operation.equals("-")) {
			res = valEncours - saisie;
		} else if (operation.equals("*")) {
			res = valEncours * saisie;
		}
		/*
		 * prend en compte le signe et transfere l info dans this.signe
		 */
		if (res < 0)
			this.signe = "-";
		return res;
	}

	// remete à zero les compteurs et les valeurs
	private void raz() {
		this.valEnCours = 0;
		this.operation = "";
		this.razSaisie();
		this.signe = "";
	}

	/*
	 * remet a zero la saisie courante
	 */
	private void razSaisie() {
		this.saisie = 0;
		this.partieDecimaleSaisie = "";
		this.partieEntiereSaisie = "0";
		this.decimal = false;
	}

	/*
	 * verifie si un signe '.' est deja insere et donc c'est un nombre deja decimal
	 */
	private boolean verifieVirgule(String saisie) {
		boolean trouve = false;
		if (saisie.length() < 1) {
			trouve = false;

		} else {
			for (int i = 0; i < saisie.length(); i++) {
				if (saisie.charAt(i) == '.') {
					trouve = true;
					break; // on sort inutile de continuer
				}
			}
		}
		affiche("pour saisie : " + saisie + " trouve : " + trouve);
		return trouve;
	}

	/*
	 * methode geer l'ensemble des operations et des operateurs
	 */
	private String ajouteOperateur(String operateur) {
		String res = "";
	//	affiche("Avant  ajoute operateur : Valencours " + this.valEnCours + " saisie " + saisie + " operation "
	//			+ operation + " operateur " + operateur);
		/*
		 * oblige de le mettre en premier sinon c'est le operateur "" qui est pris et la
		 * virgule n est jamais traitee
		 */
		if (operateur.equals(".")) {

			affiche("AjouteOperateur (virgule  . ) ");
			decimal = true; // le signe decimal est deja presse
			if (verifieVirgule(afficheur.getText()) == true) {
				// on fait rien
		//		affiche("deja une virgule ");
			}
			this.eclateDouble2String(this.saisie, operateur);
			return (this.signe + this.partieEntiereSaisie + "." + this.partieDecimaleSaisie);

		}
		if (this.operation.isEmpty()) {
			// premiere operation on affecte la saisie a la Valeur en cours
			// on efface la saisie
			this.valEnCours = this.saisie;
			this.razSaisie();
			this.eclateDouble2String(this.saisie, operateur);
			this.operation = operateur;
	//		affiche("fin ajouteoperateur .isempty val en cours " + this.valEnCours + " operation " + this.operation
	//				+ " saisie " + this.saisie);
		} else if (operateur.equals("=")) {
			// special operateur "=" operation finale
			// on vide valEnCours et on affiche que saisie ==> si decide autre operation on
			// repart sur operation.isempty
			// on ne fait donc pas le raz
	//		affiche("Affichage demande suite a \" = \" ");
			this.saisie = this.calcule(this.valEnCours, this.saisie, this.operation);
	//		affiche("val en cours affecte a saisie = " + this.saisie);
			this.valEnCours = 0;
			this.operation = "" ; // pour transferer ensuite lorsqu on appuie sur un operateur
			this.eclateDouble2String(this.saisie, operateur);
	//		affiche(this.partieEntiereSaisie + " ..  " + this.partieDecimaleSaisie);
	//		affiche("operateur = " + operateur + " saisie " + this.saisie + " val en cours " + this.valEnCours);
		
		} else {
			// on calcule la valeur intermediaire avec un operateur
			this.valEnCours = this.calcule(this.valEnCours, this.saisie, this.operation);
	/*		affiche("val en cours apres = " + this.operation + " = " + this.valEnCours + " nouvel operateur "
					+ operateur);*/
			// on remet a zero les saisie et on affecte l'operateur
			this.razSaisie();
			this.eclateDouble2String(this.saisie, operateur);
			this.operation = operateur;
		/*	affiche("Apres ajoute operateur : Valencours " + this.valEnCours + " saisie " + saisie + " operation "
					+ operation);*/

			return res.valueOf(this.saisie);
		}

		return (this.signe + this.partieEntiereSaisie + "." + this.partieDecimaleSaisie);
	}

	/*
	 * concatene a un string saisie la valeur strAConcat : si c'est 0 alors on
	 * change le 0 par le chiffre
	 */
	private String concateneChiffre(String saisie, String strAConcat) {
		String resultat = "";

		if (decimal) {/*
						 * if (y == 0.0) y= z/10 ; else y = (y/10) + z ; }else { x = x*10 + z ;
						 */
			this.partieDecimaleSaisie = this.partieDecimaleSaisie + (strAConcat);
		} else {
	//		affiche("avant concat :" + this.partieEntiereSaisie + " concat " + strAConcat);
			if (this.partieEntiereSaisie.equals("0")) {
	//			affiche("this.partieentiere " + this.partieEntiereSaisie);
				this.partieEntiereSaisie = strAConcat;
			} else
				this.partieEntiereSaisie = this.partieEntiereSaisie + strAConcat;

	//		affiche("apres concat :" + this.partieEntiereSaisie);

		}
		resultat = this.signe + this.partieEntiereSaisie + "." + this.partieDecimaleSaisie;
		try {
			this.saisie = Double.parseDouble(resultat);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();

		}
//		affiche("Resultat : string : " + this.signe + this.partieEntiereSaisie + "." + this.partieDecimaleSaisie);
//		affiche("saisie Double : " + this.saisie);

		return resultat;

	}

	public void affiche(String str) {
		System.out.println(str);
	}

	/*
	 * rafraichit l afficheur
	 */
	protected String mettreAJourAfficheur(String pStrVal) {
	//	affiche(" dans mettreAJourAfficheur val : " + pStrVal+ " saisie " + saisie);
		if (!decimal) {
			// n affiche pas le .00
			return new String(pStrVal.split(".", 2)[0]);
		} else
			return new String(pStrVal);
		/*
		 * affiche("dans mettreAJourAfficheur getText " + this.afficheur.getText() +
		 * " valeurafficheur " + valeurafficheur) ;
		 * 
		 * this.afficheur.setText(valeurafficheur); this.afficheur.repaint(); repaint();
		 */

	}

	/*
	 * extrait partie decimale et partie entiere + signe d apres la fonction decimal
	 * ci dessous transfere les donnees dans partieEntiere , partieDecimale, Signe
	 * si plastParam est 0 alors on ajoute 0 sinon on enleve zero 
	 * ex 23 plast 0 => 23.0
	 *    25 plast 1 => 23.1 et bon olus 23.01 
	 */
	protected void eclateDouble2String(double valDouble, String pStrLastParam) {
		// on crée un DecimalFormat pour formater le double en chaine :
		DecimalFormat df = new DecimalFormat();
		df.setGroupingUsed(false); // Pas de regroupement dans la partie entière
		df.setMinimumFractionDigits(1); // Au minimum 1 décimale
		df.setMaximumFractionDigits(340); // Au maximum 340 décimales (valeur max. pour les doubles / voir la doc)

		// On formate le double en chaine
		String str = df.format(valDouble);
//		affiche("on va eclater :" + str);
		// On récupère le caractère séparateur entre la partie entière et décimale :
		char separator = df.getDecimalFormatSymbols().getDecimalSeparator();
		// On ne récupère que la partie décimale :
		this.partieDecimaleSaisie = str.substring(str.indexOf(separator) + 1);
		this.partieEntiereSaisie = str.substring(0, str.indexOf(separator));
		if (this.partieDecimaleSaisie.equals("0" ) && (!pStrLastParam.equals("0"))) {
			this.partieDecimaleSaisie = "" ;
		}
		if (valDouble < 0)
			this.signe = "-";
//		affiche("fin eclateDouble : sign ent dec" + this.signe + this.partieEntiereSaisie + "."
//				+ this.partieDecimaleSaisie);

	}

	public static double decimal(double d) {
		// on crée un DecimalFormat pour formater le double en chaine :
		DecimalFormat df = new DecimalFormat();
		df.setGroupingUsed(false); // Pas de regroupement dans la partie entière
		df.setMinimumFractionDigits(1); // Au minimum 1 décimale
		df.setMaximumFractionDigits(340); // Au maximum 340 décimales (valeur max. pour les doubles / voir la doc)

		// On formate le double en chaine
		String str = df.format(d);
		// On récupère le caractère séparateur entre la partie entière et décimale :
		char separator = df.getDecimalFormatSymbols().getDecimalSeparator();
		// On ne récupère que la partie décimale :
		str = str.substring(str.indexOf(separator) + 1);
		// Que l'on transforme en double :
		return Double.parseDouble(str);
	}

}
