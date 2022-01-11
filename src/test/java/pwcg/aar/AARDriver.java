package pwcg.aar;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class AARDriver
{    
    public AARDriver() throws PWCGException
    {
        
    }
    
    @Test
    public void testArchtypeReplacement() throws PWCGException 
    {
        Campaign campaign = new Campaign();
        campaign.open("AAA");
        AARCoordinator aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.aarPreliminary(campaign);
        aarCoordinator.submitAAR(new HashMap<>());
        AARContext aarContext = aarCoordinator.getAarContext();
    }

}
