import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import org.iiitb.es103_15.traffic.Car;
import org.iiitb.es103_15.traffic.Coords;
import org.iiitb.es103_15.traffic.Intersection;
import org.iiitb.es103_15.traffic.RoadGrid;
import org.iiitb.es103_15.traffic.TrafficSignal;
import org.iiitb.es103_15.traffic.TrafficSignal.SignalListener;



public class car33 extends Car{
	Traffic t=new Traffic();
	TrafficSignal ts = null;
	Intersection inter;
	public static int dis;
	int stop=1,count,astop=1,carDist;
	Car carFront = null;
	private static int tcId = 1;
	private Color[] colors = new Color[]{Color.BLACK, Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN};
    private int tcarId = tcId++;
    private Color carColor = this.colors[this.tcarId % 5];
    
	
	public void updatePos(){
		super.updatePos();
		float s=getRoad().getSpeedLimit();
		if(getRoad().getDir()!=this.getDir()){
			inter = getRoad().getStartIntersection();
			}
			else{
				inter = getRoad().getEndIntersection();
			}
		if(astop==0 || inter.isOccupied()==true)
			accelerate(-10000);
		else if(astop==1 && inter.isOccupied()==false)
			accelerate(s-this.getSpeed());
		MyCrossIntersection();
	}
	
	public void drive(){
		super.drive();
		float s = getRoad().getSpeedLimit();
		accelerate(s);
	}
	
	private int getDist(Car c) {
        Coords p2 = c.getPos();
        Coords p1 = this.getPos();
        int dist = 0;
        dist = p2.x - p1.x + (p2.y - p1.y);
        if (dist < 0) {
            dist*=-1;
        }
        return dist - c.getLength();
    }

    public void carInFront(Car c2) {
        this.carFront = c2;
    }
	
	private class Traffic implements SignalListener{

		public void onChanged(int arg0) {
			if(arg0==0)
				stop=0;
			else
				stop=1;
		}
	}
	
	private void MyCrossIntersection(){
		
		
		Coords intco=inter.getCoords();
		Coords cur=this.getPos();
		
		if (this.carFront != null) {
			carDist = this.getDist(this.carFront);
        }
		
		if (this.carFront != null && carDist<20){
			astop=0;
		}
		else
			astop=1;
		
		
		dis=Coords.distSqrd(intco, cur);
		
		if(inter.getTrafficControl()!=null)
			ts= (TrafficSignal) inter.getTrafficControl();
		if(inter.getTrafficControl()!=null && count==0)
			{
			count++;
			synchronized(ts){
				ts.addListener(t,RoadGrid.getOppDir(this.getDir()));
				stop=ts.getSignalState(RoadGrid.getOppDir(this.getDir()));
				}
			}
		
		if(dis<400){
			if(stop==0)
				astop=0;
			else if(stop==1)
				astop=1;
			Random rand = new Random();
			//System.out.println(inter.isOccupied());
			if(stop==1 && inter.isOccupied()==false)
			{
			while(true){
			int dir = rand.nextInt(4);
			if(inter.getRoads()[dir] !=null && inter.getRoads()[dir]!=this.getRoad()){
					accelerate(-10000);
					if(inter.getTrafficControl()!=null){
					synchronized(ts){
							ts.removeListener(t, RoadGrid.getOppDir(getDir()));
							}
						}
					crossIntersection(inter,dir);
					count=0;
					astop=1;
					break;
					}
				}
			  }
			}
		}
	public void paint(Graphics g) {
        g.setColor(this.carColor);
        super.paint(g);
        g.setColor(RoadGrid.DEFAULT_COLOR);
    }	
	public String toString(){
		return "k33";
		
	}
	}
