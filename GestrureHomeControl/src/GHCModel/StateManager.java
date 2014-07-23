package GHCModel;

import Main.TestAddon;

public class StateManager {
	

	public static GHCState changeState(String value) {
		switch (value) {	
		case "Test":
			return new TestAddon();
				
		default:
			return null;
		}
		
	}
	
	
}
