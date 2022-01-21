package pwcg.mission.data;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.io.MissionFileNameBuilder;
import pwcg.mission.platoon.ITankPlatoon;

public class MissionHeaderGenerator
{

    public MissionHeader generateMissionHeader(Campaign campaign, Mission mission) throws PWCGException
    {
        // Even for Coop flights we have to set the header.  Doesn't really matter which flight 
        // as long as it is a player flight
        ITankPlatoon myPlatoon = mission.getPlatoons().getReferencePlayerUnit();
        Company myCompany = mission.getParticipatingPlayers().getMissionPlayerCompanys().get(0);
        
        MissionHeader missionHeader = new MissionHeader();
        
        String missionFileName = MissionFileNameBuilder.buildMissionFileName(campaign) ;
        missionHeader.setMissionFileName(missionFileName);
        
        missionHeader.setBase(myCompany.determineCurrentAirfieldName(campaign.getDate()));
        missionHeader.setDate(DateUtils.getDateStringYYYYMMDD(campaign.getDate()));
        missionHeader.setCompany(myCompany.determineDisplayName(campaign.getDate()));
        missionHeader.setVehicleType(myPlatoon.getLeadVehicle().getDisplayName());

        
        missionHeader.setDuty("" + myPlatoon.getPlatoonMissionType().name()); 
        
        missionHeader.setMapName(PWCGContext.getInstance().getCurrentMap().getMapName()); 

        String formattedTime = DateUtils.getDateAsMissionFileFormat(campaign.getDate());
        missionHeader.setTime(formattedTime);
        
        return missionHeader;
    }
}
