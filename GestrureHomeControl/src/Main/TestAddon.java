package Main;



import javax.swing.JOptionPane;
import GHCController.GhcCore;
import GHCModel.GHCState;

public class TestAddon implements GHCState {

	private String s;
	private GhcCore core;
	
	@Override
	public void handle(GhcCore core) {
		s = core.getAccretionValue();
		new Thread(this).start();
		this.core = core;
	}


	
	@Override
	public void run() {
		JOptionPane.showMessageDialog( null, s);
		core.setToStandByMode(); 
		System.out.println("ende");
	}
	

}
