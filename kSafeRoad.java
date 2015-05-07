import java.util.*;
import org.iiitb.es103_15.traffic.*;
import java.io.*;

public class kSafeRoad extends Road 
{
    private int[] directions = new int[2];
  
    public kSafeRoad(int dir, Intersection start, Intersection end, boolean entryStart, boolean entryEnd) 
	{
        	super(dir, start, end, entryStart, entryEnd);
        	directions[0] = dir;
        	directions[1] = RoadGrid.getOppDir((int)dir);
    	}	

    public kSafeRoad(int dir, Intersection start, Intersection end) 
	{
        	this(dir, start, end, true, true);
    	}

    public void checkCollisions() 
	{
	ArrayList front_cars =  getCarsL(directions[0]);
	ArrayList reverse_cars =  getCarsL(directions[1]);
	

    solve(front_cars);
	solve(reverse_cars);
        }
	
   public void solve(ArrayList cars)
	{
		if(cars.size()>0)
		{
			Car lead = (Car) cars.get(0);
			lead.carInFront(null);
			for(int i=1;i<cars.size();i++)
			{
				Car follow = (Car) cars.get(i);
				follow.carInFront(lead);
				lead=follow;
			}
		}
	} 		
}
