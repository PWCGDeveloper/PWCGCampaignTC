package pwcg.campaign.personnel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.TCServiceManager;

@ExtendWith(MockitoExtension.class)
public class CrewMemberReplacementFactoryTest
{
    @Mock 
    private Campaign campaign;
    
    private SerialNumber serialNumber = new SerialNumber();
    
    @BeforeEach
    public void setupTest() throws PWCGException
     {
        
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
    }

    @Test
    public void testCreateReplacementCrewMember() throws Exception
    {                
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420601"));

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedServiceById(TCServiceManager.WEHRMACHT);

        CrewMemberReplacementFactory companyMemberFactory = new  CrewMemberReplacementFactory (campaign, service);
        CrewMember replacement = companyMemberFactory.createAIReplacementCrewMember();
        
        assert(replacement.isPlayer() == false);
        assert(replacement.getSerialNumber() >= SerialNumber.AI_STARTING_SERIAL_NUMBER);
        assert(replacement.getRank().equals("Gefreiter") || replacement.getRank().equals("Oberfeldwebel"));
        assert(replacement.getCompanyId() == Company.REPLACEMENT);
        assert(replacement.getAiSkillLevel() == AiSkillLevel.COMMON);
    }
}
