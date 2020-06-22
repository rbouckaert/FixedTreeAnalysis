package beast.evolution.tree;

import beast.evolution.tree.FixedTree.mode;

public class FixedNode extends Node {

	private FixedTree.mode getMode() {
		FixedTree tree = (FixedTree) getTree();
		if (tree == null) {
			return FixedTree.mode.initial;
		}
		return tree._mode;
	}
	
	@Override
	public void setHeight(final double height) {
    	if (getMode() == mode.initial) {
    		super.setHeight(height);
    	}
    }
	
	@Override
    public void setHeightDA(final double height) {
    	if (getMode() == mode.initial) {
    		super.setHeightDA(height);
    	}
    }

	
	@Override
    public void setParent(final Node parent) {
    	if (getMode() == mode.initial) {
    		super.setParent(parent, true);
    	}
    }

	
	@Override
    protected void setParent(final Node parent, final boolean inOperator) {
    	if (getMode() == mode.initial) {
    		super.setParent(parent, inOperator);
    	}
	}
		
	@Override
	void setParentImmediate(final Node parent) {
    	if (getMode() == mode.initial) {
    		super.setParentImmediate(parent);
    	}
	}
	
	@Override
    public void removeChild(final Node child) {
    	if (getMode() == mode.initial) {
    		super.removeChild(child);
    	}
    }

	@Override
    public void removeAllChildren(final boolean inOperator) {
    	if (getMode() == mode.initial) {
    		super.removeAllChildren(inOperator);
    	}
    }

	@Override
    public void addChild(final Node child) {
    	if (getMode() == mode.initial) {
    		super.addChild(child);
    	}
    }

	
	@Override
    public int scale(final double scale) {
    	if (getMode() == mode.initial) {
    		return super.scale(scale);
    	}
    	return 0;
	}
	
	
	
	@Override
    public void setChild(final int childIndex, final Node node) {
    	if (getMode() == mode.initial) {
    		super.setChild(childIndex, node);
    	}
    }

    @Deprecated
    public void setLeft(final Node leftChild) {
    	if (getMode() == mode.initial) {
    		super.setLeft(leftChild);
    	}
    }

    @Deprecated
    public void setRight(final Node rightChild) {
    	if (getMode() == mode.initial) {
    		super.setRight(rightChild);
    	}
    }
	
}