package pwcg.aar.prelim;

import java.util.Date;
import java.util.List;

import pwcg.aar.inmission.phase1.parse.AARMissionFileLogResultMatcher;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogFileSet;
import pwcg.core.logfiles.LogSetFinder;
import pwcg.core.utils.DateUtils;

public class AARMostRecentLogSetFinder
{
    private AARMissionFileLogResultMatcher matcher;
    private LogSetFinder logSetFinder;
    private AARPwcgMissionFinder pwcgMissionFinder;
    private LogFileSet logFileSet;
    private PwcgMissionData pwcgMissionData;
    private Campaign campaign;

    public AARMostRecentLogSetFinder(
            Campaign campaign, 
            AARMissionFileLogResultMatcher matcher, 
            LogSetFinder logSetFinder,
            AARPwcgMissionFinder pwcgMissionFinder)
    {
        this.campaign = campaign;
        this.matcher = matcher;
        this.logSetFinder = logSetFinder;
        this.pwcgMissionFinder = pwcgMissionFinder;
    }

    public void determineMostRecentAARLogFileMissionDataSetForCampaign() throws PWCGException
    {        
        pwcgMissionData = getMissionDataForCampaignDate();
        if (pwcgMissionData != null)
        {
            List<String> sortedLogSets = logSetFinder.getSortedLogFileSets();
            logFileSet = matcher.matchMissionFileAndLogFile(pwcgMissionData, sortedLogSets);
        }
    }

    private PwcgMissionData getMissionDataForCampaignDate() throws PWCGException
    {
        List<PwcgMissionData> sortedPwcgMissionDataForCampaign = pwcgMissionFinder.getSortedPwcgMissionsForCampaign();
        for (PwcgMissionData missionData : sortedPwcgMissionDataForCampaign)
        {
            Date missionDataDate = DateUtils.getDateYYYYMMDD(missionData.getMissionHeader().getDate());
            if (missionDataDate.equals(campaign.getDate()))
            {
                return missionData;
            }
        }
        return null;
    }

    public LogFileSet getAarLogFileSet()
    {
        return logFileSet;
    }

    public void setAarLogFileMissionFile(LogFileSet aarLogFileMissionFile)
    {
        this.logFileSet = aarLogFileMissionFile;
    }

    public PwcgMissionData getPwcgMissionData()
    {
        return pwcgMissionData;
    }

    public void setPwcgMissionData(PwcgMissionData pwcgMissionData)
    {
        this.pwcgMissionData = pwcgMissionData;
    }
    
    public boolean isLogSetComplete()
    {
        if (pwcgMissionData == null || logFileSet == null)
        {
            return false;
        }
        
        return true;
    }
}
