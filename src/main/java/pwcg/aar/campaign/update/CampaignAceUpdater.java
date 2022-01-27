package pwcg.aar.campaign.update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.crewmember.Victory;

public class CampaignAceUpdater 
{
	private Campaign campaign = null;
	
    private Map<Integer, List<Victory>> aceVictories = new HashMap<>();
	
	public CampaignAceUpdater (Campaign campaign, Map<Integer, List<Victory>> aceVictories) 
	{
        this.campaign = campaign;
        this.aceVictories = aceVictories;
	}
	

	public void updatesCampaignAces() 
	{
		updateAceMissionsCompleted();

		updateHistoricalVictories();
	}


    private void updateAceMissionsCompleted()
    {
        for (TankAce ace : campaign.getPersonnelManager().getCampaignAces().getActiveCampaignAces().values())
		{
			if (ace.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_ACTIVE)
			{
				int missionCompleted = ace.getBattlesFought();
				missionCompleted += 3;
				ace.setBattlesFought(missionCompleted);
			}
		}
    }


    private void updateHistoricalVictories()
    {
        for (Integer serialNumber : aceVictories.keySet())
		{
			TankAce ace = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(serialNumber);
			if (ace != null)
			{
    			for (Victory victory : aceVictories.get(serialNumber)) 
    			{
    				ace.addGroundVictory(victory);
    			}
			}
		}
    }
 }
