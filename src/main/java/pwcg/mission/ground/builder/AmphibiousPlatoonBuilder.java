package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ICompanyMission;
import pwcg.mission.Mission;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.ground.MissionPlatoons;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.platoon.AiPlatoon;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.mission.platoon.PlatoonInformation;
import pwcg.mission.platoon.PlatoonMissionType;
import pwcg.mission.platoon.PlatoonMissionTypeFactory;
import pwcg.mission.platoon.PlayerPlatoon;

public class AmphibiousPlatoonBuilder implements IMissionPlatoonBuilder
{
    private Mission mission;
    private MissionPlatoons missionPlatoons;

    public AmphibiousPlatoonBuilder(Mission mission)
    {
        this.mission = mission;
    }

    @Override
    public MissionPlatoons createPlatoons() throws PWCGException
    {
        missionPlatoons = new MissionPlatoons(mission);
        buildPlatoonsForSide(Side.ALLIED);
        buildPlatoonsForSide(Side.AXIS);

        createAssaultingWaypoints();
        createDefendingingWaypoints();
                
        return missionPlatoons;
    }

    private void createAssaultingWaypoints() throws PWCGException
    {
        ArmoredAssaultRouteBuilder assaultRouteBuilder = new ArmoredAssaultRouteBuilder(mission);
        Map<Integer, List<Coordinate>> assaultRoutes = assaultRouteBuilder.buildAssaultRoutesForArmor(missionPlatoons.getPlatoons());
        for (int index : assaultRoutes.keySet())
        {
            List<Coordinate> waypointCoordinates = assaultRoutes.get(index);
            ITankPlatoon platoon = missionPlatoons.getPlatoon(index);
            
            createStartPosition(waypointCoordinates, platoon);
            
            List<McuWaypoint> assaultWaypoints = createWaypoints(waypointCoordinates, platoon.getTanks().get(0).getCruisingSpeed());
            platoon.setWaypoints(assaultWaypoints);
        }
    }

    private void createDefendingingWaypoints() throws PWCGException
    {
        ArmoredDefenseRouteBuilder defenseRouteBuilder = new ArmoredDefenseRouteBuilder(mission);
        Map<Integer, List<Coordinate>> defenseRoutes = defenseRouteBuilder.buildAssaultRoutesForArmor(missionPlatoons.getPlatoons());
        for (int index : defenseRoutes.keySet())
        {
            List<Coordinate> waypointCoordinates = defenseRoutes.get(index);
            ITankPlatoon platoon = missionPlatoons.getPlatoon(index);
            
            createStartPosition(waypointCoordinates, platoon);
            
            List<McuWaypoint> defenseWaypoints = createWaypoints(waypointCoordinates, platoon.getTanks().get(0).getCruisingSpeed());
            platoon.setWaypoints(defenseWaypoints);
        }
    }

    private List<McuWaypoint> createWaypoints(List<Coordinate> assaultWaypointCoordinates, int platoonSpeed) throws PWCGException
    {
        List<McuWaypoint> assaultWaypoints = new ArrayList<>();
        for(int i = 1; i < assaultWaypointCoordinates.size(); ++i)
        {
            Coordinate assaultWaypointCoordinate = assaultWaypointCoordinates.get(i);
            McuWaypoint waypoint;
            waypoint = WaypointFactory.createObjectiveWaypointType();
            waypoint.setTriggerArea(200);
            waypoint.setDesc("Assault Waypoint");
            waypoint.setSpeed(platoonSpeed);
            waypoint.setPosition(assaultWaypointCoordinate.copy());
            waypoint.setTargetWaypoint(true);
            
            double angle = MathUtils.calcAngle(assaultWaypointCoordinates.get(i-1), assaultWaypointCoordinate);
            waypoint.setOrientation(new Orientation(angle));
            
            assaultWaypoints.add(waypoint);
        }
        return assaultWaypoints;
    }

    private void createStartPosition(List<Coordinate> waypointCoordinates, ITankPlatoon platoon) throws PWCGException
    {
        Coordinate startPosition = waypointCoordinates.get(0);
        Coordinate towardsPosition = waypointCoordinates.get(1);
        platoon.setStartPosition(startPosition, towardsPosition);
    }

    private void buildPlatoonsForSide(Side side) throws PWCGException
    {
        List<Company> playerCompaniesInMission = mission.getParticipatingPlayers().getParticipatingCompanyIdsForSide(side);
        for (Company company : playerCompaniesInMission)
        {
            buildPlatoon(company);
        }
    }
    
    private void buildPlatoon(ICompanyMission company) throws PWCGException
    {
        List<CrewMember> playerCrews = mission.getParticipatingPlayers().getParticipatingPlayersForCompany(company.getCompanyId());
        PlatoonInformation platoonInformation = new PlatoonInformation(mission, company, playerCrews);

        ITankPlatoon tankPlatoon = null;
        if (company.isPlayercompany())
        {
            tankPlatoon = new PlayerPlatoon(platoonInformation);
            tankPlatoon.createUnit();
        }
        else
        {
            tankPlatoon = new AiPlatoon(platoonInformation);
            tankPlatoon.createUnit();
        }

        PlatoonMissionType platoonMissionType = PlatoonMissionTypeFactory.getPlatoonMissionType(mission, company, tankPlatoon.getTanks());
        tankPlatoon.setPlatoonMissionType(platoonMissionType);
        
        missionPlatoons.addPlatoon(tankPlatoon);
    }
}
