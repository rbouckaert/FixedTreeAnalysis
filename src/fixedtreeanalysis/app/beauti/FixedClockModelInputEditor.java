package fixedtreeanalysis.app.beauti;

import javax.swing.Box;

import beastfx.app.inputeditor.BeautiDoc;
import beastfx.app.inputeditor.InputEditor;
import fixedtreeanalysis.evolution.branchratemodel.FixedClockModel;
import beast.base.core.BEASTInterface;
import beast.base.core.Input;

public class FixedClockModelInputEditor extends InputEditor.Base {


	public FixedClockModelInputEditor(BeautiDoc doc) {
		super(doc);
	}

	@Override
	public Class<?> type() {
		return FixedClockModel.class;
	}
	
	@Override
	public void init(Input<?> input, BEASTInterface beastObject, int itemNr, ExpandOption isExpandOption,
			boolean addButtons) {
//		super.init(input, beastObject, itemNr, isExpandOption, addButtons);
        m_input = input;
        m_beastObject = beastObject;
        this.itemNr= itemNr;
        
        m_bAddButtons = true;
        addInputLabel("Fixed tree model partition --", "<html>Fixed tree model.<br>There are no parameters to change.</html>");
        addInputLabel("No parameter to change", "<html>Fixed tree model.<br>There are no parameters to change.</html>");
        
        m_bAddButtons = addButtons;

        //add(m_entry);
        // add(Box.createHorizontalGlue());
        addValidationLabel();
	}

}
