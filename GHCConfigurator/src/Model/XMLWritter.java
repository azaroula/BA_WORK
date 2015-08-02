package Model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javanet.staxutils.IndentingXMLEventWriter;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class XMLWritter {
	
	private XMLEventWriter writer;
	private String path;
	private XMLEvent header;
	private XMLEvent sRoot;
	private XMLEvent eRoot;
	private XMLEvent sSet;
	private XMLEvent eSet;
	private XMLEvent sWall;
	private XMLEvent eWall;
	private XMLEvent sHeightloc;
	private XMLEvent eHeightloc;
	private XMLEvent sWidthLoc;
	private XMLEvent eWidthLoc;
	private XMLEvent sAccretion;
	private XMLEvent eAccretion;
	private XMLEvent sItemURL;
	private XMLEvent eItemURL;
	private XMLEvent sOnCommand;
	private XMLEvent eOnCommand;
	private XMLEvent sOffCommand;
	private XMLEvent eoffCommand;
	private XMLEvent sRoomDepth;
	private XMLEvent eRoomDepth;
	private XMLEvent sRoomHeight;
	private XMLEvent eRoomHeight;
	private XMLEvent sRoomWidth;
	private XMLEvent eRoomWidth;
	
	
	

	private XMLEventFactory eventFactory;
	
	public XMLWritter(String path) throws FileNotFoundException, XMLStreamException {
		this.path = path;
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		
		writer = outputFactory.createXMLEventWriter(new FileOutputStream(this.path), "UTF-8");
		writer = new IndentingXMLEventWriter(writer);
		
		eventFactory = XMLEventFactory.newInstance();
		header = eventFactory.createStartDocument();
		
		sRoot = eventFactory.createStartElement("", "", "configuration");
		eRoot = eventFactory.createEndElement("", "", "configuration");
		
		sSet = eventFactory.createStartElement("", "", "set");
		eSet = eventFactory.createEndElement("", "", "set");
		
		sWall = eventFactory.createStartElement("", "", "wall");
		eWall = eventFactory.createEndElement("", "", "wall");
		
		sHeightloc = eventFactory.createStartElement("", "", "heightLocation");
		eHeightloc = eventFactory.createEndElement("", "", "heightLocation");
		
		sWidthLoc = eventFactory.createStartElement("", "","widthLocation");
		eWidthLoc = eventFactory.createEndElement("", "", "widthLocation");
		
		sAccretion = eventFactory.createStartElement("", "", "accretionValue");
		eAccretion = eventFactory.createEndElement("", "", "accretionValue");
		
		sItemURL = eventFactory.createStartElement("", "", "itemURL");
		eItemURL = eventFactory.createEndElement("", "", "itemURL");
		
		sOnCommand = eventFactory.createStartElement("", "", "onCommand");
		eOnCommand = eventFactory.createEndElement("", "", "onCommand");
		
		sOffCommand = eventFactory.createStartElement("", "", "offCommand");
		eoffCommand = eventFactory.createEndElement("", "", "offCommand");
		
		sRoomDepth = eventFactory.createStartElement("", "", "roomDepth");
		eRoomDepth = eventFactory.createEndElement("", "", "roomDepth");
		
		sRoomHeight = eventFactory.createStartElement("", "", "roomHeight");
		eRoomHeight = eventFactory.createEndElement("", "", "roomHeight");
		
		sRoomWidth = eventFactory.createStartElement("", "", "roomWidth");
		eRoomWidth = eventFactory.createEndElement("", "", "roomWidth");
		
		
		writer.add(header);
		writer.add(sRoot);
	}
	
	public void executeConfigSet(ConfigSet set) throws XMLStreamException {
		writer.add(sSet);
		
		writer.add(sWall);
		XMLEvent wall = eventFactory.createIgnorableSpace(set.getWall().toString());
		writer.add(wall);
		writer.add(eWall);
	
		if (set.getWidthLocation() != null) {
			writer.add(sWidthLoc);
			XMLEvent withLoacation = eventFactory.createIgnorableSpace(set.getWidthLocation().toString());
			writer.add(withLoacation);
			writer.add(eWidthLoc);
		}
		if (set.getHeightLocation() != null) {
			writer.add(sHeightloc);
			XMLEvent heightLoacation = eventFactory.createIgnorableSpace(set.getHeightLocation().toString());
			writer.add(heightLoacation);
			writer.add(eHeightloc);
		}
		if(set.getAccretionValue() != null) {
			writer.add(sAccretion);
			XMLEvent accretion = eventFactory.createIgnorableSpace(set.getAccretionValue());
			writer.add(accretion);
			writer.add(eAccretion);
		}
		if(set.getItemURL() != null) {
			writer.add(sItemURL);
			XMLEvent activeURL = eventFactory.createIgnorableSpace(set.getItemURL());
			writer.add(activeURL);
			writer.add(eItemURL);
		}
		if(set.getOnCommand() != null) {
			writer.add(sOnCommand);
			XMLEvent inactiveURL = eventFactory.createIgnorableSpace(set.getOnCommand());
			writer.add(inactiveURL);
			writer.add(eOnCommand);
		}
		if(set.getOffCommand() != null) {
			writer.add(sOffCommand);
			XMLEvent statusURL = eventFactory.createIgnorableSpace(set.getOffCommand());
			writer.add(statusURL);
			writer.add(eoffCommand);
		}
		
		writer.add(eSet);
		
		
	}
	public void setRoomDimension(String depthValue, String heightValue, String widthValue) throws XMLStreamException {
		writer.add(sRoomDepth);
		XMLEvent roomDepth = eventFactory.createIgnorableSpace(depthValue);
		writer.add(roomDepth);
		writer.add(eRoomDepth);
		
		writer.add(sRoomHeight);
		XMLEvent roomHeight = eventFactory.createIgnorableSpace(heightValue);
		writer.add(roomHeight);
		writer.add(eRoomHeight);
		
		writer.add(sRoomWidth);
		XMLEvent roomWidth = eventFactory.createIgnorableSpace(widthValue);
		writer.add(roomWidth);
		writer.add(eRoomWidth);
		
	}
	
	public void complete() throws XMLStreamException {
		XMLEvent endDocument = eventFactory.createEndDocument();
		writer.add(eRoot);
		writer.add(endDocument);
		writer.close();
	}

}
