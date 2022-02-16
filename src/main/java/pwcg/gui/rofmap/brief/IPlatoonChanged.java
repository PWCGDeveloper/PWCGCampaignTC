package pwcg.gui.rofmap.brief;

import pwcg.core.exception.PWCGException;

public interface IPlatoonChanged
{
    void platoonChanged(int companyId) throws PWCGException;
}
