package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body> {
	
	public BasicBodyBuilder() {
		super("basic","Default Body");
	}

	protected Body createTheInstance(JSONObject data) 
	{
		try	{
			String id = data.getString("id");
			double[] p = jsonArrayTodoubleArray(data.getJSONArray("pos"));
			Vector pos = new Vector(p);
			double[] v = jsonArrayTodoubleArray(data.getJSONArray("vel"));
			Vector vel = new Vector(v);
			Vector acc = new Vector(pos.dim());
			double m = data.getDouble("mass");
			
			return new Body(id, pos, vel, acc, m);
		
		} catch(JSONException e) {
			throw new IllegalArgumentException("[EXCEPCION] El campo 'data' de la estructura JSON contiene datos incorrectos.");
		}
		
	}
	
	protected JSONObject createData() 
	{
		JSONObject data = new JSONObject();
		
		data.put("id", "the identifier");
		data.put("pos", "the position");
		data.put("vel", "the velocity");
		data.put("acc", "the acceleration");
		data.put("mass", "the mass");
		
		return data;
	}
}
