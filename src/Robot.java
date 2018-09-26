import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;


public class Robot implements Runnable
{	
	public enum Mode 
	{
		EXPLORING, NAVIGATING, WAITING,
	};
	
	public double mX;
	public double mY;
	
	public double mXVel;
	public double mYVel;
	
	public double extent;
		
	private Point m_LastPoint;
			
	private Mode m_Mode;
			
	public LinkedList<Integer> navPath;
	
	private Point gridPoint()
	{
		Point absPoint = new Point((float)mX,(float)mY);
		return new Point(
				pointToGridX(absPoint),
				pointToGridY(absPoint));
		
	}
	
	Robot(int mapWidth, int mapHeight)
	{
		m_Mode = Mode.WAITING;
		mX = mY = mXVel = mYVel = 0;
			
		navPath = null;		
			
		m_LastPoint = new Point(0,0);
		//updateMapAndGraph();
	}		
	
	public void Draw(Graphics2D g2d)
	{
		Color oldColor = g2d.getColor();
		
		g2d.setColor(Color.GREEN);	

		g2d.fillRect(
				(int) ((mX - extent) * Globals.kMAP_VISUAL_CELL_WIDTH),
				(int) ((mY - extent) * Globals.kMAP_VISUAL_CELL_HEIGHT),
				(int) (extent*2*Globals.kMAP_VISUAL_CELL_WIDTH),
				(int) (extent*2*Globals.kMAP_VISUAL_CELL_HEIGHT));	
		
		
		g2d.setColor(oldColor);
	}
	
	public void SetPos(Point p)
	{
		mX = (double) p.x;
		mY = (double) p.y;
		
		m_LastPoint = 
			new Point(
					(int)Math.floor(mX),
					(int)Math.floor(mY));		
	}
	
	public void run()
	{
		while (true)
		{
			update();
		}
	}
	
	public void update()
	{

			if ((mXVel != 0) || (mYVel != 0))
			{
				updateMotion();
		
				
			}
			
			 try
		        {
		            Thread.sleep(50);
		        } catch (InterruptedException e)
		        {
		        }
			
			//if (!m_Map.HasUnexploredNodes())
			//	m_Mode = Mode.WAITING;
	}

	private void updateMotion() 
	{
		// update velocity
		double newX = mX + mXVel;
		double newY = mY + mYVel;
					
		double newX1 = newX - extent;
		double newX2 = newX + extent;
		double newY1 = newY - extent;
		double newY2 = newY + extent;
		
		double currX1 = mX - extent;
		double currX2 = mX + extent;
		double currY1 = mY - extent;
		double currY2 = mY + extent;
		
		int xLeft = (int)Math.floor(newX1);
		int xRight = (int)Math.floor(newX2);
		int yTop = (int) Math.floor(newY1);
		int yBot = (int) Math.floor(newY2);
		
		int xLeftCurr = (int)Math.floor(currX1);
		int xRightCurr = (int)Math.floor(currX2);
		int yTopCurr = (int) Math.floor(currY1);
		int yBotCurr = (int) Math.floor(currY2);		
		
		if (xRight < xLeft)
			xRight = xLeft;
		if (xLeft > xRight)
			xLeft = xRight;
		if (yBot < yTop)
			yBot = yTop;
		if (yTop > yBot)
			yTop = yBot;		
		
		for (int y = yTop; y <= yBot; y++)
		{
			for (int x = xLeft; x <= xRight; x++)
			{
				/*
				if (Data.navMap.gridVal(x, y) == NavMap.EGridVal.BLOCKED)
				{
					/*
					System.out.println("XLeft " + xLeft);
					System.out.println("XRight " + xRight);
					System.out.println("YTop " + yTop);
					System.out.println("YBot " + yBot);
					*/
/*
					handleBlockedMotion(xLeft, xRight, yTop, yBot);
					
					return;
				}
				*/
			}
		}
		
	
		// continue moving
		mX = newX;
		mY = newY;
		
		//m_LastPoint = newLastPoint;
		
	}

	// returns true if changed direction
	// returns false if no change
	private boolean updateNewGridExplore(Point newLastPoint) 
	{
		// check to move to a new unexplored direction
		float currAngle = getCurrentAngle();
		float currAngleLeft = 0.0f;
		float currAngleRight = 0.0f;
		
		if (currAngle == 0.0f)
		{
			currAngleLeft = 90.0f;
			currAngleRight = 270.0f;
		}
		else if (currAngle == 90.0f)
		{
			currAngleLeft = 180.0f;
			currAngleRight = 0.0f;
		}
		else if (currAngle == 180.0f)
		{
			currAngleLeft = 270.0f;
			currAngleRight = 90.0f;
		}
		else if (currAngle == 270.0f)
		{
			currAngleLeft = 0.0f;
			currAngleRight = 180.0f;
		}		
		
		return false;
	}

	private void handleBlockedMotion(int xLeft, int xRight, int yTop, int yBot) 
	{
		int bumpedX = xLeft;
		int bumpedY = yTop;
		
		if (mXVel > 0)
			bumpedX = xRight;
		if (mYVel > 0)
			bumpedY = yBot;
		
		Point newLastPoint = 
			new Point(
					(int) Math.floor(mX),
					(int) Math.floor(mY));
		
		//System.out.println("Last Point - " + m_LastPoint.x + ", " + m_LastPoint.y);
		//System.out.println("New point - " + newLastPoint.x + "," + newLastPoint.y);
			
		m_LastPoint = newLastPoint;			
	}	

	private float getCurrentAngle() 
	{
		float angle = 0.0f;
		if (mXVel < 0)
			angle = 180.0f;
		else if (mYVel < 0)
			angle = 90.0f;
		else if (mYVel > 0)
			angle = 270.0f;
		return angle;
	}
	
	public class MapResult implements Comparable<MapResult> 
	{
		public MapResult(float newAngle, int newResult) 
		{
			angle = newAngle;
			result = newResult;
		}

		public float angle;
		public int result;

		@Override
		public int compareTo(MapResult o)
		{
			final int BEFORE = -1;
			final int EQUAL = 0;
			final int AFTER = 1;

			if (result == o.result)
				return EQUAL;

			if (result > o.result)
				return BEFORE;

			return AFTER;
		}
	}	
		
	public static int pointToGridY(Point p0) {
		return (int) Math.floor((p0.y));
	}

	public static int pointToGridX(Point p0) {
		return (int) Math.floor((p0.x));
	}

}
