package pwcg.mission.platoon;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.mission.ICompanyMission;
import pwcg.mission.Mission;

public class PlatoonInformation
{
    
    private Mission mission;
    private Campaign campaign;
    private ICompanyMission company;
    private List<CrewMember> crewMembers;

    public PlatoonInformation(Mission mission, ICompanyMission company, List<CrewMember> crewMembers)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.company = company;
        this.crewMembers = crewMembers;
    }
     
    public ICountry getCountry()
    {
        return company.getCountry();
    }

    public Mission getMission()
    {
        return mission;
    }

    public ICompanyMission getCompany()
    {
        return company;
    }

    public String getBase()
    {
        return company.determineBaseName(campaign.getDate());
    }

    public Campaign getCampaign()
    {
        return campaign;
    }

    public List<CrewMember> getParticipatingPlayersForCompany()
    {
        return crewMembers;
    }

    public List<CrewMember> getCrewMembers()
    {
        return crewMembers;
    }
}
