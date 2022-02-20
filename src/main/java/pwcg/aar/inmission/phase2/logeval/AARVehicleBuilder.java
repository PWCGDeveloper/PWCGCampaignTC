package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogNonPlayerVehicle;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTurret;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.SerialNumber.SerialNumberClassification;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.IAType12;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class AARVehicleBuilder
{
    private AARBotVehicleMapper botPlaneMapper;
    private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;

    private Map<String, LogTank> logTanks = new HashMap<>();
    private Map<String, LogNonPlayerVehicle> logGroundUnits = new HashMap<>();
    private Map<String, LogTurret> logTurrets = new HashMap<>();

    public AARVehicleBuilder(AARBotVehicleMapper botPlaneMapper, PwcgMissionDataEvaluator pwcgMissionDataEvaluator)
    {
        this.botPlaneMapper = botPlaneMapper;
        this.pwcgMissionDataEvaluator = pwcgMissionDataEvaluator;
    }

    public void buildVehicleListsByVehicleType(LogEventData logEventData) throws PWCGException
    {
        sortVehiclesByType(logEventData.getVehicles());
        createTurretEntitiesForVehicle(logEventData.getTurrets());
        botPlaneMapper.mapBotsToCrews(logTanks);
    }

    public LogAIEntity getVehicle(String id) throws PWCGException
    {
        if (logGroundUnits.containsKey(id))
        {
            return logGroundUnits.get(id);
        }
        else if (logTanks.containsKey(id))
        {
            return logTanks.get(id);
        }
        else if (logTurrets.containsKey(id))
        {
            return findEntityForTurret(id);
        }

        return null;
    }

    public List<LogTank> getPlayerLogTanks()
    {
        List<LogTank> playerLogTanks = new ArrayList<>();
        for (LogTank logTank : logTanks.values())
        {
            if (SerialNumber.getSerialNumberClassification(logTank.getCrewMemberSerialNumber()) == SerialNumberClassification.PLAYER)
            {
                playerLogTanks.add(logTank);
            }
        }
        return playerLogTanks;
    }

    private void sortVehiclesByType(List<IAType12> vehicleList) throws PWCGException
    {
        for (IAType12 atype12 : vehicleList)
        {
            sortLogEntity(atype12);
        }

        if (logTanks.isEmpty())
        {
            throw new PWCGException("No planes found in logs to associate with the latest mission");
        }
    }

    private void sortLogEntity(IAType12 atype12) throws PWCGException
    {
        if (isTank(atype12))
        {
            createLogTank(atype12);
        }
        else
        {
            createLogAiUnit(atype12);
        }
    }

    private boolean isTank(IAType12 atype12) throws PWCGException
    {
        if (pwcgMissionDataEvaluator.wasCrewMemberAssignedToMissionByName(atype12.getName()))
        {
            return true;
        }
        else
        {
            VehicleDefinition vehicle = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinition(atype12.getName());
            if (vehicle != null && vehicle.getVehicleClass() == VehicleClass.Tank)
            {
                return true;
            }

            vehicle = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinition(atype12.getType());
            if (vehicle != null && vehicle.getVehicleClass() == VehicleClass.Tank)
            {
                return true;
            }
        }

        return false;
    }

    private void createLogTank(IAType12 atype12) throws PWCGException
    {
        LogTank logTank = makeTankFromMissionAndLog(atype12);

        logTanks.put(atype12.getId(), logTank);
        PWCGLogger.log(LogLevel.INFO, "Add Tank: " + atype12.getName() + " ID:" + atype12.getId() + " Type:" + atype12.getType());
    }

    private LogTank makeTankFromMissionAndLog(IAType12 atype12) throws PWCGException
    {
        LogTank logTank = new LogTank(atype12.getSequenceNum());
        logTank.initializeEntityFromEvent(atype12);

        PwcgGeneratedMissionVehicleData missionTank = pwcgMissionDataEvaluator.getTankForCrewMemberByName(atype12.getName());
        if (missionTank != null)
        {
            logTank.mapToEquippedTankFromMissionTank(missionTank);
        }

        return logTank;
    }

    private void createLogAiUnit(IAType12 atype12) throws PWCGException
    {
        LogAIEntity logEntity;
        logEntity = new LogNonPlayerVehicle(atype12.getSequenceNum());
        logEntity.initializeEntityFromEvent(atype12);

        logGroundUnits.put(atype12.getId(), (LogNonPlayerVehicle) logEntity);
        PWCGLogger.log(LogLevel.DEBUG, "Add Entity: " + atype12.getName() + " ID:" + atype12.getId() + " Type:" + atype12.getType());
    }

    private void createTurretEntitiesForVehicle(List<IAType12> turretList) throws PWCGException
    {
        for (IAType12 atype12 : turretList)
        {
            LogTank tank = logTanks.get(atype12.getPid());
            if (tank != null)
            {
                logTurrets.put(atype12.getId(), tank.createTurret(atype12));
            }

            LogNonPlayerVehicle groundUnitResult = logGroundUnits.get(atype12.getPid());
            if (groundUnitResult != null)
            {
                logTurrets.put(atype12.getId(), groundUnitResult.createTurret(atype12));
            }
        }
    }

    private LogAIEntity findEntityForTurret(String turretId) throws PWCGException
    {
        for (LogTank logTank : logTanks.values())
        {
            if (logTank.ownsTurret(turretId))
            {
                return logTank;
            }
        }

        for (LogNonPlayerVehicle logGroundUnit : logGroundUnits.values())
        {
            if (logGroundUnit.ownsTurret(turretId))
            {
                return logGroundUnit;
            }
        }

        return null;
    }

    public Map<String, LogTank> getLogTanks()
    {
        return logTanks;
    }

    public Map<String, LogNonPlayerVehicle> getLogGroundUNits()
    {
        return logGroundUnits;
    }
}
