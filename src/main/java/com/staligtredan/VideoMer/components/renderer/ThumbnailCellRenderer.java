package com.staligtredan.VideoMer.components.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.swingx.border.DropShadowBorder;

import com.staligtredan.VideoMer.controleur.DefaultController;
import com.staligtredan.VideoMer.modele.EntiteVideo;
import com.staligtredan.VideoMer.modele.Film;
import com.staligtredan.VideoMer.modele.Serie;
import com.staligtredan.VideoMer.util.ZipUtility;

/**
 * <code>ThumbnailCellRenderer</code> est un Renderer de JList adapté pour les
 * miniatures de vidéos
 * 
 * @author Brendan
 * @version 0.1 6/07/2010
 * @update 1.0.3 10/12/2016 Rajout de la gestion de taille des miniatures
 *         Changement de la taille de stockage des miniatures Et du gros
 *         changement globalement au niveau miniatures (nom, stockage, gestion)
 */
public class ThumbnailCellRenderer extends JPanel implements ListCellRenderer<Object> {

	private static final long serialVersionUID = 4026662066714419591L;
	
	final static Logger logger = LogManager.getLogger(ThumbnailCellRenderer.class);

	/**
	 * Nom de l'archive zip qui contient les posters
	 */
	private static final String postersZipArchivePath = "posters.zip";

	/**
	 * Buffer d'images pour éviter de recharger en permanence
	 */
	private static HashMap<String, ImageIcon> bufferImages;

	/**
	 * Archive zip
	 */
	private final File zFile;

	/**
	 * Largeur d'une miniature
	 */
	private int posterW;



	/**
	 * Crée une instance
	 */
	public ThumbnailCellRenderer( DefaultController controller, int taille ) {
		
		super();
		initComponents();
		
		bufferImages = new HashMap<String, ImageIcon>();
		zFile = new File(postersZipArchivePath);

		// 1.0.3 | Gestion de la taille des miniatures
		// 270 216
		if( taille == 1 ) {
			posterW = 190;
			lblDetails.setFont(new Font("Arial", Font.PLAIN, 11));
			lblTitle.setFont(new Font("Arial", Font.ITALIC, 13));
		} 
		// 210 168
		else if( taille == 2 ) {
			posterW = 140;
			lblDetails.setFont(new Font("Arial", Font.PLAIN, 10));
			lblTitle.setFont(new Font("Arial", Font.ITALIC, 12));
		} 
		// 150 120
		else {
			posterW = 100;
			lblDetails.setFont(new Font("Arial", Font.PLAIN, 9));
			lblTitle.setFont(new Font("Arial", Font.ITALIC, 11));
		}


		// Lance un thread pour télécharger et/ou ajouter les fichiers au zip
		ArrayList<EntiteVideo> al = new ArrayList<EntiteVideo>(controller.getBibliotheque().getListe(0));
		Thread t = new Thread() {
			public void run() {
					
				// Lecture et création du buffer image à partir d'une archive
				if( zFile.exists() ) {

					try ( ZipFile zipFile = new ZipFile(zFile) ) {
						
						Enumeration<? extends ZipEntry> entries = zipFile.entries();

						while ( entries.hasMoreElements() ) {
							ZipEntry entry = entries.nextElement();
							InputStream stream = zipFile.getInputStream(entry);

							BufferedImage img = ImageIO.read(stream);

							resizeThumnail(img, entry.getName());

							stream.close();
						}
						zipFile.close();
					} catch ( IOException e ) {
						logger.error("Impossible d'ouvrir le fichier zip : " + zFile.getPath(), e);
					}
				}

				// Une fois le fichier zip lu, on ajoute/télécharge les images
				// manquantes au zip
				dlMissingPosters(al);
			}
		};
		t.start();
	}



	/**
	 * Ajoute une image au buffer+zip si elle existe et la télécharge sinon
	 * avant de l'ajouter
	 * 
	 * @param list
	 *            liste des EntiteVideo de la bibliothèque
	 * @throws IOException
	 */
	private void dlMissingPosters( ArrayList<EntiteVideo> list ) {

		String posterFileName;
		HashMap<String, String> filesToZip = new HashMap<String,String>();

		for ( EntiteVideo ev : list ) {

			posterFileName = ev.getAffichette().substring(ev.getAffichette().lastIndexOf("/") + 1);

			if( (posterFileName == null) || posterFileName.isEmpty() ) {
				
				//System.out.println("Image vide de la video : "+ev.getNom());
				logger.info("Image vide de la video : "+ev.getNom()+"  chemin : "+ev.getEmplacement());
			}
			// Le buffer ne contient pas l'img
			else if( !bufferImages.containsKey(posterFileName) ) {

				File poster = new File("posters/" + posterFileName);
				
				// si fichier existe -> on l'utilise
				if( poster.exists() ) {

					BufferedImage img;
					try {
						img = ImageIO.read(poster);
						
						resizeThumnail(img, posterFileName);

						filesToZip.put(poster.getPath(), posterFileName);
					} catch ( IOException e ) {
						logger.error("impossible d'ouvrir l'image : "+posterFileName+" de la video : "+ev.getNom(), e);
					}

					
				}
				// sinon Download
				else {
					String url = ev.getAffichette();

					if( url == null || url.trim().isEmpty() || url.contains("null") ) {
						;
					} else {

						try {
							
							if( !poster.exists() )
								poster.createNewFile();

							BufferedImage img = ImageIO.read(new URL(url));

							// resize
							int w = img.getWidth();
							int h = img.getHeight();
							int newH = 300;
							double newWi = (w * 300.0) / h;
							int newW = (int) newWi;

							BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
							Graphics2D g = dimg.createGraphics();
							g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
									RenderingHints.VALUE_INTERPOLATION_BILINEAR);
							g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
							g.dispose();

							// Sauvegarde de l'image sur le disque dur
							ImageIO.write(dimg, url.substring(url.lastIndexOf(".") + 1), poster);

							resizeThumnail(dimg, posterFileName);

							filesToZip.put(poster.getPath(), posterFileName);
						} catch ( MalformedURLException e ) {
							logger.error("Impossible d'ouvrir le lien : "+url,e);
						} catch ( IOException e ) {
							logger.error("Pb d'acces au fichier : "+poster.getPath(),e);
						}
					}
				}
			}
		}
		
