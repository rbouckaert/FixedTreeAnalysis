package beast.evolution.operators;

import beast.core.Description;
import beast.evolution.operators.UniformOperator;

@Description("Uniform operator that is always accepted")
public class UniformGibbsOperator extends UniformOperator {

	
	@Override
	public double proposal() {
		super.proposal();
		return Double.POSITIVE_INFINITY;
	}
}
