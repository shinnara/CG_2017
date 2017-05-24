package climbing;

import java.util.ArrayList;

public class GeomUtil {

    public static Pnt getCenter(Pnt a, Pnt b)
    {
    	return new Pnt( (a.getX() + b.getX())/2.0, (a.getY() + b.getY())/2.0 ); 
    }
    // a -- aa -- /point/ -- bb -- b 
    public static Pnt getMiddlePoint(Pnt a, Pnt b, double aa, double bb){
     return new Pnt(a.getX()+aa*(b.getX()-a.getX())/(aa+bb),
                    a.getY()+aa*(b.getY()-a.getY())/(aa+bb)); 
    }
    // return point r which is far from a  
    public static Pnt getProportionalPoint(Pnt a, Pnt b, double leng){
      double dist = getDistance(a,b);
      Pnt c = b.subtract(a);
      Pnt r = new Pnt(c.getX()/dist*leng,c.getY()/dist*leng);
      return r.add(a);
    }
    public static int ccw(Pnt a, Pnt b, Pnt c){
      double term = a.getX()*b.getY()+b.getX()*c.getY()+c.getX()*a.getY();
      term -= a.getY()*b.getX()+b.getY()*c.getX()+c.getY()*a.getX();
      if (Math.abs(term)>Math.pow(10, -5)) return 1;
      else if (Math.abs(term)<Math.pow(10, -5))return -1;
      else return 0;
    }
    // if type 0 then return the point which is located on the cw direction
    // else if type 1 then return ccw direction
    public static Pnt getCircleIntersection(Pnt a, Pnt b, double aa, double bb, boolean type){
      double distAB = getDistance(a, b);
      double distAC = (aa*aa-bb*bb+distAB*distAB)/(2*distAB);
      double distCI = Math.sqrt(Math.abs(aa*aa-distAC*distAC));
      Pnt c = new Pnt(a.getX()+distAC*(b.getX()-a.getX())/distAB,
                  a.getY()+distAC*(b.getY()-a.getY())/distAB);
      // if meet 1 point
      if(Math.abs(distAB-aa-bb)-Math.pow(10, -5)<0) return c;
      
      Pnt in1 = new Pnt(c.getX()+distCI*(b.getY()-a.getY())/distAB,
                        c.getY()-distCI*(b.getX()-a.getX())/distAB);
      Pnt in2 = new Pnt(c.getX()-distCI*(b.getY()-a.getY())/distAB,
                        c.getY()+distCI*(b.getX()-a.getX())/distAB);
//      String temp = "a~ " + getDistance(a, in1)
//                    +" b~ "  + getDistance(b, in1)
//                    +" 占쏙옙占쏙옙占신몌옙 " + distAB;
//      System.out.println(temp);

//      System.out.println(a);
//      System.out.println(b);
//      System.out.println(c);
//      System.out.println(in1);
//      System.out.println(in2);
//      System.out.println(getDistance(b, new Pnt(155.551167,266.456229)));
//      System.out.println(getDistance(b, in2));
//      
      
      if(ccw(a,c,in1)==1)
        return (type?in2:in1);
      else  
        return (type?in1:in2);
    }
	/* getAngle(Pnt a, Pnt b)
	 * 占쏙옙 b占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙, 占쏙옙占쏙옙 ab占쏙옙 占싱뤄옙占� 占쏙옙占쏙옙. 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙	 
	 */
    public static double getAngle(Pnt a, Pnt b)
    {    	
    	double height = b.getY() - a.getY();
    	double width  = a.getX() - b.getX();
    	if( width == 0 ) return 0.0;
    	
    	double angle;
    	
    	if( width > 0 ) angle = Math.atan( width / height);
    	else angle = -1 * Math.atan( width*-1.0 / height);
    	   	
    	return angle;       	
    }
    
    public static double getDistance(Pnt a, Pnt b)
    {
    	return Math.sqrt( Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2) );
    }
	
    public static Pnt getCenterPointOfMan(Man man, ArrayList<Pnt> pointList)
    {
    	Pnt manLH = pointList.get(man.getLh());
   		Pnt manRH = pointList.get(man.getRh());
   		Pnt manLF = pointList.get(man.getLf());
   		Pnt manRF = pointList.get(man.getRf());
       	
    	Pnt bisectH = GeomUtil.getCenter(manLH, manRH);
    	Pnt bisectF = GeomUtil.getCenter(manLF, manRF);
    	Pnt center = GeomUtil.getCenter(bisectH, bisectF);
    	
    	return center;
    }
    public static ArrayList<Pnt> get3CircleTriangle(Pnt a, Pnt b, Pnt c, int r1, int r2, int r3){
        ArrayList<Pnt> inner = new ArrayList<Pnt>();
	    Pnt[] parr = {a,b,c};
	    int[] rarr = {r1,r2,r3};
	    
	    for(int i = 0; i < parr.length; i++){
	    	int j = (i+1)%parr.length;
	    	int k = (i+2)%parr.length;
	    	Pnt p1 = GeomUtil.getCircleIntersection(parr[i], parr[j], rarr[i], rarr[j], false);
	        Pnt p2 = GeomUtil.getCircleIntersection(parr[i], parr[j], rarr[i], rarr[j], true);
	        if((rarr[k]-GeomUtil.getDistance(p1,parr[k]))>Math.pow(10, -5))
	        	inner.add(p1);
	        else
	        	inner.add(p2);  
	    }
	    return inner;
	 }
	    
    public static Pnt getCircleCenter(Pnt p1, Pnt p2, Pnt p3) {
    	
       double offset = Math.pow(p2.getX(),2) + Math.pow(p2.getY(),2);
       double bc =   ( Math.pow(p1.getX(),2) + Math.pow(p1.getY(),2) - offset )/2.0;
       double cd =   (offset - Math.pow(p3.getX(), 2) - Math.pow(p3.getY(), 2))/2.0;
       double det =  (p1.getX() - p2.getX()) * (p2.getY() - p3.getY()) - (p2.getX() - p3.getX())* (p1.getY() - p2.getY()); 

       if (Math.abs(det) < 0.0000001) { throw new IllegalArgumentException("Yeah, lazy."); }

       double idet = 1/det;

       double centerx =  (bc * (p2.getY() - p3.getY()) - cd * (p1.getY() - p2.getY())) * idet;
       double centery =  (cd * (p1.getX() - p2.getX()) - bc * (p2.getX() - p3.getX())) * idet;
        
       return new Pnt(centerx, centery);
    }
}
