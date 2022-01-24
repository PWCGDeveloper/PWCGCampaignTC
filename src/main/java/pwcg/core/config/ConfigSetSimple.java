package pwcg.core.config;

public class ConfigSetSimple
{
	public static ConfigSet initialize()
	{
		ConfigSet configSet = new ConfigSet();
		configSet.setConfigSetName(ConfigSetKeys.ConfigSetSimple);
		
        configSet.addConfigItem(ConfigItemKeys.SimpleConfigGroundKey, new ConfigItem(ConfigSimple.CONFIG_LEVEL_MED));
        configSet.addConfigItem(ConfigItemKeys.SimpleConfigCpuAllowanceKey, new ConfigItem(ConfigSimple.CONFIG_LEVEL_MED));
        configSet.addConfigItem(ConfigItemKeys.SimpleConfigStructuresKey, new ConfigItem(ConfigSimple.CONFIG_LEVEL_LOW));

		return configSet;
	}
}
