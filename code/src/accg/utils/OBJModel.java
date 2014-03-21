package accg.utils;

import static accg.gui.toolkit.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.vecmath.Vector3f;

/**
 * A loader for OBJ (Wavefront) models.
 */
public class OBJModel {
	
	private ArrayList<Vector3f> vertices;
	private ArrayList<Vector3f> normals;
	private ArrayList<ArrayList<Vector3f>> facesVertices;
	private ArrayList<ArrayList<Vector3f>> facesNormals;
	
	/**
	 * Creates a new model by reading the given file.
	 * @param file The file to read from.
	 */
	public OBJModel(File file) {
		
		this.vertices = new ArrayList<>();
		this.normals = new ArrayList<>();
		this.facesVertices = new ArrayList<>();
		this.facesNormals = new ArrayList<>();
		
		Scanner s = null;
		
		try {
			s = new Scanner(file);
			s.useLocale(Locale.US);
			parseModel(s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				s.close();
			}
		}
	}
	
	private void parseModel(Scanner s) {
		
		Pattern facePattern = Pattern.compile("(\\d*)//(\\d*)");
		
		while (s.hasNext()) {
			String text = s.next();
			
			if (text.equals("#")) {
				s.nextLine();
			}
			
			if (text.equals("v")) {
				float x = s.nextFloat();
				float y = s.nextFloat();
				float z = s.nextFloat();
				vertices.add(new Vector3f(x, y, z));
			}
			
			if (text.equals("vn")) {
				float x = s.nextFloat();
				float y = s.nextFloat();
				float z = s.nextFloat();
				normals.add(new Vector3f(x, y, z));
			}
			
			if (text.equals("f")) {
				ArrayList<Vector3f> faceVertices = new ArrayList<>();
				ArrayList<Vector3f> faceNormals = new ArrayList<>();
				while (s.hasNext(facePattern)) {
					text = s.next(facePattern);
					int slash = text.indexOf("//");
					int vertexId = Integer.parseInt(text.substring(0, slash));
					faceVertices.add(vertices.get(vertexId - 1)); // I assume here that all v's become before the f's
					int normalId = Integer.parseInt(text.substring(slash + 2));
					faceNormals.add(normals.get(normalId - 1)); // I assume here that all vn's become before the f's
				}
				if (faceVertices.size() != 3) {
					System.err.println("Encountered face with " + faceVertices.size() + " vertices; 3 expected");
				}
				facesVertices.add(faceVertices);
				facesNormals.add(faceNormals);
			}
		}
	}
	
	/**
	 * Draws the model.
	 */
	public void draw() {
		glBegin(GL_TRIANGLES);
		{
			for (int i = 0; i < facesVertices.size(); i++) {
				for (int j = 0; j < facesVertices.get(i).size(); j++) {
					glNormal3f(facesNormals.get(i).get(j));
					glVertex3f(facesVertices.get(i).get(j));
				}
			}
		}
		glEnd();
	}
}
