package accg.simulation;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import accg.State;
import accg.objects.*;
import accg.objects.blocks.ConveyorBlock;
import accg.objects.blocks.EnterBlock;
import accg.utils.Utils;

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
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
	 * Creates a new simulation.
	 * @param s The {@link State} object.
	 */
	public Simulation(State s) {
		
		// initialize JBullet world and solvers
		BroadphaseInterface broadphase = new DbvtBroadphase();
		ConstraintSolver solver = new SequentialImpulseConstraintSolver();
		CollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfig);
		world = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);
		world.setGravity(new Vector3f(0, 0, -9.81f));
		
		// set contact callback
		BulletGlobals.setContactProcessedCallback(new SimulationCallback(s,
				BulletGlobals.getContactProcessedCallback()));
		
		// initialize walls and floor
		CollisionShape floor = new StaticPlaneShape(new Vector3f(0, 0, 1), 0);
		RigidBody r = new RigidBody(0, null, floor);
		r.setCollisionFlags(r.getCollisionFlags() | CollisionFlags.CUSTOM_MATERIAL_CALLBACK);
		r.setUserPointer(new SimulationBodyInfo(null, SimulationBodyType.FLOOR));
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
			wallBody.setUserPointer(SimulationBodyType.WALL);
			world.addRigidBody(wallBody);
		}
	}
	
	/**
	 * Update the simulation internally so that it takes the given conveyor
	 * block into account, at the position given by the block itself.
	 * 
	 * @param s State, used to find neighbors of the block to determine its shape.
	 * @param cb The block to be added.
	 */
	public void addConveyorBlock(State s, ConveyorBlock cb) {
		final RigidBody r = new RigidBody(0, null,
				ShapeFactory.getConveyorShape(s, cb));
		Transform blockTransform = new Transform();
		blockTransform.set(new Matrix4f(new float[] {
				1, 0, 0, cb.getX(),
				0, 1, 0, cb.getY(),
				0, 0, 1, cb.getZ() / 4.0f,
				0, 0, 0, 1
		}));
		r.setWorldTransform(blockTransform);
		r.setFriction(1.5f);
		r.setAngularVelocity(cb.getAngularVelocity());
		r.setLinearVelocity(cb.getLinearVelocity());
		r.setUserPointer(SimulationBodyType.CONVEYOR_BLOCK);
		world.addRigidBody(r);
		
		// make sure the body is cleaned up when the conveyorblock is removed
		cb.addListener(new DrawableObjectListener() {
			@Override
			public void onDestroy() {
				world.removeRigidBody(r);
			}
		});
	}
	
	/**
	 * Depending on the time that has passed (determined using
	 * {@link State#prevTime} and {@link State#time}), possibly add one or more
	 * objects to the {@link World} contained in this {@link State}.
	 * 
	 * @param s State to add objects to and read passed time from.
	 */
	public void addObjects(State s) {
		
		for (Block b : s.world.bc) {
			if (b instanceof EnterBlock) {
				EnterBlock eb = (EnterBlock) b;

				if (Utils.hasTimePassed(s, eb.timeBetweenLuggage, 0)) {
					Luggage newLuggage = new Luggage(eb.getX(), eb.getY(), eb.getZ() / 4f);
					s.world.luggage.addObject(newLuggage);
					addLuggageToPhysicsEngine(newLuggage);
				}
			}
		}
	}
	
	/**
	 * Delete any objects from the state that were added by the simulation.
	 * @param s The state of the program.
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
		MotionState motion = new LuggageMotionState(newLuggage);
		final RigidBody r = new RigidBody(Luggage.WEIGHT, motion, ShapeFactory.getLuggageShape(),
				ShapeFactory.getLuggageShapeInertia());
		r.setFriction(1.1f);
		r.setUserPointer(new SimulationBodyInfo(newLuggage, SimulationBodyType.LUGGAGE));
		world.addRigidBody(r);
		
		// make sure the body is cleaned up when the luggage is removed
		newLuggage.addListener(new DrawableObjectListener() {
			@Override
			public void onDestroy() {
				world.removeRigidBody(r);
			}
		});
	}
}
