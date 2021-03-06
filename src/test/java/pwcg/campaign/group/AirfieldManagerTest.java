package pwcg.campaign.group;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.options.MissionWeather;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AirfieldManagerTest
{
    private Campaign campaign;    
    
    @Mock private Mission mission;
    @Mock private MissionWeather weather;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        PWCGContext.getInstance().setCampaign(campaign);        
    }

	@Test
	public void airfieldValidityCheckFranceTest() throws PWCGException 
	{
        Mockito.when(mission.getWeather()).thenReturn(weather);
        Mockito.when(weather.getWindDirection()).thenReturn(90);        

        AirfieldManager airfieldManager = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.STALINGRAD_MAP).getAirfieldManager();
        for (Airfield airfield : airfieldManager.getAllAirfields().values())
        {
            Assertions.assertTrue (airfield.getTakeoffLocation(mission) != null);
            Assertions.assertTrue (airfield.getLandingLocation(mission) != null);
        }
	}

}
