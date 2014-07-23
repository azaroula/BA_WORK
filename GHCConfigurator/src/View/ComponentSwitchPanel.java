package View;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ComponentSwitchPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtActiveState;
	private JTextField txtInactiveState;
	private JTextField txtStatus;

	/**
	 * Create the panel.
	 */
	public ComponentSwitchPanel() {
		setLayout(null);
		
		JLabel lblSwitchToActive = new JLabel("Switch to Active URL:");
		lblSwitchToActive.setBounds(10, 11, 147, 14);
		add(lblSwitchToActive);
	
		txtActiveState = new JTextField();
		txtActiveState.setBounds(10, 36, 459, 25);
		txtActiveState.setForeground(Color.BLUE);
		add(txtActiveState);
		txtActiveState.setColumns(20);
		
		JLabel lblNewLabel = new JLabel("ON command name:");
		lblNewLabel.setBounds(10, 67, 165, 14);
		add(lblNewLabel);
		
		txtInactiveState = new JTextField();
		txtInactiveState.setColumns(20);
		txtInactiveState.setForeground(Color.GREEN);
		txtInactiveState.setBounds(10, 92, 91, 25);
		add(txtInactiveState);
		
		JLabel lblCheckStatusUrl = new JLabel("OFF command name:");
		lblCheckStatusUrl.setBounds(10, 130, 178, 14);
		add(lblCheckStatusUrl);
		
		txtStatus = new JTextField();
		txtStatus.setColumns(20);
		txtStatus.setForeground(Color.RED);
		txtStatus.setBounds(10, 154, 91, 25);
		add(txtStatus);
		

	}

	public JTextField getTxtStatus() {
		return txtStatus;
	}

	public JTextField getTxtInactiveState() {
		return txtInactiveState;
	}

	public JTextField getTxtActiveState() {
		return txtActiveState;
	}
}
