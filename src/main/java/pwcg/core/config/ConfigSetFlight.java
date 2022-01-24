package pwcg.core.config;

public class ConfigSetFlight
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetFlight);

		configSet.addConfigItem(ConfigItemKeys.BaseAltPeriod1Key, new ConfigItem("3000"));
		configSet.addConfigItem(ConfigItemKeys.BaseAltPeriod2Key, new ConfigItem("4000"));
        configSet.addConfigItem(ConfigItemKeys.BaseAltPeriod3Key, new ConfigItem("5000"));
        configSet.addConfigItem(ConfigItemKeys.BaseAltPeriod4Key, new ConfigItem("5000"));
        configSet.addConfigItem(ConfigItemKeys.BaseAltPeriod5Key, new ConfigItem("5000"));
        configSet.addConfigItem(ConfigItemKeys.BaseAltPeriod6Key, new ConfigItem("5000"));
        
        return configSet;
	}
}
