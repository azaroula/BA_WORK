package GHCModel;

import org.OpenNI.Point3D;



public class Vector3D {

	private float x, y, z;
	
	public Vector3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}	
	
	public Vector3D(Point3D point) {
		this.x = point.getX();
		this.y = point.getY();
		this.z = point.getZ();
	}
	

	public Vector3D add(Vector3D other) {
		return new Vector3D(x + other.x, y + other.y, z + other.z);
	}
	
	public Vector3D sub(Vector3D other) {
		return new Vector3D(x - other.x, y - other.y, z - other.z);
		}
	
	public Vector3D scale(float f) {
		return new Vector3D(x * f, y * f, z * f);
	}
	
	public Vector3D cross(Vector3D other) {
		return new Vector3D(y * other.z - z * other.y,
				z - other.x - x * other.z,
				x - other.y - y * other.x);
	}

	public float dot(Vector3D other) {
		return x * other.x + y * other.y + z * other.z;
	}
	
	public float getX(){
		return this.x;
	}
	
	public float getY(){
		return this.y;
	}
	
	public float getZ(){
		return this.z;
	}
}
