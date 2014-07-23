package GHCModel;



public class ConfigSet {
	
	
	
	private Wall wall;
	private String itemURL;	
	private String onCommand;
	private String offCommand;
	private String accretionValue;
	private HeightLocation heightLocation;
	private WidthLocation widthLocation;
	
	
	public ConfigSet(Wall wall,  WidthLocation widthLocation, HeightLocation heightLocation,
			String itemURL, String onCommand,
			String offCommand, String accretionValue) {
		this.wall = wall;
		this.widthLocation = widthLocation;
		this.heightLocation = heightLocation;
		this.itemURL = itemURL;
		this.onCommand = onCommand;
		this.offCommand = offCommand;
		this.accretionValue = accretionValue;
	}
	
	
	public Wall getWall() {
		return wall;
	}
	public void setWall(Wall wall) {
		this.wall = wall;
	}
	
	public HeightLocation getHeightLocation() {
		return heightLocation;
	}


	public void setHeightLocation(HeightLocation heightLocation) {
		this.heightLocation = heightLocation;
	}


	public WidthLocation getWidthLocation() {
		return widthLocation;
	}


	public void setWidthLocation(WidthLocation widthLocation) {
		this.widthLocation = widthLocation;
	}


	public String getItemURL() {
		return itemURL;
	}
	public void setItemURL(String setItemURL) {
		this.itemURL = setItemURL;
	}
	public String getOnCommand() {
		return onCommand;
	}
	public void setOnCommand(String onCommand) {
		this.onCommand = onCommand;
	}
	public String getOffCommand() {
		return offCommand;
	}
	public void setOffCommand(String offCommand) {
		this.offCommand = offCommand;
	}
	public String getAccretionValue() {
		return accretionValue;
	}
	
	public void setAccretionValue(String accretionVar) {
		this.accretionValue = accretionVar;
	}


	
	public enum Wall {
		FRONT, REAR, LEFT, RIGHT, CEILING
	}
	
	public enum WidthLocation {
		LEFT, RIGHT
	}
	
	public enum HeightLocation {
		TOP, BOTTOM
	}

}
