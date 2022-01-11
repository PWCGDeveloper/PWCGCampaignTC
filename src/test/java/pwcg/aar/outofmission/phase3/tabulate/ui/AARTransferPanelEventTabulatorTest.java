package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.AARTestSetup;
import pwcg.aar.tabulate.debrief.TransferPanelEventTabulator;
import pwcg.aar.ui.display.model.TransferPanelData;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARTransferPanelEventTabulatorTest extends AARTestSetup
{
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        setupAARMocks();
    }

    @Test
    public void testTransferInEvent() throws PWCGException 
    {
        List<TransferRecord> companyMemberTransferredIn = new ArrayList<>();
        TransferRecord companyMemberTransfer = new TransferRecord(crewMember1, CompanyTestProfile.ESC_3_PROFILE.getCompanyId(), CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        companyMemberTransferredIn.add(companyMemberTransfer);        
        Mockito.when(crewMembersTransferred.getCrewMembersTransferred()).thenReturn(companyMemberTransferredIn);

        List<TransferRecord> acesTransferredIn = new ArrayList<>();
        TransferRecord aceTransfer = new TransferRecord(ace1, CompanyTestProfile.ESC_3_PROFILE.getCompanyId(), CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        acesTransferredIn.add(aceTransfer);        
        Mockito.when(acesTransferred.getCrewMembersTransferred()).thenReturn(acesTransferredIn);
        
        TransferPanelEventTabulator transferPanelEventTabulator = new TransferPanelEventTabulator(campaign, aarContext);
        TransferPanelData transferPanelData = transferPanelEventTabulator.tabulateForAARTransferPanel();
        
        List<TransferEvent> transferEvents = transferPanelData.getTransfers();

        Assertions.assertTrue (transferEvents.size() == 2);
    }

    @Test
    public void testTransferOutEvent() throws PWCGException 
    {
        List<TransferRecord> companyMemberTransferredOut = new ArrayList<>();
        TransferRecord companyMemberTransfer = new TransferRecord(crewMember1, CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId(), CompanyTestProfile.ESC_3_PROFILE.getCompanyId());
        companyMemberTransferredOut.add(companyMemberTransfer);        
        Mockito.when(crewMembersTransferred.getCrewMembersTransferred()).thenReturn(companyMemberTransferredOut);

        List<TransferRecord> acesTransferredIn = new ArrayList<>();
        TransferRecord aceTransfer = new TransferRecord(ace1, CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId(), CompanyTestProfile.ESC_3_PROFILE.getCompanyId());
        acesTransferredIn.add(aceTransfer);        
        Mockito.when(acesTransferred.getCrewMembersTransferred()).thenReturn(acesTransferredIn);
        
        TransferPanelEventTabulator transferPanelEventTabulator = new TransferPanelEventTabulator(campaign, aarContext);
        TransferPanelData transferPanelData = transferPanelEventTabulator.tabulateForAARTransferPanel();
        
        List<TransferEvent> transferEvents = transferPanelData.getTransfers();

        Assertions.assertTrue (transferEvents.size() == 2);
    }
}
