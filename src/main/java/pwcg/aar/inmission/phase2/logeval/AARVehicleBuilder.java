package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBalloon;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogGroundUnit;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTurret;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.SerialNumber.SerialNumberClassification;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.IAType12;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;

public class AARVehicleBuilder
{
    private AARBotVehicleMapper botPlaneMapper;
    private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;

    private Map<String, LogTank> logTanks = new HashMap<>();
    private Map<String, LogBalloon> logBalloons = new HashMap<>();
    private Map<String, LogGroundUnit> logGroundUnits = new HashMap<>();
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
        else if (logBalloons.containsKey(id))
        {
            return logBalloons.get(id);
        }
        else if (logTurrets.containsKey(id))
        {
            return findEntityForTurret(id);
        }

        return null;
    }

    public LogAIEntity getPlaneByName(Integer serialNumber) throws PWCGException
    {
        for (LogTank logTank : logTanks.values())
        {
            if (logTank.isCrewMember(serialNumber))
            {
                return logTank;
            }
        }

        return null;
    }

    public List<LogTank> getPlayerLogPlanes()
    {
        List<LogTank> playerLogPlanes = new ArrayList<>();
        for (LogTank logTank : logTanks.values())
        {
            if (SerialNumber.getSerialNumberClassification(logTank.getCrewMemberSerialNumber()) == SerialNumberClassification.PLAYER)
            {
                playerLogPlanes.add(logTank);
            }
        }
        return playerLogPlanes;
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
        if (pwcgMissionDataEvaluator.wasCrewMemberAssignedToMissionByName(atype12.getName()))
        {
            createLogPlane(atype12);
        }
        else
        {
            createLogGroundUnit(atype12);
        }
    }

    private void createLogPlane(IAType12 atype12) throws PWCGException
    {
        LogTank logTank = makeTankFromMissionAndLog(atype12);

        logTanks.put(atype12.getId(), logTank);
        PWCGLogger.log(LogLevel.DEBUG, "Add Plane: " + atype12.getName() + " ID:" + atype12.getId() + " Type:" + atype12.getType());
    }

    private LogTank makeTankFromMissionAndLog(IAType12 atype12) throws PWCGException
    {
        PwcgGeneratedMissionVehicleData missionPlane = pwcgMissionDataEvaluator.getPlaneForCrewMemberByName(atype12.getName());
        LogTank logTank = new LogTank(atype12.getSequenceNum());
        logTank.initializeEntityFromEvent(atype12);
        logTank.initializeFromMissionPlane(missionPlane);
        return logTank;
    }

    private void createLogGroundUnit(IAType12 atype12) throws PWCGException
    {
        LogAIEntity logEntity;
        logEntity = new LogGroundUnit(atype12.getSequenceNum());
        logEntity.initializeEntityFromEvent(atype12);

        logGroundUnits.put(atype12.getId(), (LogGroundUnit) logEntity);
        PWCGLogger.log(LogLevel.DEBUG, "Add Entity: " + atype12.getName() + " ID:" + atype12.getId() + " Type:" + atype12.getType());
    }

    private void createTurretEntitiesForVehicle(List<IAType12> turretList) throws PWCGException
    {
        for (IAType12 atype12 : turretList)
        {
            LogTank planeResult = logTanks.get(atype12.getPid());
            if (planeResult != null)
            {
                logTurrets.put(atype12.getId(), planeResult.createTurret(atype12));
            }

            LogGroundUnit groundUnitResult = logGroundUnits.get(atype12.getPid());
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

        for (LogGroundUnit logGroundUnit : logGroundUnits.values())
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

    public Map<String, LogBalloon> getLogBalloons()
    {
        return logBalloons;
    }

    public Map<String, LogGroundUnit> getLogGroundUNits()
    {
        return logGroundUnits;
    }
}
