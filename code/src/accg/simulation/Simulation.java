package accg.simulation;

import java.util.ArrayList;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import accg.State;
import accg.objects.*;
import accg.objects.Luggage.LuggageColor;
import accg.objects.blocks.*;
import accg.objects.blocks.ConveyorBlock.ConveyorBlockType;
import accg.utils.Utils;

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.*;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexHullShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;

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
		final ArrayList<RigidBody> addedBodies = new ArrayList<>(); 
		RigidBody r = new RigidBody(0, null,
				ShapeFactory.getConveyorShape(s, cb));
		Transform blockTransform = new Transform();
		blockTransform.set(new Matrix4f(new float[] {
				1, 0, 0, cb.getX(),
				0, 1, 0, cb.getY(),
				0, 0, 1, cb.getZ() / 4f,
				0, 0, 0, 1
		}));
		r.setWorldTransform(blockTransform);
		r.setFriction(1.5f);
		r.setAngularVelocity(cb.getAngularVelocity());
		r.setLinearVelocity(cb.getLinearVelocity());
		r.setUserPointer(SimulationBodyType.CONVEYOR_BLOCK);
		world.addRigidBody(r);
		addedBodies.add(r);
		
		// hard-coded situations
		if (cb.getConveyorBlockType() == ConveyorBlockType.ENTER) {
			addBlockHull(cb, addedBodies, EnterBlock.HULL_POINTS);
		}
		if (cb.getConveyorBlockType() == ConveyorBlockType.LEAVE) {
			addBlockHull(cb, addedBodies, LeaveBlock.HULL_POINTS);
		}
		
		// make sure the body is cleaned up when the conveyorblock is removed
		cb.addListener(new DrawableObjectListener() {
			@Override
			public void onDestroy() {
				for (RigidBody body : addedBodies) {
					world.removeRigidBody(body);
				}
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
				
				if (Utils.hasTimePassed(s, eb.timeBetweenLuggage, 0) &&
						(eb.getGeneratedLuggageNum() < eb.getLuggageNum() ||
								eb.getLuggageNum() < 0)) {
					ArrayList<LuggageColor> lugCols = eb.getLuggageColors();
					Luggage newLuggage;
					if (lugCols == null) {
						newLuggage = new Luggage(eb.getX(), eb.getY(),
								eb.getZ() / 4f + 0.5f);
					} else {
						LuggageColor col = lugCols.get(
								(int) Math.floor(Math.random() * lugCols.size()));
						newLuggage = new Luggage(eb.getX(), eb.getY(),
								eb.getZ() / 4f + 0.5f, col);
					}
					s.world.luggage.addObject(newLuggage);
					addLuggageToPhysicsEngine(newLuggage);
					eb.incrementGeneratedLuggageNum();
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
	 * Add rigid bodies to the world that represent the shape of the hull around
	 * the conveyor belt in a block. These newly created bodies are added
	 * to the given list.
	 * 
	 * @param block The block for which bodies need to be added. Position
	 *            of this block is used for positioning the bodies.
	 * @param addedBodies A list of bodies, to which newly created bodies will
	 *            be appended.
	 * @param hullPoints Points that form the hull. Should be a set of quads.
	 */
	private void addBlockHull(ConveyorBlock block,
			ArrayList<RigidBody> addedBodies, Vector3f[] hullPoints) {		
		Transform blockTransform = new Transform();
		blockTransform.set(new Matrix4f(new float[] {
				1, 0, 0, block.getX(),
				0, 1, 0, block.getY(),
				0, 0, 1, block.getZ() / 4f,
				0, 0, 0, 1
		}));
		
		for (int i = 0; i < hullPoints.length; i += 4) {
			ObjectArrayList<Vector3f> points = new ObjectArrayList<>(4);
			for (int j = i; j < i + 4; j++) {
				points.add(hullPoints[j]);
			}
			Utils.rotatePoints(block.getOrientation(), points);
			RigidBody body = new RigidBody(0, null, new ConvexHullShape(points));
			// enable callback to delete luggage in the LeaveBlocks
			if (block.getConveyorBlockType() == ConveyorBlockType.LEAVE &&
					i == 8) {
				body.setUserPointer(new SimulationBodyInfo(block, SimulationBodyType.LEAVE_BLOCK));
			}
			
			Utils.rotatePointsBack(block.getOrientation(), points);
			body.setWorldTransform(blockTransform);
			world.addRigidBody(body);
			addedBodies.add(body);
		}
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
