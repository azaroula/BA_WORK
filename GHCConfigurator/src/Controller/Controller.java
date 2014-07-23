package Controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.xml.stream.XMLStreamException;

import org.jdom2.JDOMException;

import Model.ConfigSet;
import Model.ConfigSet.HeightLocation;
import Model.ConfigSet.Wall;
import Model.ConfigSet.WidthLocation;
import Model.ConfigureSupplier;
import Model.XMLWritter;
import View.GHCFrame;

public class Controller implements ActionListener, ItemListener {
	private ConfigureSupplier supplier;
	private GHCFrame frame; 
	private String path;
	
	
	public Controller(GHCFrame frame)  {
		path = "..\\GestrureHomeControl\\config\\config.xml";
		//path = "src\\Controller\\SampleConfig.xml";
		this.frame = frame;
	    setListener();
	    try {
			supplier = new ConfigureSupplier(path);
		} catch (JDOMException e) {
			JOptionPane.showMessageDialog(null, "There exists an error in the Config File", 
					"GestureHomeControl Configurator", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "The specified path or file name of the Config file is incorrect", 
					"GestureHomeControl Configurator", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	    
		generateFromXML();
	}

	private void setListener() {
		
		
		frame.setSaveBtnListener(this);
		frame.setCancelBtnListener(this);
		frame.setTabBoxListener(this);
		frame.setDeleteTabBtnListener(this);
		
	}
	
	public void generateFromXML() {
		if(supplier.getSetsSize() != 0) {
			for (int i = 0; i < supplier.getSetsSize(); i++) {
				
				switch (supplier.get(i).getWall()) {
				case CEILING:
					frame.addTab("Ceiling");
					break;
				case FRONT:
					frame.addTab("Front Wall");
					break;
				case LEFT:
					frame.addTab("Left Wall");
					break;
				case REAR:
					frame.addTab("Rear Wall");
					break;
				case RIGHT:
					frame.addTab("Right Wall");
					break;
				}
				selectHeightBox(i);
				selectWidthBox(i);
				frame.getWallTabsList().get(i).getConfigurePanel().setAddonBoxListener(this);
				if (supplier.get(i).getAccretionValue() == null) {
					setTextFields(i);
					frame.getWallTabsList().get(i).getConfigurePanel().getComponentSwitchPanel().setVisible(true);
					frame.getWallTabsList().get(i).getConfigurePanel().getAddonBox().setSelectedItem("Component Switching");
					
				} else if(supplier.get(i).getAccretionValue().equals("Test")) {
					
					frame.getWallTabsList().get(i).getConfigurePanel()
						.getAddonBox().setSelectedItem("Test");
				} else {
					frame.getWallTabsList().get(i).getConfigurePanel()
					.getAddonBox().setSelectedItem(supplier.get(i).getAccretionValue());
				}			
			}
			frame.getTxtRoomDepth().setText(String.valueOf(supplier.getRoomDepth()));
			
		}
	}
	
	public void selectHeightBox(int index) {
		if (supplier.get(index).getHeightLocation() == HeightLocation.BOTTOM)
			frame.getWallTabsList().get(index).getConfigurePanel().getHeightLctBox().setSelectedItem("Bottom");
		else if (supplier.get(index).getHeightLocation() == HeightLocation.TOP)
			frame.getWallTabsList().get(index).getConfigurePanel().getHeightLctBox().setSelectedItem("Top");
	}
	
	public void selectWidthBox(int index) {
		if (supplier.get(index).getWidthLocation() == WidthLocation.LEFT)
			frame.getWallTabsList().get(index).getConfigurePanel().getWidthLctBox().setSelectedItem("Left");
		else if (supplier.get(index).getWidthLocation() == WidthLocation.RIGHT)
			frame.getWallTabsList().get(index).getConfigurePanel().getWidthLctBox().setSelectedItem("Right");

	}
	
	public void setTextFields(int index) {
		frame.getWallTabsList().get(index).getConfigurePanel().getComponentSwitchPanel().
		getTxtActiveState().setText(supplier.get(index).getItemURL());
		frame.getWallTabsList().get(index).getConfigurePanel().getComponentSwitchPanel().
		getTxtInactiveState().setText(supplier.get(index).getOnCommand());
		frame.getWallTabsList().get(index).getConfigurePanel().getComponentSwitchPanel().
		getTxtStatus().setText(supplier.get(index).getOffCommand());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		XMLWritter writter = null;
		if(e.getActionCommand().equals("Save")) {
			try {
				 writter = new XMLWritter(path);
			} catch (FileNotFoundException e1) {
				System.err.println(e);
				e1.printStackTrace();
			} catch (XMLStreamException e1) {
				System.err.println(e);
				e1.printStackTrace();
			}
			
			for(int i = 0; i < frame.getWallTabsList().size(); i++) {
				
				ConfigSet set = new ConfigSet();
				
				
				switch (frame.getTabbedPane().getComponent(i).getName()) {
				case "Ceiling":
					set.setWall(Wall.CEILING);
					break;
				case "Front Wall":
					set.setWall(Wall.FRONT);
					break;
				case "Left Wall":
					set.setWall(Wall.LEFT);
					break;
				case "Right Wall":
					set.setWall(Wall.RIGHT);
					break;
				case "Rear Wall":
					set.setWall(Wall.REAR);
					break;
				
				default:
					break;
				}
				switch (frame.getWallTabsList().get(i).getConfigurePanel().getWidthLctBox().getSelectedItem().toString()) {
				case "Left":
					set.setWidthLocation(WidthLocation.LEFT);
					break;
				case "Right":
					set.setWidthLocation(WidthLocation.RIGHT);
					break;
				default:
					break;
				}
				switch (frame.getWallTabsList().get(i).getConfigurePanel().getHeightLctBox().getSelectedItem().toString()) {
				case "Top":
					set.setHeightLocation(HeightLocation.TOP);
					break;
				case "Bottom":
					set.setHeightLocation(HeightLocation.BOTTOM);
					break;
				default:
					break;
				}
				
				switch (frame.getWallTabsList().get(i).getConfigurePanel().getAddonBox().getSelectedItem().toString()) {
				case "Component Switching":
					set.setItemURL(frame.getWallTabsList().get(i).getConfigurePanel().
							getComponentSwitchPanel().getTxtActiveState().getText());
					set.setOnCommand(frame.getWallTabsList().get(i).getConfigurePanel().
							getComponentSwitchPanel().getTxtInactiveState().getText());
					set.setOffCommand(frame.getWallTabsList().get(i).getConfigurePanel().
							getComponentSwitchPanel().getTxtStatus().getText());
					break;

				default:
					set.setAccretionValue(frame.getWallTabsList().get(i).getConfigurePanel().getAddonBox().getSelectedItem().toString());
					break;
				}
				try {
					writter.executeConfigSet(set);
				} catch (XMLStreamException e1) {
					System.err.println(e);
					e1.printStackTrace();
				}
				
				
			}
			try {
				writter.executeRoomDepth(frame.getTxtRoomDepth().getText());
			} catch (XMLStreamException e2) {
				System.err.println(e2);
				e2.printStackTrace();
			}
			try {
				writter.complete();
			} catch (XMLStreamException e1) {
				System.err.println(e);
				e1.printStackTrace();
			}
		}
			
		if (e.getActionCommand().equals("Remove this Set"))
			frame.deleteTab();
		
		if(e.getActionCommand().equals("new Tab")) {	
			JComboBox<String> selectedChoice = (JComboBox<String>) e.getSource();
			if(!selectedChoice.getSelectedItem().equals("Select")) {		
				frame.addTab(selectedChoice.getSelectedItem().toString());
				frame.getWallTabsList().get(frame.getWallTabsList().size() -1).getConfigurePanel().setAddonBoxListener(this);
			}
				selectedChoice.setSelectedItem("Select");
		}
		
		if(e.getActionCommand().equals("addon")) {
			
			if (((JComboBox<String>) e.getSource()).getSelectedItem().equals("Component Switching")){
				
				frame.getWallTabsList().get(frame.getTabbedPane().getSelectedIndex())
				.getConfigurePanel().getComponentSwitchPanel().setVisible(true);
			} else 
				frame.getWallTabsList().get(frame.getTabbedPane().getSelectedIndex())
				.getConfigurePanel().getComponentSwitchPanel().setVisible(false);
		}
		
		if(e.getActionCommand().equals("Cancel")) {
			System.out.println(frame.getWallTabsList().get(0).getConfigurePanel().getComponentSwitchPanel().getTxtActiveState().getText());
			System.exit(0);
		}
		
		if(e.getActionCommand().equals("comboBoxEdited")) {
		}
			
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		
	}

}
