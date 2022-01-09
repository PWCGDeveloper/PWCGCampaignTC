package pwcg.campaign.skirmish;

import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionHumanParticipants;

public class SkirmishBuilder
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;

    public SkirmishBuilder(Campaign campaign, MissionHumanParticipants participatingPlayers)
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
    }

    public Skirmish chooseBestSkirmish() throws PWCGException
    {
        SkirmishManager skirmishManager = PWCGContext.getInstance().getCurrentMap().getSkirmishManager();
        List<Skirmish> skirmishes = skirmishManager.getSkirmishesForDate(campaign, participatingPlayers);
        if (skirmishes.isEmpty())
        {
            return null;
        }
        Collections.shuffle(skirmishes);
        return skirmishes.get(0);
    }
}
