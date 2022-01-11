package pwcg.mission.flight.crew;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.MissionCompanyFlightTypes;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.playerunit.crew.UnitCrewBuilder;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlightCrewBuilderCoopTest 
{
    private static Campaign coopCampaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        coopCampaign = CampaignCache.makeCampaign(CompanyTestProfile.COOP_COMPETITIVE_PROFILE);
        PWCGContext.getInstance().setCampaign(coopCampaign);
    }

    @Test
    public void testOneOfTwoPlayerFlightGeneration() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
    	for (CrewMember player : coopCampaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
    	{
    		if (player.getName().contentEquals("Company Mate"))
    		{
    			participatingPlayers.addCrewMember(player);
    		}
    	}
    	
        MissionCompanyFlightTypes playerFlightTypes = new MissionCompanyFlightTypes();
    	for (CrewMember player : participatingPlayers.getAllParticipatingPlayers())
    	{
    	    playerFlightTypes.add(player.determineCompany(), FlightTypes.GROUND_ATTACK);
    	}
    	
        MissionGenerator missionGenerator = new MissionGenerator(coopCampaign);
        Mission mission = missionGenerator.makeTestCoopMissionFromFlightType(participatingPlayers, playerFlightTypes, MissionProfile.DAY_TACTICAL_MISSION);
        
        FlightInformation flightInformation = new FlightInformation(mission, NecessaryFlightType.PLAYER_FLIGHT);
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.COOP_COMPETITIVE_PROFILE.getCompanyId());
        flightInformation.setCompany(company);
        
        UnitCrewBuilder flightCrewBuilder = new UnitCrewBuilder(flightInformation);
        List<CrewMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        boolean playerFound = false;
        boolean playerShouldNotBeFound = true;
        CompanyPersonnel companyPersonnel = coopCampaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.COOP_COMPETITIVE_PROFILE.getCompanyId());        
        for (CrewMember crew : assignedCrewMap)
        {
            assert(companyPersonnel.isActiveCrewMember(crew.getSerialNumber()));
            if (crew.getName().equals("Company Mate"))
            {
            	playerFound = true;
            }
            else if (crew.isPlayer())
            {
            	playerShouldNotBeFound = false;
            }
        }

        assert(playerFound && playerShouldNotBeFound);
    }

    @Test
    public void testTwoPlayerFlightGeneration() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
    	for (CrewMember player : coopCampaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
    	{
    		if (player.getCompanyId() == CompanyTestProfile.COOP_COMPETITIVE_PROFILE.getCompanyId())
    		{
    			participatingPlayers.addCrewMember(player);
    		}
    	}
        
        List<FlightTypes> playerFlightTypeList = makeFlightTypes(participatingPlayers);
        MissionCompanyFlightTypes playerFlightTypes = new MissionCompanyFlightTypes();
        for (int i = 0; i < participatingPlayers.getParticipatingCompanyIds().size(); ++i)
        {
            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(participatingPlayers.getParticipatingCompanyIds().get(i));
            playerFlightTypes.add(company, playerFlightTypeList.get(i));
        }

        MissionGenerator missionGenerator = new MissionGenerator(coopCampaign);
        Mission mission = missionGenerator.makeTestCoopMissionFromFlightType(participatingPlayers, playerFlightTypes, MissionProfile.DAY_TACTICAL_MISSION);

        FlightInformation flightInformation = new FlightInformation(mission, NecessaryFlightType.PLAYER_FLIGHT);
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.COOP_COMPETITIVE_PROFILE.getCompanyId());
        flightInformation.setCompany(company);
        
        UnitCrewBuilder flightCrewBuilder = new UnitCrewBuilder(flightInformation);
        List<CrewMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        boolean player1Found = false;
        boolean player2Found = false;
        CompanyPersonnel companyPersonnel = coopCampaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.COOP_COMPETITIVE_PROFILE.getCompanyId());        
        for (CrewMember crew : assignedCrewMap)
        {
            assert(companyPersonnel.isActiveCrewMember(crew.getSerialNumber()));
            if (crew.getSerialNumber() == participatingPlayers.getAllParticipatingPlayers().get(0).getSerialNumber())
            {
                player1Found = true;
            }
            if (crew.getSerialNumber() == participatingPlayers.getAllParticipatingPlayers().get(1).getSerialNumber())
            {
                player2Found = true;
            }
        }

        assert(player1Found && player2Found);
    }
    
    private List<FlightTypes> makeFlightTypes(MissionHumanParticipants participatingPlayers)
    {
        List<FlightTypes> playerFlightTypes = new ArrayList<>();
        for (int i = 0; i < participatingPlayers.getParticipatingCompanyIds().size(); ++i)
        {
            playerFlightTypes.add(FlightTypes.GROUND_ATTACK);
        }
        return playerFlightTypes;
    }
    

    @Test
    public void testTwoPlayerEnemyCompanyFlightGeneration() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
    	for (CrewMember player : coopCampaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
    	{
    		if (player.getCompanyId() == 10131132)
    		{
    			participatingPlayers.addCrewMember(player);
    		}
    	}
        
        List<FlightTypes> playerFlightTypeList = makeFlightTypes(participatingPlayers);
        MissionCompanyFlightTypes playerFlightTypes = new MissionCompanyFlightTypes();
        for (int i = 0; i < participatingPlayers.getParticipatingCompanyIds().size(); ++i)
        {
            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(participatingPlayers.getParticipatingCompanyIds().get(i));
            playerFlightTypes.add(company, playerFlightTypeList.get(i));
        }

        MissionGenerator missionGenerator = new MissionGenerator(coopCampaign);
        Mission mission = missionGenerator.makeTestCoopMissionFromFlightType(participatingPlayers, playerFlightTypes, MissionProfile.DAY_TACTICAL_MISSION);

        FlightInformation flightInformation = new FlightInformation(mission, NecessaryFlightType.PLAYER_FLIGHT);
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(10131132);
        flightInformation.setCompany(company);
        
        UnitCrewBuilder flightCrewBuilder = new UnitCrewBuilder(flightInformation);
        List<CrewMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        boolean player1Found = false;
        boolean player2Found = false;
        CompanyPersonnel companyPersonnel = coopCampaign.getPersonnelManager().getCompanyPersonnel(10131132);        
        for (CrewMember crew : assignedCrewMap)
        {
            assert(companyPersonnel.isActiveCrewMember(crew.getSerialNumber()));
            if (crew.getSerialNumber() == participatingPlayers.getAllParticipatingPlayers().get(0).getSerialNumber())
            {
                player1Found = true;
            }
            if (crew.getSerialNumber() == participatingPlayers.getAllParticipatingPlayers().get(1).getSerialNumber())
            {
                player2Found = true;
            }
        }

        assert(player1Found && player2Found);
    }

}
