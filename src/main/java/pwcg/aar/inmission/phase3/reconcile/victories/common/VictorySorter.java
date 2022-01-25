package pwcg.aar.inmission.phase3.reconcile.victories.common;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;

public class VictorySorter
{
    private List<LogVictory> firmTankVictories = new ArrayList <LogVictory>();
    private List<LogVictory> firmGroundVictories = new ArrayList <LogVictory>();
    private List<LogVictory> firmPlaneVictories = new ArrayList <LogVictory>();

    private List<LogVictory> fuzzyTankVictories = new ArrayList <LogVictory>();
    private List<LogVictory> fuzzyPlaneVictories = new ArrayList <LogVictory>();
    
    private enum VictoryState
    {
        FIRM,
        FUZZY
    }

    public VictorySorter ()
    {
    }

    public void sortVictories(List<LogVictory> logVictories) throws PWCGException 
    {                
        for (LogVictory logVictory : logVictories)
        {
            VictoryState victoryState = isFuzzy(logVictory);            
            if (victoryState == VictoryState.FUZZY)
            {
                sortFuzzyVictory(logVictory);
            }
            else if (victoryState == VictoryState.FIRM)
            {
                sortFirmVictory(logVictory);                
            }
        }
        
        confirmFirmGroundVicories() ;
    }

    private void sortFirmVictory(LogVictory logVictory) throws PWCGException
    {
        if (isVictimPlane(logVictory))
        {
            firmPlaneVictories.add(logVictory);
        }
        else if (logVictory.getVictim().getRoleCategory() == PwcgRoleCategory.GROUND_UNIT)
        {
            firmGroundVictories.add(logVictory);
        }
        else if (isVictimTank(logVictory))
        {
            firmTankVictories.add(logVictory);
        }
    }

    private void sortFuzzyVictory(LogVictory logVictory) throws PWCGException
    {
        if (isVictimPlane(logVictory))
        {
            fuzzyPlaneVictories.add(logVictory);
        }
        else if (logVictory.getVictim().getRoleCategory() == PwcgRoleCategory.GROUND_UNIT)
        {
            // We don't do anything with fuzzy ground victories
        }
        else if (isVictimTank(logVictory))
        {
            fuzzyTankVictories.add(logVictory);
        }
    }

    private void confirmFirmGroundVicories() throws PWCGException 
    {
        for (LogVictory logVictory : firmGroundVictories)
        {
            logVictory.setConfirmed(true);
        }
    }

    private VictoryState isFuzzy(LogVictory logVictor)
    {
        if (logVictor.getVictor() instanceof LogUnknown)
        {
            return VictoryState.FUZZY;
        }
        
        return VictoryState.FIRM;
    }

    private boolean isVictimPlane(LogVictory logVictory) throws PWCGException
    {
        LogAIEntity logVictim = logVictory.getVictim();               
        if (logVictim != null)
        {
            if (logVictim instanceof LogPlane)
            {
                return true;
            }
        }
        
        return false;
    }

    private boolean isVictimTank(LogVictory logVictory) throws PWCGException
    {
        LogAIEntity logVictim = logVictory.getVictim();
        if (logVictim != null)
        {
            if (logVictim instanceof LogTank)
            {
                return true;
            }
        }

        return false;
    }

    public List<LogVictory> getFirmTankVictories()
    {
        return firmTankVictories;
    }

    public List<LogVictory> getFirmGroundVictories()
    {
        return firmGroundVictories;
    }

    public List<LogVictory> getFirmPlaneVictories()
    {
        return firmPlaneVictories;
    }

    public List<LogVictory> getFuzzyTankVictories()
    {
        return fuzzyTankVictories;
    }

    public List<LogVictory> getFuzzyPlaneVictories()
    {
        return fuzzyPlaneVictories;
    }

    public List<LogVictory> getAllUnconfirmed()
    {
        List<LogVictory> unconfirmed = new ArrayList<LogVictory>();
        extractUnconfirmed(firmTankVictories, unconfirmed);
        extractUnconfirmed(firmPlaneVictories, unconfirmed);
        extractUnconfirmed(fuzzyTankVictories, unconfirmed);
        extractUnconfirmed(fuzzyPlaneVictories, unconfirmed);
        
        return unconfirmed;
    }


    private void extractUnconfirmed(List<LogVictory> victoryList, List<LogVictory> unconfirmed)
    {
        for (LogVictory resultVictory : victoryList )
        {
            if (!resultVictory.isConfirmed())
            {
                unconfirmed.add(resultVictory);
            }
        }
    }
 
}
