package pwcg.mission.flight.bomb;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.factory.TargetFactory;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class BombingPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;
    private FlightTypes flightType;

    public BombingPackage(FlightTypes flightType)
    {
        this.flightType = flightType;
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, flightType);

	    IFlight bombingFlight = createPackageTacticalTarget ();
		return bombingFlight;
	}

	public IFlight createPackageTacticalTarget () throws PWCGException 
	{
        IGroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(flightInformation.getSquadron().determineEnemySide());

        IFlight bombingFlight = makeBombingFlight(targetCoordinates);
        bombingFlight.addLinkedGroundUnit(groundUnitCollection);

        return bombingFlight;
	}

    private IFlight makeBombingFlight(Coordinate targetCoordinates) throws PWCGException
    {
        BombingFlight bombingFlight = new BombingFlight (flightInformation);
	    bombingFlight.createFlight();
	    return bombingFlight;
    }

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetFactory targetBuilder = new TargetFactory(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
