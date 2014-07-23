package GHCModel;


public class RayTracer {
	 private Vector3D IntersectPoint;

	//S1 = stützvektor
	//S2,S3 = Spannvektoren
	public boolean intersectRayWithSquare(Vector3D R1, Vector3D R2, Vector3D S1, Vector3D S2, 
			Vector3D S3) {
		
		Vector3D dS21 = S2.sub(S1);
		Vector3D dS31 = S3.sub(S1);
		Vector3D n = dS21.cross(dS31); //Normale
		Vector3D dR = R1.sub(R2); //Richtungsvektor der Geraden
		

		float ndotdR = n.dot(dR);

		if (Math.abs(ndotdR) < 1e-6f) { 
			
			return false;
		}

		float t = -n.dot(R1.sub(S1)) / ndotdR;
		Vector3D M = R1.add(dR.scale(t));
		//System.out.println(t);
				
		if (t > 0) 
			return false;
			
		IntersectPoint = M;
		Vector3D dMS1 = M.sub(S1);
		float u = dMS1.dot(dS21);
		float v = dMS1.dot(dS31);

		return (u >= 0.0f && u <= dS21.dot(dS21)
				&& v >= 0.0f && v <= dS31.dot(dS31));
	}

	public Vector3D getIntersectPoint() {
		return IntersectPoint;
	}

}
