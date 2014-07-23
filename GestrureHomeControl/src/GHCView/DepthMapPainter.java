package GHCView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import javax.swing.JPanel;



public class DepthMapPainter extends JPanel {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Font msgFont;
	private int imWidth, imHeight;
	private byte[] imgbytes;
	private Graphics2D graphics;
	private SkeletonPainter spainter;
	
	public DepthMapPainter(int width, int height) {
		this.imWidth = width;
		this.imHeight = height;
		imgbytes = new byte[imWidth * imHeight * 3];
		setBackground(Color.WHITE);
		msgFont = new Font("SansSerif", Font.BOLD, 18);
	}

	

	public Dimension getPreferredSize() { 
		return new Dimension(imWidth, imHeight); }
	 
	 
	
	public void paintComponent(Graphics g) { 
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;

	    drawUserDepths(g2d);
	    g2d.setFont(msgFont);   
	    spainter.drawing(g2d);
	    
	} 



	private void drawUserDepths(Graphics2D g2d) {
	    graphics = g2d;
		ColorModel colorModel = new ComponentColorModel(
	                     ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8},
	                     false, false, ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);

	    DataBufferByte dataBuffer = new DataBufferByte(imgbytes, imWidth*imHeight*3);

	    WritableRaster raster = Raster.createInterleavedRaster(dataBuffer, imWidth,
	                                 imHeight, imWidth*3, 3, new int[] { 0, 1, 2}, null);

	    BufferedImage image = new BufferedImage(colorModel, raster, false, null);

	   graphics.drawImage(image, 0, 0, null);
	} 
	
	public Graphics2D getGraphic(){
		return graphics;
	}
	
	
	public void setImageBytes(byte[] data) {
		imgbytes = data;
	}

	public void setSkeletonPainter(SkeletonPainter spainter){
		this.spainter = spainter;
	}
}
