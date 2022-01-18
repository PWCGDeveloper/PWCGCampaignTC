package pwcg.core.config;

import pwcg.core.exception.PWCGException;

public class ConfigManagerCampaign extends ConfigManager
{	
    public ConfigManagerCampaign(String campaignConfigDir) 
    {
        super(campaignConfigDir);
    }
    
	public void initialize() throws PWCGException 
	{
	    initializeDefault();
        readConfig();
	}

	public void initializeDefault()
    {
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetFlight, ConfigSetFlight.initialize());
	    defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetMissionAi, ConfigSetMissionAI.initialize());
	    defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetGroundObjects, ConfigSetGroundObjects.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetMissionLimits, ConfigSetMissionLimits.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetFighterMission, ConfigSetFighterMissionTypes.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetGroundAttackMission, ConfigSetGroundAttackMissionTypes.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetBomberMission, ConfigSetBomberMissionTypes.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetReconMission, ConfigSetReconMissionTypes.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetTransportMission, ConfigSetTransportMissionTypes.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetTarget, ConfigSetTargetTypes.initialize());

        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetAircraftNumbers, ConfigSetTankNumbers.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetUserPrefCampaign, ConfigSetUserPrefCampaign.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetWeather, ConfigSetWeather.initialize());
        defaultCampaignConfigSets.put(ConfigSetKeys.ConfigSetSimple, ConfigSetSimple.initialize());
    }
}
