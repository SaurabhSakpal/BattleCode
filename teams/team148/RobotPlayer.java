package team148;

import battlecode.common.*;



public class RobotPlayer
{
	static Direction dir[] = {Direction.NORTH,Direction.SOUTH,Direction.EAST,Direction.WEST,Direction.NORTH_EAST, Direction.NORTH_WEST,Direction.SOUTH_EAST, Direction.SOUTH_WEST};
	static int count = 0;
	public static void run(RobotController rc)
	{
		while(true)
		{
			if(rc.getType()==RobotType.HQ)
			{
				if(rc.isActive() && rc.senseRobotCount()<25)
				{
					
					count = count +1;
					try
					{
						MapLocation loc = rc.getLocation().add(dir[count%8]);
						GameObject go = rc.senseObjectAtLocation(loc);
						if(go==null)
						{
							rc.spawn(dir[count%8]);
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
					boolean flag = true;
					while(flag)
					{
						if(rc.canMove(dir[count%8]))
						{
							flag = false;
							rc.move(dir[count%8]);
							count = count++;
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

