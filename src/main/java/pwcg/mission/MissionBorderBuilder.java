package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;

public class MissionBorderBuilder 
{
    private Campaign campaign;
    private Coordinate missionObjectivePosition;
	
	public MissionBorderBuilder(Campaign campaign, Coordinate missionObjectivePosition)
	{
        this.campaign = campaign;
        this.missionObjectivePosition = missionObjectivePosition;
	}

    public CoordinateBox buildCoordinateBox() throws PWCGException
    {        
        int missionBoxRadius = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxSizeKey) * 1000;
        CoordinateBox missionBox = CoordinateBox.coordinateBoxFromCenter(missionObjectivePosition, missionBoxRadius);
        return missionBox;
    }
}