		// 2.0.1 | Et on ajoute tous les nouveaux fichiers dans le zip
		try {
			ZipUtility.addToZip(postersZipArchivePath, filesToZip);
		} catch ( IOException e ) {
			logger.error("impossible d'ouvrir le fichier zip : "+postersZipArchivePath, e);
		}
	}



	/**
	 * Redimensionne une image physique pour l'adapter à la taille des
	 * miniatures et la stocke en bufferImage
	 * 
	 * @param img
	 *            image à redimenssionner
	 * @param name
	 *            nom de l'image
	 */
	private void resizeThumnail( BufferedImage img, String name ) {

		// resize
		int w = img.getWidth();
		int h = img.getHeight();
		int newH = posterW;
		double newWi = (w * newH) / h;
		int newW = (int) newWi;
		BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
		g.dispose();
		ImageIcon ii = new ImageIcon(dimg);

		bufferImages.put(name, ii);
	}



	@Override
	public Component getListCellRendererComponent( JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus ) {

		Color bg = null;
		Color fg = null;

		// DnD
		JList.DropLocation dropLocation = list.getDropLocation();
		if( dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index ) {

			bg = UIManager.getColor("List.dropCellBackground");
			fg = UIManager.getColor("List.dropCellForeground");

			isSelected = true;
		}

		// Background foreground
		if( isSelected ) {
			setBackground(bg == null ? list.getSelectionBackground() : bg);
			setForeground(fg == null ? list.getSelectionForeground() : fg);
		} else {
			setBackground(new Color(UIManager.getColor("List.background").getRGB()));
			setForeground(new Color(UIManager.getColor("List.foreground").getRGB()));
		}
		setEnabled(list.isEnabled());
		setFont(list.getFont());

		// Bordure
		setBorder(new DropShadowBorder(Color.black, 5, 0.5f, 12, true, true, true, true));

		// Données
		EntiteVideo v = (EntiteVideo) value;
		lblTitle.setText(v.getNom());
		chargeImage(v.getAffichette());

		if( v.getClass() == Serie.class ) {

			Serie s = (Serie) v;

			Formatter fSaison = new Formatter(new StringBuilder());
			Formatter fEpisode = new Formatter(new StringBuilder());
			fSaison.format("%02d", s.getNoSaison());
			fEpisode.format("%02d", s.getNoEpisode());

			lblDetails.setText("S" + fSaison + "E" + fEpisode);

			fSaison.close();
			fEpisode.close();

		} else if( ((Film) v).getNumSupport() != 0 ) {

			lblDetails.setText("CD" + ((Film) v).getNumSupport());

		} else
			lblDetails.setText(" ");

		return this;
	}



	/**
	 * Charges les images du disque dur au bufferImage
	 */
	private void chargeImage( String url ) {

		// On évite les problème de null etc..
		if( url == null || url.trim().isEmpty() || url.contains("null") ) {
			lblThumbnail
					.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/empty_poster.jpg")));
			return;
		}

		// Si image dans le buffer
		String posterFileName = url.substring(url.lastIndexOf("/") + 1);

		if( bufferImages.containsKey(posterFileName) ) {
			lblThumbnail.setIcon(bufferImages.get(posterFileName));
			return;
		}

		// Si fichier existant (ajout récent, à cette session)
		File f = new File("posters/" + posterFileName);

		if( f.isFile() && f.length() != 0 ) {

			BufferedImage img;
			try {
				img = ImageIO.read(f);
				resizeThumnail(img, posterFileName);
			} catch ( IOException e ) {
				lblThumbnail
				.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/empty_poster.jpg")));
				logger.error("Impossible de lire l'affichette : "+f.getPath(), e);
			}
		}
	}



	private void initComponents() {

		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		lblThumbnail = new JLabel();
		lblDetails = new JLabel();
		lblTitle = new JLabel();

		// ======== this ========

		setLayout(new GridBagLayout());
		((GridBagLayout) getLayout()).columnWidths = new int[] { 0, 0 };
		((GridBagLayout) getLayout()).rowHeights = new int[] { 0, 0, 0, 0 };
		((GridBagLayout) getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
		((GridBagLayout) getLayout()).rowWeights = new double[] { 1.0, 0.0, 0.0, 1.0E-4 };
		add(lblThumbnail, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 0), 0, 0));

		// ---- lblDetails ----
		lblDetails.setFont(new Font("Arial", Font.PLAIN, 9));
		add(lblDetails, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.VERTICAL, new Insets(0, 0, 5, 0), 0, 0));

		// ---- lblTitle ----
		lblTitle.setFont(new Font("Arial", Font.ITALIC, 11));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblTitle, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Mouloud rachid
	private JLabel lblThumbnail;
	private JLabel lblDetails;
	private JLabel lblTitle;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
