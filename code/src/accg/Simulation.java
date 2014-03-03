package accg;

import java.util.Iterator;

import javax.vecmath.Vector3f;

import accg.objects.Block;
import accg.objects.Luggage;
import accg.objects.World;
import accg.objects.blocks.ConveyorBlock;
import accg.utils.Utils;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;

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
	
	public Simulation() {
		
		BroadphaseInterface broadphase = new DbvtBroadphase();
		ConstraintSolver solver = new SequentialImpulseConstraintSolver();
		CollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfig);
		world = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);
		world.setGravity(new Vector3f(0, 0, -9.81f));
		
		CollisionShape shape = new BoxShape(new Vector3f(1, 1, 1));
		MotionState motion = new DefaultMotionState();
		RigidBody r = new RigidBody(1, motion, shape);
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
			s.world.luggage.addObject(new Luggage(2.75 + 0.5 * Math.random(),
					6.75 + 0.5 * Math.random(), 6));
		}
		if (Utils.hasTimePassed(s, 1.0, 0.5)) {
			s.world.luggage.addObject(new Luggage(5.75 + 0.5 * Math.random(),
					8.75 + 0.5 * Math.random(), 4));
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
		System.out.println(s.time);
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
		world.stepSimulation(dt);
	}
}
