package fixedtreeanalysis.evolution.sitemodel;

import beast.base.core.Description;
import beast.base.core.Input.Validate;
import beast.base.evolution.sitemodel.SiteModel;

@Description("Site clock model, useful for fixed tree analysis")
public class FixedSiteModel extends SiteModel {

	public FixedSiteModel() {
		substModelInput.setRule(Validate.OPTIONAL);
	}
}
