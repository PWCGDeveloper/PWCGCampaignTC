package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.outofmission.phase4.ElapsedTIme.ElapsedTimeEventGenerator;
import pwcg.aar.outofmission.phase4.ElapsedTIme.ElapsedTimeEvents;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ElapsedTimeEventGeneratorTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private CrewMembers playerMembers;
    @Mock private CrewMember player;
    @Mock private AARContext aarContext;
    @Mock private CampaignUpdateData campaignUpdateData;
    @Mock private Company company;
    @Mock private Airfield currentAirfield;
    @Mock private Airfield newAirfield;

    private Date campaignDate;
    private Date newDate;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        
        campaignDate = DateUtils.getDateYYYYMMDD("19420901");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(company.determineCurrentBaseAnyMap(campaignDate)).thenReturn(currentAirfield);
       
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getAllActivePlayers()).thenReturn(playerMembers);
        List<CrewMember> players = new ArrayList<>();
        players.add(player);
        Mockito.when(playerMembers.getCrewMemberList()).thenReturn(players);   
        Mockito.when(player.determineCompany()).thenReturn(company);   
    }

    @Test
    public void noCompanyMoveOrEndOfWar () throws PWCGException
    {             
        newDate = DateUtils.getDateYYYYMMDD("19420902");
        Mockito.when(aarContext.getNewDate()).thenReturn(newDate);
 
        Mockito.when(company.determineCurrentAirfieldName(campaignDate)).thenReturn("Tuzov");
        Mockito.when(company.determineCurrentAirfieldName(newDate)).thenReturn("Tuzov");

        ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);
        ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
        Assertions.assertTrue (elapsedTimeEvents.getEndOfWarEvent() == null);
        Assertions.assertTrue (elapsedTimeEvents.getCompanyMoveEvents().size() == 0);
        
    }

    @Test
    public void companyMove () throws PWCGException
    {             
        campaignDate = DateUtils.getDateYYYYMMDD("19420901");
        newDate = DateUtils.getDateYYYYMMDD("19420909");
        Mockito.when(aarContext.getNewDate()).thenReturn(newDate);
        Mockito.when(company.determineCurrentBaseAnyMap(newDate)).thenReturn(newAirfield);

        Mockito.when(company.determineCurrentAirfieldName(campaignDate)).thenReturn("Tuzov");
        Mockito.when(company.determineCurrentAirfieldName(newDate)).thenReturn("Kalach");

        ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);
        ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
        Assertions.assertTrue (elapsedTimeEvents.getEndOfWarEvent() == null);
        Assertions.assertTrue (elapsedTimeEvents.getCompanyMoveEvents().size() > 0);
        
    }

    @Test
    public void endOfWar () throws PWCGException
    {    
        campaignDate = DateUtils.getDateYYYYMMDD("19450502");
        newDate = DateUtils.getDateYYYYMMDD("19450508");
        Mockito.when(aarContext.getNewDate()).thenReturn(newDate);


        ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);
        ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
        Assertions.assertTrue (elapsedTimeEvents.getEndOfWarEvent() != null);
        Assertions.assertTrue (elapsedTimeEvents.getCompanyMoveEvents().size() == 0);
        
    }

}
