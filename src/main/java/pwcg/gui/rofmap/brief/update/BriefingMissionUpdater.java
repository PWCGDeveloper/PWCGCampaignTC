package pwcg.gui.rofmap.brief.update;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingPlayerPlatoon;
import pwcg.gui.rofmap.brief.model.IBriefingPlatoon;
import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;

public class BriefingMissionUpdater
{

    public static void finalizeMission(BriefingData briefingContext) throws PWCGException
    {
        Mission mission = briefingContext.getMission();
        if (!mission.getFinalizer().isFinalized())
        {
            pushEditsToMission(briefingContext);
            mission.finalizeMission();
            mission.write();

            Campaign campaign = PWCGContext.getInstance().getCampaign();
            campaign.setCurrentMission(mission);
        }
    }

    public static void pushEditsToMission(BriefingData briefingData) throws PWCGException
    {
        Mission mission = briefingData.getMission();
        if (!mission.getFinalizer().isFinalized())
        {
            pushWaypointUpdatesToMission(briefingData);
            pushCrewAndPayloadToMission(briefingData);
            pushFuelToMission(briefingData);

        }
    }

    private static void pushWaypointUpdatesToMission(BriefingData briefingData) throws PWCGException
    {
        Mission mission = briefingData.getMission();
        mission.getMissionOptions().getMissionTime().setMissionTime(briefingData.getMissionTime());

        for (IBriefingPlatoon briefingPlatoon : briefingData.getBriefingPlatoons())
        {
            ITankPlatoon platoon = mission.getPlatoons().getPlatoonForCompany(briefingPlatoon.getCompanyId());
            platoon.updateWaypointsFromBriefing(briefingPlatoon.getBriefingPlatoonParameters().getBriefingMapMapPoints());
        }
    }

    private static void pushCrewAndPayloadToMission(BriefingData briefingData) throws PWCGException
    {
        Mission mission = briefingData.getMission();
        for (BriefingPlayerPlatoon briefingPlatoon : briefingData.getPlayerBriefingPlatoons())
        {
            ITankPlatoon playerPlatoon = mission.getPlatoons().getPlayerPlatoonForCompany(briefingPlatoon.getCompanyId());
            BriefingCrewTankUpdater crewePlaneUpdater = new BriefingCrewTankUpdater(mission.getCampaign(), playerPlatoon);
            crewePlaneUpdater.updatePlayerTanks(briefingPlatoon.getBriefingAssignmentData().getCrews());
        }
    }


    private static void pushFuelToMission(BriefingData briefingData) throws PWCGException
    {
        Mission mission = briefingData.getMission();
        mission.getMissionOptions().getMissionTime().setMissionTime(briefingData.getMissionTime());

        for (BriefingPlayerPlatoon briefingPlatoon : briefingData.getPlayerBriefingPlatoons())
        {
            ITankPlatoon playerPlatoon = mission.getPlatoons().getPlayerPlatoonForCompany(briefingPlatoon.getCompanyId());
            playerPlatoon.getPlatoonTanks().setFuelForUnit(briefingPlatoon.getSelectedFuel());
        }
    }

}
