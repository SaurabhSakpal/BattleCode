package SaurabhSakpal;

/*

 1) Channel 0-3: For 4 types of Robots
 2) Channel 4: For HeadQuater Location 
 ------------------------------------------------------------------------------------------------
 3) Channel 10: For Status Check if HQ has written the path to the channel(NoiseTower)
 4) Channel 100: for the length of Path(Noise Tower)
 5) Channel 101-300: Broadcasting the path of the NOISETOWER.
 6) Channel 11: For the destination of Noise Tower.
 ------------------------------------------------------------------------------------------------
 7) Channel 90 : For PASTR 1 initial Location
 8) Channel 91 : For PASTR 2 initial Location
 9) Channel 92 : For PASTR 3 initial Location
 10) Channel 93 : For PASTR 4 initial Location
 11) Channel 94 : For PASTR 5 initial Location
 
 */

import battlecode.common.*;

import java.util.*;

public class RobotPlayer
{
    static Random rd = new Random();
    static int last = 0;
	static Direction dir[] = {Direction.NORTH,Direction.SOUTH,Direction.EAST,Direction.WEST,Direction.NORTH_EAST, Direction.NORTH_WEST,Direction.SOUTH_EAST, Direction.SOUTH_WEST};
	static TerrainTile tTile[] = {TerrainTile.VOID,TerrainTile.ROAD,TerrainTile.NORMAL};
	static int count = 0;
	static long ridchannel[] = {300,501,702,903,1104};
	static int ridcha[] = {300,500,700,900,1100};
	static boolean flag = true;
	static boolean flag2 = true;
	static boolean flag3 = false;
	static boolean temp2 = true;
	static boolean temp3 = false;
	static boolean temp4 = false;
	static boolean yuva = false;
	static boolean draco = false;
	static int stepc = 0;
	static int usc[] = null;
	static int MapTerrain[][] = null;
	static int var1 = 0;
	static RobotController src;
	static int totalPermittedPastures = 5;
	static int totalPermittedSwarmA = 10;
	static int totalPermittedNoiseTower = 1;
	static int totalPermittedCowBoy =4;
	static int type;
	static int type0cons = 1;
	private static void swarmMove(MapLocation averagePositionOfSwarm, MapLocation currentloc) throws Exception
	{
		
		Direction d = currentloc.directionTo(averagePositionOfSwarm);
		if(src.isActive())
		{
			if(src.canMove(d))
				src.move(d);
		}
	}
	private static MapLocation mladd(MapLocation m1, MapLocation m2)
	{
		return new MapLocation(m1.x+m2.x,m1.y+m2.y);
	}
	
	private static MapLocation mldivide(MapLocation bigM, int divisor)
	{
		return new MapLocation(bigM.x/divisor, bigM.y/divisor);
	}

	private static int locToInt(MapLocation m)
	{
		return (m.x*100 + m.y);
	}
	
