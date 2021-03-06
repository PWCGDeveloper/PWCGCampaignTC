package pwcg.campaign.skin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class SkinLoader
{
    private Map<String, SkinsForTank> skinsForTanks = new HashMap<>();
    
    public SkinLoader ()
    {
    }
    
    public Map<String, SkinsForTank> loadPwcgSkins()
    {
        initialize();
        readConfiguredAndDoNotUseSkins();
        loadCompanySkins();
        loadAceSkins();
        readLooseSkins();
        
        return skinsForTanks;
    }

    private void initialize()
    {
        try
        {
            List<TankTypeInformation> allPlanes = PWCGContext.getInstance().getPlayerTankTypeFactory().getAllTanks();
            for(TankTypeInformation plane : allPlanes)
            {
                SkinsForTank skinsForPlane = new SkinsForTank();
                skinsForTanks.put(plane.getType(), skinsForPlane);
            }
        }
        catch (Exception exp)
        {
             PWCGLogger.logException(exp);
        }
    }

    private void readConfiguredAndDoNotUseSkins()
    {
        try
        {
            ConfiguredSkinReader configuredSkinReader = new ConfiguredSkinReader(skinsForTanks);
            configuredSkinReader.readConfiguredSkinsFromPlaneSkinFiles();
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
        }
    }

    private void loadCompanySkins()
    {
        try
        {
            CompanySkinLoader companySkinLoader = new CompanySkinLoader(skinsForTanks);
            companySkinLoader.loadCompanySkins();
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
        }        
    }

    private void loadAceSkins()
    {
        try
        {
            AceSkinLoader companySkinLoader = new AceSkinLoader(skinsForTanks);
            companySkinLoader.loadHistoricalAceSkins();;
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
        }        
    }

    private void readLooseSkins()
    {
        LooseSkinLoader looseSkinLoader = new LooseSkinLoader(skinsForTanks);
        looseSkinLoader.readLooseSkins();
    }

}
