package GHCModel;

import java.awt.Color;
import java.nio.ShortBuffer;

import org.OpenNI.Context;
import org.OpenNI.DepthGenerator;
import org.OpenNI.DepthMetaData;
import org.OpenNI.GestureGenerator;
import org.OpenNI.HandsGenerator;
import org.OpenNI.License;
import org.OpenNI.MapOutputMode;
import org.OpenNI.SceneMetaData;
import org.OpenNI.UserGenerator;



public class SensorGenerator {
	private static final int MAX_DEPTH_SIZE = 10000;  

	
	private Color color;
	private Color userColor;

	private byte[] imgbytes;
	private int imWidth, imHeight;
	private float histogram[];    
	private int maxDepth = 0;	
	private Context context;
	private DepthMetaData depthMD;	  
	private SceneMetaData sceneMD;	     
	private UserGenerator userGen;
	private DepthGenerator depthGen;
	private HandsGenerator handsGen; 
	private GestureGenerator gestureGen;
	
	

	public SensorGenerator(Context context) {
		this.context = context;
		configOpenNI();
		imWidth = depthMD.getFullXRes();
		imHeight = depthMD.getFullYRes();
		histogram = new float[MAX_DEPTH_SIZE];
	    imgbytes = new byte[imWidth * imHeight * 3];
	  }
	  
	  private void configOpenNI() {
		  try {
			  
			  License license = new License("PrimeSense", 
	                        "0KOIk2JeIBYClPWVnMoRKn5cdY4=");   // vendor, key
			  context.addLicense(license); 
	      
			  depthGen = DepthGenerator.create(context);
			  MapOutputMode mapMode = new MapOutputMode(640, 480, 30);   // xRes, yRes, FPS
			  depthGen.setMapOutputMode(mapMode); 
			  depthGen.getDeviceMaxDepth();
			  
			  context.setGlobalMirror(true); 
			  

			  depthMD = depthGen.getMetaData();	  
			  
			  userGen = UserGenerator.create(context);
			  sceneMD = getUserGenerator().getUserPixels(0);
	          // used to return a map containing user IDs (or 0) at each depth location
			  handsGen = HandsGenerator.create(context);
			  gestureGen = GestureGenerator.create(context);
			  context.startGeneratingAll(); 
			 
		  } catch (Exception e) {
			  System.out.println(e);
			  System.exit(1);
		  }
	  }  
	  
	  public void updateUserDepths() {
	    ShortBuffer depthBuf = depthMD.getData().createShortBuffer();
	    calcHistogram(depthBuf);
	    depthBuf.rewind();

	    ShortBuffer usersBuf = sceneMD.getData().createShortBuffer();
	     
	    while (depthBuf.remaining() > 0) {
	      int pos = depthBuf.position();
	      short depthVal = depthBuf.get();
	      short userID = usersBuf.get();

	      imgbytes[3*pos] = 0;     // default color is black when there's no depth data
	      imgbytes[3*pos + 1] = 0;
	      imgbytes[3*pos + 2] = 0;

	      if (depthVal != 0) {   
	    	color = userColor;
	     
	        if (userID == 0)    // not a user; actually the background
	        	color = Color.WHITE;     

	        float histValue = histogram[depthVal];
	        imgbytes[3*pos] = (byte) (histValue * color.getRed());
	        imgbytes[3*pos + 1] = (byte) (histValue * color.getGreen());
	        imgbytes[3*pos + 2] = (byte) (histValue * color.getBlue());
	      }
	    }
	  } 
	  
	  
	  private void calcHistogram(ShortBuffer depthBuf) {
	
	    for (int i = 0; i <= maxDepth; i++)
	      histogram[i] = 0;

	    // record number of different depths in histogram[]
	    int numPoints = 0;
	    maxDepth = 0;
	    while (depthBuf.remaining() > 0) {
	      short depthVal = depthBuf.get();
	      if (depthVal > maxDepth)
	        maxDepth = depthVal;
	      if ((depthVal != 0)  && (depthVal < MAX_DEPTH_SIZE)){      // skip histogram[0]
	        histogram[depthVal]++;
	        numPoints++;
	      }
	    }
	   
	    for (int i = 1; i <= maxDepth; i++)
	      histogram[i] += histogram[i-1];

	    if (numPoints > 0) {
	      for (int i = 1; i <= maxDepth; i++)    
	        histogram[i] = 1.0f - (histogram[i] / (float) numPoints);
	    }
	  } 
	  

	  public UserGenerator getUserGenerator() {
		  return userGen;		  
	  }  
	  
	  public DepthGenerator getDepthGenerator() {
		  return depthGen;
	  }
	  	  
	  public int getImWidth() {
		  return imWidth;
	  }
	  public int getImHeight() {
		  return imHeight;
	  }
	  
	  public byte[] getGeneratedData() {
		  return imgbytes;
	  }
	  
	  public void setUserColor (Color userColor){
		  this.userColor = userColor;
	  }

	  public GestureGenerator getGestureGenerator() {
		return gestureGen;
	}

	public HandsGenerator getHandsGenerator() {
			return handsGen;
		}

	public Context getContext() {
		return context;
	}

}
