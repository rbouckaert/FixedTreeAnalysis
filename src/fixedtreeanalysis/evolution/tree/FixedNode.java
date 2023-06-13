package beast.evolution.tree;

import java.util.TreeMap;

import beast.base.core.Description;
import beast.base.evolution.tree.Node;

@Description("Node in a fixed tree: it will not allow editing once initialised")
public class FixedNode extends Node {

	private FixedTree.mode getMode() {
		ModedTree tree = (ModedTree) getTree();
		if (tree == null) {
			return ModedTree.mode.initial;
		}
		return tree.getMode();
	}
	
	@Override
	public void setHeight(final double height) {
		FixedTree.mode mode_ = getMode();
    	if (mode_ == ModedTree.mode.initial || mode_ == ModedTree.mode.topology) {
    		super.setHeight(height);
    	}
    }
	
	@Override
    public void setHeightDA(final double height) {
		FixedTree.mode mode_ = getMode();
    	if (mode_ == ModedTree.mode.initial || mode_ == ModedTree.mode.topology) {
    		super.setHeightDA(height);
    	}
    }

	
	@Override
    public void setParent(final Node parent) {
    	if (getMode() == ModedTree.mode.initial) {
    		super.setParent(parent, true);
    	}
    }

	
	@Override
    public void setParent(final Node parent, final boolean inOperator) {
    	if (getMode() == ModedTree.mode.initial) {
    		super.setParent(parent, inOperator);
    	}
	}
		
//	@Override
//	void setParentImmediate(final Node parent) {
//    	if (getMode() == ModedTree.mode.initial) {
//    		super.setParentImmediate(parent);
//    	}
//	}
	
	@Override
    public void removeChild(final Node child) {
    	if (getMode() == ModedTree.mode.initial) {
    		super.removeChild(child);
    	}
    }

	@Override
    public void removeAllChildren(final boolean inOperator) {
    	if (getMode() == ModedTree.mode.initial) {
    		super.removeAllChildren(inOperator);
    	}
    }

	@Override
    public void addChild(final Node child) {
    	if (getMode() == ModedTree.mode.initial) {
    		super.addChild(child);
    	}
    }

	
	@Override
    public int scale(final double scale) {
    	if (getMode() == ModedTree.mode.initial) {
    		return super.scale(scale);
    	}
    	return 0;
	}
	
	
	
	@Override
    public void setChild(final int childIndex, final Node node) {
    	if (getMode() == ModedTree.mode.initial) {
    		super.setChild(childIndex, node);
    	}
    }

    @Deprecated
    public void setLeft(final Node leftChild) {
    	if (getMode() == ModedTree.mode.initial) {
    		super.setLeft(leftChild);
    	}
    }

    @Deprecated
    public void setRight(final Node rightChild) {
    	if (getMode() == ModedTree.mode.initial) {
    		super.setRight(rightChild);
    	}
    }
	
	@Override
    public Node copy() {
        final Node node = new FixedNode();
        node.setHeight(height);
        node.setNr(labelNr);
        node.metaDataString = metaDataString;
        node.lengthMetaDataString = lengthMetaDataString;
        // node.setMetaData = new TreeMap<>(metaData);
        // node.lengthMetaData = new TreeMap<>(lengthMetaData);
        for (String key : lengthMetaData.keySet()) {
        	node.setLengthMetaData(key, lengthMetaData.get(key));
        }
        node.setParent(null);
        node.setID(getID());

        for (final Node child : getChildren()) {
            node.addChild(child.copy());
        }
        return node;
    } // copy

}
