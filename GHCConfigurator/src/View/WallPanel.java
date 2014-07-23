package View;

import javax.swing.JPanel;
import java.awt.Color;


public class WallPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	

	private ConfigurePanel configurePanel;

	/**
	 * Create the panel.
	 */
	public WallPanel() {
		setForeground(Color.WHITE);
		setLayout(null);

		configurePanel = new ConfigurePanel();
		configurePanel.setBounds(0, 0, 717, 291);
		
		add(configurePanel);
		
		
	}
	
	public ConfigurePanel getConfigurePanel() {
		return configurePanel;
	}

	
}
