package accg;

public class Material {
	
	public Material(float[] color) {
		if (color.length != 4) {
			throw new RuntimeException("Colors must have 4 components, you provided " + color.length);
		}
	}
	
	public void setMaterial() {
		// TODO
	}
}
