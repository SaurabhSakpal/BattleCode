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
			if(rc.getType()==RobotType.HQ)
			{
				if(rc.isActive() && rc.senseRobotCount()<25)
				{
					int rand = rd.nextInt(100);
					try
					{
						MapLocation loc = rc.getLocation().add(dir[rand%8]);
						GameObject go = rc.senseObjectAtLocation(loc);
						if(go==null)
						{
							System.out.println(rand%8);
							rc.spawn(dir[rand%8]);
						}
                                                
						else if(go.getTeam()==rc.getTeam().opponent())
						{
							if(rc.canAttackSquare(loc))
							{
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
						//code to move the robot
						int rand = rd.nextInt(8);
						if(flag)
						{
							last = rd.nextInt(8);
							flag = false;
						}
						if(rc.canMove(dir[last]))
						{
							rc.move(dir[last]);
						}
						else if(rc.canMove(dir[rand%8]))
	                    {
							last = rand%8;
							rc.move(dir[rand%8]);
	                    }
					}
				}
				catch(Exception e)
				{
					System.out.println("Error Type 2");
				}
			}
			rc.yield();
		}
		
	}
	
}

