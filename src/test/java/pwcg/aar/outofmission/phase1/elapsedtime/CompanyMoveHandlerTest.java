package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.outofmission.phase4.ElapsedTIme.CompanyMoveHandler;
import pwcg.aar.ui.events.model.CompanyMoveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class CompanyMoveHandlerTest
{
    @Mock private Campaign campaign;
    @Mock Company company;
    @Mock Airfield currentAirfield;
    @Mock Airfield newAirfield;

    private Date campaignDate;
    private Date newDate;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
                
        campaignDate = DateUtils.getDateYYYYMMDD("19411120");
        newDate = DateUtils.getDateYYYYMMDD("19411215");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
    }

    @Test
    public void noCompanyMove () throws PWCGException
    {             
        Mockito.when(company.determineBaseName(campaignDate)).thenReturn("Ivanskoe");
        Mockito.when(company.determineBaseName(newDate)).thenReturn("Ivanskoe");

        CompanyMoveHandler companyMoveHandler = new CompanyMoveHandler(campaign);
        CompanyMoveEvent companyMoveEvent = companyMoveHandler.companyMoves(newDate, company);
        Assertions.assertTrue (companyMoveEvent == null);
        
    }
}