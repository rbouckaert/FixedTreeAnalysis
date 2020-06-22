package beast.evolution.tree;

import beast.core.Description;
import beast.evolution.speciation.SpeciesTreeDistribution;

@Description("Dummy/uniform tree prior, useful for a fixed tree analysis")
public class FixedTreeDistribution extends SpeciesTreeDistribution {

	@Override
	public double calculateTreeLogLikelihood(TreeInterface tree) {
		return 0;
	}
	
	@Override
	public boolean canHandleTipDates() {
		return false;
	}

}
