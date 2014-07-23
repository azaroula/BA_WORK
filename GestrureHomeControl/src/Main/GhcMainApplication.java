package Main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import GHCController.GhcCore;


public class GhcMainApplication extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GhcCore core;

	public GhcMainApplication() {
			super("Gesture Home Control");

		    Container c = getContentPane();
		    c.setLayout( new BorderLayout() );   

		    core = new GhcCore();
		    c.add( core.getPainter(), BorderLayout.CENTER);

		    addWindowListener( new WindowAdapter() {
		      public void windowClosing(WindowEvent e)
		      { core.close();  }
		    });
		    
		    pack();  
		    setResizable(false);
		    setLocationRelativeTo(null);
		    setVisible(true);
		   // TestAccretionAddon test = new TestAccretionAddon();
		   // core.addObserver(test);
		    
	}
	
	public static void main(String[] args) {
		new GhcMainApplication();
	}
	

}
