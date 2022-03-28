package simulator.factories;

import org.json.JSONObject;
import simulator.model.GravityLaws;
import simulator.model.NoGravity;

public class NoGravityBuilder extends Builder<GravityLaws> {

	public NoGravityBuilder() {
		super("ng","No gravity");
	}

	protected GravityLaws createTheInstance(JSONObject data) {
		
		return new NoGravity();
	}
}
