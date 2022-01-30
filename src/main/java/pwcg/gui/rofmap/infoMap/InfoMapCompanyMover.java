package pwcg.gui.rofmap.infoMap;

import java.util.Date;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.CompanyIOJson;
import pwcg.core.exception.PWCGException;

public class InfoMapCompanyMover
{
    private Integer companyIdToMove = -1;

    public void moveCompany (String targetTown, Date assignmentDate) throws PWCGException
    {
        if (companyIdToMove > 0)
        {
            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyIdToMove);
            if (company != null)
            {
                company.assignBase(assignmentDate, targetTown);
                CompanyIOJson.writeJson(company);
                companyIdToMove = -1;
            }
        }
    }
    
    public Integer getCompanyIdToMove()
    {
        return companyIdToMove;
    }

    public void setCompanyIdToMove(Integer companyIdToMove)
    {
        this.companyIdToMove = companyIdToMove;
    }    
}
