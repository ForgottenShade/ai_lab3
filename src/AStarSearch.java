import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class AStarSearch implements SearchAlgorithm {

	private Heuristics heuristics;
	private HashMap<Integer, ArrayList<State>> myHashMap;
	public AStarSearch(Heuristics h) {
		this.heuristics = h;
	}
	boolean foundSolution = false;
	int nodeExpansionCount = 0;
	int maxFrontierSize = 0;
	Node solutionNode = null;

	//keep track of state but not the environment
	private ArrayList<Node> frontierList;

	@Override
	public void doSearch(Environment env) {
		// TODO implement the search here

		// initalize
		heuristics.init(env);
		foundSolution = false;
		int nodeExpansionCount = 0;
		int maxFrontierSize = 0;
		Node solutionNode = null;
		frontierList = new ArrayList<Node>();
	

		//be careful here because we can't use max value at other places?
		Node currentNode = new Node(env.getCurrentState(), Integer.MAX_VALUE);
		frontierList.add(currentNode);

		//find best solution
		while (!foundSolution){
			//keep searching
			State s = env.currentState.clone();
			//get position of neighbors, if not obstacles, create a node
			//need to do checks to determine cost of movement from start orientation

			//north
			s.position.y += 1;
			if(!env.obstacles.contains(s.position)){
				Node northNode = new Node();
			}

			//east
			s.position.y -= 1;
			s.position.x += 1;
			if(!env.obstacles.contains(s.position)){
				Node eastNode = new Node();
			}

			//south
			s.position.x -= 1;
			s.position.y -= 1;
			if(!env.obstacles.contains(s.position)){
				Node southNode = new Node();
			}

			//west
			s.position.y += 1;
			s.position.x -= 1;
			if(!env.obstacles.contains(s.position)){
				Node westNode = new Node();
			}


			//keep expanding nodes
			// if a state is already in the hash map (duplicate) then DONT expand that node

			//to detect that the current solution is in a goal state, we can check if the state = home 
			//and all dirt is cleaned and you are turned off
			//we can also use the eval function, and check there if all dirt has been cleaned
			
			//1. find node to expand

			//2. expand it

			//3. check if maxFrontierSize is not larger, if so update

			//chack lab 2 to see how states/nodes are stored in hashmap
		}
		
	}

	private Node findBestNodeInFrontier(){
		//check if null or empty for example
		//iterate through the frontier list and find best evaluation
		return null;
	}

	private void expandNode(Node n){
		//check if null
		
		//expand it for each move you can do 	

		//check if the new state already exists
		checkIfStateExistsIfSoAddIt(n.state);

		//update the frontier, both remove current node and add the new ones
	}

	private boolean checkIfStateExistsIfSoAddIt(State s) {
		//check if we already have this state inside the hash map

		//if not then add it

		//if so we do not add it
		return false;
	}

	@Override
	public List<Action> getPlan() {
		// TODO Auto-generated method stub (done)
		// if we are in a solution node we just need to return thatnode.getPlan()
		List<Action> toRet = solutionNode.getPlan();

		if (toRet != null && !toRet.isEmpty()){
			return toRet;
		}
		if (toRet == null) {
			System.out.println("inside getPlan() : solutionNodes getPlan is null");
		}
		else if (toRet.isEmpty()) {
			System.out.println("inside getPlan() : solutionNodes getPlan is empty");
		}
		return null;
	}

	@Override
	public int getNbNodeExpansions() {
		// TODO Auto-generated method stub (done)
		return nodeExpansionCount;
	}

	@Override
	public int getMaxFrontierSize() {
		// TODO Auto-generated method stub (done)
		return maxFrontierSize;
	}

	@Override
	public int getPlanCost() {
		// TODO Auto-generated method stub (done)
		if (solutionNode != null) {
			return getPlan().size();
		}
		System.out.println("getPlanCost() : solutionNode is still null");

		return -1;
	}

}
