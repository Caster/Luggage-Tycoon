package accg;

import accg.objects.Block;
import accg.objects.Luggage;
import accg.objects.blocks.ConveyorBlock;

/**
 * This class manages the simulation.
 */
public class Simulation {
	
	/**
	 * The step size of the simulation.
	 */
	double dt = 0.001;
	
	/**
	 * The current time in the simulation.
	 */
	double time = 0;
	
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
		for (Luggage l : s.world.luggage) {
			if (l.supportingBlock == null) {
				
				// lying on floor
				if (l.z <= 0) {
					l.z = 0;
					l.vz = 0;
					l.vx -= 10 * dt * Math.signum(l.vx);
					l.vy -= 10 * dt * Math.signum(l.vy);
					l.vx = Math.abs(l.vx) <= 0.01 ? 0 : l.vx;
					l.vy = Math.abs(l.vy) <= 0.01 ? 0 : l.vy;
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
