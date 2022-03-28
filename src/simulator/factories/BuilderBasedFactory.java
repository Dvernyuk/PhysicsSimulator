package simulator.factories;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {
	private List<Builder<T>> _builders;
	private List<JSONObject> _factoryElements;
	
	public BuilderBasedFactory(List<Builder<T>> builders) 
	{
		_builders = new ArrayList<Builder<T>>();
		_builders.addAll(builders);
		_factoryElements = new ArrayList<JSONObject>();
		
		for(Builder<T> i : _builders)
		{
			_factoryElements.add(i.getBuilderInfo());
		}
	}
	
	public T createInstance(JSONObject info) 
	{
		try {
			T ret = null;
			for(Builder<T> i : _builders)
				if(i.createInstance(info)!=null)
					ret = i.createInstance(info);

			return ret;
			
		} 
		catch(JSONException e) {
			throw new IllegalArgumentException("[EXCEPCION] La estructura JSON no es correcta.");
		}
	}
	
	public List<JSONObject> getInfo() {
		return _factoryElements;
	}
}
