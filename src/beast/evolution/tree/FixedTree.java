package beast.evolution.tree;

import beast.core.Description;
import beast.util.TreeParser;

@Description("Tree that operator proposals leave unchanged")
public class FixedTree extends TreeParser {
	enum mode {initial,fixed}

	mode _mode = mode.initial;
	
	
	@Override
	public void initAndValidate() {
		super.initAndValidate();
		_mode = mode.fixed;
	}
	
	@Override
	protected Node newNode() {
		return new FixedNode();
	}
	
}
