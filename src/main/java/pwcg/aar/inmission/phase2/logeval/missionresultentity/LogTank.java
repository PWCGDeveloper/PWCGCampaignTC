package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.event.IAType12;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;

public class LogTank extends LogAIEntity
{
    private int tankStatus = TankStatus.STATUS_DEPLOYED;
    private LogTurrets turrets = new LogTurrets();
    private LogPlayerCompanyTank playerCompanyTank = new LogPlayerCompanyTank();

    public LogTank(int sequenceNumber)
    {
        super(sequenceNumber);
    }

    public void mapToEquippedTankFromMissionTank(PwcgGeneratedMissionVehicleData missionTank)
    {        
        playerCompanyTank.initializeFromMissionTank(missionTank);
    }

    public void mapToEquippedTankForTest(Campaign campaign, EquippedTank tank, CrewMember crewMember) throws PWCGException
    {
        playerCompanyTank.initializeFromOutOfMission(tank, crewMember);

        super.setId(""+super.getSequenceNum());
        super.setCountry(crewMember.determineCountry(campaign.getDate()));
        super.setName(crewMember.getNameAndRank());
        super.setVehicleType(tank.getDisplayName());
        super.setRoleCategory(tank.determinePrimaryRoleCategory());
    }
    
    public void mapBotToCrew(String botId) throws PWCGException
    {
        playerCompanyTank.mapBotToCrew(botId);
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
        
        if (playerCompanyTank.getLogCrewMember().getBotId().equals(botId))
        {
            return true;
        }
        
        return false;
    }

    public boolean isCrewMember(int serialNumber)
    {
        return playerCompanyTank.isCrewMember(serialNumber);
    }
    
    public CrewMember getCrewMemberForLogEvent(Campaign campaign) throws PWCGException
    {
        CrewMember crewMember = null;
        crewMember = campaign.getPersonnelManager().getAnyCampaignMember(getCrewMemberSerialNumber());
        return crewMember;
    }

    public LogCrewMember getLogCrewMember() throws PWCGException
    {
        return playerCompanyTank.getLogCrewMember();
    }

    public Integer getCompanyId()
    {
        return playerCompanyTank.getCompanyId();
    }

    public void setCompanyId(Integer companyId)
    {
        playerCompanyTank.setCompanyId(companyId);
    }

    public int getCrewMemberSerialNumber()
    {
        return playerCompanyTank.getCrewMemberSerialNumber();
    }

    public void setCrewMemberSerialNumber(int crewMemberSerialNumber)
    {
        playerCompanyTank.setCrewMemberSerialNumber(crewMemberSerialNumber);
    }

    public int getTankSerialNumber()
    {
        return playerCompanyTank.getTankSerialNumber();
    }

    public void setTankSerialNumber(int tankSerialNumber)
    {
        playerCompanyTank.setTankSerialNumber(tankSerialNumber);
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
        if (playerCompanyTank.getCompanyId() != Company.AI)
        {
            return true;
        }
        return false;
    }
}
