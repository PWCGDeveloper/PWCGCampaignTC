package pwcg.campaign.skin;

import java.util.Map;

import pwcg.campaign.io.json.SkinIOJson;
import pwcg.core.exception.PWCGException;

public class ConfiguredSkinReader
{
    private Map<String, SkinsForTank> skinsForTanks;
    
    public ConfiguredSkinReader (Map<String, SkinsForTank> skinsForTanks)
    {
        this.skinsForTanks = skinsForTanks;
    }

    public void readConfiguredSkinsFromPlaneSkinFiles() throws PWCGException
    {
        Map<String, SkinSet>  configuredSkinSets = SkinIOJson.readSkinSet(SkinSetType.SKIN_CONFIGURED.getSkinSetName());
        Map<String, SkinSet>  doNotUseSkinSets = SkinIOJson.readSkinSet(SkinSetType.SKIN_DO_NOT_USE.getSkinSetName());

        for (String planeType : skinsForTanks.keySet())
        {
            SkinsForTank skinsForPlane = skinsForTanks.get(planeType);
            if (configuredSkinSets.containsKey(planeType))
            {
                SkinSet configuredSkins = configuredSkinSets.get(planeType);
                skinsForPlane.setConfiguredSkins(configuredSkins);
            }
            if (doNotUseSkinSets.containsKey(planeType))
            {
                SkinSet doNotUseSkins = doNotUseSkinSets.get(planeType);
                skinsForPlane.setDoNotUse(doNotUseSkins);
            }
        }
    }

}
