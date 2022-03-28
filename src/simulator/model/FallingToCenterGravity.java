package simulator.model;

import java.util.List;
import simulator.misc.Vector;
import simulator.model.Body;

public class FallingToCenterGravity implements GravityLaws 
{
	 static private final double g = 9.81;
	
	public FallingToCenterGravity() {	}
	
	public void apply(List<Body>bodies)
	{
		for (Body Bi : bodies)
		{
			Vector origen = new Vector(Bi.getPosition().dim());
			Vector direc = (Bi.getPosition().minus(origen)).direction();
			
			Vector gravedad = (direc).scale(-g);
			Bi.setAcceleration(gravedad);
		}
	}
	
	public String toString() {
		return "Falling to center gravity";
	}
}
