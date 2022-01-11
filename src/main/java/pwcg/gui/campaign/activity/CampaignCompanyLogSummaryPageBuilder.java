package pwcg.gui.campaign.activity;

import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.CompanySummaryStatistics;
import pwcg.core.exception.PWCGException;

public class CampaignCompanyLogSummaryPageBuilder
{
    private Campaign campaign = null;
    private int logsForCompanyId = 0;

    public CampaignCompanyLogSummaryPageBuilder (Campaign campaign, int logsForCompanyId)
	{        
        this.campaign = campaign;
        this.logsForCompanyId = logsForCompanyId;
	}

    public Map<Integer, StringBuffer> buildSummaryPage() throws PWCGException
	{
        CompanySummaryStatistics companySummaryStatistics = new CompanySummaryStatistics(campaign, logsForCompanyId);
        companySummaryStatistics.calculateStatistics();
        
		Map<Integer, StringBuffer> companyDetailPages = new TreeMap<Integer, StringBuffer>();

		StringBuffer summaryPageBuffer = new StringBuffer("\n\n");
        
        summaryPageBuffer.append("Company Members Killed: " + companySummaryStatistics.getNumKilled() + "\n");
        summaryPageBuffer.append("Company Members Captured: " + companySummaryStatistics.getNumCaptured() + "\n");
        summaryPageBuffer.append("Company Members Lost To Wounds: " + companySummaryStatistics.getNumMaimed() + "\n");
        summaryPageBuffer.append("Company Members Lost Total: " + companySummaryStatistics.getCrewMembersLostTotal() + "\n");

        summaryPageBuffer.append("Company Air To Air Victories: " + companySummaryStatistics.getNumAirToAirVictories() + "\n");
        summaryPageBuffer.append("Company Tank Kills: " + companySummaryStatistics.getNumTankKills() + "\n");
        summaryPageBuffer.append("Company Train Kills: " + companySummaryStatistics.getNumTrainKills() + "\n");
        summaryPageBuffer.append("Company Ground Kills: " + companySummaryStatistics.getNumGroundUnitKills() + "\n");
        summaryPageBuffer.append("Company Total; Ground Kills: " + companySummaryStatistics.getGroundKillsTotal() + "\n");

		companyDetailPages.put(0, summaryPageBuffer);
		
        return companyDetailPages;
	}
}
