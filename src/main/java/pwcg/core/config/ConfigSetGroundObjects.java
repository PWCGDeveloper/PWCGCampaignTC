package pwcg.core.config;

public class ConfigSetGroundObjects
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetGroundObjects);

		configSet.addConfigItem(ConfigItemKeys.KeepGroupSpreadKey, new ConfigItem("50000"));			// Distance from waypoint box to keep a group (city, new ConfigItem("large airfield)
		configSet.addConfigItem(ConfigItemKeys.AirfieldInclusionRadiusKey, new ConfigItem("50000")); // Distance from which to draw flights from
		return configSet;
	}
}
