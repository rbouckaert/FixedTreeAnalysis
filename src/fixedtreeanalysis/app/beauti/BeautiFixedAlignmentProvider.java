package fixedtreeanalysis.app.beauti;






import java.util.List;

import javax.swing.JOptionPane;

import beast.base.core.BEASTInterface;
import beast.base.core.Description;
import beastfx.app.inputeditor.BeautiAlignmentProvider;
import beastfx.app.inputeditor.BeautiDoc;


@Description("Class for creating new alignments to be edited by AlignmentListInputEditor")
public class BeautiFixedAlignmentProvider extends BeautiAlignmentProvider {
	
	@Override
	public List<BEASTInterface> getAlignments(BeautiDoc doc) {
		if (doc.pluginmap.containsKey("Tree.t:tree")) {
			return super.getAlignments(doc);
		}
		JOptionPane.showMessageDialog(null, "Could not find a fixed tree: select menu File/Import Fixed Tree before adding alignments");
		return null;
	}
}
