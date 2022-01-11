package pwcg.aar.tabulate.combatreport;

import pwcg.aar.ui.display.model.AARCombatReportMapData;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;

public class UICombatReportData
{
    private int companyId;
    private AARCombatReportPanelData combatReportPanelData;
    private AARCombatReportMapData combatReportMapData;

    public UICombatReportData(int companyId)
    {
        this.companyId = companyId;
        this.combatReportPanelData = new AARCombatReportPanelData();
        this.combatReportMapData = new AARCombatReportMapData();
    }

    public int getCompanyId()
    {
        return companyId;
    }

    public void setCompanyId(int companyId)
    {
        this.companyId = companyId;
    }

    public AARCombatReportPanelData getCombatReportPanelData()
    {
        return combatReportPanelData;
    }

    public void setCombatReportPanelData(AARCombatReportPanelData combatReportPanelData)
    {
        this.combatReportPanelData = combatReportPanelData;
    }

    public AARCombatReportMapData getCombatReportMapData()
    {
        return combatReportMapData;
    }

    public void setCombatReportMapData(AARCombatReportMapData combatReportMapData)
    {
        this.combatReportMapData = combatReportMapData;
    }
}
