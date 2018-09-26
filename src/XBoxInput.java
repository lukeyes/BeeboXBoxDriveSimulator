import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;


public class XBoxInput {
	
	private void makeController(Controller c) 
	{
		mController = c;
		Controller[] subControllers = c.getControllers();
		if (subControllers.length != 0 ) 
			return;
		
		{
			mInputComponents = c.getComponents();
			//createControllerWindow(c);
			
			System.out.println("Component count = "+mInputComponents.length);
		}
	}
	
	public boolean setup()
	{
		ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
		Controller[] ca = ce.getControllers();
		for(int i =0; i<ca.length; i++)
		{
			if(ca[i].getType() != Controller.Type.GAMEPAD )
			{
				continue;
			}				
				
			makeController(ca[i]);
		}
		
		if( mInputComponents == null )
			return false;
		
		AbstractComponent c = null;
		
		int i = 0;
		
		while( i < mInputComponents.length )
		{
			c = (AbstractComponent)mInputComponents[i];
			
			if( c.isAnalog() )
			{
				if(c.getName().equalsIgnoreCase("X Rotation"))
				{
					mXRotation = c;
				}
				else if(c.getName().equalsIgnoreCase("Y Axis"))
				{
					mYAxis = c;
				}
			}
			else
			{
				if(c.getName().equalsIgnoreCase("Button 7"))
				{
					mStartButton = c;
				}
				else if(c.getName().equalsIgnoreCase("Button 1"))
				{
					mAButton = c;
				}
				else if( c.getName().equalsIgnoreCase("Button 2"))
				{
					mBButton = c;
				}
			}
			
			i++;
		}
		
		return true;
	}
	
	public boolean left()
	{
		if( mXRotation == null )
			return false;		

		float data = mXRotation.getPollData();
		
		if( mXRotation.getDeadZone() > Math.abs(data))
			return false;			
		
		return (data < -0.5);
	}
	
	public boolean right()
	{
		if( mXRotation == null )
			return false;		

		float data = mXRotation.getPollData();
		
		if( mXRotation.getDeadZone() > Math.abs(data))
			return false;		
			
		return (data > 0.5);
	}
	
	public boolean forward()
	{
		if( mYAxis == null )
			return false;		

		float data = mYAxis.getPollData();
		
		if( mYAxis.getDeadZone() > Math.abs(data))
			return false;		
		
		// dead zone
		return (data < -0.5);		
	}
	
	public boolean backward()
	{
		if( mYAxis == null )
			return false;		

		float data = mYAxis.getPollData();
		
		if( mYAxis.getDeadZone() > Math.abs(data))
			return false;					
			
		// dead zone
		return (data > 0.5);		
	}
	
	boolean isPressed(AbstractComponent aC )
	{
		if( aC.isAnalog() )
			return false;
		
		if( aC == null )
			return false;		

		float data = aC.getPollData();
		
		// dead zone
		return (data == 1.0);		
	}
	
	public boolean isStartPressed()
	{
		return isPressed(mStartButton);		
	}
	
	public boolean isAPressed()
	{
		return isPressed(mAButton);
	}
	
	public boolean isBPressed()
	{
		return isPressed(mBButton);
	}
	
	public boolean poll()
	{
		return mController.poll();
	}
	
	AbstractComponent mYAxis;
	AbstractComponent mXRotation;
	AbstractComponent mStartButton;
	AbstractComponent mAButton;
	AbstractComponent mBButton;
	
	Component[] mInputComponents;
	Controller mController;

}
