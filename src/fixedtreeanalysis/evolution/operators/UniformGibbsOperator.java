package fixedtreeanalysis.evolution.operators;


import beast.base.core.Description;
import beast.base.inference.operator.UniformOperator;

@Description("Uniform operator that is always accepted")
public class UniformGibbsOperator extends UniformOperator {

	
	@Override
	public double proposal() {
		super.proposal();
		return Double.POSITIVE_INFINITY;
	}
}
