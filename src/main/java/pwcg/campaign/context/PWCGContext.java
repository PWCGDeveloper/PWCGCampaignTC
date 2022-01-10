package pwcg.campaign.context;

import pwcg.core.exception.PWCGException;

public class PWCGContext 
{
    protected static TCContext tcContext = null;
    protected static PWCGProduct product = PWCGProduct.TC;

	protected PWCGContext()
    {
    }

    public static TCContext getInstance() 
    {
        try
        {
            buildProductContext();
        }
        catch(PWCGException e)
        {
            
        }
        return tcContext;
    }

    private static void buildProductContext() throws PWCGException
    {
        if (PWCGContext.tcContext == null)
        {
            PWCGContext.tcContext = new TCContext();
            PWCGContext.tcContext.initialize();
        }
    }
}
