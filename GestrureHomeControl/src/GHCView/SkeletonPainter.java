package GHCView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

import org.OpenNI.DepthGenerator;
import org.OpenNI.Point3D;
import org.OpenNI.SkeletonCapability;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.StatusException;
import org.OpenNI.UserGenerator;

public class SkeletonPainter {
	
	 		     
	 private UserGenerator userGen;
	 private DepthGenerator depthGen;
	 private SkeletonCapability skelCap;
	 private HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> userSkels;
	 Color sekeletonColor;
	
	 
	 
	 public SkeletonPainter(HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> userSkels, 
			 				UserGenerator userGen, SkeletonCapability skelCap, DepthGenerator depthGen) {
		 	this.userSkels = userSkels;
		 	this.userGen = userGen;
		 	this.skelCap = skelCap;
		 	this.depthGen = depthGen;
		}
	 
	 public void drawing(Graphics2D g2d) {
		 g2d.setStroke(new BasicStroke(8));
		 
		 try {   
			 int[] userIDs = userGen.getUsers();
			 for (int i = 0; i < userIDs.length; ++i) {
				 g2d.setColor(sekeletonColor);
				 
				 if (skelCap.isSkeletonCalibrating(userIDs[i])) {}  
			     else if (skelCap.isSkeletonTracking(userIDs[i])) { 
			    	 HashMap<SkeletonJoint, SkeletonJointPosition> skel = userSkels.get(userIDs[i]);
			    	 drawSkeleton(g2d, skel);
			         
			     }
				 drawUserStatus(g2d, userIDs[i]);
			 }
		 }
		 catch (StatusException e) {
			 System.out.println(e); 
		 }
	 }
	 
	 		  
	 private void drawSkeleton(Graphics2D g2d, HashMap<SkeletonJoint, SkeletonJointPosition> skel) {
		 drawLine(g2d, skel, SkeletonJoint.HEAD, SkeletonJoint.NECK);

		 drawLine(g2d, skel, SkeletonJoint.LEFT_SHOULDER, SkeletonJoint.TORSO);
		 drawLine(g2d, skel, SkeletonJoint.RIGHT_SHOULDER, SkeletonJoint.TORSO);

		 drawLine(g2d, skel, SkeletonJoint.NECK, SkeletonJoint.LEFT_SHOULDER);
		 drawLine(g2d, skel, SkeletonJoint.LEFT_SHOULDER, SkeletonJoint.LEFT_ELBOW);
		 drawLine(g2d, skel, SkeletonJoint.LEFT_ELBOW, SkeletonJoint.LEFT_HAND);

		 drawLine(g2d, skel, SkeletonJoint.NECK, SkeletonJoint.RIGHT_SHOULDER);
		 drawLine(g2d, skel, SkeletonJoint.RIGHT_SHOULDER, SkeletonJoint.RIGHT_ELBOW);
		 drawLine(g2d, skel, SkeletonJoint.RIGHT_ELBOW, SkeletonJoint.RIGHT_HAND);
	 }  
			  
	 private void drawLine(Graphics2D g2d, HashMap<SkeletonJoint, SkeletonJointPosition> skel, SkeletonJoint j1, SkeletonJoint j2) {
		 Point3D p1 = getJointPos(skel, j1);
		 Point3D p2 = getJointPos(skel, j2);
		 if ((p1 != null) && (p2 != null))
			 g2d.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
		 
		
	
	 }
	 
	 private Point3D getJointPos(HashMap<SkeletonJoint, SkeletonJointPosition> skel, SkeletonJoint j) {
		 SkeletonJointPosition pos = skel.get(j);
		 if (pos == null)
			 return null;

		 if (pos.getConfidence() == 0)
			 return null;   

		 return pos.getPosition();
	 }		  
		
	 private void drawUserStatus(Graphics2D g2d, int userID) throws StatusException	{
		 Point3D massCenter = depthGen.convertRealWorldToProjective(userGen.getUserCoM(userID));
		 String label = null;
		 if (skelCap.isSkeletonTracking(userID))     
			 label = new String("Ready for interaction with user " + userID);
		 else if (skelCap.isSkeletonCalibrating(userID))  
			 label = new String("Calibrating user " + userID);
		 else   
			 label = new String("Calibration error for user " + userID);

		 g2d.drawString(label, (int) massCenter.getX(), (int) massCenter.getY());
	 }
	 
	 public void setSkeletonColor(Color SkeletonColor) {
		 this.sekeletonColor = SkeletonColor;
		 
	 }
	 


}
