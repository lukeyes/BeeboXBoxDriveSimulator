
public class Point 
{
	Point(float newX, float newY) 
	{
		x = newX;
		y = newY;
	}
	
	public boolean equals(Point p)
	{
		return ((x == p.x) && (y == p.y));
	}
	
	public String toString()
	{
		return "(" + x + "," + y + ")";
	}

	float x;
	float y;
}
