package pwcg.campaign.company;

import java.util.Date;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CompanyConversionPeriod
{
    private Date conversionStartDate;
    private Date conversionCompleteDate;
    
    public CompanyConversionPeriod(Date conversionStartDate, Date conversionCompleteDate)
    {
        this.conversionStartDate = conversionStartDate;
        this.conversionCompleteDate = conversionCompleteDate;
    }
    
    public boolean isConversionPeriodActive(Date date) throws PWCGException
    {
        if (DateUtils.isDateInRange(date, conversionStartDate, conversionCompleteDate))
        {
            return true;
        }
        
        return false;
    }
}
