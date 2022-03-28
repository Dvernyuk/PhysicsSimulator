package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Controller _ctrl;
	
	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		_ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		
		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		
		JPanel secPanel = new JPanel();
		secPanel.setLayout(new BoxLayout(secPanel, BoxLayout.Y_AXIS));
		secPanel.add(new BodiesTable(_ctrl));
		secPanel.add(new Viewer(_ctrl));
		mainPanel.add(secPanel, BorderLayout.CENTER);
		secPanel.setBackground(Color.white);
		
		mainPanel.add(new StatusBar(_ctrl), BorderLayout.PAGE_END);
		
		mainPanel.setPreferredSize(new Dimension(800,700));
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
