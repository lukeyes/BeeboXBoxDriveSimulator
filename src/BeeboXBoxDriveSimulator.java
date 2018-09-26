import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;



/**
 * 
 */

/**
 * @author Luke
 *
 */
public class BeeboXBoxDriveSimulator 
{
	JPanel mMainPane;
	MapsPanel mMapsPane;
	XBoxInput mInput;
	
	public class InputThread implements Runnable
	{
		@Override
		public void run()
		{
			while( true )
			{
				if( mInput.poll() )
				{
					boolean hasInput = false;		
					
					// if input says go forward
					// roomba go forward
					if (mInput.forward())
					{			
						hasInput = true;
						Data.robot.mXVel = 0;
						Data.robot.mYVel = -0.1;
						System.out.println("Forward");
					}
					
					// if input says go back
					// roomba go back
					if( mInput.backward() )
					{
						hasInput = true;
						Data.robot.mXVel = 0;
						Data.robot.mYVel = 0.1;
						System.out.println("Backward");
					}
					
					// if input says left
					// roomba go left
					if( mInput.left() )
					{
						hasInput = true;
						Data.robot.mXVel = -0.1;
						Data.robot.mYVel = 0;
						System.out.println("Left");
					}
					
					// if input says right
					// roomba go right
					if ( mInput.right() )
					{
						hasInput = true;
						Data.robot.mXVel = 0.1;
						Data.robot.mYVel = 0;
						System.out.println("Right");
					}
					
					
					if( !hasInput )
					{
						Data.robot.mXVel = 0;
						Data.robot.mYVel = 0;
						//System.out.println("Stop");
					}						
				}
				
				Thread.yield();
			}
		}

	}
	
	InputThread mInputThread;
		
	BeeboXBoxDriveSimulator()
	{
		mMapsPane = new MapsPanel();
		  
		mMainPane = new JPanel();
		
		mMainPane.setLayout(new BoxLayout(mMainPane,BoxLayout.LINE_AXIS));
		
		mMainPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		mMainPane.add(Box.createRigidArea(new Dimension(0, 5)));
		mMainPane.add(mMapsPane);
		mMainPane.add(Box.createRigidArea(new Dimension(0,5)));       
        mMainPane.add(Box.createGlue());
        
        Data.robot = new Robot(Globals.kGRID_WIDTH,Globals.kGRID_HEIGHT);
    	Data.robot.extent = 0.4f;   	
     
		Data.robot.SetPos(new Point(5.5f,5.5f));	
		
		new Thread(Data.robot).start();
		
		new Thread(mMapsPane).start();
		
		mInput = new XBoxInput();
		mInput.setup();
		
		mInputThread = new InputThread();
		new Thread(mInputThread).start();
		
	}
	
	 /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() 
    {
        //Create and set up the window.
        JFrame frame = new JFrame("BT Simulator 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        BeeboXBoxDriveSimulator simulator = new BeeboXBoxDriveSimulator();
        simulator.mMainPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(simulator.mMainPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);       	
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		 //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                createAndShowGUI();
            }
        });

	}

}
