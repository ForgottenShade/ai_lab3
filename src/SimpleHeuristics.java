
public class SimpleHeuristics implements Heuristics {
	/**
	 * reference to the environment to be able to figure out positions of obstacles
	 */
	private Environment env;

	/**
	 * @param solvingAgent
	 */
	SimpleHeuristics() {
	}
	
	public void init(Environment env) {
		this.env = env;
	}

	/**
	 * estimates the number of steps between locations a and b by Manhattan distance plus 1 if there is at least 1 turn necessary
	 * @param a
	 * @param b
	 * @return
	 */
	private int nbSteps(Coordinates a, Coordinates b) {
		int h = 0;
		h += Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
		if (a.x - b.x != 0 && a.y - b.y != 0) {
			h += 1; // for turning
		}

		return h;
	}

	/**
	 * estimates the number of steps between location a and b by Manhattan distance, with the given orientation at location a
	 * as Manhattan distance between the locations plus 0, 1 or 2 extra steps for necessary turns
	 * @param a
	 * @param b
	 * @return
	 */
	private int nbSteps(Coordinates a, int orientation, Coordinates b) {
		int h = 0;
		h += Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
		if (a.x - b.x != 0 && a.y - b.y != 0) {
			h += 1; // for turning
		}
		// orientation is 0,1,2,3 for north, east, south, west
		boolean inDirection = 
			(a.y - b.y < 0 && orientation == 0) ||
			(a.y - b.y > 0 && orientation == 2) ||
			(a.x - b.x < 0 && orientation == 1) ||
			(a.x - b.x > 0 && orientation == 3);
		if (h>0 && !inDirection) { // we are not already there and we are not facing the right way
			h++;
		}

		return h;
	}

	public int eval(State s) {
		// ADD CODE to prevent that the agent turns off right after it has turned on

		int h = 0;
		if (s.turned_on) {
			// if there is dirt: max of { manhattan distance to dirt + manhattan distance from dirt to home }
			// else manhattan distance to home
			if (s.dirt.isEmpty()) {
				h = nbSteps(s.position, s.orientation, env.home);
			} else {
				for (Coordinates d:s.dirt) {
					int steps = nbSteps(s.position, d) + nbSteps(d, env.home);
					if (steps > h) {
						h = steps;
					}
				}
				h += s.dirt.size(); // sucking
			}
			h++; // to turn off
		}

		//is turned off
		else {
			h += s.dirt.size() * 50;

			//penalty if you are not at home
			if (!s.position.equals(env.home)){
				h += 100;
			}
		}
		return h;

		// if h == 0 we know that it is a solution node
	}
}