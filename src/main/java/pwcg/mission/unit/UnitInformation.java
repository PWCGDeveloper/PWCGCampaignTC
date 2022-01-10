package pwcg.mission.unit;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;

public class UnitInformation
{
    
    private Mission mission;
    private Campaign campaign;
    private Company company;
    private List<CrewMember> crewMembers;
    private UnitObjectiveDefinition objective;

    public UnitInformation(Mission mission, Company company, List<CrewMember> crewMembers, UnitObjectiveDefinition objective)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.company = company;
        this.crewMembers = crewMembers;
        this.objective = objective;
    }
    
    public double calcAngleToObjective() throws PWCGException
    {
        return objective.calcAngleToObjective();
    }
    
    public ICountry getCountry()
    {
        return company.getCountry();
    }

    public Mission getMission()
    {
        return mission;
    }

    public Company getCompany()
    {
        return company;
    }

    public String getBase()
    {
        return company.determineCurrentAirfieldName(campaign.getDate());
    }

    public Campaign getCampaign()
    {
        return campaign;
    }

    public List<CrewMember> getParticipatingPlayersForCompany()
    {
        return crewMembers;
    }
    
    public UnitMissionType getMissionType()
    {
        return objective.getMissionType();
    }
    
    public UnitObjectiveDefinition getObjective()
    {
        return objective;
    }

}
