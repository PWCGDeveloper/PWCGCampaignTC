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
public class SquadronHistoryValidationTest
{
    @Test
    public void readJsonBoSSquadronsTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.TC);
        List<Company> squadrons = CompanyIOJson.readJson();
        Assertions.assertTrue (squadrons.size() > 0);
        
        boolean success = true;
        for (Company squadron : squadrons)
        {
            verifyVVSTransition(squadron);
            if (!verifyBoSTransitionDates(squadron))
            {
                success = false;
            }
        }
        
        assert(success);
    }
    
    private void verifyVVSTransition(Company squadron) throws PWCGException
    {
        if (squadron.getCompanyId() == 10131136)
        {
            CompanyHistory squadronHistory = squadron.getSquadHistory();
            Assertions.assertTrue (squadronHistory != null);
            
            CompanyHistoryEntry  squadHistoryEntry = squadronHistory.getSquadHistoryEntry(DateUtils.getDateYYYYMMDD("19420301"));
            Assertions.assertTrue (squadHistoryEntry != null);
            Assertions.assertTrue (squadHistoryEntry.getArmedServiceName().equals("Voyenno-Vozdushnye Sily"));
            Assertions.assertTrue (squadHistoryEntry.getSquadName().equals("45th Bomber Air Regiment"));
            Assertions.assertTrue (squadHistoryEntry.getSkill() == 40);
            
            assert(squadron.determineSquadronSkill(DateUtils.getDateYYYYMMDD("19420228")) == 30);
            assert(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19420228")).equals("136th Bomber Air Regiment"));

            assert(squadron.determineSquadronSkill(DateUtils.getDateYYYYMMDD("19420301")) == 40);
            assert(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19420301")).equals("45th Bomber Air Regiment"));
        }
    }
    
    private boolean verifyBoSTransitionDates(Company squadron) throws PWCGException
    {
        CompanyHistory squadronHistory = squadron.getSquadHistory();
        boolean success = true;
        try 
        {
            if (squadronHistory != null)
            {
                validateBoSSquadHistoryEntries(squadronHistory);
            }
        }
        catch (PWCGException e)
        {
            System.out.println(e.getMessage());
            success = false;
        }
        
        return success;
    }

    private void validateBoSSquadHistoryEntries(CompanyHistory squadronHistory) throws PWCGException
    {
        validateNoDuplicateEntries(squadronHistory);
        validateNoOutOfOrderEntries(squadronHistory);
    }
    
    private void validateNoDuplicateEntries(CompanyHistory squadronHistory) throws PWCGException
    {
        List<CompanyHistoryEntry> validatedEntries = new ArrayList<>();
        for (CompanyHistoryEntry  squadHistoryEntry : squadronHistory.getSquadHistoryEntries())
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
    
    private void validateNoOutOfOrderEntries(CompanyHistory squadronHistory) throws PWCGException
    {
        CompanyHistoryEntry lastEntry = null;
        for (CompanyHistoryEntry  squadHistoryEntry : squadronHistory.getSquadHistoryEntries())
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
