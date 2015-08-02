package GHCModel;


import org.OpenNI.Context;
import org.OpenNI.GeneralException;
import org.OpenNI.IObservable;
import org.OpenNI.IObserver;
import org.OpenNI.Point3D;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.StatusException;
import com.primesense.NITE.NullEventArgs;
import com.primesense.NITE.PointEventArgs;
import com.primesense.NITE.SessionManager;
import com.primesense.NITE.StringPointValueEventArgs;

public class InitGestureDetector {

	private Context context;
	private SessionManager sessionMan;
	private boolean initPoseMatch;
	private boolean initPoseFlag;
	private SkeletonTracker stracker;
	private int user;
	
	public InitGestureDetector(Context context, SkeletonTracker stracker) {
		this.context = context;
		this.stracker = stracker;
		user = 0;
		configNITE();
	}

	 private void configNITE() {
	    try {
	    	
	    	sessionMan = new SessionManager(context, "Wave", "Wave"); 	
	    	setSessionEvents(sessionMan);

	    } catch (GeneralException e) {
	    	e.printStackTrace();
	    	System.exit(1);	
	    }
	 }
	 
	 public SessionManager getSessionManager() {
		 return sessionMan;
	 }
	 
	 private void setSessionEvents(final SessionManager sessionMan) {
		 try {
	       sessionMan.getSessionFocusProgressEvent().addObserver( 
					 new IObserver<StringPointValueEventArgs>() {
						 public void update(IObservable<StringPointValueEventArgs> observable, 
								 StringPointValueEventArgs args) { 
							 Point3D focusPt = args.getPoint();		
							 float progress = args.getValue();
							 String focusName = args.getName();
							 System.out.printf("Session focused at (%.0f, %.0f, %.0f) on %s [progress %.2f]\n", 
									 focusPt.getX(), focusPt.getY(), focusPt.getZ(), focusName, progress);
						 }
					 });
	      
			 sessionMan.getSessionStartEvent().addObserver( new IObserver<PointEventArgs>() {
				 public void update(IObservable<PointEventArgs> observable, PointEventArgs args) { 
					 Point3D focusPt = args.getPoint();
					  
					 if(initPoseFlag){
						 initPoseMatch = true;
						 associateWithUser(args.getPoint());
						 System.out.printf("Initial Pose Recognized  at (%.0f, %.0f, %.0f)\n", 
								 focusPt.getX(), focusPt.getY(), focusPt.getZ());
						
					 }
					 endSession();
				 }
			 });
	      
			 sessionMan.getSessionEndEvent().addObserver( new IObserver<NullEventArgs>() {
				 public void update(IObservable<NullEventArgs> observable, NullEventArgs args) {
					 System.out.println("end session");
					/* try {
						sessionMan.setQuickRefocusTimeout(10);
					} catch (StatusException e) {
						e.printStackTrace();
					}*/
				 }
				 
			 });
			 
		 } catch (StatusException e) {
			 e.printStackTrace();
		 }
	 }  
	 
	 public boolean isInitPoseMatch() {
		 return initPoseMatch;
	 }

	 public void setInitPoseMatch(boolean initPoseMatch) {
		 this.initPoseMatch = initPoseMatch;
	 }

	public boolean isInitPoseFlag() {
		return initPoseFlag;
	}

	public void setInitPoseFlag(boolean initPoseFlag) {
		this.initPoseFlag = initPoseFlag;
	} 
	
	public void associateWithUser(Point3D handPoint){
		try {
			for (int i=0; i < stracker.getUserList().size(); i++){
				
				SkeletonJointPosition posRight = stracker.getSkeletonCapability().
						getSkeletonJointPosition(stracker.getUserList().get(i), SkeletonJoint.RIGHT_HAND);
				Point3D UserRightHandPoint = posRight.getPosition();
				if(Math.abs(UserRightHandPoint.getX() - handPoint.getX()) <= 150 &&
			       Math.abs(UserRightHandPoint.getY() - handPoint.getY()) <= 150 &&
			       Math.abs(UserRightHandPoint.getZ() - handPoint.getZ()) <= 150){
					user = stracker.getUserList().get(i);
					break;
				}
				SkeletonJointPosition posLeft = stracker.getSkeletonCapability().
						getSkeletonJointPosition(stracker.getUserList().get(i), SkeletonJoint.LEFT_HAND);
				Point3D UserLeftHandPoint = posLeft.getPosition();
				if(Math.abs(UserLeftHandPoint.getX() - handPoint.getX()) <= 150 &&
				   Math.abs(UserLeftHandPoint.getY() - handPoint.getY()) <= 150 &&
			       Math.abs(UserLeftHandPoint.getZ() - handPoint.getZ()) <= 150){
					user = stracker.getUserList().get(i);
					break;
				}
				
			}
		} catch (StatusException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	public int getUser() {
		return user;
	}
	
	public void endSession() {
		try {
			sessionMan.EndSession();
		} catch (StatusException e) {
			
			e.printStackTrace();
		}	
	}
	
}
