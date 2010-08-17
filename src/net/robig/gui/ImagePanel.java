package net.robig.gui;

import javax.imageio.ImageIO;
import javax.swing.*; 

import net.robig.logging.Logger;

import java.awt.*; 
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/** Panel, welchem ein Hintergrundbild hinzugefuegt werden kann
 *  unterstuetzt gezerrt (SCALED), angepasst an Bildgroesse (FIT) 
 *  und gekachelt (TILED) (default)
 *  
 * @author robig
 * @version 0.5.1
 */
public class ImagePanel extends JPanel{ 
	protected ImageIcon bgImage;
	private static Logger log = new Logger(ImagePanel.class);
	
	/* double buffered: */
	private BufferedImage bufferImg;
	private Graphics bufferG;
	private boolean doubleBuffered=false;
	private JApplet applet;
	
	public enum DrawMode { SCALED, FIT, TILED };
	protected DrawMode mode=DrawMode.FIT; //default drawmode
	
	public void setDoubleBuffered(boolean b,JApplet a){
		if(b && a!=null || !b){
			doubleBuffered=b;
			applet=a;
			setSize(getWidth(),getHeight());
		}
	}
	
	private static Map<String,ImageIcon> imageMap=new HashMap<String,ImageIcon>();
	public synchronized static ImageIcon loadImageIcon(String path) {
		// Image cache:
		if(imageMap.size()>0){
			ImageIcon i=imageMap.get(path);
			if(i!=null) return i;
		}
		ImageIcon img = null;
		log.debug("Loading Image: "+path);
		URL url=ImagePanel.class.getResource(path);
		if(url==null){
			//retry with leading slash
			url=ImagePanel.class.getResource("/"+path);
			if(url==null){
				try {
					// Eclipse needs an own implementation: :-S
					BufferedImage bimg = ImageIO.read(new File(path));
					img=new ImageIcon(bimg);
				} catch (IOException e) {
					log.error("Could not load image: "+path+": "+e.getMessage());
					e.printStackTrace(log.getDebugPrintWriter());
					return null;
				}
				if(img==null){
					log.error("Could not load image: "+path);
					return null;
				}
			}
		}
		if(img==null)
			img = new ImageIcon(url);
		imageMap.put(path, img);	
		return img;
	}
	
	public static Image loadImage(String path){
		return loadImageIcon(path).getImage();
	}
	
	public ImagePanel(String string){
		bgImage=loadImageIcon(string);
		setFocusable(false);
		initComponent();
	}
	
	/** setzt die Hintergrundfarbe
	 */
	public void setBackground(Color c){
		if(c!=null){
			setOpaque(true);
		}else
			setOpaque(false);
		super.setBackground(c);
	}
	
	/* Initialisieren mit Bild */ 
	public ImagePanel(Image img){ 
	    if(img!=null) {
	        setImage(img);
	    }
	    setFocusable(false);
	    initComponent();
	} 
	
	protected void initComponent(){
		setSize(0,0);
	    //setBorder(BorderFactory.createLineBorder(new Color(255,0,0)));
		setOpaque(false);
	}
	
	/** setze Hintergrunfbild (neu) */
	public void setImage(Image img){
	    if(img!=null){
	    	bgImage = new ImageIcon(img);
	    	flush();
	    	//repaint(); //neu zeichnen
	        //System.out.println("bild groesse: "+img.getHeight(null)+"x"+img.getWidth(null));
	    }else{System.out.println(this.toString()+": got null pointer as image!");}
	}
	
	public void setImage(Image img, DrawMode m){
		setImage(img);
		setMode(m);
	}
	
	/** setze ZeienModus neu
	 * @param m - SCALED oder TILED oder FIT
	 */
	public void setMode(DrawMode m){
		mode=m;
	}
	
	private BufferedImage convert(Image im)
	{
	    BufferedImage bi = new BufferedImage(im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_ARGB_PRE);
	    Graphics bg = bi.getGraphics();
	    bg.drawImage(im, 0, 0, null);
	    bg.dispose();
	    return bi;
	}
	
	/** entferne das akt. Hintergrundbild */
	public void removeImage(){
		bgImage=null;
	}
	
	/*public void update(Graphics g){
		System.out.println("update "+this);
		paint(g);
	}*/
	
	/*
	public void paint(Graphics g) {
		Graphics2D g2;
		if(!doubleBuffered)
			g2=(Graphics2D)g;
		else
			g2=bufferG;
		
		tileImage(g2);

		if(doubleBuffered){
			g.drawImage(bufferImg,0,0,this);
			g2.dispose();
		}
		
		paintComponents(g);
	} */
	
	public Image getImage(){
		return bgImage.getImage();
	}
	
	private void scaleImage(Graphics g){
		if(bgImage != null){
			g.drawImage(getImage(),0,0,getImageWidth(),getImageHeight(),null );
		}
	}
	
	private void tileImage(Graphics g){
		if(bgImage != null) {
			Graphics2D g2=(Graphics2D)g;
			int x = 0, y = 0; 
		    while(y < getHeight()) { 
		    	x = 0; 
		        while(x< getWidth()) {
		        	
		        	bgImage.paintIcon(this, g2, x, y);
		        	//g2.drawImage(bgImage, x, y, this); 
		            x=x+getImageWidth(); 
		        } 
		        y=y+getImageHeight(); 
		    }
		} 
	}
	
	private void fitImage(Graphics g){
		if(bgImage!=null){
			fitToImage();
			g.drawImage(bgImage.getImage(),0,0,this);
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//if(bgImage!=null) g.drawImage(bgImage.getImage(),0,0,this);
		switch(mode){
		case SCALED:
			scaleImage(g);
			break;
		case FIT:
			fitImage(g);
			break;
		case TILED:
			tileImage(g);
		}
	}

	public void fitToImage(){
		if(bgImage==null) return;
		setSize(getImageWidth(),getImageHeight());
	}
	
	/** @return int - gibt die Elementhoehe zurueck */
	public int getImageHeight(){
		return bgImage.getIconHeight();
	}
	
	/** @return int - gibt die Elementbreite zurueck */
	public int getImageWidth(){
		return bgImage.getIconWidth();
	}
	
	/** setzt Elementhoehe und -breite */
	public void setSize(int w, int h){
		if(w*h<=0 && !(w>0 && w>0)){
			if(bgImage!=null){
				w=getImageWidth();
				h=getImageHeight();
			}
			if(!(w>0 && h>0)){
				w=50; h=50;
			}
		}
		Dimension d=new Dimension(w,h);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		if(doubleBuffered && applet!=null){
			bufferImg = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
			bufferG = bufferImg.getGraphics();
		}
	}
	
	public String toString(){
		if(bgImage==null) return "no Image loaded!";
		return "Image "+bgImage+" size: "+getImageWidth()+"x"+getImageHeight();
	}
	
	/** startet (GIF) Animation neu, und loest repaint() aus */
	public void flush(){
		if(bgImage!=null){
			//System.out.println("flushing "+this);
			bgImage.getImage().flush();
		}
		repaint();
	}

} 