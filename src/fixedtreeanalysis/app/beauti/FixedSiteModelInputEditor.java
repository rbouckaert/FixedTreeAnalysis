package fixedtreeanalysis.app.beauti;

import beastfx.app.inputeditor.BeautiDoc;
import fixedtreeanalysis.evolution.sitemodel.FixedSiteModel;

public class FixedSiteModelInputEditor extends FixedClockModelInputEditor {

	public FixedSiteModelInputEditor(BeautiDoc doc) {
		super(doc);
	}

	@Override
	public Class<?> type() {
		return FixedSiteModel.class;
	}

}
