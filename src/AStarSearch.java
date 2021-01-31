import javax.xml.transform.stax.StAXResult;
import java.util.*;

public class AStarSearch implements SearchAlgorithm {

	private Heuristics heuristics;
	private HashMap<Integer, State > myHashMap = new HashMap<Integer, State>();
	public AStarSearch(Heuristics h) {
		this.heuristics = h;
	}

	boolean foundSolution = false;
	boolean clean = false;
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
		frontierList = new ArrayList<Node>();
	
		//be careful here because we can't use max value at other places?
		Node currentNode = new Node(env.getCurrentState(), Integer.MAX_VALUE);
		expandNode(currentNode, env);
		//find best solution
		while (!foundSolution){
			// findBestNodeInFrontier(frontierList);
			
			// // ##############
			// //keep searching

			// //keep expanding nodes
			// Iterator<Node> fList = frontierList.iterator();
			// while(fList.hasNext()){
			// 	expandNode(fList.next(), env);
			// }
			// // if a state is already in the hash map (duplicate) then DONT expand that node

			// //to detect that the current solution is in a goal state, we can check if the state = home 
			// //and all dirt is cleaned and you are turned off
			// //we can also use the eval function, and check there if all dirt has been cleaned
			
			//1. find node to expand
			Node pathNode = findBestNodeInFrontier();
			//2. expand it
			expandNode(pathNode, env);
			
			if (pathNode.state.dirt.size() == 0 && !clean){
				clean = true;
				myHashMap = new HashMap<Integer, State>();
				frontierList = new ArrayList<Node>();
				frontierList.add(pathNode);
			}

			if (pathNode.state.position.equals(env.home) && clean){
				solutionNode = pathNode;
				foundSolution = true;
			}


			//3. check if maxFrontierSize is not larger, if so update
			
			// //chack lab 2 to see how states/nodes are stored in hashmap

			// //  #### Probably crap ####
			// // #################
			// TODO: If we've found a pathnode that has no dirt left -> solutionNode is found
		}
		
	}

	// private Node findBestNodeInFrontier(ArrayList<Node> frontierList){
	// 	// if frontireList is not empty or null then iterate through it and find best evaluation
	// 	if (!frontierList.isEmpty() && frontierList != null ){
	// 		Node cheapestNode = frontierList.get(0);
	// 		for (int i = 0; i < frontierList.size(); i++){
	// 			if (frontierList.get(i).evaluation < cheapestNode.evaluation){
	// 				cheapestNode = frontierList.get(i);
	// 			}
	// 		}
	// 		return cheapestNode;
	// 	}
		
	// 	return null;
	// }
	
	private Node findBestNodeInFrontier(){
		//check if null or empty for example
		//iterate through the frontier list and find best evaluation (lowest value)
		Node bestNode = null;
		int bestNodeVal = -1;
		int leastDirt = 0;
		Iterator<Node> fList = frontierList.iterator();

		while(fList.hasNext()){
			Node nextNode = fList.next();
			//catch for initial value
			if(bestNodeVal == -1){
				bestNodeVal = nextNode.evaluation;
				bestNode = nextNode;
				leastDirt = nextNode.state.dirt.size();
			}
			//evaluate node
			if(nextNode.evaluation < bestNodeVal && nextNode.state.dirt.size() < leastDirt){
				bestNodeVal = nextNode.evaluation;
				bestNode = nextNode;
			}
		}
		return bestNode;
	}

	private void expandNode(Node n, Environment env){
		// ##############
		// make clone of state

		State s = n.state;
		
		List<Action> legalMoves = env.legalMoves(n.state);
		
		for (int i = 0; i < legalMoves.size(); i++){
			// create and reset clone to current state
			State sClone = s.clone();
			Node newNode = null;
			//System.out.println("current legal move : " + legalMoves.get(i));

			if (legalMoves.get(i) == Action.GO){
				// get next location 
				sClone.position = sClone.facingPosition();

				// create a new node using the adjusted state clone
				newNode = new Node(n, sClone, Action.GO, heuristics.eval(sClone));
			}
			else if (legalMoves.get(i) == Action.TURN_LEFT){
				if (sClone.orientation == 0){
					sClone.orientation = 3;
				}
				else {
					sClone.orientation -= 1;
				}

				// create a new node using the adjusted state clone
				newNode = new Node(n, sClone, Action.TURN_LEFT, heuristics.eval(sClone));
			}
			else if (legalMoves.get(i) == Action.TURN_RIGHT){
				if (sClone.orientation == 3){
					sClone.orientation = 0;
				}
				else {
					sClone.orientation += 1;
				}

				// create a new node using the adjusted state clone
				newNode = new Node(n, sClone, Action.TURN_RIGHT, heuristics.eval(sClone));
			}
			else if (legalMoves.get(i) == Action.SUCK){
				sClone.dirt.remove(sClone.position);

				// create a new node using the adjusted state clone
				newNode = new Node(n, sClone, Action.SUCK, heuristics.eval(sClone));
				
			}
			else if (legalMoves.get(i) == Action.TURN_ON){
				// Should only happen at the first step
				sClone.turned_on = true;

				// create a new node using the adjusted state clone
				newNode = new Node(n, sClone, Action.TURN_ON, heuristics.eval(sClone));
			
			}
			else if (legalMoves.get(i) == Action.TURN_OFF){
				s.clone().turned_on = false;
				System.out.println("turns off.");
				newNode = new Node(n, sClone, Action.TURN_OFF, heuristics.eval(sClone));
			}
			// add the new node to frontierList, if not in hashmap
			if (!checkIfInHashMap(sClone) && newNode != null){
				addToHashMap(sClone);
				frontierList.add(newNode);
				nodeExpansionCount++;
			}
			else if (newNode == null){
				System.out.println("expandNode : newNode is null");
			}
			else if (checkIfInHashMap(sClone)){
				System.out.println("expandNode : state is already in hashmap");
			}
		}
		// remove the expanded node from the frontier list
		
		frontierList.remove(n);
		// ###############

		// // ##################

		// //check if null
		
		// //expand it for each move you can do
		// State s = n.state;
		// Node northNode;
		// Node eastNode;
		// Node southNode;
		// Node westNode;
		// //get position of neighbors, if not obstacles, create a node, add to frontier
		// //need to do checks to determine cost of movement from start orientation

		// //north
		// s.position.y += 1;
		// if(!env.obstacles.contains(s.position)){
		// 	State northClone = s.clone();
		// 	northNode = new Node(northClone, heuristics.eval(northClone));
		// 	northNode.parent = n;
		// 	frontierList.add(northNode);
		// }

		// //east
		// s.position.y -= 1;
		// s.position.x += 1;
		// if(!env.obstacles.contains(s.position)){
		// 	State eastClone = s.clone();
		// 	eastNode = new Node(eastClone, heuristics.eval(eastClone));
		// 	eastNode.parent = n;
		// 	frontierList.add(eastNode);
		// }

		// //south
		// s.position.x -= 1;
		// s.position.y -= 1;
		// if(!env.obstacles.contains(s.position)){
		// 	State southClone = s.clone();
		// 	southNode = new Node(southClone, heuristics.eval(southClone));
		// 	southNode.parent = n;
		// 	frontierList.add(southNode);
		// }

		// //west
		// s.position.y += 1;
		// s.position.x -= 1;
		// if(!env.obstacles.contains(s.position)){
		// 	State westClone = s.clone();
		// 	westNode = new Node(westClone, heuristics.eval(westClone));
		// 	westNode.parent = n;
		// 	frontierList.add(westNode);
		// }

		// //check if the new state already exists
		// checkIfStateExistsIfSoAddIt(n.state);

		// //update the frontier, both remove current node and add the new ones
		// frontierList.remove(n);
		// // #################
	}

	private boolean checkIfInHashMap(State s) {
		//check if we already have this state inside the hash map
		if (!myHashMap.containsKey(s.hashCode())){
			return false;
		}
		return true;
	}

	private void addToHashMap(State s){
		myHashMap.put(s.hashCode(), s);
	}


	@Override
	public List<Action> getPlan() {
		// TODO Auto-generated method stub (done)
		// if we are in a solution node we just need to return thatnode.getPlan()
		System.out.println("inside getPlan()");
		System.out.println("solutionNode: " + solutionNode);
		List<Action> toRet = solutionNode.getPlan();
		toRet.add(Action.TURN_OFF);


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
