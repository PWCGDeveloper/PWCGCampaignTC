package pwcg.mission.unit;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;

public class PlatoonInformation
{
    
    private Mission mission;
    private Campaign campaign;
    private Company company;
    private List<CrewMember> crewMembers;
    private PlatoonObjectiveDefinition objective;

    public PlatoonInformation(Mission mission, Company company, List<CrewMember> crewMembers, PlatoonObjectiveDefinition objective)
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
    
    public PlatoonMissionType getMissionType()
    {
        return objective.getMissionType();
    }
    
    public PlatoonObjectiveDefinition getObjective()
    {
        return objective;
    }

}
