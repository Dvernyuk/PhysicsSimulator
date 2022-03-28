package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSimulator {
	private double _dt; //tiempo real por paso
	private double _time;
	private GravityLaws _gravityLaws;
	private List<Body> _bodies;
	private List<SimulatorObserver> _observers;
	
	public PhysicsSimulator(GravityLaws gravityLaws, double dt) {
		if(gravityLaws == null)
			throw new IllegalArgumentException("Gravity laws == null");
		if(dt <= 0)
			throw new IllegalArgumentException("Tiempo real por paso <= 0");
		
		_dt = dt;
		_gravityLaws = gravityLaws;
		_time = 0;
		_bodies = new ArrayList<Body>();
		_observers = new ArrayList<SimulatorObserver>();
	}
	
	public void advance() {
		_gravityLaws.apply(_bodies);
		for(Body bd: _bodies)
			bd.move(_dt);
		
		_time += _dt;
		
		for(SimulatorObserver ob: _observers)
			ob.onAdvance(_bodies, _time);
	}
	
	public void addBody(Body b) {
		for(Body bd: _bodies)
			if(bd.equals(b))
				throw new IllegalArgumentException("[EXCEPCION] Hay dos cuerpos iguales.");
		
		_bodies.add(b);
		
		for(SimulatorObserver ob: _observers)
			ob.onBodyAdded(_bodies, b);
	}
	
	public void reset() {
		_bodies.clear();
		_time = 0;
		
		for(SimulatorObserver ob: _observers)
			ob.onReset(_bodies, _time, _dt, _gravityLaws.toString());
	}
	
	public void setDeltaTime(double dt) {
		if(dt <= 0)
			throw new IllegalArgumentException("Tiempo real por paso <= 0");
		
			_dt = dt;
		
		for(SimulatorObserver ob: _observers)
			ob.onDeltaTimeChanged(_dt);
	}
	
	public void setGravityLaws(GravityLaws gravityLaws) {
		if(gravityLaws == null)
			throw new IllegalArgumentException("Gravity laws == null");
		_gravityLaws = gravityLaws;
		
		for(SimulatorObserver ob: _observers)
			ob.onGravityLawChanged(_gravityLaws.toString());
	}
	
	public void addObserver(SimulatorObserver o) {
		if(!_observers.contains(o)) {
			o.onRegister(_bodies, _time, _dt, _gravityLaws.toString());
			_observers.add(o);
		}
	}
	
	public String toString() {
		String ret = "{ \"time\": "+ _time +", \"bodies\": [ ";
		
		for(Body bd: _bodies) 
		{
			ret += bd.toString();
			ret += ", ";
		}
		ret = ret.substring(0, ret.length()-2);
		ret += " ] }";

		return ret;
	}
}
