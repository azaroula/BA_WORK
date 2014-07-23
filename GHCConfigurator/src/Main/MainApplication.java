package Main;

import java.awt.EventQueue;

import Controller.Controller;
import View.GHCFrame;

public class MainApplication {
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GHCFrame frame = new GHCFrame();
					new Controller(frame);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
