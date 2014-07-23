package GHCModel;

import GHCController.GhcCore;

public interface GHCState extends Runnable {

	void handle(GhcCore core);
	
}
