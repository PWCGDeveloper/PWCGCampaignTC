package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.transport.GroundTruckAAConvoyUnit;
import pwcg.mission.ground.unittypes.transport.GroundTruckAmmoConvoyUnit;
import pwcg.mission.ground.unittypes.transport.GroundTruckConvoyUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetType;

public class TruckUnitTransportBuilder
{
    private Campaign campaign;
    private IGroundUnit groundUnit;
    private ICountry country;
    
    public TruckUnitTransportBuilder (Campaign campaign, IGroundUnit artilleryUnit, ICountry country)
    {
        this.campaign = campaign;
        this.groundUnit  = artilleryUnit;
        this.country  = country;
    }

    public GroundUnitCollection createTruckConvoy () throws PWCGException
    {
        GroundUnitCollection groundUnitCollection = createTrucks();
        return groundUnitCollection;
    }

    private GroundUnitCollection createTrucks () throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createGroundUnitInformationForUnit();
        IGroundUnit truckConvoy = new GroundTruckConvoyUnit(groundUnitInformation);
        truckConvoy.createGroundUnit();
        
        IGroundUnit ammoTruckConvoy = new GroundTruckAmmoConvoyUnit(groundUnitInformation);
        ammoTruckConvoy.createGroundUnit();
        
        IGroundUnit aaTruckConvoy = new GroundTruckAAConvoyUnit(groundUnitInformation);
        aaTruckConvoy.createGroundUnit();
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, 
                "Truck Convoy", 
                TargetType.TARGET_TRANSPORT,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));
        
        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(campaign, "Truck Convoy", groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(truckConvoy);
        groundUnitCollection.addGroundUnit(ammoTruckConvoy);
        groundUnitCollection.addGroundUnit(aaTruckConvoy);
        groundUnitCollection.setPrimaryGroundUnit(truckConvoy);
        groundUnitCollection.finishGroundUnitCollection();

        return groundUnitCollection;
    }

    private GroundUnitInformation createGroundUnitInformationForUnit() throws PWCGException
    {
        Coordinate startPosition = getConvoyStartPosition();
        Coordinate destination = getConvoyDestination();

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                country, 
                TargetType.TARGET_TRANSPORT,
                startPosition, 
                destination,
                groundUnit.getOrientation());

        groundUnitInformation.setDestination(destination);
        return groundUnitInformation;
    }

    private Coordinate getConvoyStartPosition() throws PWCGException
    {
        double angleBack = MathUtils.adjustAngle(groundUnit.getOrientation().getyOri(), 180);
        Coordinate startPosition = MathUtils.calcNextCoord(groundUnit.getPosition(), angleBack, 500);
        return startPosition;
    }

    private Coordinate getConvoyDestination() throws PWCGException
    {
        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Bridge destinationBridge = groupData.getBridgeFinder().findDestinationBridge(groundUnit.getPosition(), country.getSide(), campaign.getDate());
        if (destinationBridge != null)
        {
            return destinationBridge.getPosition();
        }
        else
        {
            return groundUnit.getPosition();
        }
    }
}
