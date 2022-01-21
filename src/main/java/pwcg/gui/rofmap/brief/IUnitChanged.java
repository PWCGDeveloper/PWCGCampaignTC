package pwcg.gui.rofmap.brief;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ICompanyMission;

public interface IUnitChanged
{
    void unitChanged(ICompanyMission company) throws PWCGException;
}
