package pwcg.campaign.crewmember;

import java.util.Date;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogGroundUnit;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTurret;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class VictoryEntity
{
    private int airOrGround = Victory.UNSPECIFIED_VICTORY;
    private String name = "";
    private String type = "";
    private String companyName = "";
    private Integer crewMemberSerialNumber = SerialNumber.NO_SERIAL_NUMBER;
    private String crewMemberName = CrewMember.UNKNOWN_CREW_NAME;
    private int crewMemberStatus = CrewMemberStatus.STATUS_ACTIVE;
    private boolean isGunner = false;

    public VictoryEntity()
    {
        super();
    }

    public void initialize (Date victoryDate, LogAIEntity logEntity, String crewMemberName) throws PWCGException
    {
        if (logEntity instanceof LogTank)
        {
            LogTank logPlane = (LogTank)logEntity;
            initializeForTank(victoryDate, logPlane, crewMemberName);
        }
        if (logEntity instanceof LogPlane)
        {
            LogPlane logPlane = (LogPlane)logEntity;
            initializeForPlane(victoryDate, logPlane, crewMemberName);
        }
        else if (logEntity instanceof LogGroundUnit)
        {
            LogGroundUnit logGroundUnit = (LogGroundUnit)logEntity;
            initializeForGround(logGroundUnit);
        }
        else if (logEntity instanceof LogTurret)
        {
            LogTurret logTurret = (LogTurret)logEntity;
            initializeForTurret(victoryDate, logTurret, crewMemberName);
        }
    }

    public boolean determineCompleteForAir()
    {
        if (type == null || type.isEmpty())
        {
            return false;
        }

        return true;
    }

    private void initializeForPlane(Date victoryDate, LogPlane logPlane, String crewMemberName) throws PWCGException
    {
        airOrGround = Victory.AIRCRAFT;
        setType(logPlane.getVehicleType());
        name = logPlane.getName();
    }

    private void initializeForTank(Date victoryDate, LogTank logTank, String crewMemberName) throws PWCGException
    {
        if (!crewMemberName.equals(CrewMember.UNKNOWN_CREW_NAME))
        {
            LogCrewMember logCrewMember = logTank.getLogCrewMember();
            this.crewMemberName = crewMemberName;
            this.crewMemberStatus = logCrewMember.getStatus();
            this.crewMemberSerialNumber = logCrewMember.getSerialNumber();
            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(logTank.getCompanyId());
            if (company != null)
            {
                this.companyName = company.determineDisplayName(victoryDate);
            }
        }

        airOrGround = Victory.VEHICLE;
        setType(logTank.getVehicleType());
        name = logTank.getName();
    }

    private void initializeForGround(LogGroundUnit logGrountUnit) throws PWCGException
    {
        airOrGround = Victory.VEHICLE;
        setType(logGrountUnit.getVehicleType());
        name = logGrountUnit.getName();
    }

    private void initializeForTurret(Date victoryDate, LogTurret logTurret, String crewMemberName) throws PWCGException
    {
        if (!(logTurret.getParent() instanceof LogTank))
            throw new PWCGException("Parent of turret is not a plane");

        LogTank logPlane = (LogTank) logTurret.getParent();
        initializeForTank(victoryDate, logPlane, crewMemberName);
        isGunner = true;
    }

    public int getAirOrGround()
    {
        return airOrGround;
    }

    public void setAirOrGround(int airOrGround)
    {
        this.airOrGround = airOrGround;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String vehicleType)
    {
        if (vehicleType.contains("["))
        {
            int indexEnd = vehicleType.indexOf("[");
            vehicleType = vehicleType.substring(0, indexEnd);
        }

        this.type = vehicleType;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public Integer getCrewMemberSerialNumber()
    {
        return crewMemberSerialNumber;
    }

    public void setCrewMemberSerialNumber(Integer crewMemberSerialNumber)
    {
        this.crewMemberSerialNumber = crewMemberSerialNumber;
    }

    public int getCrewMemberStatus()
    {
        return crewMemberStatus;
    }

    public void setCrewMemberStatus(int crewMemberStatus)
    {
        this.crewMemberStatus = crewMemberStatus;
    }

    public String getCrewMemberName()
    {
        return crewMemberName;
    }

    public void setCrewMemberName(String crewMemberName)
    {
        this.crewMemberName = crewMemberName;
    }

    public boolean isGunner() {
        return isGunner;
    }

    public void setGunner(boolean isGunner) {
        this.isGunner = isGunner;
    }
}