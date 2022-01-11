package integration.campaign.io.json;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.CompanyIOJson;
import pwcg.campaign.plane.CompanyPlaneAssignment;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class CompanyAircraftAssignmentTest
{    
    @Test
    public void verifyValidBoSAirfieldMoveDatesTest() throws PWCGException
    {
        
        List<Company> companys = CompanyIOJson.readJson();
        Assertions.assertTrue (companys.size() > 0);
        
        boolean success = true;
        for (Company company : companys)
        {
            if (!verifyCompanyAircraftTransitions(company))
            {
                success = false;
            }
        }
        
        assert(success);
    }
 
    private boolean verifyCompanyAircraftTransitions(Company company) throws PWCGException
    {
        boolean success = true;
        Date lastEndDate = null;
        List<CompanyTankAssignment> planeAssignments = company.getPlaneAssignments();
        for (CompanyTankAssignment planeAssignment : planeAssignments)
        {
            if (lastEndDate == null)
            {
                lastEndDate = planeAssignment.getCompanyWithdrawal();
            }
            else
            {
                Date thisEndDate = planeAssignment.getCompanyWithdrawal();
                thisEndDate = DateUtils.advanceTimeDays(thisEndDate, 1);
                if (thisEndDate.before(lastEndDate))
                {
                    success = false;
                }
                lastEndDate = thisEndDate;
            }
        }
        
        if (lastEndDate.before(DateUtils.getDateYYYYMMDD("19450601")))
        {
            success = false;
        }
        
        return success;
    }
}
