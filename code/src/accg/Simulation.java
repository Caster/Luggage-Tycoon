package accg;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import accg.objects.Luggage;
import accg.objects.World;
import accg.objects.blocks.ConveyorBlock;
import accg.simulation.LuggageMotionState;
import accg.utils.Utils;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

/**
 * This class manages the simulation.
 */
public class Simulation {
	
	/**
	 * The step size of the simulation.
	 */
	float dt = 0.005f;
	
	/**
	 * The current time in the simulation.
	 */
	float time = 0;
	
	/**
	 * The JBullet world that simulates all objects.
	 */
	DiscreteDynamicsWorld world;
	
	/**
	 * The shape of a piece of luggage, in a form JBullet can understand.
	 * It is shared by all pieces of luggage.
	 */
	CollisionShape luggageShape;

	public Simulation(State s) {
		
		BroadphaseInterface broadphase = new DbvtBroadphase();
		ConstraintSolver solver = new SequentialImpulseConstraintSolver();
		CollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfig);
		world = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);
		world.setGravity(new Vector3f(0, 0, -9.81f));
		
		// initialize the luggage shape, that is shared for all luggage 
		luggageShape = new BoxShape(new Vector3f(0.174f, 0.131f, 0.030f));
		
		// initialize walls and floor
		CollisionShape floor = new StaticPlaneShape(new Vector3f(0, 0, 1), 0);
		RigidBody r = new RigidBody(0, null, floor);
		world.addRigidBody(r);
		
		float[][] wallConstants = new float[][] {
				new float[] {  1,  0, 0, -0.5f },
				new float[] { -1,  0, 0, s.fieldLength - 0.5f },
				new float[] {  0,  1, 0, -0.5f },
				new float[] {  0, -1, 0, s.fieldWidth - 0.5f }
			};
		for (int i = 0; i < 4; i++) {
			RigidBody wallBody = new RigidBody(0, null,
					new StaticPlaneShape(new Vector3f(wallConstants[i][0],
							wallConstants[i][1], wallConstants[i][2]), 0));
			Transform wallTransform = new Transform();
			Matrix4f transformMat;
			if (i < 2) {
				transformMat = new Matrix4f(new float[] {
						1, 0, 0, wallConstants[i][3],
						0, 1, 0, 0,
						0, 0, 1, 0,
						0, 0, 0, 1
				});
			} else {
				transformMat = new Matrix4f(new float[] {
						1, 0, 0, 0,
						0, 1, 0, wallConstants[i][3],
						0, 0, 1, 0,
						0, 0, 0, 1
				});
			}
			wallTransform.set(transformMat);
			wallBody.setWorldTransform(wallTransform);
			world.addRigidBody(wallBody);
		}
	}
	
	/**
	 * Update the simulation internally so that it takes the given conveyor
	 * block into account, at the position given by the block itself.
	 * 
	 * @param cb The block to be added.
	 */
	public void addBlock(ConveyorBlock cb) {
		// TODO
	}
	
	/**
	 * Depending on the time that has passed (determined using
	 * {@link State#prevTime} and {@link State#time}), possibly add one or more
	 * objects to the {@link World} contained in this {@link State}.
	 * 
	 * @param s State to add objects to and read passed time from.
	 */
	public void addObjects(State s) {
		if (Utils.hasTimePassed(s, 1.0, 0)) {
			Luggage newLuggage = new Luggage((float) (2.75 + 0.5 * Math.random()),
					(float) (6.75 + 0.5 * Math.random()), 6);
			s.world.luggage.addObject(newLuggage);
			addLuggageToPhysicsEngine(newLuggage);
		}
		if (Utils.hasTimePassed(s, 1.0, 0.5)) {
			Luggage newLuggage = new Luggage((float) (5.75 + 0.5 * Math.random()),
					(float) (8.75 + 0.5 * Math.random()), 4);
			s.world.luggage.addObject(newLuggage);
			addLuggageToPhysicsEngine(newLuggage);
		}
	}
	
	/**
	 * Delete any objects from the state that were added by the simulation.
	 */
	public void clearObjects(State s) {
		s.world.luggage.clear();
	}
	
	/**
	 * Update the internal simulation time without actually doing simulation steps.
	 * 
	 * @param time New simulation time.
	 */
	public void skipToTime(float time) {
		this.time = time;
	}
	
	/**
	 * Updates the simulation to the current time step. This method
	 * performs simulation steps until the current time is synchronized again
	 * with the real time.
	 * 
	 * @param s The state of the program.
	 */
	public void update(State s) {
		world.stepSimulation(s.time - this.time,
				(int) (Math.ceil((s.time - this.time) / dt) + 1), dt);
		this.time = s.time;
	}
	
	/**
	 * Add a rigid body to the physics engine that represents the given piece of
	 * luggage. It will also ensure that the position and rotation of the given
	 * piece of luggage will be updated by the engine if needed.
	 * 
	 * @param newLuggage Luggage to add.
	 */
	private void addLuggageToPhysicsEngine(Luggage newLuggage) {
		newLuggage.inPhysics = true;
		MotionState motion = new LuggageMotionState(newLuggage);
		RigidBody r = new RigidBody(1, motion, luggageShape);
		world.addRigidBody(r);
	}
}
