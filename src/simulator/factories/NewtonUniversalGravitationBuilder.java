package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<GravityLaws> {
	
	public NewtonUniversalGravitationBuilder() {
		super("nlug","Newton�s law of universal gravitation");
	}

	protected GravityLaws createTheInstance(JSONObject data) {
		
		return new NewtonUniversalGravitation();
	}
}
