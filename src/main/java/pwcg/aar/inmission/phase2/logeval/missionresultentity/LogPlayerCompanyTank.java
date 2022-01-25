package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;

public class LogPlayerCompanyTank
{
    private Integer companyId = Company.AI;
    private LogCrewMember logCrewMember= new LogCrewMember();
    private int crewMemberSerialNumber = SerialNumber.NO_SERIAL_NUMBER;
    private int tankSerialNumber = SerialNumber.NO_SERIAL_NUMBER;
    
    public void initializeFromMissionTank(PwcgGeneratedMissionVehicleData missionTank)
    {        
        this.companyId = missionTank.getCompanyId();
        this.crewMemberSerialNumber = missionTank.getCrewMemberSerialNumber();
        this.tankSerialNumber = missionTank.getVehicleSerialNumber();
        intializeCrewMember(missionTank.getCrewMemberSerialNumber());
    }
    
    public void initializeFromOutOfMission(EquippedTank tank, CrewMember crewMember) throws PWCGException
    {
        this.companyId = crewMember.getCompanyId();
        this.crewMemberSerialNumber = crewMember.getSerialNumber();
        this.tankSerialNumber = tank.getSerialNumber();
    }


    private void intializeCrewMember(int serialNumber)
    {
        logCrewMember = new LogCrewMember();
        logCrewMember.setSerialNumber(serialNumber);
    }
    
    public void mapBotToCrew(String botId) throws PWCGException
    {
        logCrewMember.setBotId(botId);
    }

    public boolean isCrewMember(int serialNumber)
    {
        if (logCrewMember.getSerialNumber() == serialNumber)
        {
            return true;
        }
        
        return false;
    }

    public Integer getCompanyId()
    {
        return companyId;
    }

    public LogCrewMember getLogCrewMember()
    {
        return logCrewMember;
    }

    public int getCrewMemberSerialNumber()
    {
        return crewMemberSerialNumber;
    }

    public int getTankSerialNumber()
    {
        return tankSerialNumber;
    }

    public void setCompanyId(Integer companyId)
    {
        this.companyId = companyId;
    }

    public void setLogCrewMember(LogCrewMember logCrewMember)
    {
        this.logCrewMember = logCrewMember;
    }

    public void setCrewMemberSerialNumber(int crewMemberSerialNumber)
    {
        this.crewMemberSerialNumber = crewMemberSerialNumber;
    }

    public void setTankSerialNumber(int tankSerialNumber)
    {
        this.tankSerialNumber = tankSerialNumber;
    }
}
