package pwcg.gui.campaign.config;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.CommonUIActions;

public class CampaignConfigurationSimpleGUIController
{
    static public final String ACTION_SET_GROUND_DENSITY = "Ground Density";
    static public final String ACTION_SET_CPU_ALOWANCE_DENSITY = "CPU Allowance";
    static public final String ACTION_SET_STRUCTURE_DENSITY = "Structure";
    
    static public final String CAMPAIGN_TYPE = "Campaign Type";
    
    private Campaign campaign;

    public CampaignConfigurationSimpleGUIController(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
	public void setSimpleConfig(String action)
	{
		try
		{
            if (action.contains(ACTION_SET_GROUND_DENSITY))
            {
                setGroundDensity(action);
            }
            else if (action.contains(ACTION_SET_CPU_ALOWANCE_DENSITY))
            {
                setCpuAllowanceDensity(action);
            }           
            else if (action.contains(ACTION_SET_STRUCTURE_DENSITY))
            {
                setStructureDensity(action);
            }           
            else if (action.equalsIgnoreCase(CommonUIActions.ACTION_ACCEPT))
			{
				acceptSimpleConfigChanges(campaign);
			}
			else if (action.equalsIgnoreCase(CommonUIActions.ACTION_RESET))
			{
				resetConfigurationToDefault(campaign);
			}
			if (action.equalsIgnoreCase(CommonUIActions.ACTION_CANCEL))
			{
				cancelSimpleConfigChanges(campaign);
			}
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private void setGroundDensity(String action) throws PWCGException
    {
        ConfigSimple configSetSimpleConfig = new ConfigSimple(campaign);
        if (action.contains(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            configSetSimpleConfig.setGroundLow();
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_MED))
        {
            configSetSimpleConfig.setGroundMed();
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            configSetSimpleConfig.setGroundHigh();
        }
    }

    private void setCpuAllowanceDensity(String action) throws PWCGException
    {
        ConfigSimple configSetSimpleConfig = new ConfigSimple(campaign);
        if (action.contains(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            configSetSimpleConfig.setCpuAllowanceLow();
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_MED))
        {
            configSetSimpleConfig.setCpuAllowanceMed();
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            configSetSimpleConfig.setCpuAllowanceHigh();
        }
    }

    private void setStructureDensity(String action) throws PWCGException
    {
        ConfigSimple configSetSimpleConfig = new ConfigSimple(campaign);
        if (action.contains(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            configSetSimpleConfig.setStructureLow();;
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_MED))
        {
            configSetSimpleConfig.setStructureMed();;
        }
        else if (action.contains(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            configSetSimpleConfig.setStructureHigh();;
        }
    }

    private void acceptSimpleConfigChanges(Campaign campaign) throws PWCGException
    {
        campaign.getCampaignConfigManager().write();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void resetConfigurationToDefault(Campaign campaign) throws PWCGException
    {
        campaign.getCampaignConfigManager().reset();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void cancelSimpleConfigChanges(Campaign campaign) throws PWCGException
    {
        campaign.getCampaignConfigManager().readConfig();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

}

