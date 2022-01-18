package pwcg.mission;

import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.builder.AssaultArmoredPlatoonBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.unit.ITankPlatoon;
import pwcg.mission.unit.IUnitPackage;
import pwcg.mission.unit.PlatoonInformation;
import pwcg.mission.unit.PlatoonInformationFactory;
import pwcg.mission.unit.PlatoonMissionType;
import pwcg.mission.unit.aaa.AAAPackage;
import pwcg.mission.unit.assault.AssaultPackage;
import pwcg.mission.unit.defense.DefensePackage;
import pwcg.mission.unit.infantrysupport.InfantrySupportPackage;

public class MissionPlatoonBuilder
{
    private Mission mission;
    private MissionPlatoons missionPlatoons;
    
    public MissionPlatoonBuilder(Mission mission)
    {
        this.mission = mission;
    }

    public MissionPlatoons createPlatoons() throws PWCGException
    {
        missionPlatoons = new MissionPlatoons(mission);
        List<Company> alliedCompaniesInMission = participatingPlayers.getParticipatingCompanyIdsForSide(Side.ALLIED);
        
        // get tanks for platoons
        
        // AI platoons have  
        
        // get waypoints for platoons

        return missionPlatoons;
    }
    
    private void  buildPlatoonsForSide(Side side)
    {
        buildPlayerPlatoons(side);
        buildAiPlatoons(side);
    }
    
    private void buildPlayerPlatoons(Side side)
    {
        List<Company> companiesInMission = mission.getParticipatingPlayers().getParticipatingCompanyIdsForSide(side);
        for(Company playerCompany : companiesInMission)
        {            
            PlatoonInformation UnitInformation = PlatoonInformationFactory.buildUnitInformation(mission, playerCompany, mission.getParticipatingPlayers());

            List<ITankPlatoon> playerPlatoons = buildPlayerUnit(UnitInformation);
            for (ITankPlatoon playerPlatoon : playerPlatoons)
            {
                missionPlatoons.addPlatoon(playerPlatoon);
            }
        }
    }
    
    private void buildAiPlatoons(Side side)
    {
        List<Company> companiesInMission = mission.getParticipatingPlayers().getParticipatingCompanyIdsForSide(Side.ALLIED);
        int numAlliedPlatoons = MissionPlatoonSize.getNumAiPlatoonsForSide(mission, side, companiesInMission.size());
        for(int i = 0; i < numAlliedPlatoons; ++i)
        {            
            PlatoonInformation UnitInformation = PlatoonInformationFactory.buildUnitInformation(mission, null, null);

            List<ITankPlatoon> playerPlatoons = buildAiUnit(UnitInformation);
            for (ITankPlatoon playerPlatoon : playerPlatoons)
            {
                missionPlatoons.addPlatoon(playerPlatoon);
            }
        }
    }

    private List<ITankPlatoon> buildPlayerUnit(PlatoonInformation platoonInformation) throws PWCGException
    {
        IUnitPackage unitPackage = null;
        if (platoonInformation.getMissionType() == PlatoonMissionType.ASSAULT)
        {
            unitPackage = new AssaultPackage();
        }
        else if (platoonInformation.getMissionType() == PlatoonMissionType.DEFENSE)
        {
            unitPackage = new DefensePackage();
        }
        else if (platoonInformation.getMissionType() == PlatoonMissionType.INFANTRY_SUPPORT)
        {
            unitPackage = new InfantrySupportPackage();
        }
        else if (platoonInformation.getMissionType() == PlatoonMissionType.AAA)
        {
            unitPackage = new AAAPackage();
        }
        
        List<ITankPlatoon> playerPlatoons = unitPackage.createUnitPackage(platoonInformation);
        return playerPlatoons;
    }
}
