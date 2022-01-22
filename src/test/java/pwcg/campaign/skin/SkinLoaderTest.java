package pwcg.campaign.skin;

import java.util.Map;

import org.junit.jupiter.api.Test;

import pwcg.core.exception.PWCGException;

public class SkinLoaderTest
{
    @Test
    public void skinLoaderRoFTest() throws PWCGException
    {
        
        SkinLoader skinLoader = new SkinLoader();
        Map<String, SkinsForTank> skinsForPlanes = skinLoader.loadPwcgSkins();
        
        assert(skinsForPlanes != null);
    }

    @Test
    public void skinLoaderBoSTest() throws PWCGException
    {
        
        SkinLoader skinLoader = new SkinLoader();
        Map<String, SkinsForTank> skinsForPlanes = skinLoader.loadPwcgSkins();
        
        assert(skinsForPlanes != null);
    }

}
