package fixedtreeanalysis.app.beauti;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import beast.base.core.BEASTInterface;
import beast.base.core.Description;
import beast.base.inference.State;
import beast.base.inference.parameter.IntegerParameter;
import beast.base.evolution.alignment.Alignment;
import beast.base.evolution.alignment.Sequence;
import beast.base.evolution.alignment.Taxon;
import beast.base.evolution.alignment.TaxonSet;
import beastfx.app.inputeditor.BeautiDoc;
import beastfx.app.util.FXUtils;
import fixedtreeanalysis.evolution.likelihood.FixedTreeLikelihood;
import fixedtreeanalysis.evolution.tree.IndexedTreeFromSet;
import beast.base.evolution.tree.Tree;
import beast.base.parser.NexusParser;

@Description("Alignment provider for a tree set analysis")
public class BeautiTreeSetAlignmentProvider extends BeautiFixedAlignmentProvider {

	@Override
	public List<BEASTInterface> getAlignments(BeautiDoc doc) {
		try {
            File file = FXUtils.getLoadFile("Open tree file with tree set");
            if (file != null) {
            	NexusParser parser = new NexusParser();
            	parser.parseFile(file);
            	if (parser.trees == null || parser.trees.size() == 0) {
            		JOptionPane.showMessageDialog(null, "Did not find any tree in the file -- giving up.");
            		return null;
            	}
            	if (parser.trees.size() == 1) {
            		JOptionPane.showMessageDialog(null, "Found only one tree in the file -- expected more than 1!");
            		return null;
            	}
            	
            	Tree tree = parser.trees.get(0);
            	return processTree(file, doc, tree, parser.trees.size());
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	JOptionPane.showMessageDialog(null, "Something went wrong: " + e.getMessage());
        }
		return null;
	}

	private List<BEASTInterface> processTree(File file, BeautiDoc doc, Tree tree, int treeSetSize) {
    	// create dummy alignment
    	Alignment data = new Alignment();
    	List<Sequence> seqs = new ArrayList<>();
    	for (String name : tree.getTaxaNames()) {
    		seqs.add(new Sequence(name,"?"));
    	}
    	data.initByName("sequence", seqs);
    	String id = file.getName();
    	if (id.lastIndexOf('.') > -1) {
    		id = id.substring(0, id.lastIndexOf('.'));
    	}
    	data.setID(id);

    	Object index = doc.pluginmap.get("treeIndex.t:tree");
    	((IntegerParameter) index).setUpper(treeSetSize);
    	TaxonSet taxonset = new TaxonSet();
    	taxonset.setID("taxonset." + id);
    	for (String name : tree.getTaxaNames()) {
    		taxonset.taxonsetInput.get().add(new Taxon(name));
    	}
    	//taxonset.initByName("alignment", data);
    	taxonset.initAndValidate();
    	
    	IndexedTreeFromSet treeSet = new IndexedTreeFromSet();
    	treeSet.initByName("treeSetFile", file.getPath(),
    			"burnin", 10,
    			"taxonset", taxonset,
    			"index", index,
    			"estimate", true
    			);
    	treeSet.setID("Tree.t:tree");
    	doc.registerPlugin(treeSet);
    	
    	// park the tree in the state
        State state = (State) doc.pluginmap.get("state");
        state.stateNodeInput.get().add(treeSet);
    
    	List<BEASTInterface> list = new ArrayList<>();
    	list.add(data);
    	addAlignments(doc, list);
    	return list;
    }

	@Override
	public int matches(Alignment alignment) {
		for (BEASTInterface output : alignment.getOutputs()) {
			if (output instanceof FixedTreeLikelihood) {
				FixedTreeLikelihood likelihood = (FixedTreeLikelihood) output;
				if (likelihood.treeInput.get() instanceof IndexedTreeFromSet) {
					return 11;
				}
			}
		}
		return 0;
	}
	
	@Override
	public void editAlignment(Alignment alignment, BeautiDoc doc) {		
		FixedTreeLikelihood likelihood = null;
		for (BEASTInterface output : alignment.getOutputs()) {
			if (output instanceof FixedTreeLikelihood) {
				likelihood = (FixedTreeLikelihood) output;
				IndexedTreeFromSet tree = (IndexedTreeFromSet) likelihood.treeInput.get();
				
				Object result = JOptionPane.showInputDialog("Tree set file:", tree.treeSetFileInput.get());
				if (result != null) {
					tree.treeSetFileInput.setValue(result, tree);
				}
				return;
			}
		}
	}

	@Override
    public List<BEASTInterface> getAlignments(BeautiDoc doc, File[] files, String [] args) {
		if (files.length != 1) {
			throw new IllegalArgumentException("Expected exactly 1 file");
		}
    	NexusParser parser = new NexusParser();
    	try {
			parser.parseFile(files[0]);
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
    	if (parser.trees == null || parser.trees.size() == 0) {
    		throw new IllegalArgumentException("Did not find any tree in the file -- giving up.");
    	}
    	if (parser.trees.size() > 1) {
    		throw new IllegalArgumentException("Found more than one tree in the file -- expected only 1!");
    	}
    	Tree tree = parser.trees.get(0);
    	return processTree(files[0], doc, tree, parser.trees.size());
    }

}
