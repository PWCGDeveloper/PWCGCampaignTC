package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.target.TargetCategory;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class P51D15Payload extends PlanePayload implements IPlanePayload
{
    public P51D15Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(-4, "1000000000", PayloadElement.MIRROR);
        setAvailablePayload(-3, "100000000", PayloadElement.MN28);
        setAvailablePayload(-2, "10000000", PayloadElement.OCTANE_150_FUEL);
	    setAvailablePayload(-1, "1000000", PayloadElement.P51_GUNSIGHT);
	    
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.MG50CAL_4x);
        setAvailablePayload(2, "101", PayloadElement.ADDITIONAL_AMMO);
        setAvailablePayload(4, "1001", PayloadElement.M64_X2);
        setAvailablePayload(8, "10001", PayloadElement.M65_X2);
        setAvailablePayload(12, "100001", PayloadElement.P51_ROCKETS);
	}
 
    @Override
    public IPlanePayload copy()
    {
        P51D15Payload clone = new P51D15Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectGroundAttackPayload(flight);
        }

        return selectedPrimaryPayloadId;
    }    

    protected void selectGroundAttackPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 4;
        if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPrimaryPayloadId = 4;
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPrimaryPayloadId = 12;
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPrimaryPayloadId = 8;
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPrimaryPayloadId = 8;
        }
    }
}