	private static MapLocation intToLoc(int i)
	{
		return new MapLocation(i/100,i%100);
	}
	private static void tryToShoot() throws GameActionException {
		//shooting
		Robot[] enemyRobots = src.senseNearbyGameObjects(Robot.class,10000,src.getTeam().opponent());
		if(enemyRobots.length>0)
		{//if there are enemies
			Robot anEnemy = enemyRobots[0];
			RobotInfo anEnemyInfo;
			anEnemyInfo = src.senseRobotInfo(anEnemy);
			if(anEnemyInfo.location.distanceSquaredTo(src.getLocation())<src.getType().attackRadiusMaxSquared)
			{
				if(src.isActive())
				{
					src.attackSquare(anEnemyInfo.location);
				}
			}
		}
	}
	public static void headQuatersIntialBroadcast(int x, int y)throws Exception
	{
		src.broadcast(0,0);
		src.broadcast(1,0);
		src.broadcast(2,0);
		src.broadcast(3,0);
		src.broadcast(4,(x*100)+y);

	}
	public static void initalCowboyProcessing()throws Exception
	{
			int a = src.readBroadcast(0);
			int b = src.readBroadcast(1);
			int c = src.readBroadcast(2);
			int d = src.readBroadcast(3);
			if(a<totalPermittedNoiseTower)
			{
				type = 0;
				src.broadcast(0,a+1);
			}
			else if(b<totalPermittedPastures)
			{
				MapLocation loc = src.getLocation();
				int b1 = loc.x; 
				int b2 = loc.y;
				ridchannel[b] = ((src.getRobot().getID())*10000)+ridchannel[b];
				src.broadcast(90+b,((100*b1)+b2));
				//System.out.println(ridchannel[b]);
				type = 1;
				src.broadcast(1,b+1);
			}
			else if(c<totalPermittedSwarmA)
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
		src.setIndicatorString(0,"ByteCode Left"+Clock.getBytecodesLeft());
		MapLocation mp = src.getLocation();
		int hqx = mp.x;
		int hqy = mp.y;
		if(src.isActive())
		{
				if(flag2)
				{
					headQuatersIntialBroadcast(hqx,hqy);
					flag2 = false;
				} 
				int rand = rd.nextInt(100);
				MapLocation loc = src.getLocation().add(dir[rand%8]);
				GameObject go = src.senseObjectAtLocation(loc);
				if(go==null&& src.senseRobotCount()<GameConstants.MAX_ROBOTS)
				{
					src.spawn(dir[rand%8]);
				}                                           
				else if(go.getTeam()==src.getTeam().opponent())
				{
					if(src.canAttackSquare(loc))
					{
						src.attackSquare(loc);
					}
				}                           
		}
		else
		{
			if((temp2)&&(!flag2))
				{
					MapTerrain = MapProcessor.HQProcessMap(src,hqx,hqy);
					int klp = src.readBroadcast(11);
					//System.out.println(Clock.getRoundNum()+" "+klp);
					if(klp!=0)
					{
						int a[][] = MapProcessor.findPathOnMap(src,klp/100,klp%100,hqx,hqy,MapTerrain);
						src.broadcast(100,a.length);
						for(int i = 0;i<a.length;i++)
						{
							int j = (100*a[i][0])+a[i][1];
							//System.out.println(j);
							src.broadcast(101+i,j);
						}
						src.broadcast(10,-1);
						temp2 = false;
						//System.out.println("Awesome");
						int gatech = src.readBroadcast(11);
						usc = MapProcessor.findPositionForPasture(src,gatech/100,gatech%100);
//						for(int i = 0;i<5;i++)
//						{
//							//System.out.println((usc[i]/100)+" +++++++ "+(usc[i]%100));
//						}
						MapTerrain[klp%100][klp/100] = 0;
						MapTerrain[hqy][hqx] = 0;
						temp3 = true;
					}
				}
			else if(temp3)
			{
				//code for the pasture towers.
				int xolo = src.readBroadcast(90+var1);
				//System.out.println(xolo);
				if(xolo!=0)
				{
					//System.out.println((usc[var1]/100)+" +++++++ "+(usc[var1]%100));
					int a[][] = MapProcessor.findPathOnMap(src,usc[var1]/100,usc[var1]%100,xolo/100,xolo%100,MapTerrain);
					src.broadcast(80+var1,a.length-1);
					for(int i = 0;i<a.length;i++)
					{
						int j = (100*a[i][0])+a[i][1]; 
						src.broadcast(ridcha[var1]+i,j);
						//System.out.println(a[i][0]+" **** "+a[i][1]);
					}
					src.broadcast(15+var1,-1);
					int op = (100*a[a.length-1][0])+a[a.length-1][1];
					src.broadcast(50+var1,op);
					MapTerrain[usc[var1]%100][usc[var1]/100] = 0;
					++var1;
				}
				if(var1>=5)
				{
					temp3 = false;
					temp4 = true;
//					int width = src.getMapWidth();
//					int height = src.getMapHeight();
//					for(int v = 0;v<height;v++)
//					{
//						for(int z = 0;z<width;z++)
//						{
//							System.out.print(MapTerrain[v][z]+" ");
//						}
//						System.out.println();
//					}
				}
			}
			else if(temp4)
			{
				if(src.readBroadcast(2)>4&&!yuva)
				{
					draco = true;
					yuva = true;
				}
				if(draco)
				{
					System.out.println("Flag DRACO set");
					src.broadcast(9000, -1);
					draco = false;
				}
				int round = Clock.getRoundNum();
				if(round%2==0)
				{
					//System.out.println(" HQ::EVEN "+src.readBroadcast(5001)+" "+src.readBroadcast(5003)+" "+src.readBroadcast(2));
					src.broadcast(5000,0);
					src.broadcast(5001,0);
				}
				else
				{
					//System.out.println(" HQ::ODD "+src.readBroadcast(5001)+" "+src.readBroadcast(5003)+" "+src.readBroadcast(2));
					src.broadcast(5002,0);
					src.broadcast(5003,0);
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
			if(type == 3)
			{
				tryToShoot();
//				canSenseObject(GameObject o)
				int rand = rd.nextInt(8);
				if(flag)
				{
					last = rd.nextInt(8); 
					flag = false;
				}
				if(src.canMove(dir[last]))
				{
					src.move(dir[last]);
				}
				else if(src.canMove(dir[rand%8]))
	            {
					last = rand%8;
					src.move(dir[rand%8]);
	            }
			}
			else if(type==0)
			{
				if(!flag3&&src.readBroadcast(10)==-1)
				{
					flag3 = true;
				}
				if(flag3)
				{
					int num2 = src.readBroadcast(11);
					//System.out.println("Inside Soilder"+num2);
					if(num2!=0)
					{
						MapLocation mq = src.getLocation();
						int sx = num2/100;
						int sy = num2%100;
						//System.out.println(sx+" ### "+sy);
						if(mq.y==sy&&mq.x==sx)
						{
							if(src.isActive())
								src.construct(RobotType.NOISETOWER);
						}
						else if(src.isActive())
						{
							int loc = src.readBroadcast(101+type0cons); 
							MapLocation dp = new MapLocation(loc/100,loc%100);
							++type0cons;
							Direction dl = mq.directionTo(dp);
							if(src.canMove(dl))
							{
								src.move(dl);
							}	
						}
					}
				}
			}
			else if(type==1)
			{
				//CODE FOR PASTR
				//src.setIndicatorString(0,""+src.getActionDelay());
				int a = src.getRobot().getID();
				int channel = 0; 
				int b = 0;
				for(int i = 0;i<5;i++)
				{
					if(ridchannel[i]/10000==a)
					{
						channel = (int)ridchannel[i]%10000;
						b = channel%10;
						break;
					}
				}
				int num2 = src.readBroadcast(50+b);
//				System.out.println("~~~ "+channel);
				if(num2!=0)
				{
					stepc = src.readBroadcast(80+b);
					MapLocation mq = src.getLocation();
					int sx = num2/100;
					int sy = num2%100;
					if(channel==501)
						System.out.println(mq.x+" ### "+mq.y);
					if((mq.y==sy&&mq.x==sx)||(type0cons>=stepc))
					{
						if(src.isActive())
							src.construct(RobotType.PASTR);
					}
					else if(src.isActive()&&(mq.y!=sy||mq.x!=sx))
					{
						int loc = src.readBroadcast(channel+type0cons); 
						MapLocation dp = new MapLocation(loc/100,loc%100);
						if(channel==501)
							System.out.println((loc/100)+","+(loc%100)+" ---->"+sx+","+sy);
						++type0cons;
						Direction dl = mq.directionTo(dp);
						if(src.canMove(dl)&&src.isActive())
						{
							src.move(dl);
						}	
					}
				}
				
			}
			else if(type ==2)
			{
				int hillu = src.readBroadcast(9000);
				if(hillu==-1)
				{
					//System.out.println("Flag YUVA set");
					yuva = true;
				}
				//tryToShoot();
				if(yuva)
				{
					//System.out.println("Entered Yuva");
					MapLocation current = src.getLocation();
					//int rid = src.getRobot().getID();
					int round = Clock.getRoundNum();
					MapLocation averagePositionOfSwarm;
					if(round%2==0)
					{
						int numero1 = src.readBroadcast(5000);
						int numero2 = src.readBroadcast(5001);
						int numero3 = src.readBroadcast(5002);
						int numero4 = src.readBroadcast(5003)==0?src.readBroadcast(2):src.readBroadcast(5003);
						//System.out.println(rid+" Even Round: "+numero1+","+numero2+","+numero3+","+numero4);
						averagePositionOfSwarm = mldivide(intToLoc(numero3),numero4);
						src.broadcast(5000,locToInt(mladd(intToLoc(numero1),current)));
						src.broadcast(5001,numero2+1);
					}
					else
					{
						int numero1 = src.readBroadcast(5000);
						int numero2 = src.readBroadcast(5001);
						int numero3 = src.readBroadcast(5002)==0?src.readBroadcast(2):src.readBroadcast(5002);
						int numero4 = src.readBroadcast(5003);
						//System.out.println(rid+" ODD Round: "+numero1+","+numero2+","+numero3+","+numero4);
						averagePositionOfSwarm = mldivide(intToLoc(numero1),numero2);
						src.broadcast(5002,locToInt(mladd(intToLoc(numero3),current)));
						src.broadcast(5003,numero4+1);
					}
					if(rd.nextInt()%2==0)
					{
						swarmMove(averagePositionOfSwarm,current);
					}
					else
					{
							int rand = rd.nextInt(100);
							if(src.canMove(dir[rand%8]))
							{
								src.move(dir[rand%8]);
							}
					}
				}
				else
				{
					int daku = src.readBroadcast(2);
					int rand = rd.nextInt(100);
						if(src.canMove(dir[rand%8]))
						{
							src.move(dir[rand%8]);
						}
					src.broadcast(5001,daku);
					src.broadcast(5003,daku);	
				}
			}
		}
	}
	public static void noisetower()throws Exception
	{
		MapLocation mp = src.getLocation();
		MapLocation uic[] = MapLocation.getAllMapLocationsWithinRadiusSq(mp,30);
		if(src.isActive())
		{
			 if(src.canAttackSquare(uic[type0cons]))
			 {
				 src.attackSquareLight(uic[type0cons]);
				 ++type0cons;
			 }
			 if(type0cons>=uic.length)
			 {
				 type0cons = 0;
			 }
		}
	}
	public static void run(RobotController rc)
	{
		src = rc;
		while(true)
		{
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
				else if(rc.getType()==RobotType.NOISETOWER)
				{
					noisetower();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			rc.yield();
		}
		
	}
	
}
