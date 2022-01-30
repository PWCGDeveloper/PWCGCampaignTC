package pwcg.aar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;

public class ExpectedResults
{
    private Campaign campaign;
    private List<Integer> lostCrewMembers = new ArrayList<>();
    private int playerTankVictories = 0;
    private int playerGroundVictories = 0;
    private int companyMemberTankVictories = 0;
    private int companyMemberGroundVictories = 0;
    private int enemyTankVictories = 0;

    public ExpectedResults (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public void buildExpectedResultsFromAARContext(AARContext aarContext)
    {
        addLostCrewMembers(aarContext.getPersonnelLosses().getPersonnelKilled());
        addLostCrewMembers(aarContext.getPersonnelLosses().getPersonnelCaptured());
        addLostCrewMembers(aarContext.getPersonnelLosses().getPersonnelMaimed());
    }

    private void addLostCrewMembers(Map<Integer, CrewMember> killedCrewMembers)
    {
        for(CrewMember lostCrewMember : killedCrewMembers.values())
        {
            lostCrewMembers.add(lostCrewMember.getSerialNumber());
        }
    }

    public Campaign getCampaign()
    {
        return campaign;
    }

    public void setCampaign(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<Integer> getLostCrewMembers()
    {
        return lostCrewMembers;
    }

    public int getPlayerTankVictories()
    {
        return playerTankVictories;
    }

    public void addPlayerTankVictories()
    {
        ++playerTankVictories;
    }

    public void addEnemyTankVictories()
    {
        ++enemyTankVictories;
    }

    public int getEnemyTankVictories()
    {
        return enemyTankVictories;
    }

    public int getPlayerGroundVictories()
    {
        return playerGroundVictories;
    }

    public void addPlayerGroundVictories()
    {
        ++playerGroundVictories;
    }

    public int getCrewMemberTankVictories()
    {
        return companyMemberTankVictories;
    }

    public void addCrewMemberTankVictories()
    {
        ++companyMemberTankVictories;
    }

    public int getCrewMemberGroundVictories()
    {
        return companyMemberGroundVictories;
    }

    public void addCrewMemberGroundVictories()
    {
        ++companyMemberGroundVictories;
    }


}
