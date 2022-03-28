package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private JFileChooser _chooser;
	private SpinnerNumberModel _steps, _delay;
	private JSpinner _spinnerSteps, _spinnerDelay;
	private JTextField _dt;
	private JButton _open, _physics, _run, _stop, _exit;
	private ImageIcon _deleteIcon;
	//private volatile Thread _thread;
	private SwingWorker<Void, Void> _worker;
	
	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_worker=null;
		initGUI();
		_ctrl.addObserver(this);
	}
	
	private void initGUI() {
		this.setLayout(new BorderLayout(0,0));
		JToolBar toolBar = new JToolBar();
		
		_deleteIcon = new ImageIcon("resources/icons/delete.png");
		
		this.add(toolBar, BorderLayout.CENTER);
		
		_open = createButton("Open file","resources/icons/open.png");
			_chooser = new JFileChooser();
			_chooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources"));
			_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);		
		_open.addActionListener(new OpenListener());
		toolBar.add(_open);
		toolBar.addSeparator();
		
		_physics = createButton("Physics","resources/icons/physics.png");
		_physics.addActionListener(new PhysicsListener());
		toolBar.add(_physics);
		toolBar.addSeparator();
		
		_run = createButton("Run","resources/icons/run.png");
		_run.addActionListener(new RunListener());
		toolBar.add(_run);
		
		_stop = createButton("Stop","resources/icons/stop.png");
		_stop.addActionListener(new StopListener());
		toolBar.add(_stop);
		toolBar.addSeparator();
		
		toolBar.add(new JLabel("Delay:"));
		toolBar.addSeparator();
			_delay = new SpinnerNumberModel(1, 0, 1000, 1);
			_spinnerDelay = new JSpinner(_delay);
			_spinnerDelay.setMinimumSize(new Dimension(75,100));
			_spinnerDelay.setMaximumSize(new Dimension(100,100));
			toolBar.add(_spinnerDelay);
		toolBar.addSeparator();
		
		toolBar.add(new JLabel("Steps:"));
		toolBar.addSeparator();		
			_steps = new SpinnerNumberModel(10000,0,100000,1000);
			_spinnerSteps = new JSpinner(_steps);
			_spinnerSteps.setMinimumSize(new Dimension(100,100));
			_spinnerSteps.setMaximumSize(new Dimension(100,100));
			toolBar.add(_spinnerSteps);
		toolBar.addSeparator();
		
		toolBar.add(new JLabel("Delta-Time:"));
		toolBar.addSeparator();	
			_dt = new JTextField(9);
			_dt.setMinimumSize(new Dimension(100,100));
			_dt.setMaximumSize(new Dimension(100,100));
			toolBar.add(_dt);
		toolBar.addSeparator();
		
		toolBar.add(Box.createHorizontalGlue());
		
		_exit = createButton("Exit","resources/icons/exit.png");
		_exit.addActionListener(new ExitListener());
		toolBar.add(_exit);
	}
	
	private JButton createButton(String toolTip, String iconPath) {
		JButton button = new JButton();
		
		button.setToolTipText(toolTip);
		button.setIcon(new ImageIcon(iconPath));
		
		return button;
	}
	
	protected class OpenListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(_chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				try {
					FileInputStream file = new FileInputStream(_chooser.getSelectedFile());
					_ctrl.reset();
					_ctrl.loadBodies(file);
				} catch (FileNotFoundException ex) {
					JOptionPane.showMessageDialog(null, "Error: the file doesn't exist.", 
						"Error", JOptionPane.INFORMATION_MESSAGE, _deleteIcon);
				}
			}
		}
		
	}
	
	protected class PhysicsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			List<JSONObject> laws = _ctrl.getGravityLawsFactory().getInfo();
			List<String> poss = new ArrayList<String>();

			for(JSONObject j: laws) 
				poss.add(j.getString("desc") + " (" + j.getString("type") + ")");
			
			String opt = (String) JOptionPane.showInputDialog(null, "Select gravity laws to be used.", 
				"Gravity Laws Selector", JOptionPane.DEFAULT_OPTION, null,
				poss.toArray(), poss.get(0));
			int index = poss.indexOf(opt);
			
			_ctrl.setGravityLaws(laws.get(index));
		}
	}
	
	protected class RunListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				_ctrl.setDeltaTime(Double.parseDouble(_dt.getText()));

				/*_thread = new Thread() {		
					public void run() {
						run_sim((Integer)_steps.getValue(), (Integer)_delay.getValue());
						setButtons(true);
						_thread = null;
					}
				};
				
				setButtons(false);
				_thread.start();*/

				_worker = new SwingWorker<Void, Void>(){

					@Override
					protected Void doInBackground() throws Exception {
						setButtons(false);
						run_sim((Integer)_steps.getValue(), (Integer)_delay.getValue());
						
						return null;
					}
			
					@Override
					protected void done() {
						setButtons(true);
					}
					
				};
				_worker.execute();
				
			} catch(IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(null, "Error: non-appropriate delta-time value.", 
						"Error", JOptionPane.INFORMATION_MESSAGE, _deleteIcon);
			}
		}
		
	}
	
	protected class StopListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (_worker != null)
				_worker.cancel(true);
			
			//if (_thread != null)
				//_thread.interrupt();
			//setButtons(true);
		}
		
	}
	
	protected class ExitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {		
			if(JOptionPane.showOptionDialog(null, "Are you sure?", "Exit",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, null, null) 
					== JOptionPane.YES_OPTION)
						System.exit(0);
		}
		
	}
	
	private void setButtons(boolean enabled) {
		_run.setEnabled(enabled);
		_open.setEnabled(enabled);
		_physics.setEnabled(enabled);
		_exit.setEnabled(enabled);
		_dt.setEnabled(enabled);
		_spinnerSteps.setEnabled(enabled);
		_spinnerDelay.setEnabled(enabled);
	}
	
	private void run_sim(int n, long delay) {
		
		while ( n>0 && !Thread.currentThread().isInterrupted()) {
			try {
				_ctrl.run(1);

			} catch(Exception e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", 
								JOptionPane.INFORMATION_MESSAGE, _deleteIcon);
					}
				});
				
				return;
			}

			try {
				Thread.sleep(delay);	
			} catch (InterruptedException e) {
				
				return;
			}
			n--;
		}
	}
	
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				_dt.setText(dt+"");
			}
		});
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				_dt.setText(dt+"");
			}
		});
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {}

	@Override
	public void onAdvance(List<Body> bodies, double time) {}

	@Override
	public void onDeltaTimeChanged(double dt) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				_dt.setText(dt+"");
			}
		});
	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {}
}
