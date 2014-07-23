package GHCModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import GHCModel.ConfigSet.HeightLocation;
import GHCModel.ConfigSet.Wall;
import GHCModel.ConfigSet.WidthLocation;

public class ConfigureSupplier {
	
	private List<Element> elementsList;
	private List<ConfigSet> configSetList;
	private Element roomDepth;
	private Document doc;
	private String path;
	
	
	public ConfigureSupplier(String path) throws JDOMException, IOException {
		this.path = path;
		doc = new SAXBuilder().build( this.path );	
		Element config = doc.getRootElement();
		elementsList = config.getChildren( "set" );
		roomDepth = config.getChild("roomDepth");
		configSetList = new ArrayList<ConfigSet>();
		generateFromXML();
	}
	
	
	private void generateFromXML() {
		
		for (int i = 0; i < elementsList.size(); i++) {
			HeightLocation heightLocation;
			WidthLocation widthLocation;
			
			Wall wall = Wall.valueOf(elementsList.get(i).getChildText("wall"));
			
			try{
				heightLocation = HeightLocation.valueOf(elementsList.get(i).getChildText("heightLocation"));
			} catch(NullPointerException e) {
				heightLocation = null;
			}
			try {
				widthLocation = WidthLocation.valueOf(elementsList.get(i).getChildText("widthLocation"));
			} catch (NullPointerException e) {
				widthLocation = null;
			}
			String itemURL = elementsList.get(i).getChildText("itemURL");
			String onCommand = elementsList.get(i).getChildText("onCommand");
			String offCommand = elementsList.get(i).getChildText("offCommand");
			String accretionValue = elementsList.get(i).getChildText("accretionValue");
			ConfigSet set = new ConfigSet(wall, widthLocation, heightLocation, itemURL, onCommand, offCommand, accretionValue);
			configSetList.add(set);
		}
		
	}
	
	public ConfigSet get(int dataSet){
		return configSetList.get(dataSet);
	}
						
			
	public int getDataSize() {
		return configSetList.size();	
	}
	
	public float getRoomDepth() {
		return Float.parseFloat(roomDepth.getText());
	}
	
	public void setRoomDepth(float depthValue) {
		roomDepth.setText(String.valueOf(depthValue));
		XMLOutputter out = new XMLOutputter();
		try {
			out.output(doc, new FileWriter(new File(path)));
		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
}