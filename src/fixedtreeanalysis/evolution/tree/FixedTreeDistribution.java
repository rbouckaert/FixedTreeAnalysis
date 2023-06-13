package fixedtreeanalysis.evolution.tree;

import beast.base.core.Description;
import beast.base.evolution.speciation.SpeciesTreeDistribution;
import beast.base.evolution.tree.TreeInterface;

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
