package GHCModel;
import java.util.TimerTask;


public class EventScheduler extends TimerTask{
	
	private static boolean eventRelease; 
	
	public EventScheduler() {
		
	}

	@Override
	public void run() {
		eventRelease = true;
	}
	
	public boolean isEventRelease(){
		return eventRelease;
	}
	
	public void eventFinished(){
		eventRelease = false;
	}
	

}
