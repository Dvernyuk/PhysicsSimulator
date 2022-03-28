package simulator.model;

import java.util.List;
import simulator.misc.Vector;
import simulator.model.Body;

public class NewtonUniversalGravitation implements GravityLaws 
{
	static private final double G = 6.67E-11;
	
	public NewtonUniversalGravitation() {	}
	
	public void apply(List<Body> bodies) 
	{
		for (Body Bi: bodies) 
		{
			if (Bi.getMass() > 0)
			{
				Vector fuerzaBi = new Vector(Bi.getPosition().dim());
				for (Body Bj: bodies) 
				{
					if (Bi != Bj)
						fuerzaBi = fuerzaBi.plus(force(Bi,Bj));
				}
				Bi.setAcceleration(fuerzaBi.scale(1 / Bi.getMass()));
			}
			else if (Bi.getMass() == 0)
			{
				Bi.setVelocity(new Vector(Bi.getVelocity().dim()));
				Bi.setAcceleration(new Vector(Bi.getAcceleration().dim()));
			}
		}
	}
	
	Vector force(Body a, Body b) 
	{
		double distanciaPosiciones = (a.getPosition().distanceTo(b.getPosition())) * (a.getPosition().distanceTo(b.getPosition()));
		double f;
		f = G * (a.getMass() * b.getMass()) / distanciaPosiciones;
		
		Vector dir = new Vector((b.getPosition()).minus(a.getPosition()).direction());
		
		Vector fuerza = new Vector(dir.scale(f));
		
		return fuerza;
	}
	
	public String toString() {
		return "Newton’s law of universal gravitation";
	}
}
