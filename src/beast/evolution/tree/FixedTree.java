package beast.evolution.tree;

import beast.core.BEASTInterface;
import beast.core.Description;
import beast.core.Operator;
import beast.core.util.Log;
import beast.util.TreeParser;

@Description("Tree that operator proposals leave unchanged")
public class FixedTree extends TreeParser implements ModedTree {

	mode _mode = mode.initial;
	public mode getMode() {return _mode;}
	
	@Override
	public void initAndValidate() {
		super.initAndValidate();
		_mode = mode.fixed;
		
		for (BEASTInterface o : getOutputs()) {
			if (o instanceof Operator && ((Operator)o).getWeight() > 0) {
				Log.warning("WARNING: tree operator " + o.getID() + " has no effect on fixed tree -- consider removing (or setting weight to 0)");
			}
		}
	}
	
	@Override
	protected Node newNode() {
		return new FixedNode();
	}
	
	@Override
	public void setRoot(Node root) {
		if (_mode == mode.initial) {
			super.setRoot(root);
		}
	}
	
}
