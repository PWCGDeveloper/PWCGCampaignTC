package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.event.IAType12;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;

public class LogTank extends LogAIEntity
{
    private Coordinate landAt = null;
    private Integer companyId;
    private boolean crashedInSight = false;
    private LogCrewMember logCrewMember;
    private int crewMemberSerialNumber;
    private int tankSerialNumber;
    private int tankStatus = TankStatus.STATUS_DEPLOYED;
    private LogTurrets turrets = new LogTurrets();

    public LogTank(int sequenceNumber)
    {
        super(sequenceNumber);
    }

    public Coordinate getLandAt()
    {
        return landAt;
    }

    public void setLandAt(Coordinate landAt)
    {
        this.landAt = landAt;
    }

    public void initializeFromMissionPlane(PwcgGeneratedMissionVehicleData missionPlane)
    {        
        this.companyId = missionPlane.getCompanyId();
        this.crewMemberSerialNumber = missionPlane.getCrewMemberSerialNumber();
        this.tankSerialNumber = missionPlane.getVehicleSerialNumber();
        intializeCrewMember(missionPlane.getCrewMemberSerialNumber());
    }

    public void initializeFromOutOfMission(Campaign campaign, EquippedTank tank, CrewMember crewMember) throws PWCGException
    {
        this.companyId = crewMember.getCompanyId();
        this.crewMemberSerialNumber = crewMember.getSerialNumber();
        this.tankSerialNumber = tank.getSerialNumber();

        super.setId(""+super.getSequenceNum());
        super.setCountry(crewMember.determineCountry(campaign.getDate()));
        super.setName(crewMember.getNameAndRank());
        super.setVehicleType(tank.getDisplayName());
        super.setRoleCategory(tank.determinePrimaryRoleCategory());
    }

    public void intializeCrewMember(int serialNumber)
    {
        logCrewMember = new LogCrewMember();
        logCrewMember.setSerialNumber(serialNumber);
    }
    
    public void mapBotToCrew(String botId) throws PWCGException
    {
        if (logCrewMember != null)
        {
            logCrewMember.setBotId(botId);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "While mapping bot = No crew member found for bot: " + botId);
        }
    }

    public LogTurret createTurret(IAType12 atype12) throws PWCGException
    {
        return turrets.createTurret(atype12, this);
    }

    public boolean ownsTurret(String turretId) throws PWCGException
    {
        return turrets.hasTurret(turretId);
    }

    public boolean isWithPlane(String searchId)
    {
        if (getId().equals(searchId))
        {
            return true;
        }
        if (isBot(searchId))
        {
            return true;
        }
 
        return false;
    }

    public boolean isBot(String botId)
    {
        if (logCrewMember.getBotId().equals(botId))
        {
            return true;
        }
        
        return false;
    }

    public boolean isCrewMember(int serialNumber)
    {
        if (logCrewMember.getSerialNumber() == serialNumber)
        {
            return true;
        }
        
        return false;
    }

    public boolean isLogPlaneFromPlayerCompany(Campaign campaign) throws PWCGException
    {
        CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(crewMemberSerialNumber);
        if (crewMember != null)
        {
            if (Company.isPlayerCompany(campaign, companyId))
            {
                return true;
            }
        }
        
        return false;
    }
    
    public CrewMember getCrewMemberForLogEvent(Campaign campaign) throws PWCGException
    {
        CrewMember crewMember = null;
        crewMember = campaign.getPersonnelManager().getAnyCampaignMember(getCrewMemberSerialNumber());
        return crewMember;
    }

    public LogCrewMember getLogCrewMember()
    {
        return logCrewMember;
    }

    public boolean isCrashedInSight()
    {
        return crashedInSight;
    }

    public void setCrashedInSight(boolean crashedInSight)
    {
        this.crashedInSight = crashedInSight;
    }    

    public Integer getCompanyId()
    {
        return companyId;
    }

    public void setCompanyId(Integer companyId)
    {
        this.companyId = companyId;
    }

    public int getCrewMemberSerialNumber()
    {
        return crewMemberSerialNumber;
    }

    public void setCrewMemberSerialNumber(int crewMemberSerialNumber)
    {
        this.crewMemberSerialNumber = crewMemberSerialNumber;
    }

    public int getTankSerialNumber()
    {
        return tankSerialNumber;
    }

    public void setTankSerialNumber(int tankSerialNumber)
    {
        this.tankSerialNumber = tankSerialNumber;
    }

    public int getTankStatus()
    {
        return tankStatus;
    }

    public void setTankStatus(int tankStatus)
    {
        this.tankStatus = tankStatus;
    }
}
