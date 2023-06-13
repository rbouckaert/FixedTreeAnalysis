package beast.evolution.tree;

import beast.base.core.BEASTInterface;
import beast.base.core.Description;
import beast.base.core.Input;
import beast.base.inference.Operator;
import beast.base.core.Log;
import beast.base.evolution.tree.Node;
import beast.base.evolution.tree.TreeParser;

@Description("Tree that operator proposals leave unchanged")
public class FixedTree extends TreeParser implements ModedTree {
	final public Input<Boolean> modeInput = new Input<>("allowNodeHeightChanges", "if true, only topology is fixed and node heights changes are allowed, if false the whole tree is  fixed", false);
	

	mode _mode = mode.initial;
	public mode getMode() {return _mode;}
	
	@Override
	public void initAndValidate() {
		super.initAndValidate();
		if (modeInput.get()) {
			_mode = mode.topology;
		} else {
			_mode = mode.fixed;
		}
		
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
