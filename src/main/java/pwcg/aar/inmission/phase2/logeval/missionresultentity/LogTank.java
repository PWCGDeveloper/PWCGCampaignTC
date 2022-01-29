package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.event.IAType12;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;

public class LogTank extends LogAIEntity
{
    private int tankStatus = TankStatus.STATUS_DEPLOYED;
    private LogTurrets turrets = new LogTurrets();
    private int tankSerialNumber = SerialNumber.NO_SERIAL_NUMBER;
    private LogCrewMember logCrewMember= new LogCrewMember();
    private Integer companyId = Company.AI;

    public LogTank(int sequenceNumber)
    {
        super(sequenceNumber);
    }

    public void mapToEquippedTankFromMissionTank(PwcgGeneratedMissionVehicleData missionTank)
    {        
        this.logCrewMember.setSerialNumber(missionTank.getCrewMemberSerialNumber());
        this.tankSerialNumber = missionTank.getVehicleSerialNumber();
        this.logCrewMember.setSerialNumber(missionTank.getCrewMemberSerialNumber());
        this.companyId = missionTank.getCompanyId();
    }

    public void mapToEquippedTankForTest(Campaign campaign, EquippedTank tank, CrewMember crewMember) throws PWCGException
    {
        this.logCrewMember.setSerialNumber(crewMember.getSerialNumber());
        this.tankSerialNumber = tank.getSerialNumber();
        this.companyId = crewMember.getCompanyId();

        super.setId(""+super.getSequenceNum());
        super.setCountry(crewMember.determineCountry(campaign.getDate()));
        super.setName(crewMember.getNameAndRank());
        super.setVehicleType(tank.getDisplayName());
        super.setRoleCategory(tank.determinePrimaryRoleCategory());
    }
    
    public void mapBotToCrew(String botId) throws PWCGException
    {
        logCrewMember.setBotId(botId);
    }

    public LogTurret createTurret(IAType12 atype12) throws PWCGException
    {
        return turrets.createTurret(atype12, this);
    }

    public boolean ownsTurret(String turretId) throws PWCGException
    {
        return turrets.hasTurret(turretId);
    }

    public boolean isWithTank(String searchId)
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
    
    public CrewMember getCrewMemberForLogEvent(Campaign campaign) throws PWCGException
    {
        CrewMember crewMember = null;
        crewMember = campaign.getPersonnelManager().getAnyCampaignMember(getCrewMemberSerialNumber());
        return crewMember;
    }

    public LogCrewMember getLogCrewMember() throws PWCGException
    {
        return logCrewMember;
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
        return logCrewMember.getSerialNumber();
    }

    public void setCrewMemberSerialNumber(int crewMemberSerialNumber)
    {
        logCrewMember.setSerialNumber(crewMemberSerialNumber);
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

    public boolean isEquippedTank()
    {
        if (companyId != Company.AI)
        {
            return true;
        }
        return false;
    }
}
