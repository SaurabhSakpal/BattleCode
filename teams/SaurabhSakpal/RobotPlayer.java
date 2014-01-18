package SaurabhSakpal;

import battlecode.common.*;
import java.util.*;
public class RobotPlayer
{
    static Random rd = new Random();
    static int last = 0;
	static Direction dir[] = {Direction.NORTH,Direction.SOUTH,Direction.EAST,Direction.WEST,Direction.NORTH_EAST, Direction.NORTH_WEST,Direction.SOUTH_EAST, Direction.SOUTH_WEST};
	static int count = 0;
	static boolean flag = true;
	static boolean flag2 = true;
	static RobotController src;
	static int totalPermittedPastures = 4;
	static int totalPermittedSwarmA = 4;
	static int totalPermittedSwarmB = 4;
	static int totalPermittedCowBoy =4;
	static int type;
	
	public static void initalCowboyProcessing()throws Exception
	{
			int a = src.readBroadcast(0);
			int b = src.readBroadcast(1);
			int c = src.readBroadcast(2);
			int d = src.readBroadcast(3);
		 if(a<totalPermittedSwarmA)
		 {
			 type = 0;
			 src.broadcast(0,a+1);
		 }
		 else if(b<totalPermittedSwarmB)
		 {
			 type = 1;
			 src.broadcast(1, b+1);
		 }
		 else if(c<totalPermittedPastures)
		 {
			 type = 2;
			 src.broadcast(2, c+1); 
		 }
		 else if(d<totalPermittedCowBoy)
		 {
			 type = 3;
			 src.broadcast(3, d+1);
		 }
	}
	public static void headQuaters()throws Exception
	{
		if(src.isActive() && src.senseRobotCount()<25)
		{
				if(flag2)
				{
					src.broadcast(0,0);
					src.broadcast(1,0);
					src.broadcast(2,0);
					src.broadcast(3,0);
					flag2 = false;
				}
			//if the headquater is active and if the total players created is lesser than certain limit then.... 
				int rand = rd.nextInt(100); //create a RANDOM integer between 0(inclusive) and 100
				MapLocation loc = src.getLocation().add(dir[rand%8]); //get the location of the map pointed by direction selected by dir[rand%8]
				GameObject go = src.senseObjectAtLocation(loc); //is there any game object already present at that location
				if(go==null)
				{
					//if the location is free
					System.out.println(rand%8); //just for debugging
					src.spawn(dir[rand%8]); //create a robot/player at that location
				}                                           
				else if(go.getTeam()==src.getTeam().opponent())
				{
					//else if the location is occupied and it has an opposite team object then shoot it.
					if(src.canAttackSquare(loc))
					{
						//but first we have to check if the tower is ready to attack
						src.attackSquare(loc);
					}
				}                           
		}
		
	}
	public static void cowBoy()throws Exception
	{
		if(flag2)
		{
			initalCowboyProcessing();
			flag2 = false;
		}
		if(src.isActive())
		{
			src.setIndicatorString(0,""+src.readBroadcast(0));
			src.broadcast(0, src.getRobot().getID());
			//check if the player is active
			//code to move the robot
			int rand = rd.nextInt(8); //generates a random integer
			if(flag)
			{
				//this code is only executed for the first time for any robot
				last = rd.nextInt(8); 
				flag = false;
			}
			if(src.canMove(dir[last]))
			{
				//we give higher preference to the previous valid direction over others and if it is still valid to move in that direction then it moves or else it moves in some different direction 
				src.move(dir[last]);
			}
			else if(src.canMove(dir[rand%8]))
            {
				//if can't move in the previous direction then selects randomly a new direction and moves in that direction
				last = rand%8; //copies new direction to the last direction.
				src.move(dir[rand%8]);
            }
		}
	}
	public static void run(RobotController rc)
	{
		src = rc;
		while(true)
		{
			//checking if the current robot is of the type headquaters
			try
			{
				if(rc.getType()==RobotType.HQ)
				{
					headQuaters();
				}
				else if(rc.getType()==RobotType.SOLDIER)
				{
					cowBoy();
				}
			}
			catch(Exception e)
			{
				System.out.println("Exception Caught");
			}
			rc.yield(); //function used to end the round.
		}
		
	}
	
}


