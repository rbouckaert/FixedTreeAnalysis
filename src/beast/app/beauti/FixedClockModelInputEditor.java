package beast.app.beauti;

import javax.swing.Box;

import beast.app.draw.InputEditor;
import beast.core.BEASTInterface;
import beast.core.Input;
import beast.evolution.branchratemodel.FixedClockModel;

public class FixedClockModelInputEditor extends InputEditor.Base {

	private static final long serialVersionUID = 1L;

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
        addInputLabel("Fixed tree model. Do not change.", "<html>Fixed tree model.<br>Do not change.</html>");
        
        m_bAddButtons = addButtons;

        //add(m_entry);
        add(Box.createHorizontalGlue());
        addValidationLabel();
	}

}
