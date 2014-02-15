package accg.utils;

import static org.lwjgl.opengl.GL11.*;
import static accg.utils.GLUtils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import org.lwjgl.util.vector.Vector3f;

/**
 * A loader for OBJ (Wavefront) models.
 */
public class OBJModel {
	
	private ArrayList<Vector3f> vertices;
	private ArrayList<ArrayList<Vector3f>> faces;
	private ArrayList<Vector3f> normals;
	
	public OBJModel(File file) {
		
		this.vertices = new ArrayList<>();
		this.faces = new ArrayList<>();
		this.normals = new ArrayList<>();
		
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
		while (s.hasNext()) {
			String type = s.next();
			
			if (type.equals("#")) {
				s.nextLine();
			}
			
			if (type.equals("v")) {
				float x = s.nextFloat();
				float y = s.nextFloat();
				float z = s.nextFloat();
				vertices.add(new Vector3f(x, y, z));
			}
			
			if (type.equals("f")) {
				ArrayList<Vector3f> face = new ArrayList<>();
				while (s.hasNextInt()) {
					int vertexId = s.nextInt();
					face.add(vertices.get(vertexId - 1)); // I assume here that all v's become before the f's
				}
				if (face.size() != 3) {
					System.err.println("Encountered face with " + face.size() + " vertices; 3 expected");
				}
				faces.add(face);
				
				Vector3f a = new Vector3f(), b = new Vector3f(), normal = new Vector3f();
				Vector3f.sub(face.get(1), face.get(0), a);
				Vector3f.sub(face.get(2), face.get(0), b);
				Vector3f.cross(a, b, normal);
				normals.add(normal);
			}
		}
	}

	public void draw() {
		glBegin(GL_TRIANGLES);
		{
			for (int i = 0; i < faces.size(); i++) {
				glNormal3f(normals.get(i));
				for (Vector3f vertex : faces.get(i)) {
					glVertex3f(vertex);
				}
			}
		}
		glEnd();
	}
}
