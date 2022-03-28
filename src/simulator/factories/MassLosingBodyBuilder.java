package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.MassLossingBody;

public class MassLosingBodyBuilder extends Builder<Body> {

	public MassLosingBodyBuilder() {
		super("mlb","Mass losing body");
	}

	protected Body createTheInstance(JSONObject data) {
		try {
			double fc = data.getDouble("factor");
			double fr = data.getDouble("freq");
			String id = data.getString("id");
			double[] p = jsonArrayTodoubleArray(data.getJSONArray("pos"));
			Vector pos = new Vector(p);
			double[] v = jsonArrayTodoubleArray(data.getJSONArray("vel"));
			Vector vel = new Vector(v);
			Vector acc = new Vector(pos.dim());
			double m = data.getDouble("mass");
			
			return new MassLossingBody(fc, fr, id, pos, vel, acc, m);
		
		} catch(JSONException e) {
			throw new IllegalArgumentException("[EXCEPCION] El campo 'data' de la estructura JSON contiene datos incorrectos.");
		}
	}
	
	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		
		data.put("factor", "the factor");
		data.put("freq", "the frequency");
		data.put("id", "the identifier");
		data.put("pos", "the position");
		data.put("vel", "the velocity");
		data.put("acc", "the acceleration");
		data.put("mass", "the mass");
		
		return data;
	}
}
