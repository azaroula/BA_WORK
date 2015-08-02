package View;

import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class ConfigurePanel extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> heightLctBox;
	private JComboBox<String> widthLctBox;
	private JComboBox<String> addonBox;
	private ComponentSwitchPanel componentSwitchPanel;
	private String[] heightLctEntries = {
			"None",  "Top", "Bottom"
			};
	private String[] widthLctEntries = {
			 "None", "Left", "Right"
			};
	private String[] addonEntries = {
			"None", "Component Switching", "Test"
			};
	/**
	 * Create the panel.
	 */
	public ConfigurePanel() {
		setLayout(null);
		
		JLabel lblHeightLocation = new JLabel("Height Location");
		lblHeightLocation.setBounds(10, 21, 97, 14);
		add(lblHeightLocation);
		
		heightLctBox = new JComboBox<String>();
		heightLctBox.setBounds(110, 18, 108, 20);
		for(String s : heightLctEntries)
			heightLctBox.addItem(s);
		add(heightLctBox);
		
		
		JLabel lblWidthLocation = new JLabel("Width Location");
		lblWidthLocation.setBounds(409, 21, 85, 14);
		add(lblWidthLocation);
		
		widthLctBox = new JComboBox<String>();
		widthLctBox.setBounds(498, 18, 108, 20);
		for(String s : widthLctEntries)
			widthLctBox.addItem(s);
		add(widthLctBox);
		
		JLabel lblAddon = new JLabel("addon");
		lblAddon.setBounds(224, 87, 46, 14);
		add(lblAddon);
		
		addonBox = new JComboBox<String>();
		addonBox.setBounds(280, 84, 171, 20);
		addonBox.setActionCommand("addon");
		for(String s : addonEntries)
			addonBox.addItem(s);
		addonBox.setEditable(true);
		add(addonBox);
		
		componentSwitchPanel = new ComponentSwitchPanel();
		componentSwitchPanel.getTxtInactiveState().setLocation(130, 93);
		componentSwitchPanel.getTxtStatus().setLocation(365, 93);
		componentSwitchPanel.setBounds(0, 112, 540, 250);
		componentSwitchPanel.setVisible(false);
		add(componentSwitchPanel);

	}
	public ComponentSwitchPanel getComponentSwitchPanel() {
		return componentSwitchPanel;
	}
	public JComboBox<String> getAddonBox() {
		return addonBox;
	}
	public JComboBox<String> getWidthLctBox() {
		return widthLctBox;
	}
	public JComboBox<String> getHeightLctBox() {
		return heightLctBox;
	}
	
	public void setAddonBoxListener(ActionListener l) {
		addonBox.addActionListener(l);
	}
}
