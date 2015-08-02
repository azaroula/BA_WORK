import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.BrickletRemoteSwitch;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

@Path( "homecontrol/socket" )
public class HomeServiceServlet {
	
	private IPConnection ipcon;
	private BrickletRemoteSwitch rs;
	private static final String HOST = "localhost";
	private static final int PORT = 4223;
	private static final String UID = "jL1"; 
	static boolean stateSocketA;
	static boolean stateSocketB;
	static boolean stateSocketC;
	static boolean stateSocketD;
	
	
	
	public HomeServiceServlet() throws UnknownHostException, AlreadyConnectedException, IOException, TimeoutException, NotConnectedException, InterruptedException {
				
		ipcon = new IPConnection(); // Create IP connection
		ipcon.connect(HOST, PORT);
		rs = new BrickletRemoteSwitch(UID, ipcon); // Create device object
		rs.setRepeats((short) 10);
	}
	
	@Path("all/{switchTo}")
	@GET
	@Produces(MediaType.TEXT_PLAIN )

	public String switchAll(@PathParam("switchTo") String switchTo) throws UnknownHostException, 

		AlreadyConnectedException, IOException, TimeoutException, NotConnectedException {
		
		if(switchTo.equals("ON")) {
			try{
				rs.switchSocketA((short)1, (short) 1, BrickletRemoteSwitch.SWITCH_TO_ON);
				Thread.sleep(700);
				rs.switchSocketA((short)1, (short) 2, BrickletRemoteSwitch.SWITCH_TO_ON);
				Thread.sleep(700);
				rs.switchSocketA((short)1, (short) 4, BrickletRemoteSwitch.SWITCH_TO_ON);
				Thread.sleep(700);
				rs.switchSocketA((short)1, (short) 8, BrickletRemoteSwitch.SWITCH_TO_ON);	
		
				stateSocketA = true;
				stateSocketB = true;
				stateSocketC = true;
				stateSocketD = true;
				return "Switch all Sockets to ON";
			} catch (InterruptedException e) {
				return "Cannot switch all Sockets to ON";		
			}
		} else if(switchTo.equals("OFF")) {
			try{
				rs.switchSocketA((short)1, (short) 1, BrickletRemoteSwitch.SWITCH_TO_OFF);
				Thread.sleep(700);
				rs.switchSocketA((short)1, (short) 2, BrickletRemoteSwitch.SWITCH_TO_OFF);
				Thread.sleep(700);
				rs.switchSocketA((short)1, (short) 4, BrickletRemoteSwitch.SWITCH_TO_OFF);
				Thread.sleep(700);
				rs.switchSocketA((short)1, (short) 8, BrickletRemoteSwitch.SWITCH_TO_OFF);	
				
				stateSocketA = false;
				stateSocketB = false;
				stateSocketC = false;
				stateSocketD = false;
				return "Switch all Sockets to OFF";
			} catch (InterruptedException e) {
					return "Cannot switch all Sockets to OFF";
					
			}
		} else {
			 return "ERROR: Please check your URL specification";
		}
	}
	
	
	@Path("{receiverCode}/state")
	@GET
	@Produces(MediaType.TEXT_PLAIN )
	public String switching(@PathParam("receiverCode") String receiveCode){
		int code;
		  
		try {
			code = Integer.parseInt(receiveCode);
			  
		} catch (NumberFormatException e) {
			
			System.err.println(e);
			return "ERROR : Please check your URL specification"; 
		}
		 switch (code) {
		  	case 1:
		  		return (stateSocketA) ? "ON" : "OFF";
		  	case 2:
		  		return (stateSocketB) ? "ON" : "OFF";		  		
		  	case 4:
		  		return (stateSocketC) ? "ON" : "OFF";	  		
		  	case 8:
		  		return (stateSocketD) ? "ON" : "OFF";
		  	default:
		  		return "State not available";
		 }
	}
	 	  
	 @Path("{receiverCode}")
	 @POST
	 @Produces( MediaType.TEXT_PLAIN )
	 @Consumes(MediaType.TEXT_PLAIN)
	 public String switching(@PathParam("receiverCode") String receiverCode, String command) {
		  
		  int code;
		  
		  try {
			  code = Integer.parseInt(receiverCode);
			  
		  } catch (NumberFormatException e) {
			
			System.err.println(e);
			Response.status(Response.Status.BAD_REQUEST).entity("Please check your URL specification").build();
			return "ERROR : Please check your URL specification: "; 
		  }
		  
		  if (command.equals("ON")) {
			  
			  try {
				rs.switchSocketA((short)1, (short)code, BrickletRemoteSwitch.SWITCH_TO_ON);
			} catch (TimeoutException e) {
				e.printStackTrace();
				return "TimeOut";
				
			} catch (NotConnectedException e) {
				
				e.printStackTrace();
				return "Not Connected";
			}
			  
			  switch (code) {
			  	case 1:
			  		stateSocketA = true;
			  		break;
			  	case 2:
			  		stateSocketB = true;
			  		break;
			  	case 4:
			  		stateSocketC = true;
			  		break;
			  	case 8:
			  		stateSocketD = true;
			  		break;
			  	default:
			  		break;
			  }
			 
			  return "Socket: " + receiverCode + " switch to ON";
			  
		  } else if (command.equals("OFF")) {
			  try {
				rs.switchSocketA((short)1, (short) code, BrickletRemoteSwitch.SWITCH_TO_OFF);
			} catch (TimeoutException e) {
				System.err.println(e);
				e.printStackTrace();
			} catch (NotConnectedException e) {
				System.err.println(e);
				e.printStackTrace();
			}
			 
			  switch (code) {
			  	case 1:
			  		stateSocketA = false;
			  		break;
			  	case 2:
			  		stateSocketB = false;
			  		break;
			  	case 4:
			  		stateSocketC = false;
			  		break;
			  	case 8:
			  		stateSocketD = false;
			  		break;
			  	default:
			  		break;
			  }
			  return "Socket: " + receiverCode + " switch to OFF";
		  } else
			  return "ERROR: Please check your URL specification";  
	  }
	 
	
	  
	 public static void main(String args[]) throws IllegalArgumentException, IOException, AlreadyConnectedException, TimeoutException, NotConnectedException, InterruptedException{
		  
		 new HomeServiceServlet();
			  	  		   
		 HttpServer server = HttpServerFactory.create( "http://localhost:8080/" );

		 server.start();
		 JOptionPane.showMessageDialog( null, "Close REST Server" );
		 server.stop( 0 );
		 
	
	 }

}
