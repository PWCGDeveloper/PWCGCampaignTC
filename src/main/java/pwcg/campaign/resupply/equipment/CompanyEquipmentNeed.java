package pwcg.campaign.resupply.equipment;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.resupply.ICompanyNeed;
import pwcg.campaign.tank.Equipment;
import pwcg.core.exception.PWCGException;

public class CompanyEquipmentNeed implements ICompanyNeed
{
    private Campaign campaign;
    private Company company;
    private int tanksNeeded = 0;

    public CompanyEquipmentNeed(Campaign campaign, Company company)
    {
        this.campaign = campaign;
        this.company = company;
    }

    @Override
    public void determineResupplyNeeded() throws PWCGException
    {
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(company.getCompanyId());
        int activeCompanySize = equipment.getActiveEquippedTanks().size();
        int recentlyInactive = equipment.getRecentlyInactiveEquippedTanks(campaign.getDate()).size();
      
        tanksNeeded = Company.COMPANY_EQUIPMENT_SIZE - activeCompanySize - recentlyInactive;

    }

    @Override
    public int getCompanyId()
    {
        return company.getCompanyId();
    }

    @Override
    public boolean needsResupply()
    {
        return (tanksNeeded > 0);
    }

    @Override
    public void noteResupply()
    {
        --tanksNeeded;
    }

    @Override
    public int getNumNeeded()
    {
        return tanksNeeded;
    }

    public void setTanksNeeded(int tanksNeeded)
    {
        this.tanksNeeded = tanksNeeded;
    }
}
