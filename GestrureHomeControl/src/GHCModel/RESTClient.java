package GHCModel;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class RESTClient implements Runnable {
	private String URL;
	private long timeDelay;
	private String command;
	
	
	
	
	public void setTimeDelay(long timeDelay) {
		this.timeDelay = timeDelay;
	}

	public RESTClient() {
		 timeDelay = 0;
		
	}
	
	public void consumeService (String URL, String command) {
		this.URL = URL; 
		this.command = command;
		new Thread(this).start();
		
	}

	public boolean statusRequest(String URL, String command) {
		WebResource service = Client.create().resource( URL + "/state");
		String status = service.accept( MediaType.TEXT_PLAIN ).get( String.class );
		
		if (status.equals(command))
			return true;
		else 
			return false;
	}

	@Override
	public void run() {
		try {
			new Thread(this);
			Thread.sleep(timeDelay);
		} catch (InterruptedException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		WebResource service = Client.create().resource( URL);
		System.out.println( service.accept( MediaType.TEXT_PLAIN ).post( String.class, command) );
	}
	
}
