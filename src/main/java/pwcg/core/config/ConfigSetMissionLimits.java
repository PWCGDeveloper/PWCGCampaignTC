package pwcg.core.config;

public class ConfigSetMissionLimits
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetMissionLimits);

        configSet.addConfigItem(ConfigItemKeys.MissionBoxSizeKey, new ConfigItem("30000"));
        configSet.addConfigItem(ConfigItemKeys.MissionBoxMaxDistanceFromBaseKey, new ConfigItem("40"));
				
        configSet.addConfigItem(ConfigItemKeys.AlliedPlatoonsInMissionKey, new ConfigItem("6"));
        configSet.addConfigItem(ConfigItemKeys.AxisPlatoonsInMissionKey, new ConfigItem("3"));

        configSet.addConfigItem(ConfigItemKeys.MaxSmokeInMissionKey, new ConfigItem("100"));
        configSet.addConfigItem(ConfigItemKeys.MaxSmokeInAreaKey, new ConfigItem("3"));
        configSet.addConfigItem(ConfigItemKeys.GroundUnitSpawnDistanceKey, new ConfigItem("10000"));

        return configSet;
	}
}
