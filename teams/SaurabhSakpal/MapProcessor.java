package SaurabhSakpal;

import battlecode.common.*;
import java.util.*;

class Coordinate
{
	int x;
	int y;
	String s;
	int total;
	Coordinate(int xi, int yi, String si,int total)
	{
		this.s = si+" "+xi+" "+yi;
		this.x = xi;
		this.y = yi;
		this.total = total+1;
		
	}
	Coordinate(int xi, int yi,int total)
	{
		this.s = ""+xi+" "+yi;
		this.x = xi;
		this.y = yi;
		this.total = total+1;
		
	}
}
public class MapProcessor
{
	static int MapHeight;
	static int MapWidth;
	static Direction dir[] = {Direction.NORTH,Direction.SOUTH,Direction.EAST,Direction.WEST,Direction.NORTH_EAST, Direction.NORTH_WEST,Direction.SOUTH_EAST, Direction.SOUTH_WEST};
	static TerrainTile tTile[] = {TerrainTile.VOID,TerrainTile.ROAD,TerrainTile.NORMAL,TerrainTile.OFF_MAP};
	
	public static int[] findPositionForPasture(RobotController src,int x, int y)
	{
		MapLocation mp = new MapLocation(x,y);
		MapLocation uic[] = MapLocation.getAllMapLocationsWithinRadiusSq(mp,30);
		int i = 0;
		int j = 0;
		int e = uic.length-1;
		int arr[] = new int[5];
		while(i<5)
		{
			TerrainTile t = src.senseTerrainTile(uic[j]);
			TerrainTile t2 = src.senseTerrainTile(uic[e]);
			if(t.compareTo(tTile[0])!=0&&t.compareTo(tTile[3])!=0)
			{
				arr[i] = (100*(uic[j].x))+uic[j].y;
				i++;
			}
			if(t2.compareTo(tTile[0])!=0&&t2.compareTo(tTile[3])!=0)
			{
				arr[i] = (100*(uic[e].x))+uic[e].y;
				i++;
			}
			j++;
			e--;
		}
		for(int l = 0;l<5;l++)
		{
			System.out.println((arr[l]/100)+" === "+(arr[l]%100));
		}
		return arr;
	}
	public static int[][] findPathOnMap(RobotController src,int dx, int dy, int sx, int sy,int MapTerrain[][])
	{
		//System.out.println(dx+"~~~~"+dy);
		Queue<Coordinate>q = new LinkedList<Coordinate>();
		Coordinate st = new Coordinate(sx,sy,0); 
		q.add(st);
		int curx = 0;
		int cury = 0;
		Coordinate r = null;
		//System.out.println("STAGE1");
		//System.out.println(dx+" @@@ "+dy);
		int temp[][] = new int[src.getMapHeight()][src.getMapWidth()];
		while(curx!=dx||cury!=dy)
		{
			r = q.peek();
			curx = r.x;
			cury = r.y;
			int t = r.total;
			String p = r.s;
			//System.out.println(r);
			if(curx>=1)
			{
				if(MapTerrain[cury][curx-1]!=0&&temp[cury][curx-1]==0)
				{
					temp[cury][curx-1]=1;
					Coordinate mt = new Coordinate(curx-1,cury,p,t);
					q.add(mt);
				}
			}
			if(curx<MapTerrain.length-1)
			{
				if(MapTerrain[cury][curx+1]!=0&&temp[cury][curx+1]==0)
				{
					temp[cury][curx+1]=1;
					Coordinate mt = new Coordinate(curx+1,cury,p,t);
					q.add(mt);
				}
			}
			if(cury>=1)
			{
				if(MapTerrain[cury-1][curx]!=0&&temp[cury-1][curx]==0)
				{
					temp[cury-1][curx]=1;
					Coordinate mt = new Coordinate(curx,cury-1,p,t);
					q.add(mt);
				}
			}
			if(cury<MapTerrain[0].length-1)
			{
				if(MapTerrain[cury+1][curx]!=0&&temp[cury+1][curx]==0)
				{
					temp[cury+1][curx]=1;
					Coordinate mt = new Coordinate(curx,cury+1,p,t);
					q.add(mt);
				}
			}
			q.remove();
		}
	//System.out.println("STAGE2");
	String s = r.s;
	//System.out.println(s);
	int arr[][] = new int[r.total][2];
	String cor[] = s.split(" ");
	int tp = 0;
	//System.out.println(dx+","+dy+"----->"+cor[cor.length-2]+","+cor[cor.length-1]);
	for(int j=0;j<r.total;j++)
	{
		//System.out.println(cor[tp]+"===="+cor[tp+1]);
		//System.out.println(j);
		arr[j][0] = Integer.parseInt(cor[tp]);
		arr[j][1] = Integer.parseInt(cor[tp+1]);
		//System.out.println("("+arr[j][0]+","+arr[j][1]+") := "+MapTerrain[arr[j][1]][arr[j][0]]);
		tp = tp+2;
	}
	//System.out.println("STAGE3");
	return arr;	
	}
	public static int[][] HQProcessMap(RobotController src, int hqx, int hqy)throws Exception
	{
		int cx = 0;
		int cy = 0;
		int tcp = 0;
		int width = src.getMapWidth();
		int height = src.getMapHeight();
		int MapTerrain[][] = new int[height][width];
		//src.setIndicatorString(0,+" "+);
		boolean flag = true;
		int des_x = 0;
		int des_y = 0;
		for(int y = 0;y<height;y++)
		{
			for(int x = 0;x<width;x++)
			{
				MapLocation loc = new MapLocation(x,y);
				TerrainTile t = src.senseTerrainTile(loc);
				if(t.compareTo(tTile[0])==0)
				{
					MapTerrain[y][x] = 0;
				}
				else if(t.compareTo(tTile[1])==0)
				{
					if(flag)
					{
						cx = x;
						cy = y;
						flag = false;
						++tcp;
					}
					if(Math.abs(cx-x)==1||Math.abs(cy-y)==1)
					{
						cx = x;
						cy = y;
						tcp++;
					}
					MapTerrain[y][x] = 1;
					if(tcp==10)
					{
						des_x = x;
						des_y = y;
					}
				}
				else if(t.compareTo(tTile[2])==0)
				{
					MapTerrain[y][x] = 2;
				}
			}
			//System.out.println();
		}
		src.broadcast(11,((des_x*100)+des_y));
		//System.out.println("Succeess at Map Terrain: "+((des_x*100)+des_y));
		return MapTerrain;
	}

}
