package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;
import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class Viewer extends JComponent implements SimulatorObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _centerX;
	private int _centerY;
	private double _scale;
	private List<Body> _bodies;
	private boolean _showHelp;
	
	Viewer(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {
		this.setLayout(new BorderLayout());
		this.setBorder(
			BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2),
			"Viewer", TitledBorder.LEFT, TitledBorder.TOP));
		this.setPreferredSize(new Dimension(getWidth(),500));
		
		_bodies = new ArrayList<>();
		_scale = 1.0;
		_showHelp = true;
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
					case '-':
						_scale = _scale * 1.1;
						break;
					case '+':
						_scale = Math.max(1000.0, _scale / 1.1);
						break;
					case '=':
						autoScale();
						break;
					case 'h':
						_showHelp = !_showHelp;
						break;
					default:
				}
				repaint();
			}
	
			@Override
			public void keyReleased(KeyEvent arg0) {}
	
			@Override
			public void keyTyped(KeyEvent e) {}
		});
		addMouseListener(new MouseListener() {

			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}
		
			@Override
			public void mouseClicked(MouseEvent e) {}
		
			@Override
			public void mouseExited(MouseEvent e) {}
		
			@Override
			public void mousePressed(MouseEvent e) {}
		
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		//El centro
		_centerX = getWidth() / 2;
		_centerY = getHeight() / 2;
		gr.setColor(Color.red);
		gr.drawLine(_centerX + 10, _centerY, _centerX - 10, _centerY);
		gr.drawLine(_centerX, _centerY + 10, _centerX, _centerY - 10);

		//Los cuerpos
		for (Body b : _bodies) {
			int x = (int)(b.getPosition().coordinate(0) / _scale) + (_centerX - 5);
			int y = (int)(b.getPosition().coordinate(1) / _scale) + (_centerY - 5);
			
			gr.setColor(Color.BLUE);
			gr.fillOval(x, y, 10, 10);
			
			gr.setColor(Color.BLACK);
			gr.drawString(b.getId(), x-2, y-5);
		}
		
		//La ayuda
		if (_showHelp) {
			gr.setColor(Color.red);
			gr.drawString("h: toggle help, +: zoom-in, -: zoom-out, =: fit", 10, 25);
			gr.drawString("Scaling ratio: " + _scale, 10, 40);
		}
	}

	private void autoScale() {
		double max = 1.0;
		
		for (Body b : _bodies) {
			Vector p = b.getPosition();
			
			for (int i = 0; i < p.dim(); i++)
				max = Math.max(max,
			
			Math.abs(b.getPosition().coordinate(i)));
		}
		
		double size = Math.max(1.0, Math.min((double) getWidth(), (double) getHeight()));
		_scale = max > size ? 4.0 * max / size : 1.0;
	}
	
	// SimulatorObserver methods
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();
			}
		});
	}
	
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();
			}
		});
	}
	
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();
			}
		});
	}
	
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				repaint();
			}
		});
	}
	
	@Override
	public void onDeltaTimeChanged(double dt) {}
	
	@Override
	public void onGravityLawChanged(String gLawsDesc) {}

}
