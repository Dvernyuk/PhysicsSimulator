package simulator.model;

import simulator.misc.Vector;

public class Body {
	private String _id;
	protected Vector _vel;
	protected Vector _acc;
	protected Vector _pos;
	protected double _mass;
	
	public Body(String id, Vector pos, Vector vel, Vector acc, double mass) {
		_id = id;
		_pos = pos;
		_vel = vel;
		_acc = acc;
		_mass = mass;
	}
	
	public String getId() {
		return _id;
	}
	
	public Vector getPosition() {
		return new Vector(_pos);
	}
	
	public Vector getVelocity() {
		return new Vector(_vel);
	}
	
	public Vector getAcceleration() {
		return new Vector(_acc);
	}
	
	public double getMass() {
		return _mass;
	}
	
	void setPosition(Vector p) {
		_pos = new Vector(p);
	}
	
	void setVelocity(Vector v) {
		_vel = new Vector(v);
	}
	
	void setAcceleration(Vector a) {
		_acc = new Vector(a);
	}

	void move(double t) {
		Vector nVelPos, nAccPos, nAccVel, aux;
		
		nVelPos = _vel.scale(t);
		nAccPos = _acc.scale(t*t*0.5);
		
		aux = _pos.plus(nVelPos);
		_pos = aux.plus(nAccPos);
		
		nAccVel = _acc.scale(t);
		
		_vel = _vel.plus(nAccVel);
	}
	
	public String toString() {
		return "{  \"id\": \""+_id+"\", \"mass\": "+_mass+", \"pos\": "+_pos+", \"vel\": "+_vel+", \"acc\": "+_acc+" }";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Body other = (Body) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}
	
	
}
