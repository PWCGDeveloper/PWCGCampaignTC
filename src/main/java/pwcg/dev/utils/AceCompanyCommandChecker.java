package pwcg.dev.utils;

import java.util.Set;

import pwcg.campaign.context.AceManager;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class AceCompanyCommandChecker
{
    public static void main (String[] args)
    {
        UserDir.setUserDir();

        try
        {
            AceCompanyCommandChecker checker = new AceCompanyCommandChecker();
            checker.checkForCompanyCommand();
        }
        catch (Exception e)
        {
             PWCGLogger.logException(e);;
        }
    }

    public void checkForCompanyCommand()
    {
        try
        {
            AceManager aceManager = new AceManager();
            aceManager.configure();

            Set<Integer> aceCommandedCompanys = aceManager.getAceCommandedCompanys();
             
            for (Integer aceCompanyId : aceCommandedCompanys)
            {
                PWCGLogger.log(LogLevel.DEBUG, "" + aceCompanyId);
            }
            
        }
        catch (Exception e)
        {
             PWCGLogger.logException(e);
        }
    }
}
