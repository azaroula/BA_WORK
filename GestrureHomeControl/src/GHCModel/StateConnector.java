package GHCModel;

import Main.TestAddon;

public class StateConnector {
	

	public static GHCState changeState(String value) {
		switch (value) {	
		case "Test":
			return new TestAddon();
				
		default:
			return null;
		}
		
	}
	
	
}
