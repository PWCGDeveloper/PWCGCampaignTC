package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.ground.builder.IndirectFireAssignmentHandler;
import pwcg.mission.mcu.group.MissionObjectiveGroup;
import pwcg.mission.mcu.group.StopAttackingNearAirfieldSequence;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.unit.TankMcu;

public class MissionFinalizer
{
    private Mission mission;
    private Campaign campaign;
    private boolean isFinalized = false;
    private MissionFrontLineIconBuilder frontLineIconBuilder;
    private MissionWaypointIconBuilder waypointIconBuilder = new MissionWaypointIconBuilder();
    private MissionAssaultIconBuilder assaultIconBuilder = new MissionAssaultIconBuilder();
    private List<StopAttackingNearAirfieldSequence> stopSequenceForMission = new ArrayList<>();
    private MissionObjectiveGroup missionObjectiveSuccess = new MissionObjectiveGroup();
    private MissionObjectiveGroup missionObjectiveFailure = new MissionObjectiveGroup();

    public MissionFinalizer(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }

    public void finalizeMission() throws PWCGException
    {
        if (!isFinalized)
        {
            frontLineIconBuilder = new MissionFrontLineIconBuilder(campaign);

            setMissionScript(mission.getMissionOptions());

            mission.getPlatoons().finalizeMissionUnits();
            mission.getGroundUnitBuilder().finalizeGroundUnits();
            
            frontLineIconBuilder.buildFrontLineIcons();
            waypointIconBuilder.createWaypointIcons(mission.getPlatoons().getPlayerUnits());
            assaultIconBuilder.createAssaultIcons(mission.getBattleManager().getMissionAssaultDefinitions());
            mission.getMissionBlocks().adjustBlockDamageAndSmoke();

            setGroundUnitTriggers();
            assignIndirectFireTargets();

            if (mission.getCampaign().getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
            {
                finalizeForSinglePlayer();
            }
        }

        getGroundUnitCount();
        
        isFinalized = true;
    }

    private void setMissionScript(MissionOptions missionOptions) throws PWCGException
    {
        List<TankMcu> playerVehicles = mission.getPlatoons().getReferencePlayerUnit().getTanks();
        String playerScript = playerVehicles.get(0).getScript();
        missionOptions.setPlayerConfig(playerScript);
    }

    private void setGroundUnitTriggers() throws PWCGException
    {
        MissionCheckZoneTriggerBuilder missionCheckZoneTriggerBuilder = new MissionCheckZoneTriggerBuilder(mission);
        missionCheckZoneTriggerBuilder.triggerGroundUnits();
    }

    private void assignIndirectFireTargets() throws PWCGException
    {
        IndirectFireAssignmentHandler indirectFireAssignmentHandler = new IndirectFireAssignmentHandler(mission);
        indirectFireAssignmentHandler.makeIndirectFireAssignments();
    }

    private void finalizeForSinglePlayer() throws PWCGException
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            missionObjectiveSuccess.createSuccessMissionObjective(campaign, mission);
            missionObjectiveFailure.createFailureMissionObjective(campaign, mission);
        }
    }

    private int getGroundUnitCount() throws PWCGException
    {
        int unitCountMissionGroundUnits = mission.getGroundUnitBuilder().getUnitCount();        
        int unitCountInFlights = 0;

        int unitCountInMission = 0;
        unitCountInMission += unitCountInFlights;
        unitCountInMission += unitCountMissionGroundUnits;

        PWCGLogger.log(LogLevel.INFO, "unit count flights : " + unitCountInFlights);
        PWCGLogger.log(LogLevel.INFO, "unit count misson : " + unitCountMissionGroundUnits);
        PWCGLogger.log(LogLevel.INFO, "unit count total : " + unitCountInMission);

        return unitCountInMission;
    }

    public MissionFrontLineIconBuilder getFrontLineIconBuilder()
    {
        return frontLineIconBuilder;
    }

    public void setFrontLineIconBuilder(MissionFrontLineIconBuilder frontLineIconBuilder)
    {
        this.frontLineIconBuilder = frontLineIconBuilder;
    }

    public MissionAssaultIconBuilder getAssaultIconBuilder()
    {
        return assaultIconBuilder;
    }

    public void setAssaultIconBuilder(MissionAssaultIconBuilder assaultIconBuilder)
    {
        this.assaultIconBuilder = assaultIconBuilder;
    }

    public boolean isFinalized()
    {
        return isFinalized;
    }

    public MissionWaypointIconBuilder getWaypointIconBuilder()
    {
        return waypointIconBuilder;
    }

    public List<StopAttackingNearAirfieldSequence> getStopSequenceForMission()
    {
        return stopSequenceForMission;
    }

    public MissionObjectiveGroup getMissionObjectiveSuccess()
    {
        return missionObjectiveSuccess;
    }

    public MissionObjectiveGroup getMissionObjectiveFailure()
    {
        return missionObjectiveFailure;
    }
}
