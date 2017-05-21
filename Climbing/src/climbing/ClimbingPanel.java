package climbing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class ClimbingPanel extends JPanel {

	/**
	 * ClimbingPanel
	 */
	private static final long serialVersionUID = 1L;
	private Graphics g;
	public static int pointRadius = 3;
	public static int handRadius  = 5;

	private BufferedImage imgAndroid;
	AffineTransform transIdentity = new AffineTransform();
	
	private String title;
	
	private ArrayList<Pnt> pointList;
	
	private Pnt manLH, manRH, manLF, manRF;
	private boolean isManDeclared = false;
	
	public ClimbingPanel()
	{
		pointList = new ArrayList<Pnt>();
		title = null;
		try{
			imgAndroid = ImageIO.read(new File("android_torso.png"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void addPnt(Pnt point)
	{
		pointList.add(point);
	}
	
	public void setMan(Pnt lh, Pnt rh, Pnt lf, Pnt rf)
	{
		manLH = lh;
		manRH = rh;
		manLF = lf;
		manRF = rf;
		isManDeclared = true;
	}
	
	public void clearMan() { isManDeclared = false; }
	
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        this.g = g;
               
        Color temp = g.getColor();
           
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        
        drawTitle(); /* draw Ttitle */
        
        g.setColor(Color.BLUE);
        for(Pnt point : pointList)
        	draw(point);
        
        if( isManDeclared )   
        {	
        	Pnt bisectH = getCenter(manLH, manRH);
        	Pnt bisectF = getCenter(manLF, manRF);
        	System.out.println("BisectH:" + bisectH.toString());
        	System.out.println("BisectF:" + bisectF.toString());
        	Pnt center = getCenter(bisectH, bisectF);
        	Double angle = getAngle(bisectH, bisectF);
        	System.out.println("Angle: " + angle + ", " + Math.toDegrees(angle));
        	drawMan(manLH, manRH, manLF, manRF, center, angle);
        }
        
        g.setColor(temp);
    }
    
    
    public Pnt getCenter(Pnt a, Pnt b)
    {
    	return new Pnt( (a.getX() + b.getX())/2.0, (a.getY() + b.getY())/2.0 ); 
    }
    
    public double getAngle(Pnt a, Pnt b)
    {    	
    	double height = b.getY() - a.getY();
    	double width  = a.getX() - b.getX();
    	if( width == 0 ) return 0.0;
    	
    	double angle;
    	
    	if( width > 0 ) angle = Math.atan( width / height);
    	else angle = -1 * Math.atan( width*-1.0 / height);
    	   	
    	return angle;    	
    	
    }
    
    public void drawTitle() {
    	if( title == null ) return;
    	
    	Color temp = g.getColor();
    	Font tempFont = g.getFont();
    	
    	g.setColor(Color.BLACK);
    	Font titleFont = new Font(g.getFont().getName(), Font.BOLD | Font.ITALIC , 15);
    	g.setFont(titleFont);
    	g.drawString(title, 25, 20);
    	
    	g.setColor(temp);
    	g.setFont(tempFont);
    }
    
    public void draw (Pnt point) {
        int r = pointRadius;
        int x = (int) point.coord(0);
        int y = (int) point.coord(1);
        g.fillOval(x-r, y-r, r+r, r+r);
    }
    
    
    public void drawMan(Pnt leftHand, Pnt rightHand, Pnt leftFoot, Pnt rightFoot, Pnt center, double angle)
    {
    	Color temp = g.getColor();
    	
    	g.setColor(Color.RED);    	
    	drawHand(leftHand,5);
    	g.setColor(Color.GREEN);
    	drawHand(rightHand,7);
    	g.setColor(Color.RED); 
    	drawHand(leftFoot,5);
    	g.setColor(Color.GREEN);
    	drawHand(rightFoot,7);
    	    	
    	int maWidth = 50;
    	int maHeight = 90;
    	Image miniAndroid = imgAndroid.getScaledInstance(50, 90, Image.SCALE_DEFAULT);
    	
    	Graphics2D g2d = (Graphics2D) g;
    	Stroke tempStroke = g2d.getStroke();
    	g2d.setStroke(new BasicStroke(5));
    	g2d.setColor(Color.GREEN);
    	g2d.drawLine((int)center.getX(), (int)center.getY(), (int)leftHand.getX(), (int)leftHand.getY());
    	
    	g2d.setColor(Color.GREEN);
    	g.drawLine((int)center.getX(), (int)center.getY(), (int)rightHand.getX(), (int)rightHand.getY());
    	
    	g2d.setColor(Color.GREEN);
    	g2d.drawLine((int)center.getX(), (int)center.getY(), (int)leftFoot.getX(), (int)leftFoot.getY());
    	
    	g2d.setColor(Color.GREEN);
    	g2d.drawLine((int)center.getX(), (int)center.getY(), (int)rightFoot.getX(), (int)rightFoot.getY());
    	
    	g2d.setStroke(tempStroke);
    	
    	AffineTransform trans = new AffineTransform();
    	trans.setToTranslation(center.getX() - maWidth/2, center.getY()- maHeight/2);
    	trans.rotate( angle, maWidth/2, maHeight/2); 	
    	
    	System.out.println("Center Point: " + center.toString());
    	g2d.drawImage(miniAndroid, trans, null);
    	
    	
    	
    	g.setColor(temp);
    }
    
    public void drawHand(Pnt point, int r)
    {
        int x = (int) point.coord(0);
        int y = (int) point.coord(1);
        g.drawOval(x-r, y-r, r+r, r+r);        
    }
    
}
