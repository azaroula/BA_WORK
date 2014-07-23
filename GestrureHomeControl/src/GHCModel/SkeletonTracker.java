package GHCModel;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.OpenNI.CalibrationProgressEventArgs;
import org.OpenNI.CalibrationProgressStatus;
import org.OpenNI.DepthGenerator;
import org.OpenNI.IObservable;
import org.OpenNI.IObserver;
import org.OpenNI.Point3D;
import org.OpenNI.PoseDetectionCapability;
import org.OpenNI.PoseDetectionEventArgs;
import org.OpenNI.SkeletonCapability;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.SkeletonProfile;
import org.OpenNI.StatusException;
import org.OpenNI.UserEventArgs;
import org.OpenNI.UserGenerator;




public class SkeletonTracker {
	
	 
	 private UserGenerator userGen;
	 private DepthGenerator depthGen;
	 private SkeletonCapability skelCap;
	 private PoseDetectionCapability poseDetectionCap;	 	 
	 private HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> userSkels;
	 private List<Integer> userList;
	 
	 
	 public SkeletonTracker(UserGenerator userGen, DepthGenerator depthGen) {
		 
		 this.userGen = userGen;
		 
		 this.depthGen = depthGen;
		 configure();
		 userSkels = new HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>>(); 
		 userList = new ArrayList<Integer>();
	}
	 
	 private void configure() {
	    try {
	      poseDetectionCap = userGen.getPoseDetectionCapability();

	      skelCap = userGen.getSkeletonCapability();
	      
	      skelCap.setSkeletonProfile(SkeletonProfile.UPPER_BODY);   
	      userGen.getNewUserEvent().addObserver(new NewUserObserver());   
	      userGen.getLostUserEvent().addObserver(new LostUserObserver()); 
	      poseDetectionCap.getPoseDetectedEvent().addObserver(new PoseDetectedObserver());  
	      skelCap.getCalibrationCompleteEvent().addObserver(new CalibrationCompleteObserver());
	     
	    } catch (Exception e) {
	      System.out.println(e);
	      System.exit(1);
	    }
	 }
	 
	 public void update() {
		 try {   
			 int[] userIDs = userGen.getUsers();   
			 for (int i = 0; i < userIDs.length; ++i) {
				 int userID = userIDs[i];
				 if (skelCap.isSkeletonCalibrating(userID))
					 continue;    // test to avoid occasional crashes with isSkeletonTracking()
				 if (skelCap.isSkeletonTracking(userID))
					 updateJoints(userID);
			 }
		 } catch (StatusException e) { 
			 System.out.println(e); 
		 }
	 }
	 
	 private void updateJoints(int userID) {
		 HashMap<SkeletonJoint, SkeletonJointPosition> skel = userSkels.get(userID);

		 updateJoint(skel, userID, SkeletonJoint.HEAD);
		 updateJoint(skel, userID, SkeletonJoint.NECK);

		 updateJoint(skel, userID, SkeletonJoint.LEFT_SHOULDER);
		 updateJoint(skel, userID, SkeletonJoint.LEFT_ELBOW);
		 updateJoint(skel, userID, SkeletonJoint.LEFT_HAND);

		 updateJoint(skel, userID, SkeletonJoint.RIGHT_SHOULDER);
		 updateJoint(skel, userID, SkeletonJoint.RIGHT_ELBOW);
		 updateJoint(skel, userID, SkeletonJoint.RIGHT_HAND);

		 updateJoint(skel, userID, SkeletonJoint.TORSO);

	 }

	 private void updateJoint(HashMap<SkeletonJoint, SkeletonJointPosition> skel, int userID, SkeletonJoint joint) {
		 try {

			 if (!skelCap.isJointAvailable(joint) || !skelCap.isJointActive(joint)) {
				 System.out.println(joint + " not available for updates");
				 return;
			 }

			 SkeletonJointPosition pos = skelCap.getSkeletonJointPosition(userID, joint);

			 if (pos == null) {
				 System.out.println("No update for " + joint);
				 return;
			 }
			
			 
			 SkeletonJointPosition jPos = null;

			 if (pos.getPosition().getZ() != 0) {  
				 jPos = new SkeletonJointPosition(depthGen.convertRealWorldToProjective(pos.getPosition()),pos.getConfidence());
				
			 }
			 else  
				 jPos = new SkeletonJointPosition(new Point3D(), 0);
			 skel.put(joint, jPos);
		 } catch (StatusException e) {
			 System.out.println(e);
		 }
	 } 
	 
	 public HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> getData(){
		 return userSkels;
	 }
	 
	 public SkeletonCapability getSkeletonCapability(){
		 return skelCap;
	 }
	 
	 public void stopTracking (int userID) {
		 try {
			skelCap.stopTracking(userID); 
			skelCap.requestSkeletonCalibration(userID, true);
		} catch (StatusException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		 
	 }
	 
	 
	 public List<Integer> getUserList() {
		return userList;
	}

	public float getUserDepth(int userID) {
		try {
			Point3D massCenter = depthGen.convertRealWorldToProjective(userGen.getUserCoM(userID));
			return massCenter.getZ();
		} catch (StatusException e) {
			System.err.println(e);
			e.printStackTrace();
			return 0;
		}
	}



	class NewUserObserver implements IObserver<UserEventArgs> {
		 
	    public void update(IObservable<UserEventArgs> observable, UserEventArgs args) {
	    	System.out.println("Detected new user " + args.getId());
	    
	    	
	    		userList.add(args.getId());
	    		Collections.sort(userList);
	    	
	    	try {	
	    		skelCap.requestSkeletonCalibration(args.getId(), true);	
	    	} catch (StatusException e) { 
	    		e.printStackTrace(); }
	    	}
	 }


	 class LostUserObserver implements IObserver<UserEventArgs> {
		 public void update(IObservable<UserEventArgs> observable, UserEventArgs args) { 
	    	System.out.println("Lost track of user " + args.getId());  	 
	    	userSkels.remove(args.getId()); 
	    	userList.remove((Integer)args.getId());
	    	
	    	Collections.sort(userList);
	    
		 }
	 }

	 
	 class PoseDetectedObserver implements IObserver<PoseDetectionEventArgs> {
	
		 public void update(IObservable<PoseDetectionEventArgs> observable, PoseDetectionEventArgs args) {
			 int userID = args.getUser();
			 System.out.println(args.getPose() + " pose detected for user " + userID);
			 try {
				 poseDetectionCap.stopPoseDetection(userID);
				 skelCap.requestSkeletonCalibration(userID, true);
			 } catch (StatusException e) {  
				 e.printStackTrace(); 
			}
		 }
	 }


	  class CalibrationCompleteObserver implements IObserver<CalibrationProgressEventArgs> {
	    
		  public void update(IObservable<CalibrationProgressEventArgs> observable,
	                                                    CalibrationProgressEventArgs args) {
			  int userID = args.getUser();
			 
			  System.out.println("Calibration status: " + args.getStatus() + " for user " + userID);
			  	  
			  try {
				  if (args.getStatus() == CalibrationProgressStatus.OK) {
					  System.out.println("Starting tracking user " + userID);
					  skelCap.startTracking(userID);
					  userSkels.put(new Integer(userID),
							  new HashMap<SkeletonJoint, SkeletonJointPosition>());  
					  
				  } else {    
					  skelCap.requestSkeletonCalibration(userID, true);
				  }
	
			  } catch (StatusException e) {
				  e.printStackTrace(); 
			  }
		  }
	  }
}
