package GHCModel;

import org.OpenNI.DepthGenerator;
import org.OpenNI.SkeletonCapability;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.StatusException;
import org.OpenNI.UserGenerator;



public class PoseDetector {
	
	private SkeletonCapability skelCab;
	private DepthGenerator depthGen;
	private float depth;
	private float width;
	private float height;
	
	
	
	public PoseDetector(SkeletonCapability skelCab, UserGenerator userGen, DepthGenerator depthGen) {
		this.skelCab = skelCab;
		this.depthGen = depthGen;
		width = 640;
		height = 480;
			
	}
	
	public boolean lookOut(ConfigSet data, int userID, SkeletonJoint joint) throws StatusException {
		
		
		SkeletonJointPosition pos = skelCab.getSkeletonJointPosition(userID, joint);
		SkeletonJointPosition jPos1 = new SkeletonJointPosition(depthGen.convertRealWorldToProjective(pos.getPosition()),pos.getConfidence());

		SkeletonJointPosition pos2 = skelCab.getSkeletonJointPosition(userID, addPositionVector(joint));
		SkeletonJointPosition jPos2 = new SkeletonJointPosition(depthGen.convertRealWorldToProjective(pos2.getPosition()),pos2.getConfidence());
			
	
		RayTracer rayTracer = new RayTracer();
		
		Vector3D S1 = null;
		Vector3D S2 = null;
		Vector3D S3 = null;
		
		switch (data.getWall()) {
		case RIGHT:
			S1 = new Vector3D(width, 0, depth);
			S2 = new Vector3D(width, 0, 0);
			S3 = new Vector3D(width, height, depth);
			break;
		case REAR:
			S1 = new Vector3D(0, 0, depth);
			S2 = new Vector3D(0, height, depth);
			S3 = new Vector3D(width, 0, depth);
			break;
		case LEFT:
			S1 = new Vector3D(0, height, depth);
			S2 = new Vector3D(0, 0, depth);
			S3 = new Vector3D(0, height, 0);
			break;
		case FRONT:
			S1 = new Vector3D(width, height, 0);
			S2 = new Vector3D(0, height, 0);
			S3 = new Vector3D(width, 0, 0);
			break;
		case CEILING:
			S1 = new Vector3D(width, 0, depth);
			S2 = new Vector3D(0, 0, depth);
			S3 = new Vector3D(width, 0, 0);
			break;			
		default:
			break;
		}
			
		Vector3D R1 = new Vector3D(jPos2.getPosition());
		Vector3D R2 = new Vector3D(jPos1.getPosition());
		
		if (rayTracer.intersectRayWithSquare(R1, R2, S1, S2, S3)) {
			int intersectCase;
			
			if(data.getWidthLocation() == null) {
				if(data.getHeightLocation() == null) 
					intersectCase = 0;
				else 
					intersectCase = 1;	
			} else {
				if(data.getHeightLocation() == null)
					intersectCase = 2;
				else 
					intersectCase = 3;
			}
			
			switch (intersectCase) {
			case 0:
				return true;
				
			case 1:
				if(intesectPlaneInHeight(data, rayTracer.getIntersectPoint()))
					return true;
				else return false;
				
			case 2:
				if(intesectPlaneInWidth(data, rayTracer.getIntersectPoint()))
					return true;
				else return false;
				
			case 3:
				if(intesectPlaneInHeight(data, rayTracer.getIntersectPoint()) && intesectPlaneInWidth(data, rayTracer.getIntersectPoint()))
					return true;
				else return false;
			}
				
			return true;
		} else 
			return false;
							
	}
	
	public boolean intesectPlaneInWidth(ConfigSet data, Vector3D intersectPoint){
		switch (data.getWidthLocation()) {
		
		case LEFT:
			switch (data.getWall()) {
			
			case RIGHT:
				if(intersectPoint.getZ() < (depth/2))
					return true;
				else return false;
				
			case LEFT:	
				if(intersectPoint.getZ() > (depth/2))
					return true;
				else return false;
				
			case FRONT:
				if(intersectPoint.getX() < (width/2))
					return true;
				else return false;
						
			case REAR:
				if(intersectPoint.getX() > (width/2))
					return true;
				else return false;
				
			case CEILING:
				if(intersectPoint.getX() < (width/2))
					return true;
				else return false;
			}

		case RIGHT:
			switch (data.getWall()) {
			
			case RIGHT:
				if(intersectPoint.getZ() > (depth/2))
					return true;
				else return false;
				
			case LEFT:
				if(intersectPoint.getZ() < (depth/2))
					return true;
				else return false;
				
			case FRONT:
				if(intersectPoint.getX() > (width/2))
					return true;
				else return false;
				
			case REAR:
				if(intersectPoint.getX() < (width/2))
					return true;
				else return false;
				
			case CEILING:
				if(intersectPoint.getX() > (width/2))
					return true;
				else return false;
			}
		
		}
		return true;
	}
	
	public boolean intesectPlaneInHeight(ConfigSet data, Vector3D intersectPoint){
		
		switch (data.getHeightLocation()) {
		case TOP:
			switch (data.getWall()) {
			
			case RIGHT:
				if(intersectPoint.getY() < (height/2))
					return true;
				else return false;
				
			case LEFT:
				if(intersectPoint.getY() < (height/2))
					return true;
				else return false;
			
			case FRONT:
				if(intersectPoint.getY() < (height/2))
					return true;
				else return false;
				
			case REAR:
				if(intersectPoint.getY() < (height/2))
					return true;
				else return false;
				
			case CEILING:
				if(intersectPoint.getZ() < (depth/2))
					return true;
				else return false;						
			}
			
		case BOTTOM:
			switch (data.getWall()) {
			case LEFT:
				if(intersectPoint.getY() > (height/2))
					return true;
				else return false;
				
			case RIGHT:
				if(intersectPoint.getY() > (height/2))
					return true;
				else return false;
			
			case FRONT:
				if(intersectPoint.getY() > (height/2))
					return true;
				else return false;
			
			case REAR:
				if(intersectPoint.getY() > (height/2))
					return true;
				else return false;
			
			case CEILING:
				if(intersectPoint.getZ() > (depth/2))
					return true;
				else return false;			
			}
		}
		return true;
	}
	
	
	public SkeletonJoint addPositionVector (SkeletonJoint joint) {
		switch (joint) {
			case RIGHT_HAND:
				return SkeletonJoint.RIGHT_ELBOW;  
			case LEFT_HAND:
				return SkeletonJoint.LEFT_ELBOW;  
			
		default:
			return null;
		}
	}

	public float getDepth() {
		return depth;
	}

	public void setDepth(float depth) {
		this.depth = depth;
	}
	
}
