package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class StatusBar extends JPanel implements SimulatorObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel _currTime; // for current time
	private JLabel _numOfBodies; // for number of bodies
	private JLabel _currLaws; // for gravity laws
	
	StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	private void initGUI() {
		this.setLayout(new FlowLayout( FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder( 1 ));
		
		JPanel panelTime = new JPanel(new BorderLayout());
		_currTime = new JLabel();
		panelTime.add(_currTime, BorderLayout.CENTER);
		panelTime.setPreferredSize(new Dimension(150,13));
		this.add(panelTime);
		
		
		JPanel panelBodies = new JPanel(new BorderLayout());
		_numOfBodies = new JLabel();
		panelBodies.add(_numOfBodies, BorderLayout.CENTER);
		panelBodies.setPreferredSize(new Dimension(150,13));
		this.add(panelBodies);
		
		JPanel panelLaws = new JPanel(new BorderLayout());
		_currLaws = new JLabel();
		panelLaws.add(_currLaws, BorderLayout.CENTER);
		panelLaws.setPreferredSize(new Dimension(300,13));
		this.add(panelLaws);
	}
	
	private double oneDecimal(double time) {
		int nTime = (int)(time * 10);
		return (double)nTime / 10;
	}
	
	// SimulatorObserver methods
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				_currTime.setText("Time:  " + oneDecimal(time));
				_numOfBodies.setText("Bodies:  " + bodies.size());
				_currLaws.setText("Laws:  " + gLawsDesc);
			}
		});
	}
	
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				_currTime.setText("Time:  " + oneDecimal(time));
				_numOfBodies.setText("Bodies:  " + bodies.size());
				_currLaws.setText("Laws:  " + gLawsDesc);
			}
		});
	}
	
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				_numOfBodies.setText("Bodies:  " + bodies.size());
			}
		});
	}
	
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				_currTime.setText("Time:  " + oneDecimal(time));
				_numOfBodies.setText("Bodies:  " + bodies.size());
			}
		});
	}
	
	@Override
	public void onDeltaTimeChanged(double dt) {}
	
	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				_currLaws.setText("Laws:  " + gLawsDesc);
			}
		});
	}

}
