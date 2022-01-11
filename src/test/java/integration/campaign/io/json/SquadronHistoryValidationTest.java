package integration.campaign.io.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.CompanyHistory;
import pwcg.campaign.CompanyHistoryEntry;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.CompanyIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class CompanyHistoryValidationTest
{
    @Test
    public void readJsonBoSCompanysTest() throws PWCGException
    {
        
        List<Company> companys = CompanyIOJson.readJson();
        Assertions.assertTrue (companys.size() > 0);
        
        boolean success = true;
        for (Company company : companys)
        {
            verifyVVSTransition(company);
            if (!verifyBoSTransitionDates(company))
            {
                success = false;
            }
        }
        
        assert(success);
    }
    
    private void verifyVVSTransition(Company company) throws PWCGException
    {
        if (company.getCompanyId() == 10131136)
        {
            CompanyHistory companyHistory = company.getSquadHistory();
            Assertions.assertTrue (companyHistory != null);
            
            CompanyHistoryEntry  squadHistoryEntry = companyHistory.getSquadHistoryEntry(DateUtils.getDateYYYYMMDD("19420301"));
            Assertions.assertTrue (squadHistoryEntry != null);
            Assertions.assertTrue (squadHistoryEntry.getArmedServiceName().equals("Voyenno-Vozdushnye Sily"));
            Assertions.assertTrue (squadHistoryEntry.getSquadName().equals("45th Bomber Air Regiment"));
            Assertions.assertTrue (squadHistoryEntry.getSkill() == 40);
            
            assert(company.determineCompanySkill(DateUtils.getDateYYYYMMDD("19420228")) == 30);
            assert(company.determineDisplayName(DateUtils.getDateYYYYMMDD("19420228")).equals("136th Bomber Air Regiment"));

            assert(company.determineCompanySkill(DateUtils.getDateYYYYMMDD("19420301")) == 40);
            assert(company.determineDisplayName(DateUtils.getDateYYYYMMDD("19420301")).equals("45th Bomber Air Regiment"));
        }
    }
    
    private boolean verifyBoSTransitionDates(Company company) throws PWCGException
    {
        CompanyHistory companyHistory = company.getSquadHistory();
        boolean success = true;
        try 
        {
            if (companyHistory != null)
            {
                validateBoSSquadHistoryEntries(companyHistory);
            }
        }
        catch (PWCGException e)
        {
            System.out.println(e.getMessage());
            success = false;
        }
        
        return success;
    }

    private void validateBoSSquadHistoryEntries(CompanyHistory companyHistory) throws PWCGException
    {
        validateNoDuplicateEntries(companyHistory);
        validateNoOutOfOrderEntries(companyHistory);
    }
    
    private void validateNoDuplicateEntries(CompanyHistory companyHistory) throws PWCGException
    {
        List<CompanyHistoryEntry> validatedEntries = new ArrayList<>();
        for (CompanyHistoryEntry  squadHistoryEntry : companyHistory.getSquadHistoryEntries())
        {
            for (CompanyHistoryEntry validatedSquadHistoryEntry : validatedEntries)
            {
                if (squadHistoryEntry.getDate().equals(validatedSquadHistoryEntry.getDate()))
                {
                    String errorMsg = squadHistoryEntry.getSquadName() + " duplicate transition date " + squadHistoryEntry.getDate(); 
                    throw new PWCGException(errorMsg);
                }
            }
            
            validatedEntries.add(squadHistoryEntry);
        }
    }
    
    private void validateNoOutOfOrderEntries(CompanyHistory companyHistory) throws PWCGException
    {
        CompanyHistoryEntry lastEntry = null;
        for (CompanyHistoryEntry  squadHistoryEntry : companyHistory.getSquadHistoryEntries())
        {
            if (lastEntry != null)
            {
                Date thisEntryDate = DateUtils.getDateYYYYMMDD(squadHistoryEntry.getDate());
                Date lastEntryDate = DateUtils.getDateYYYYMMDD(lastEntry.getDate());
                if (!thisEntryDate.after(lastEntryDate))
                {
                    String errorMsg = squadHistoryEntry.getSquadName() + " out of sequence transition date " + squadHistoryEntry.getDate(); 
                    throw new PWCGException(errorMsg);
                }
            }
            
            lastEntry = squadHistoryEntry;
        }
    }
}
