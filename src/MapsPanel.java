
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.JPanel;


public class MapsPanel extends JPanel implements Runnable
{	
	static final int WIDTH = 400;
	static final int HEIGHT = 400;
	
	Canvas canvas;
	
	MapsPanel()
	{
		setSize(WIDTH,HEIGHT);
		canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setSize(WIDTH, HEIGHT);
		add(canvas);
	}
	
	 public void paintComponent(Graphics g) 
	 {
		super.paintComponent(g);

		canvas.createBufferStrategy(2);
		BufferStrategy buffer = canvas.getBufferStrategy();
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		BufferedImage bi = gc.createCompatibleImage(WIDTH, HEIGHT);

		Graphics graphics = null;
		Graphics2D g2d = null;
	
		g2d = bi.createGraphics();
		
		g2d.setColor(Color.BLUE);
		
		int gridWidth = Globals.kGRID_WIDTH;
		int gridHeight = Globals.kGRID_HEIGHT;

		
		
		for (int y = 0; y < gridHeight; y++) 
		{
			for (int x = 0; x < gridWidth; x++) 
			{
				/*
				switch (Data.navMap.gridVal(x, y)) 
				{
				case UNEXPLORED:
					g2d.setColor(Color.WHITE);
					break;
				case CLEAR:
					g2d.setColor(Color.BLUE);
					break;
				case BLOCKED:
					g2d.setColor(Color.RED);
					break;
				}
				*/
				g2d.setColor(Color.BLUE);
				g2d.fillRect((int) 
						(x * Globals.kMAP_VISUAL_CELL_WIDTH),
						(int) (y * Globals.kMAP_VISUAL_CELL_HEIGHT),
						(int) Globals.kMAP_VISUAL_CELL_WIDTH,
						(int) Globals.kMAP_VISUAL_CELL_HEIGHT);
			}
		}
		
		Data.robot.Draw(g2d);
		
		drawGrid(g2d);		
		
		// Blit image and flip...
		graphics = buffer.getDrawGraphics();

		graphics.drawImage(bi, 0, 0, null);
		if (!buffer.contentsLost())
			buffer.show();

		// Release resources
		if (graphics != null)
			graphics.dispose();
		if (g2d != null)
			g2d.dispose();

		
		//g2d.setColor(oldColor);

	 }
	 
	 private void drawGrid(Graphics2D g2d) 
	 {
			g2d.setColor(Color.GRAY);
			// draw grid
			int gridWidth = Globals.kGRID_WIDTH;
			int gridHeight = Globals.kGRID_HEIGHT;

			for (int y = 0; y < gridHeight; y++)
				g2d.drawLine(
						Float.valueOf(0).intValue(), 
						Float.valueOf(
								y * Globals.kMAP_VISUAL_CELL_HEIGHT).intValue(), 
						Float.valueOf(
								gridWidth * Globals.kMAP_VISUAL_CELL_HEIGHT).intValue(),
						Float.valueOf(
								y * Globals.kMAP_VISUAL_CELL_HEIGHT).intValue());
			

			for (int x = 0; x < gridWidth; x++)
				g2d.drawLine(
						Float.valueOf(x * Globals.kMAP_VISUAL_CELL_WIDTH).intValue(),
						Float.valueOf(0).intValue(),
						Float.valueOf(
								x * Globals.kMAP_VISUAL_CELL_WIDTH).intValue(), 
						Float.valueOf(
								gridHeight * Globals.kMAP_VISUAL_CELL_HEIGHT)
								.intValue());
			g2d.drawLine(
					gridWidth * Globals.kMAP_VISUAL_CELL_WIDTH,
					0,
					gridWidth * Globals.kMAP_VISUAL_CELL_WIDTH,
					gridHeight * Globals.kMAP_VISUAL_CELL_HEIGHT);
		}

	@Override
	public void run() 
	{
		while (true)
		{					
			 try
	        {
	            Thread.sleep(50);
	        } catch (InterruptedException e)
	        {
	        }

			repaint();	
		}
		
	}
}

