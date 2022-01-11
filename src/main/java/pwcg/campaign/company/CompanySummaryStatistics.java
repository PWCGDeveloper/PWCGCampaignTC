package pwcg.campaign.company;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMemberVictories;
import pwcg.core.exception.PWCGException;

public class CompanySummaryStatistics 
{
    private Campaign campaign = null;
    private int logsForCompanyId = 0;
    
    private int numKilled = 0;
    private int numMaimed = 0;
    private int numCaptured = 0;
    private int numAirToAirVictories = 0;
    private int numTankKills = 0;
    private int numTrainKills = 0;
    private int numGroundUnitKills = 0;

    public CompanySummaryStatistics (Campaign campaign, int logsForCompanyId)
    {
        this.campaign = campaign;
        this.logsForCompanyId = logsForCompanyId;
    }
    
    public void calculateStatistics() throws PWCGException
    {
        for (CrewMember crewMember : campaign.getPersonnelManager().getCompanyPersonnel(logsForCompanyId).getCrewMembers().getCrewMemberList())
        {
            if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_KIA)
            {
                ++numKilled;
            }
            else if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_CAPTURED)
            {
                ++numCaptured;
            }
            else if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED)
            {
                ++numMaimed;
            }
            
            CrewMemberVictories companyMemberVictories = crewMember.getCrewMemberVictories();
            numAirToAirVictories += companyMemberVictories.getAirToAirVictoryCount();
            numTankKills += companyMemberVictories.getTankVictoryCount();
            numTrainKills += companyMemberVictories.getTrainVictoryCount();
            numGroundUnitKills += companyMemberVictories.getGroundVictoryCount();
        }
    }

    public int getNumKilled()
    {
        return numKilled;
    }

    public int getNumMaimed()
    {
        return numMaimed;
    }

    public int getNumCaptured()
    {
        return numCaptured;
    }

    public int getCrewMembersLostTotal()
    {
        return numKilled + numCaptured + numMaimed;
    }

    public int getNumAirToAirVictories()
    {
        return numAirToAirVictories;
    }

    public int getNumTankKills()
    {
        return numTankKills;
    }

    public int getNumTrainKills()
    {
        return numTrainKills;
    }

    public int getNumGroundUnitKills()
    {
        return numGroundUnitKills;
    }

    public int getGroundKillsTotal()
    {
        return numTankKills + numTrainKills + numGroundUnitKills;
    }
}
