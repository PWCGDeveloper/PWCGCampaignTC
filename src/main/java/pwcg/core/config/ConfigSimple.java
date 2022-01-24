package pwcg.core.config;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class ConfigSimple
{
    public static String CONFIG_LEVEL_LOW = "Low";
    public static String CONFIG_LEVEL_MED = "Med";
    public static String CONFIG_LEVEL_HIGH = "High";

    private Campaign campaign = null;
    
    public ConfigSimple (Campaign campaign)
    {
    	this.campaign = campaign;
    }

    public void setPlatoonsLow() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.AlliedPlatoonsInMissionKey, "3");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisPlatoonsInMissionKey, "2");
    }

    public void setPlatoonsMed() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.AlliedPlatoonsInMissionKey, "6");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisPlatoonsInMissionKey, "3");
    }

    public void setPlatoonsHigh() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.AlliedPlatoonsInMissionKey, "12");
        setParamForSimpleConfigChange(ConfigItemKeys.AxisPlatoonsInMissionKey, "6");
    }

    public void setGroundLow() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigGroundKey, ConfigSimple.CONFIG_LEVEL_LOW);
    }

    public void setGroundMed() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigGroundKey, ConfigSimple.CONFIG_LEVEL_MED);
    }

    public void setGroundHigh() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigGroundKey, ConfigSimple.CONFIG_LEVEL_HIGH);
    }

    public void setCpuAllowanceHigh() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigCpuAllowanceKey, ConfigSimple.CONFIG_LEVEL_HIGH);
    }
    public void setCpuAllowanceLow() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigCpuAllowanceKey, ConfigSimple.CONFIG_LEVEL_LOW);
    }
    
    public void setCpuAllowanceMed() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigCpuAllowanceKey, ConfigSimple.CONFIG_LEVEL_MED);
    }

    public void setStructureHigh() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_HIGH);
    }
    public void setStructureLow() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_LOW);
    }
    
    public void setStructureMed() throws PWCGException 
    {
        setParamForSimpleConfigChange(ConfigItemKeys.SimpleConfigStructuresKey, ConfigSimple.CONFIG_LEVEL_MED);
    }

    public void setParamForSimpleConfigChange (String key, String value) throws PWCGException 
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        configManager.setParam(key, value);
    }
}
