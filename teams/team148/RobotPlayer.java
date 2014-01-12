package team148;

import battlecode.common.*;

import java.util.*;

public class RobotPlayer
{
    static Random rand;
    
	static Direction dir[] = {Direction.NORTH,Direction.SOUTH,Direction.EAST,Direction.WEST,Direction.NORTH_EAST, Direction.NORTH_WEST,Direction.SOUTH_EAST, Direction.SOUTH_WEST};
	static int count = 0;
	public static void run(RobotController rc)
	{
            rand=new Random();
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
                                                System.out.println(loc);
						GameObject go = rc.senseObjectAtLocation(loc);
                                               
						if(go==null)
						{
							rc.spawn(dir[(count+((int)Math.random()*8))%8]);
                                                         System.out.println(dir[(count+((int)Math.random()*8)%8)]);
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
                            int action = (rc.getRobot().getID()*rand.nextInt(101) + 50)%101;
			
                                                System.out.println("Action is "+action);
						if (action < 1 && rc.getLocation().distanceSquaredTo(rc.senseHQLocation()) > 2) 
							rc.construct(RobotType.PASTR);
					
                                                if(rc.canMove(dir[(count+((int)Math.random()*8))%8]))
						{
							flag = false;
							rc.move(dir[(count+((int)Math.random()*8))%8]);
							count = count++;
						}
					}
					
				}
				catch(Exception e)
				{
                                    //e.printStackTrace();
					//System.out.println("Error Type 2");
				}
			}
			rc.yield();
		}
		
	}
	
}

