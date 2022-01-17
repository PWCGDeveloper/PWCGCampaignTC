package pwcg.core.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightTypes;
import pwcg.product.bos.config.TCProductSpecificConfiguration;

@ExtendWith(MockitoExtension.class)
public class PositionFinderTest
{
    @Test
    public void readJsonTest() throws PWCGException
    {
        
        PositionFinder<Airfield> positionFinder = new PositionFinder<Airfield>();
        TCProductSpecificConfiguration productSpecific =new TCProductSpecificConfiguration();
        double radius = productSpecific.getAdditionalInitialTargetRadius(FlightTypes.GROUND_ATTACK);
        double maxDistance = productSpecific.getAdditionalMaxTargetRadius(FlightTypes.GROUND_ATTACK);
        AirfieldManager airfieldManager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        Airfield airfield = positionFinder.selectPositionWithinExpandingRadius(airfieldManager.getAirFieldsForSide(DateUtils.getDateYYYYMMDD("19420701"), Side.AXIS), new Coordinate(10000, 0, 10000), radius, maxDistance);
        assert(airfield != null);
    }
}
