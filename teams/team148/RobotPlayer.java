package team148;
import battlecode.common.*;
import java.util.*;
public class RobotPlayer
{
    static Random rd = new Random();
    static int last = 0;
	static Direction dir[] = {Direction.NORTH,Direction.SOUTH,Direction.EAST,Direction.WEST,Direction.NORTH_EAST, Direction.NORTH_WEST,Direction.SOUTH_EAST, Direction.SOUTH_WEST};
	static int count = 0;
	static boolean flag = true;
	public static void run(RobotController rc)
	{
		while(true)
		{
			//checking if the current robot is of the type headquaters
			if(rc.getType()==RobotType.HQ)
			{
				if(rc.isActive() && rc.senseRobotCount()<25)
				{
					//if the headquater is active and if the total players created is lesser than certain limit then.... 
					int rand = rd.nextInt(100); //create a RANDOM integer between 0(inclusive) and 100
					try
					{
						MapLocation loc = rc.getLocation().add(dir[rand%8]); //get the location of the map pointed by direction selected by dir[rand%8]
						GameObject go = rc.senseObjectAtLocation(loc); //is there any game object already present at that location
						if(go==null)
						{
							//if the location is free
							System.out.println(rand%8); //just for debugging
							rc.spawn(dir[rand%8]); //create a robot/player at that location
						}                                           
						else if(go.getTeam()==rc.getTeam().opponent())
						{
							//else if the location is occupied and it has an opposite team object then shoot it.
							if(rc.canAttackSquare(loc))
							{
								//but first we have to check if the tower is ready to attack
								rc.attackSquare(loc);
							}
						}                           
					}
					catch(Exception e)
					{
						System.out.println("Error Number 1");
					}
				}
			}
			else if(rc.getType()==RobotType.SOLDIER)
			{
				try
				{
					if(rc.isActive())
					{
						//check if the player is active
						//code to move the robot
						int rand = rd.nextInt(8); //generates a random integer
						if(flag)
						{
							//this code is only executed for the first time for any robot
							last = rd.nextInt(8); 
							flag = false;
						}
						if(rc.canMove(dir[last]))
						{
							//we give higher preference to the previous valid direction over others and if it is still valid to move in that direction then it moves or else it moves in some different direction 
							rc.move(dir[last]);
						}
						else if(rc.canMove(dir[rand%8]))
	                    {
							//if can't move in the previous direction then selects randomly a new direction and moves in that direction
							last = rand%8; //copies new direction to the last direction.
							rc.move(dir[rand%8]);
	                    }
					}
				}
				catch(Exception e)
				{
					System.out.println("Error Type 2");
				}
			}
			rc.yield(); //function used to end the round.
		}
		
	}
	
}

