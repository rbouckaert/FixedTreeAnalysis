package fixedtreeanalysis.app.beauti;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//import javax.swing.BoxLayout;
//import javax.swing.Icon;
//import javax.swing.ImageIcon;
//import javax.swing.JDialog;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.border.EmptyBorder;

import beastfx.app.inputeditor.BeautiDoc;
import beastfx.app.inputeditor.BooleanInputEditor;
import beastfx.app.inputeditor.InputEditor.ExpandOption;
import beastfx.app.inputeditor.InputEditorFactory;
import beastfx.app.tools.ModelBuilder;
import beastfx.app.util.Alert;
import beastfx.app.util.FXUtils;
import fixedtreeanalysis.evolution.likelihood.FixedTreeLikelihood;
import fixedtreeanalysis.evolution.tree.FixedTree;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;
import beastfx.app.inputeditor.StringInputEditor;
import beast.base.core.BEASTInterface;
import beast.base.core.BEASTObject;
import beast.base.core.Description;
import beast.base.core.Input;
import beast.base.inference.State;
import beast.base.evolution.alignment.Alignment;
import beast.base.evolution.alignment.Sequence;
import beast.base.evolution.tree.Tree;
import beast.pkgmgmt.BEASTClassLoader;
import beast.base.parser.NexusParser;
import beast.base.evolution.tree.TreeParser;

@Description("Alignment provider for a fixed tree analysis")
public class BeautiFixedTreeAlignmentProvider extends BeautiFixedAlignmentProvider {

	@Override
	public List<BEASTInterface> getAlignments(BeautiDoc doc) {
		try {
            File file = FXUtils.getLoadFile("Open tree file with fixed tree");
            if (file != null) {
            	NexusParser parser = new NexusParser();
            	parser.parseFile(file);
            	if (parser.trees == null || parser.trees.size() == 0) {
            		Alert.showMessageDialog(null, "Did not find any tree in the file -- giving up.");
            		return null;
            	}
            	if (parser.trees.size() > 1) {
            		Alert.showMessageDialog(null, "Found more than one tree in the file -- expected only 1!");
            		return null;
            	}
            	
            	Tree tree = parser.trees.get(0);
            	return processTree(file, doc, tree);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	Alert.showMessageDialog(null, "Something went wrong: " + e.getMessage());
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
	public int matches(Alignment alignment) {
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
	public void editAlignment(Alignment alignment, BeautiDoc doc) {		
		FixedTreeLikelihood likelihood = null;
		for (BEASTInterface output : alignment.getOutputs()) {
			if (output instanceof FixedTreeLikelihood) {
				likelihood = (FixedTreeLikelihood) output;
				FixedTree tree = (FixedTree) likelihood.treeInput.get();
				TreeParser parser = (TreeParser) likelihood.treeInput.get();
				
				//Object result = JOptionPane.showInputDialog("Edit Newick tree:", parser.newickInput.get());
				
				//JPanel panel  = new JPanel();
				//panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				StringInputEditor newick = new StringInputEditor(doc);
				StringContainer c = new StringContainer(parser.newickInput.get());
				c.setID("newick");
				newick.init(c.stringInput, c, -1, ExpandOption.FALSE, true);
				VBox panel = FXUtils.newVBox();
				panel.getChildren().add(newick);
				
				boolean originalMode = tree.modeInput.get(); 
				BooleanInputEditor mode = new BooleanInputEditor(doc);
				mode.init(tree.modeInput, tree, -1, ExpandOption.FALSE, false);
				panel.getChildren().add(mode);
				
				
				Dialog dlg = new Dialog();
				DialogPane pane = new DialogPane();
				pane.setContent(panel);
				pane.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
				dlg.setDialogPane(pane);
				dlg.setTitle("Discrete trait editor");
		    	pane.setId("DiscreteTraitEditor");
		        dlg.setResizable(true);
		        Optional<?> value = dlg.showAndWait();

				
//		        URL url = BEASTClassLoader.classLoader.getResource(ModelBuilder.ICONPATH + "beast.png");
//		        Icon icon = new ImageIcon(url);
//		        JOptionPane optionPane = new JOptionPane(panel,
//		                JOptionPane.PLAIN_MESSAGE,
//		                JOptionPane.DEFAULT_OPTION,
//		                icon,
//		                null,
//		                null);
//		        optionPane.setBorder(new EmptyBorder(12, 12, 12, 12));
//
//		        Frame frame = (doc != null ? doc.getFrame(): Frame.getFrames()[0]);
//		        final JDialog dialog = optionPane.createDialog(frame, "Fixed tree properties");
//		        dialog.pack();
//
//		        dialog.setVisible(true);
//		        Integer value = (Integer) optionPane.getValue();
		        if (value.toString().toLowerCase().contains("ok")) {
		        	parser.newickInput.setValue(c.stringInput.get(), parser);		        	
		        	tree.modeInput.set(originalMode); 
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
    	
    	return processTree(files[0], doc, tree);
    }

}
