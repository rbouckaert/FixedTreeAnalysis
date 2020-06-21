package beast.evolution.sitemodel;

import beast.core.Description;
import beast.core.Input.Validate;

@Description("Site clock model, useful for fixed tree analysis")
public class FixedSiteModel extends SiteModel {

	public FixedSiteModel() {
		substModelInput.setRule(Validate.OPTIONAL);
	}
}
