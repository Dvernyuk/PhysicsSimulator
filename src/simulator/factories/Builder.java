package simulator.factories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Builder<T> {
	private String _typeTag;
	private String _desc;
	
	public Builder(String typeTag, String desc) {
		_typeTag = typeTag;
		_desc = desc;
	}
	
	public T createInstance(JSONObject info) 
	{
		try
		{
			T b = null;
			
			if (_typeTag != null && _typeTag.equals(info.getString("type")))
				b = createTheInstance(info.getJSONObject("data"));
			
			return b;
		}
		catch(JSONException e) 
		{
			throw new IllegalArgumentException("[EXCEPCION] El campo 'data' de la estructura JSON contiene datos incorrectos.");
		}
	}
	
	protected abstract T createTheInstance(JSONObject jsonObject);
	
	
	public JSONObject getBuilderInfo() {
		JSONObject info = new JSONObject();
		
		info.put("type", _typeTag);
		info.put("data", createData());
		info.put("desc", _desc);
		
		return info;
	}
	
	protected JSONObject createData() {
		return new JSONObject();
	}
	
	protected double[] jsonArrayTodoubleArray(JSONArray ja) {
		double[] da = new double[ja.length()];
		
		for (int i = 0; i < da.length; i++)
			da[i] = ja.getDouble(i);
		
		return da;
	}
}
