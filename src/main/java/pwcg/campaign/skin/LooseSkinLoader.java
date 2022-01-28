package pwcg.campaign.skin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class LooseSkinLoader
{
    private Map<String, SkinsForTank> skinsForTanks = new HashMap<>();
    
    LooseSkinLoader(Map<String, SkinsForTank> skinsForTanks)
    {
        this.skinsForTanks = skinsForTanks;
    }

    public void readLooseSkins()
    {
        try
        {
            String skinDirName = "..\\data\\graphics\\skins\\";
            File dir = new File(skinDirName);
            if (dir.exists())
            {
                if (dir.isDirectory())
                {
                    LooseSkinReader looseSkinReader = new LooseSkinReader();
                    Map<String, List<String>> looseSkins = looseSkinReader.readLooseSkins(skinDirName);
                    loadLooseSkins(looseSkins);
                }
            }
        }
        catch (Exception e)
        {
             PWCGLogger.logException(e);
        }
    }


    private void loadLooseSkins(Map<String, List<String>> looseSkinNames) throws PWCGException
    {        
        for (String planeType : looseSkinNames.keySet())
        {
            planeType = planeType.toLowerCase();
        
            if (skinsForTanks.containsKey(planeType))
            {
                SkinsForTank skinsForPlane = skinsForTanks.get(planeType);
                List<String> skinFiles = looseSkinNames.get(planeType);
                for (String looseSkinName : skinFiles)
                {
                    Skin looseSkin = new Skin();
                    looseSkin.setSkinName(looseSkinName);
                    skinsForPlane.addLooseSkin(looseSkinName);
                }
            }
        }
    }
}
