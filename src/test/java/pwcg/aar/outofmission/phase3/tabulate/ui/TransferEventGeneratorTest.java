package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.AARTestSetup;
import pwcg.aar.ui.events.TransferEventGenerator;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransferEventGeneratorTest extends AARTestSetup
{

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        setupAARMocks();
    }

    @Test
    public void testTransferInEvent() throws PWCGException 
    {
        List<TransferRecord> transferRecords = new ArrayList<>();
        TransferRecord companyMemberTransfer1 = new TransferRecord(crewMember1, CompanyTestProfile.TANK_DIVISION_147_PROFILE.getCompanyId(), CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        transferRecords.add(companyMemberTransfer1);        
        TransferRecord companyMemberTransfer2 = new TransferRecord(crewMember2, CompanyTestProfile.TANK_DIVISION_147_PROFILE.getCompanyId(), CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        transferRecords.add(companyMemberTransfer2);        

        TransferEventGenerator transferEventGenerator = new TransferEventGenerator(campaign);
        List<TransferEvent> transferEvents = transferEventGenerator.createCrewMemberTransferEvents(transferRecords);
        TransferEvent transferEvent1 = (TransferEvent)transferEvents.get(0);
        TransferEvent transferEvent2 = (TransferEvent)transferEvents.get(1);

        Assertions.assertTrue (transferEvents.size() == 2);
        Assertions.assertTrue (transferEvent1.getTransferTo() == CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        Assertions.assertTrue (transferEvent2.getTransferTo() == CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
    }
    
    
    @Test
    public void testInCompanyAceUpdate() throws PWCGException 
    {
        List<TransferRecord> transferRecords = new ArrayList<>();
        TransferRecord companyMemberTransfer1 = new TransferRecord(crewMember1, CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId(), CompanyTestProfile.TANK_DIVISION_147_PROFILE.getCompanyId());
        transferRecords.add(companyMemberTransfer1);        
        TransferRecord companyMemberTransfer2 = new TransferRecord(crewMember2, CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId(), CompanyTestProfile.TANK_DIVISION_147_PROFILE.getCompanyId());
        transferRecords.add(companyMemberTransfer2);        

        TransferEventGenerator transferEventGenerator = new TransferEventGenerator(campaign);
        List<TransferEvent> transferEvents = transferEventGenerator.createCrewMemberTransferEvents(transferRecords);
        TransferEvent transferEvent1 = (TransferEvent)transferEvents.get(0);
        TransferEvent transferEvent2 = (TransferEvent)transferEvents.get(1);

        Assertions.assertTrue (transferEvents.size() == 2);
        Assertions.assertTrue (transferEvent1.getTransferTo() != CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        Assertions.assertTrue (transferEvent2.getTransferTo() != CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
    }

}
