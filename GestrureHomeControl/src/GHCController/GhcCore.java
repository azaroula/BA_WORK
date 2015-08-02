package GHCController;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Timer;
import javax.swing.JOptionPane;
import org.OpenNI.Context;
import org.OpenNI.GeneralException;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.StatusException;
import org.jdom2.JDOMException;
import GHCModel.ConfigureSupplier;
import GHCModel.SensorGenerator;
import GHCModel.EventScheduler;
import GHCModel.InitGestureDetector;
import GHCModel.GestureDetector;
import GHCModel.RESTClient;
import GHCModel.SkeletonTracker;
import GHCModel.SoundPlayer;
import GHCModel.GHCState;
import GHCModel.StateConnector;
import GHCView.DepthMapPainter;
import GHCView.SkeletonPainter;

public class GhcCore extends Observable implements Runnable {
	
	private volatile boolean isRunning;
	private Context context;
	private InitGestureDetector initGestureDetector;
	public DepthMapPainter painter;
	private SensorGenerator generator;
	private SkeletonTracker stracker;
	private SkeletonPainter spainter;
	private GestureDetector  detector;
	private ConfigureSupplier supplier;
	private EventScheduler scheduler;
	private boolean isEventMatch;
	private boolean isAccretionMatch;
	private long timeDelay;
	private static final int LAG_MILLIS = 700;
	private SoundPlayer player;
	private String accretionValue;
	private List<SkeletonJoint> interactionJoints;
	private GHCState state;
	private String path;
	private float roomDepth;
	
	
	public GhcCore() {
		
		try {
			context = new Context();
		
			generator = new SensorGenerator(context);
			painter = new DepthMapPainter(generator.getImWidth(), generator.getImHeight());	
			stracker = new SkeletonTracker(generator.getUserGenerator(), generator.getDepthGenerator());
			spainter = new SkeletonPainter(stracker.getData(), generator.getUserGenerator(),
									   stracker.getSkeletonCapability(), generator.getDepthGenerator());
			painter.setSkeletonPainter(spainter);
			detector = new GestureDetector(stracker.getSkeletonCapability(), 
										generator.getUserGenerator(), generator.getDepthGenerator());
			initGestureDetector = new InitGestureDetector(context, stracker);
			
			scheduler = new EventScheduler();
			interactionJoints = new ArrayList<SkeletonJoint>();
			interactionJoints.add(SkeletonJoint.LEFT_HAND);
			interactionJoints.add(SkeletonJoint.RIGHT_HAND);
			timeDelay = 0;
			generator.setUserColor(Color.BLUE);
			spainter.setSkeletonColor(Color.WHITE);
			initGestureDetector.setInitPoseFlag(true);
			path = "config\\config.xml";
			try {
				supplier = new ConfigureSupplier(path);
			} catch (JDOMException e) {
				JOptionPane.showMessageDialog(null, "There exists an error in the Config File", 
						"GestureHomeControl", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "The specified path or file name of the Config file is incorrect", 
						"GestureHomeControl", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(1);
			}
			detector.setHeight(supplier.getRoomHeight());
			detector.setWidth(supplier.getRoomWidth());
			new Thread(this).start(); 
		
		} catch (GeneralException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	
	@Override
	public void run() {
		  isRunning = true;
		  while (isRunning) {
			  try {
				  context.waitAnyUpdateAll();
				  initGestureDetector.getSessionManager().update(context);
		      }
		      catch(StatusException e)
		      {  System.out.println(e); 
		         System.exit(1);
		      }
			  
			  try {
				if (generator.getUserGenerator().getUsers().length == 0)
					  	continue;
				
			  } catch (StatusException e1) {
				  System.err.println(e1);
				  e1.printStackTrace();
			  }
			  
		      generator.updateUserDepths();
		      stracker.update();
		      try {
				for (int i = 0; i < stracker.getUserList().size(); i++) {
					if (stracker.getSkeletonCapability().isSkeletonTracking(stracker.getUserList().get(i))) {
							if (supplier.getRoomDepth() < stracker.getUserDepth(stracker.getUserList().get(i))) {
								roomDepth = stracker.getUserDepth(stracker.getUserList().get(i));
								supplier.setRoomDepth(roomDepth);
							}
							else
								roomDepth = supplier.getRoomDepth();
							detector.setDepth(roomDepth);
						if (initGestureDetector.isInitPoseFlag()) {
							if(initGestureDetector.isInitPoseMatch()){
								try {
									player = new SoundPlayer();
									player.run();
								} catch (MalformedURLException e) {
									System.err.println(e);
									e.printStackTrace();
								}
								initGestureDetector.setInitPoseFlag(false);
								generator.setUserColor(Color.GREEN);
							}	
						}	
						
						if (initGestureDetector.isInitPoseMatch()) {
							if(stracker.getUserList().get(i) == initGestureDetector.getUser()){
								try {
									Timer timer = new Timer();
									timer.schedule(scheduler, 3000);
								} catch(IllegalStateException e) {}
								for(int z=0; z <interactionJoints.size(); z++) {
									for (int j = 0; j < supplier.getDataSize(); j++) {
									
										if (scheduler.isEventRelease()){
										
											if (detector.lookOut(supplier.get(j), stracker.getUserList().get(i), 
													interactionJoints.get(z))) {
												if(supplier.get(j).getAccretionValue() != null) {
													initGestureDetector.setInitPoseMatch(false);
													accretionValue = supplier.get(j).getAccretionValue();
													state = StateConnector.changeState(supplier.get(j).getAccretionValue());
													state.handle(this);
													setChanged();
													notifyObservers(supplier.get(j).getAccretionValue());
													isAccretionMatch = true;
												
												} else { 
													isEventMatch = true;
													RESTClient client = new RESTClient();
													client.setTimeDelay(timeDelay);
													timeDelay += LAG_MILLIS;
													if (!client.statusRequest(supplier.get(j).getItemURL() ,supplier.get(j).getOnCommand())) {
														client.consumeService(supplier.get(j).getItemURL(), supplier.get(j).getOnCommand());
													} else {
														client.consumeService(supplier.get(j).getItemURL(),supplier.get(j).getOffCommand());  
													}
												}
											}
										}									
									}  
								}
								if (isEventMatch && !isAccretionMatch) {  
									isEventMatch = false;
									initGestureDetector.setInitPoseMatch(false);
									setToStandByMode();									
								}
							}	
						}
					}
				}
			} catch (StatusException e) {
				System.err.println(e);
				e.printStackTrace();
			}
		    	        
		      painter.setImageBytes(generator.getGeneratedData());	
			  painter.repaint();   
		    
		  	}
		    
		    try {
		    	context.stopGeneratingAll();
		    } catch (StatusException e) {}
		    context.release();
		   	System.exit(0);	
	}
	
	public void close() { 
		isRunning = false;  	
	} 
	
	public DepthMapPainter getPainter() {
		return painter;
	}
	
	public void setToStandByMode(){
		scheduler.eventFinished();
		scheduler = new EventScheduler();
		timeDelay = 0;
		initGestureDetector.setInitPoseFlag(true);
		generator.setUserColor(Color.BLUE);
		isAccretionMatch = false;
		System.out.println("Switch to StandBy");
	}


	public String getAccretionValue() {
		return accretionValue;
	}

	
	public void setState(GHCState state) {
		this.state = state;
	}
	
	
	
	
}
