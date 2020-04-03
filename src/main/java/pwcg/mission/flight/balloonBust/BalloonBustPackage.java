package pwcg.mission.flight.balloonBust;

import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.builder.BalloonUnitBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class BalloonBustPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;
    private IGroundUnitCollection balloonUnit;

    public BalloonBustPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.BALLOON_BUST);

        buildBalloon();
        BalloonBustFlight balloonBust = buildBalloonBustFllght();
		buildOpposingFlights(balloonBust);
		
		return balloonBust;
	}

    private void buildBalloon() throws PWCGException
    {
        Side enemySide = flightInformation.getSquadron().determineEnemySide();
        Squadron enemyScoutSquadron = PWCGContext.getInstance().getSquadronManager().getSquadronByProximityAndRoleAndSide(
                        flightInformation.getCampaign(), 
                        flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate()), 
                        Role.ROLE_FIGHTER, 
                        flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()).getSide().getOppositeSide());
        ICountry balloonCountry = determineBalloonCountry(enemySide, enemyScoutSquadron);
        balloonUnit = createBalloonUnit(flightInformation.getTargetPosition(), balloonCountry);
    }

    private BalloonBustFlight buildBalloonBustFllght() throws PWCGException
    {
        BalloonBustFlight balloonBust = new BalloonBustFlight (flightInformation);
        balloonBust.addLinkedGroundUnit(balloonUnit);
        balloonBust.createFlight();
        return balloonBust;
    }

    private IGroundUnitCollection createBalloonUnit(Coordinate balloonPosition, ICountry balloonCountry) throws PWCGException
    {
        BalloonUnitBuilder groundUnitBuilderBalloonDefense = new BalloonUnitBuilder(flightInformation.getMission(), flightInformation.getTargetDefinition());
        IGroundUnitCollection balloonUnit = groundUnitBuilderBalloonDefense.createBalloonUnit(balloonCountry);
        if (balloonUnit == null)
        {
          System.out.println("oops");  
        }
        return balloonUnit;
    }

    private ICountry determineBalloonCountry(Side enemySide, Squadron enemyScoutSquadron) throws PWCGException
    {
        ICountry balloonCountry;
        if (enemyScoutSquadron != null)
        {
            int enemyCountryCode = enemyScoutSquadron.determineSquadronCountry(flightInformation.getCampaign().getDate()).getCountryCode();
            balloonCountry = CountryFactory.makeCountryByCode(enemyCountryCode);
        }
        else
        {
            balloonCountry = CountryFactory.makeMapReferenceCountry(enemySide);
        }
        return balloonCountry;
    }

    private void buildOpposingFlights(IFlight flight) throws PWCGException
    {
        if (this.flightInformation.isPlayerFlight())
        {
            BalloonBustOpposingFlightBuilder opposingFlightBuilder = new BalloonBustOpposingFlightBuilder(flight.getFlightInformation(), balloonUnit);
            List<IFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
            for (IFlight opposingFlight: opposingFlights)
            {
                flight.getLinkedFlights().addLinkedFlight(opposingFlight);
            }
        }
    }
}
