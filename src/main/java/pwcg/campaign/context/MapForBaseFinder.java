package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.PWCGLocation;

public class MapForBaseFinder
{

    public static List<FrontMapIdentifier> getMapForBase(String baseName) throws PWCGException
    {
        List<FrontMapIdentifier> mapsForBase = new ArrayList<>();
        for (PWCGMap map : PWCGContext.getInstance().getMaps())
        {
            PWCGLocation town = map.getGroupManager().getTownFinder().getTown(baseName);
            if (town != null)
            {
                mapsForBase.add(map.getMapIdentifier());
            }
        }

        return mapsForBase;
    }

}
