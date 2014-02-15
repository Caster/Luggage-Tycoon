package accg;

import accg.objects.Luggage;

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
	 * Updates the simulation to the current time step.
	 * 
	 * @param s The state of the program.
	 */
	public void update(State s) {
		while (this.time + dt < s.time) {
			doSimulationStep(s);
		}
	}

	private void doSimulationStep(State s) {
		time += dt;
		for (Luggage l : s.world.luggage) {
			if (l.supportingBlock == null) {
				// gravity
				l.vz -= 9.81 * dt;
				
				// update positions
				l.x += l.vx * dt;
				l.y += l.vy * dt;
				l.z += l.vz * dt;
				
				// bounce
				if (l.z < 0) {
					l.z = 0;
					l.vz *= -0.9;
				}
			}
		}
	}
}
