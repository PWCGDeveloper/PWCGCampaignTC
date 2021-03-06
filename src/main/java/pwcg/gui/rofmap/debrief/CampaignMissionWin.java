package pwcg.gui.rofmap.debrief;

import java.util.List;
import java.util.Map;

import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.aar.ui.events.model.CrewMemberStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;

public class CampaignMissionWin
{
    private AARCombatReportPanelData combatResultsForMission;

    public CampaignMissionWin (AARCombatReportPanelData campaignUpdateResults)
    {
        this.combatResultsForMission = campaignUpdateResults;
    }
    
    public boolean isMissionAWin()
    {
        Map<Integer, CrewMemberStatusEvent> crewMembersLost = combatResultsForMission.getCrewMembersLostInMission();
        List<VictoryEvent> victoriesScored = combatResultsForMission.getVictoriesForCrewMembersInMission();
        
        if (crewMembersLost.size() == 0)
        {
            return true;
        }
        
        if (crewMembersLost.size() > victoriesScored.size())
        {
            return false;
        }

        return true;
    }
}
