import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class AStarSearch implements SearchAlgorithm {

	private Heuristics heuristics;
	private HashMap<Integer, State > myHashMap;
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
			
			// expandNode(currentNode, env);

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
			//3. check if maxFrontierSize is not larger, if so update

			// //chack lab 2 to see how states/nodes are stored in hashmap

			// //  #### Probably crap ####
			// // #################
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
		Iterator<Node> fList = frontierList.iterator();

		while(fList.hasNext()){
			Node nextNode = fList.next();
			//catch for initial value
			if(bestNodeVal == -1){
				bestNodeVal = nextNode.evaluation;
				bestNode = nextNode;
			}
			//evaluate node
			if(nextNode.evaluation < bestNodeVal){
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
		Node newNode = null;

		for (int i = 0; i < legalMoves.size(); i++){
			// reset clone to current state
			State sClone = s.clone();
			

			if (legalMoves.get(i) == Action.GO){
				// get next location 
				sClone.position = sClone.facingPosition();

				// clone the next state with the correct coordinates
				newNode = new Node(n, sClone, Action.GO, heuristics.eval(sClone));
			
				// add node to frontierList, if not in hashmap
				if (!checkStateHashAndAdd(sClone)){
					frontierList.add(newNode);
				}
			}

			else if (legalMoves.get(i) == Action.TURN_LEFT){
				if (sClone.orientation == 0){
					sClone.orientation = 3;
				}
				else {
					sClone.orientation -= 1;
				}

				newNode = new Node(n, sClone, Action.TURN_LEFT, heuristics.eval(sClone));
				
				if (!checkStateHashAndAdd(sClone)){
					frontierList.add(newNode);
				}	
			}

			else if (legalMoves.get(i) == Action.TURN_RIGHT){
				if (sClone.orientation == 3){
					sClone.orientation = 0;
				}
				else {
					sClone.orientation += 1;
				}

				newNode = new Node(n, sClone, Action.TURN_RIGHT, heuristics.eval(sClone));
				
				if (!checkStateHashAndAdd(sClone)){
					frontierList.add(newNode);
				}
			}

			else if (legalMoves.get(i) == Action.SUCK){
				sClone.dirt.remove(sClone.position);

				newNode = new Node(n, sClone, Action.SUCK, heuristics.eval(sClone));
				
			}	
			
			if (!checkStateHashAndAdd(sClone) && newNode != null){
				frontierList.add(newNode);
			}
			else if (newNode == null){
				System.out.println("expandNode : newNode is null");
			}
			else if (checkStateHashAndAdd(sClone)){
				System.out.println("expandNode : state is already in hashmap");
			}
		}
		//update the frontier, both remove current node and add the new ones
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

	private boolean checkStateHashAndAdd(State s) {
		//check if we already have this state inside the hash map

		if (!myHashMap.containsKey(s.hashCode())){
			//if not then add it
			myHashMap.put(s.hashCode(), s);

			return false;
		}
		return true;
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
