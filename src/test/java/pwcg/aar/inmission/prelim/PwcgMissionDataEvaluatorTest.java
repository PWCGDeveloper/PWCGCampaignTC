package pwcg.aar.inmission.prelim;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class PwcgMissionDataEvaluatorTest
{
    @Mock
    private Campaign campaign;

    @Mock
    private PwcgMissionData pwcgMissionData;

    @Mock
    CampaignPersonnelManager personnelManager;

    @Mock
    Company company;
    
    static int thisCompanyId = TestIdentifiers.TEST_GERMAN_COMPANY_ID; // JG52

    public PwcgMissionDataEvaluatorTest() throws PWCGException
    {
        
    }

    @Test
    public void determineCrewMembersInMissionTest () throws PWCGException
    {             
        
    }
    
    @Test
    public void determineCrewMembersNotInMissionTest () throws PWCGException
    {             
    }
    
    @Test
    public void determineCrewsInMissionFromPlayerCompanyTest () throws PWCGException
    {             
    }
    
    @Test
    public void determineAxisPlanesInMissionTest () throws PWCGException
    {             
    }
    
    @Test
    public void determineAlliedPlanesInMissionTest () throws PWCGException
    {             
    }

    
}
