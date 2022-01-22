package pwcg.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.skin.SkinsForTank;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;

public class SkinAnalyzer 
{
    private Map<String, List<MissingSkin>> missingSkinListMap = new HashMap<String, List<MissingSkin>>();

	public void analyze() throws PWCGException 
	{
	    initMissingSkinMap();
	    
		findMissingConfiguredSkins();
		findMissingCompanySkins();
		findMissingAceSkins();
	}

	private void initMissingSkinMap() throws PWCGException
	{
        List<TankTypeInformation> allPlanes = PWCGContext.getInstance().getFullTankTypeFactory().getAllTanks();
        
        missingSkinListMap.clear();
        
        for (TankTypeInformation plane : allPlanes)
        {
            List<MissingSkin> missingSkinList = new ArrayList<>();
            missingSkinListMap.put(plane.getType(), missingSkinList);
        }
	}

	private void findMissingConfiguredSkins() throws PWCGException 
	{
		for (TankTypeInformation plane : PWCGContext.getInstance().getFullTankTypeFactory().getAllTanks())
		{
			SkinsForTank skinsForPlane = PWCGContext.getInstance().getSkinManager().getSkinsForTank(plane.getType());
		
			for (Skin skin : skinsForPlane.getConfiguredSkins().getSkins().values())
			{
		        if (isSkinMissing(skin))
		        {
	                addMissingSkin(plane, skin.getSkinName(), "Configured");
		        }
			}
		}
	}
	
	private void findMissingCompanySkins() throws PWCGException 
	{
		for (TankTypeInformation plane : PWCGContext.getInstance().getFullTankTypeFactory().getAllTanks())
		{
			SkinsForTank skinsForPlane = PWCGContext.getInstance().getSkinManager().getSkinsForTank(plane.getType());
		
			for (Skin skin : skinsForPlane.getCompanySkins().getSkins().values())
			{
                if (isSkinMissing(skin))
				{		
                    addMissingSkin(plane, skin.getSkinName(), "Company");
				}
			}
		}
	}
	private void findMissingAceSkins() throws PWCGException 
	{
		for (TankTypeInformation plane : PWCGContext.getInstance().getFullTankTypeFactory().getAllTanks())
		{
			SkinsForTank skinsForPlane = PWCGContext.getInstance().getSkinManager().getSkinsForTank(plane.getType());
		
			for (Skin skin : skinsForPlane.getAceSkins().getSkins().values())
			{
                if (isSkinMissing(skin))
				{		
                    addMissingSkin(plane, skin.getSkinName(), "Ace");
				}
			}
		}
	}

    private boolean isSkinMissing(Skin skin)
    {
        if (!skin.isDefinedInGame())
        {
            if (!skin.skinExists(Skin.PRODUCT_SKIN_DIR))
            {
                return true;
            }
        }
        
        return false;
    }
    
    private void addMissingSkin(TankTypeInformation plane, String skinName, String category)
    {
        MissingSkin missingSkin = new MissingSkin();
        
        missingSkin.setTankType(plane.getType());
        missingSkin.setSkinName(skinName);
        missingSkin.setCategory(category);
        
        List<MissingSkin> missingSkinList = missingSkinListMap.get(plane.getType());
        missingSkinList.add(missingSkin);
    }

    public List<MissingSkin> getMissingSkinsForPlane(String planeType)
    {
        List<MissingSkin> missingSkinList = missingSkinListMap.get(planeType);

        return missingSkinList;
    }
}
