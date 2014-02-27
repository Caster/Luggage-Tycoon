package accg;

import java.util.Iterator;

import accg.objects.Block;
import accg.objects.Luggage;
import accg.objects.blocks.ConveyorBlock;
import accg.utils.Utils;

/**
 * This class manages the simulation.
 */
public class Simulation {
	
	/**
	 * The step size of the simulation.
	 */
	double dt = 0.01;
	
	/**
	 * The current time in the simulation.
	 */
	double time = 0;
	
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
	public void skipToTime(double time) {
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
		Iterator<Luggage> it = s.world.luggage.iterator();
		while (it.hasNext()) {
			
			Luggage l = it.next();
			
			if (l.supportingBlock == null) {
				
				// below floor: remove this luggage
				if (l.z <= 0) {
					it.remove();
					// TODO add something like an "explosion" here :D
				} else {
					// gravity
					l.vz -= 9.81 * dt;
				}
				
				// update positions
				l.x += l.vx * dt;
				l.y += l.vy * dt;
				l.z += l.vz * dt;
				
				// check for conveyor belts that can take the luggage
				for (int z = (int) (4 * l.z - 3); z <= (int) (4 * l.z); z++) {
					if (!s.world.bc.inBounds((int) (l.x + 0.5), (int) (l.y + 0.5), z)) {
						continue; // TODO better check of course
					}
					Block b = s.world.bc.getBlock((int) (l.x + 0.5), (int) (l.y + 0.5), z);
					if (b instanceof ConveyorBlock) {
						if (((ConveyorBlock) b).canTakeLuggage(l)) {
							l.supportingBlock = b;
							((ConveyorBlock) b).takeLuggage(l);
						}
					}
				}
			} else {
				if (l.supportingBlock instanceof ConveyorBlock) {
					((ConveyorBlock) l.supportingBlock).furtherPosition(l, dt);
				}
			}
		}
	}
}
