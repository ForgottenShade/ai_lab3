import java.util.Collection;
import java.util.List;

public class VaccuumCleanerAgent implements Agent {
	
	// the model of the environment 
	private Environment env;
	
	// the plan to execute
	protected List<Action> plan; 

	// the search algorithm to use
	private SearchAlgorithm searchAlgorithm;

	public VaccuumCleanerAgent(SearchAlgorithm searchAlgorithm) {
		this.searchAlgorithm = searchAlgorithm;
	}

	/**
	 * init(Collection<String> percepts) is called once before you have to select the first action.
	 * It initializes the environment model, and runs the search to find a plan. The plan is than executed step by step in nextAction.
	 */
    public void init(Collection<String> percepts) {
    	env = new Environment(percepts);
		long startTime = System.currentTimeMillis();

		// do the planning and remember the plan
		searchAlgorithm.doSearch(env);
		plan = searchAlgorithm.getPlan();
		
		long endTime = System.currentTimeMillis();
		System.out.println("planning took " + (endTime-startTime)/1000.0 + "s and " + searchAlgorithm.getNbNodeExpansions() + " node expansions");
		System.out.println("maximal frontier size was " + searchAlgorithm.getMaxFrontierSize() + " nodes");
		System.out.println("plan has cost " + searchAlgorithm.getPlanCost());
    }

    public String nextAction(Collection<String> percepts) {
    	// execute actions from the plan 1 after the other
		Action a = plan.remove(0);
		System.out.println("executing " + a);
		return a.name();
	}
}
