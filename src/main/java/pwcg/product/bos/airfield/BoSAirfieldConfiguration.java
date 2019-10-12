package pwcg.product.bos.airfield;

import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IAirfieldConfiguration;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.BoSAirfieldDescriptorIOJson;
import pwcg.core.exception.PWCGException;

public class BoSAirfieldConfiguration implements IAirfieldConfiguration
{
    public Map<String, IAirfield> configure (String mapName) throws PWCGException
    {
        Map<String, IAirfield> airfields = new TreeMap<String, IAirfield>();
        
        airfields.clear();

        String pwcgInputDir = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\";
        AirfieldDescriptorSet airfieldDescriptors = BoSAirfieldDescriptorIOJson.readJson(pwcgInputDir, AIRFIELD_LOCATION_FILE_NAME);
        for (AirfieldDescriptor desc : airfieldDescriptors.getLocations())
        {
            BoSAirfield field = new BoSAirfield();
            field.initializeAirfieldFromDescriptor(desc);
            airfields.put(desc.getName(), field);
        }

        return airfields;
    }
}