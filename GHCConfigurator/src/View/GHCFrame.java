package View;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Color;



public class GHCFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnSave;
	private JButton btnCancel;
	private JButton btnDeleteTab;
	private List<WallPanel> wallTabsList;
	private JTabbedPane tabbedPane;
	private JComboBox<String> wallBox;
	private String[] wallTabNames = {
			"Select" , "Right Wall",  "Left Wall", "Ceiling", "Front Wall", "Rear Wall"
			};
	private JTextField txtRoomDepth;
	private JLabel lblMilimeter;
	

	/**
	 * Create the frame.
	 */
	public GHCFrame() {
		super("Gesture Home Control Configurator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 712, 493);
		setResizable(false);
		setLocationRelativeTo(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 691, 346);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	
		contentPane.add(tabbedPane);
		
		wallTabsList = new ArrayList<WallPanel>();
		
		btnSave = new JButton("Save");
		btnSave.setBounds(449, 421, 116, 23);
		
		contentPane.add(btnSave);
		
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(575, 421, 116, 23);
		contentPane.add(btnCancel);
		
		JLabel lblAddConfigSet = new JLabel("Add Config Set:");
		lblAddConfigSet.setBounds(10, 425, 88, 14);
		contentPane.add(lblAddConfigSet);
		
		wallBox = new JComboBox<String>();
		wallBox.setBounds(103, 422, 106, 20);
		wallBox.setActionCommand("new Tab");
		for(String s: wallTabNames)
			wallBox.addItem(s);
		contentPane.add(wallBox);
		
		btnDeleteTab = new JButton("Remove this Set");
		btnDeleteTab.setUI(new BasicButtonUI());
		btnDeleteTab.setContentAreaFilled(false);
		btnDeleteTab.setRolloverEnabled(true);
		btnDeleteTab.setBounds(260, 357, 131, 34);
		contentPane.add(btnDeleteTab);
		
		txtRoomDepth = new JTextField();
		txtRoomDepth.setForeground(Color.BLACK);
		txtRoomDepth.setBounds(103, 368, 58, 20);
		contentPane.add(txtRoomDepth);
		txtRoomDepth.setColumns(10);
		
		JLabel lblRoomDepth = new JLabel("Room Depth:");
		lblRoomDepth.setBounds(10, 370, 88, 14);
		contentPane.add(lblRoomDepth);
		
		lblMilimeter = new JLabel("mm");
		lblMilimeter.setBounds(165, 370, 25, 14);
		contentPane.add(lblMilimeter);
	}
	
	
	public List<WallPanel> getWallTabsList() {
		return wallTabsList;
	}


	public void setSaveBtnListener(ActionListener l) {
		btnSave.addActionListener(l);
	}
	
	public void setCancelBtnListener(ActionListener l) {
		btnCancel.addActionListener(l);
	}
	
	public void setDeleteTabBtnListener(ActionListener l) {
		btnDeleteTab.addActionListener(l);
	}
	
	public void setTabBoxListener(ActionListener l) {
		wallBox.addActionListener(l);
	}
	
	public void addTab(String tabName) {
		WallPanel panel = new WallPanel();
		tabbedPane.insertTab(tabName, null, panel, null, wallTabsList.size());
		panel.setName(tabName);
		tabbedPane.setSelectedIndex(wallTabsList.size());
		wallTabsList.add(panel);
		
	}
	public void deleteTab() {
		wallTabsList.remove(tabbedPane.getSelectedIndex());
		tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
			
	}
	
	public String[] getWallTabNames() {
		return wallTabNames;
	}


	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}


	public JTextField getTxtRoomDepth() {
		return txtRoomDepth;
	}
}
