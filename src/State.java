import java.util.HashSet;

/**
 * 
 * This class holds all information about the state of the environment and the robot
 *
 */
public class State implements Cloneable {
		public HashSet<Coordinates> dirt;
		public Coordinates position;
		public int orientation; // 0,1,2,3 for north, east, south, west
		public boolean turned_on;

		public State() {
			dirt = new HashSet<Coordinates>();
			position = new Coordinates(0,0);
			orientation = 0;
			turned_on = false;
		}

		public Coordinates facingPosition() {
			Coordinates res = (Coordinates)position.clone();
			switch (orientation) {
				case 0: res.y++;
				break;
				case 1: res.x++;
				break;
				case 2: res.y--;
				break;
				case 3: res.x--;
				break;
			}
			return res;
		}

		@SuppressWarnings("unchecked")
		public State clone() {
			State cloned;
			try {
				cloned = (State)super.clone();
				cloned.dirt = (HashSet<Coordinates>)dirt.clone();
				cloned.position = (Coordinates)position.clone();
			} catch (CloneNotSupportedException e) { e.printStackTrace(); System.exit(-1); cloned=null; }
			return cloned;
		}
		

		public String toString() {
			return "State{#dirt: " + dirt.size() + ", position: " + position + ", orientation: " + orientation + ", on:" + turned_on + "}";
		}

		public boolean equals(Object o) {
			if (!(o instanceof State)) {
				return false;
			}
			State s = (State) o;
			return s.position.equals(position) && s.orientation == orientation && s.turned_on == turned_on && s.dirt.equals(dirt);
		}

		// use this function to prevent duplicate states
		// but this hash functio does not guarantee unique states, we need to store the
		// states in a hash map
		public int hashCode() {
			return position.hashCode() ^ orientation ^ (turned_on ? 1 : 0) ^ dirt.hashCode();
		}
	}