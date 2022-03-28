package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {
	private PhysicsSimulator _sim;
	private Factory<Body> _bodiesFactory;
	private Factory<GravityLaws> _lawsFactory;
	
	public Controller(PhysicsSimulator sim, Factory<Body> bodiesFactory, Factory<GravityLaws> lawsFactory)
	{
		_sim = sim;
		_bodiesFactory = bodiesFactory;
		_lawsFactory = lawsFactory;
	}
	
	public void loadBodies(InputStream in) {
		JSONObject jsonInupt = new JSONObject(new JSONTokener(in));
		JSONArray bodies = jsonInupt.getJSONArray("bodies");
		
		for (int i = 0; i < bodies.length(); i++)
			_sim.addBody(_bodiesFactory.createInstance(bodies.getJSONObject(i)));
	}
	
	public void run(int n, OutputStream out)
	{
		PrintStream p = (out == null) ? null : new PrintStream(out);
		String s;
		
		s = "{" + "\r\n";
		s += "\"states\": [" + "\r\n";
		s += _sim.toString() + ',' + "\r\n";
		p.println(s);
		
		for(int i = 0; i < n; i++) {
			_sim.advance();
			s = _sim.toString();
			
	 		if(i < n - 1)
				s += ',';	
	 		s += "\r\n";
	 		
	 		p.println(s);
	 	}
		s = "]" + "\r\n";
		s += "}";
		
		p.println(s);
		p.close();
	}
	
	public void run(int n) {
		for(int i=0;i<n;i++)
			_sim.advance();
	}
	
	public void reset() {
		_sim.reset();
	}
	
	public void setDeltaTime(double dt) {
		_sim.setDeltaTime(dt);
	}
	
	public void addObserver(SimulatorObserver o) {
		_sim.addObserver(o);
	}
	
	public Factory<GravityLaws> getGravityLawsFactory() {
		return _lawsFactory;
	}
	
	public void setGravityLaws(JSONObject info) {
		_sim.setGravityLaws(_lawsFactory.createInstance(info));
	}
}
