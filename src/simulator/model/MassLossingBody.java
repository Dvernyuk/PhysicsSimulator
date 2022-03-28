package simulator.model;

import simulator.misc.Vector;

public class MassLossingBody extends Body {
	private double lossFactor, //Entre 0 y 1, factor de perdida de masa 
					lossFrequency, //Frecuencia de perdida
					cnt; //contador
	
	public MassLossingBody(double nLossFactor, double nLossFrequency, String nId, Vector nPos, Vector nVel, Vector nAcc, double nMass) 
	{
		super(nId, nPos, nVel, nAcc, nMass);
		
		lossFactor = nLossFactor;
		lossFrequency = nLossFrequency;
		cnt=0;
	}
	
	void move(double t) {
		super.move(t);
		
		cnt += t;
		if(cnt>=lossFrequency) {
			_mass = _mass*(1-lossFactor);
			cnt = 0;
		}
	}
}
