package pwcg.mission.flight.escort;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToGround;


public class EscortedByPlayerFlightBuilder
{
    private IFlightInformation playerEscortFlightInformation;
    private TargetDefinition playerEscortTargetDefinition;
    private IFlightInformation escortedFlightInformation;
    private TargetDefinition escortedTargetDefinition;

	public EscortedByPlayerFlightBuilder(IFlightInformation playerEscortFlightInformation, TargetDefinition playerEscortTargetDefinition)
    {
        this.playerEscortFlightInformation = playerEscortFlightInformation;
        this.playerEscortTargetDefinition = playerEscortTargetDefinition;
    }

    public EscortedByPlayerFlight createEscortedFlight() throws PWCGException
    {
        MissionBeginUnit missionBeginUnit = buildEscortedFlightInformation();
        EscortedByPlayerFlight PlayerEscortedFlightEscortedByPlayer = buildEscortedFlight(missionBeginUnit);        
        return PlayerEscortedFlightEscortedByPlayer;
	}

    private EscortedByPlayerFlight buildEscortedFlight(MissionBeginUnit missionBeginUnit) throws PWCGException
    {
        EscortedByPlayerFlight playerEscortedFlight = new EscortedByPlayerFlight (escortedFlightInformation, escortedTargetDefinition);
		playerEscortedFlight.createFlight();
        
        return playerEscortedFlight;
    }

    private MissionBeginUnit buildEscortedFlightInformation() throws PWCGException
    {
        Squadron friendlyBomberSquadron = determineSquadronToBeEscorted();
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(friendlyBomberSquadron.determineCurrentPosition(playerEscortFlightInformation.getCampaign().getDate()));     
        
        this.escortedFlightInformation = EscortedByPlayerFlightInformationBuilder.buildEscortedByPlayerFlightInformation(
                playerEscortFlightInformation, playerEscortTargetDefinition, friendlyBomberSquadron);
        
        this.escortedTargetDefinition = buildTargetDefintion(escortedFlightInformation);

        return missionBeginUnit;
    }

    private Squadron determineSquadronToBeEscorted() throws PWCGException
    {
        Squadron friendlyBombSquadron = PWCGContext.getInstance().getSquadronManager().getSquadronByProximityAndRoleAndSide(
                playerEscortFlightInformation.getCampaign(), 
                playerEscortFlightInformation.getSquadron().determineCurrentPosition(playerEscortFlightInformation.getCampaign().getDate()), 
                Role.ROLE_BOMB, 
                playerEscortFlightInformation.getSquadron().determineSquadronCountry(playerEscortFlightInformation.getCampaign().getDate()).getSide());
        
        if (friendlyBombSquadron == null)
        {
            throw new PWCGMissionGenerationException ("Escort mission with no viable squadrons to be escorted - please create another mission");
        }
        return friendlyBombSquadron;
    }

    private TargetDefinition buildTargetDefintion(IFlightInformation escortedFlightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToGround(escortedFlightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
