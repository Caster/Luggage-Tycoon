package accg;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.objects.Luggage;
import accg.objects.World;
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
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

/**
 * This class manages the simulation.
 */
public class Simulation {
	
	/**
	 * The step size of the simulation.
	 */
	float dt = 0.01f;
	
	/**
	 * The current time in the simulation.
	 */
	float time = 0;
	
	DiscreteDynamicsWorld world;
	CollisionShape luggageShape;

	private ArrayList<RigidBody> luggageList = new ArrayList<>();
	
	public Simulation() {
		
		BroadphaseInterface broadphase = new DbvtBroadphase();
		ConstraintSolver solver = new SequentialImpulseConstraintSolver();
		CollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfig);
		world = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);
		world.setGravity(new Vector3f(0, 0, -9.81f));
		
		// initialize the luggage shape, that is shared for all luggage 
		luggageShape = new BoxShape(new Vector3f(0.174f, 0.131f, 0.030f));
		
		// initialize walls and floor
		MotionState motion = new DefaultMotionState();
		CollisionShape floor = new StaticPlaneShape(new Vector3f(0, 0, 1), 0);
		RigidBody r = new RigidBody(0, motion, floor);
		world.addRigidBody(r);
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
			s.world.luggage.addObject(new Luggage((float) (2.75 + 0.5 * Math.random()),
					(float) (6.75 + 0.5 * Math.random()), 6));
		}
		if (Utils.hasTimePassed(s, 1.0, 0.5)) {
			s.world.luggage.addObject(new Luggage((float) (5.75 + 0.5 * Math.random()),
					(float) (8.75 + 0.5 * Math.random()), 4));
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
		while (this.time + dt < s.time) {
			doSimulationStep(s);
		}
	}
	
	/**
	 * Performs a single simulation step.
	 * @param s The state of the program.
	 */
	private void doSimulationStep(State s) {
		time += dt;
		
		// if Bullet doesn't know some luggage yet, add it
		for (Luggage luggage : s.world.luggage) {
			if (!luggage.inPhysics) {
				luggage.inPhysics = true;
				MotionState motion = new DefaultMotionState();
				RigidBody r = new RigidBody(1, motion, luggageShape);
				r.setUserPointer(luggage);
				Transform transform = new Transform();
				transform.setIdentity();
				transform.transform(new Vector3f(luggage.x, luggage.y, luggage.z));
				r.setWorldTransform(transform);
				world.addRigidBody(r);
				luggageList.add(r);
			}
		}
		
		// do the step
		world.stepSimulation(dt);
		
		for (RigidBody r : luggageList) {
			Object object = r.getUserPointer();
			if (object instanceof Luggage) {
				Vector3f position = new Vector3f();
				r.getCenterOfMassPosition(position);
				((Luggage) object).x = position.x;
				((Luggage) object).y = position.y;
				((Luggage) object).z = position.z;
			}
		}
	}
}
