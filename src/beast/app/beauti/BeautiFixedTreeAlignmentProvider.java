package beast.app.beauti;

import java.awt.Frame;
import java.awt.LayoutManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import beast.app.draw.BooleanInputEditor;
import beast.app.draw.InputEditor.ExpandOption;
import beast.app.draw.InputEditorFactory;
import beast.app.draw.ModelBuilder;
import beast.app.draw.StringInputEditor;
import beast.app.util.Utils;
import beast.core.BEASTInterface;
import beast.core.BEASTObject;
import beast.core.Description;
import beast.core.Input;
import beast.core.State;
import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.Sequence;
import beast.evolution.likelihood.FixedTreeLikelihood;
import beast.evolution.tree.FixedTree;
import beast.evolution.tree.Tree;
import beast.util.BEASTClassLoader;
import beast.util.NexusParser;
import beast.util.TreeParser;

@Description("Alignment provider for a fixed tree analysis")
public class BeautiFixedTreeAlignmentProvider extends BeautiFixedAlignmentProvider {

	@Override
	public List<BEASTInterface> getAlignments(BeautiDoc doc) {
		try {
            File file = Utils.getLoadFile("Open tree file with fixed tree");
            if (file != null) {
            	NexusParser parser = new NexusParser();
            	parser.parseFile(file);
            	if (parser.trees == null || parser.trees.size() == 0) {
            		JOptionPane.showMessageDialog(null, "Did not find any tree in the file -- giving up.");
            		return null;
            	}
            	if (parser.trees.size() > 1) {
            		JOptionPane.showMessageDialog(null, "Found more than one tree in the file -- expected only 1!");
            		return null;
            	}
            	
            	Tree tree = parser.trees.get(0);
            	return processTree(file, doc, tree);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	JOptionPane.showMessageDialog(null, "Something went wrong: " + e.getMessage());
        }
		return null;
	}

	private List<BEASTInterface> processTree(File file, BeautiDoc doc, Tree tree) {
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

    	FixedTree treeParser = new FixedTree();
    	treeParser.initByName("newick", tree.getRoot().toNewick(),
    			"IsLabelledNewick", true,
    			"taxa", data,
    			"adjustTipHeights", false,
    			"estimate", true
    			);
    	treeParser.setID("Tree.t:tree");
    	doc.registerPlugin(treeParser);
    	
    	// park the tree in the state
        State state = (State) doc.pluginmap.get("state");
        state.stateNodeInput.get().add(treeParser);
    
    	List<BEASTInterface> list = new ArrayList<>();
    	list.add(data);
    	addAlignments(doc, list);
    	return list;
    }

	@Override
	protected int matches(Alignment alignment) {
		for (BEASTInterface output : alignment.getOutputs()) {
			if (output instanceof FixedTreeLikelihood) {
				return 10;
			}
		}
		return 0;
	}
	
	public class StringContainer extends BEASTObject {
		final public Input<String> stringInput = new Input<>("Newick tree:", "");

		public StringContainer(String str) {
			stringInput.setType(String.class);
			stringInput.set(str);
		}
		
		@Override
		public void initAndValidate() {
		}
	}
	
	@Override
	void editAlignment(Alignment alignment, BeautiDoc doc) {		
		FixedTreeLikelihood likelihood = null;
		for (BEASTInterface output : alignment.getOutputs()) {
			if (output instanceof FixedTreeLikelihood) {
				likelihood = (FixedTreeLikelihood) output;
				FixedTree tree = (FixedTree) likelihood.treeInput.get();
				TreeParser parser = (TreeParser) likelihood.treeInput.get();
				
				//Object result = JOptionPane.showInputDialog("Edit Newick tree:", parser.newickInput.get());
				
				JPanel panel  = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				StringInputEditor newick = new StringInputEditor(doc);
				StringContainer c = new StringContainer(parser.newickInput.get());
				newick.init(c.stringInput, c, -1, ExpandOption.FALSE, true);
				panel.add(newick);
				
				boolean originalMode = tree.modeInput.get(); 
				BooleanInputEditor mode = new BooleanInputEditor(doc);
				mode.init(tree.modeInput, tree, -1, ExpandOption.FALSE, false);
				panel.add(mode);
				
				
				
		        URL url = BEASTClassLoader.classLoader.getResource(ModelBuilder.ICONPATH + "beast.png");
		        Icon icon = new ImageIcon(url);
		        JOptionPane optionPane = new JOptionPane(panel,
		                JOptionPane.PLAIN_MESSAGE,
		                JOptionPane.DEFAULT_OPTION,
		                icon,
		                null,
		                null);
		        optionPane.setBorder(new EmptyBorder(12, 12, 12, 12));

		        Frame frame = (doc != null ? doc.getFrame(): Frame.getFrames()[0]);
		        final JDialog dialog = optionPane.createDialog(frame, "Fixed tree properties");
		        dialog.pack();

		        dialog.setVisible(true);
		        Integer value = (Integer) optionPane.getValue();
		        if (value != null && value != -1 && value != JOptionPane.CANCEL_OPTION) {
		        	parser.newickInput.setValue(c.stringInput.get(), parser);		        	
		        } else {
		        	tree.modeInput.set(originalMode); 
		        }

				
//				Object result = JOptionPane.showMessageDialog(panel);
//				if (result != null) {
//					parser.newickInput.setValue(result, parser);
//				}
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
    	
    	return processTree(files[0], doc, tree);
    }

}
