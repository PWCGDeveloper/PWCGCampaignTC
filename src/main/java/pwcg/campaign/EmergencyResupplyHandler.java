package pwcg.campaign;

import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.io.json.CampaignEquipmentIOJson;
import pwcg.campaign.io.json.CampaignPersonnelIOJson;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.personnel.CrewMemberReplacementFactory;
import pwcg.campaign.resupply.depot.EquipmentReplacementUtils;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankEquipmentFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class EmergencyResupplyHandler
{
    private Campaign campaign;
    
    public EmergencyResupplyHandler(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public void emergencyResupply() throws PWCGException
    {
        emergencyResupplyPersonnel();
        emergencyResupplyEquipment();
    }

    private void emergencyResupplyPersonnel() throws PWCGException
    {
        for (CompanyPersonnel companyPersonnel : campaign.getPersonnelManager().getCampaignPersonnel().values())
        {
            if (!companyPersonnel.isCompanyPersonnelViable())
            {
                makeCompanyPersonnelViable(companyPersonnel);
            }
        }
    }

    private void makeCompanyPersonnelViable(CompanyPersonnel companyPersonnel) throws PWCGException
    {
        int totalTransfers = calculatePersonnelToReplaceForCompany(companyPersonnel);
        replacePersonnelForCompany(companyPersonnel, totalTransfers);
        CampaignPersonnelIOJson.writeCompany(campaign, companyPersonnel.getCompany().getCompanyId());
    }

    private int calculatePersonnelToReplaceForCompany(CompanyPersonnel companyPersonnel) throws PWCGException
    {
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        int activeCompanySize = activeCrewMembers.getActiveCount(campaign.getDate());
        int transfersNeededForFull = Company.COMPANY_STAFF_SIZE -  activeCompanySize;
        int transfersNeededForViable = (Company.COMPANY_STAFF_SIZE / 2) -  activeCompanySize;
        
        int transfersNeededForViableWithCushion = transfersNeededForViable + 2;
        int extraTransfers = RandomNumberGenerator.getRandom(transfersNeededForFull - transfersNeededForViableWithCushion);
        int totalTransfers = transfersNeededForViableWithCushion + extraTransfers;
        return totalTransfers;
    }

    private void replacePersonnelForCompany(CompanyPersonnel companyPersonnel, int totalTransfers) throws PWCGException
    {
        CrewMemberReplacementFactory replacementFactory = new CrewMemberReplacementFactory(
                campaign, companyPersonnel.getCompany().determineServiceForCompany(campaign.getDate()));
        
        for (int i = 0; i < totalTransfers; ++i)
        {
            CrewMember replacement = replacementFactory.createAIReplacementCrewMember();
            replacement.setCompanyId(companyPersonnel.getCompany().getCompanyId());
            companyPersonnel.addCrewMember(replacement);
        }
    }

    private void emergencyResupplyEquipment() throws PWCGException
    {
        for (int companyId : campaign.getEquipmentManager().getEquipmentAllCompanies().keySet())
        {
            Equipment companyEquipment = campaign.getEquipmentManager().getEquipmentForCompany(companyId);
            if (!companyEquipment.isCompanyEquipmentViable())
            {
                makeCompanyEquipmentViable(companyEquipment, companyId);
            }
        }
    }

    private void makeCompanyEquipmentViable(Equipment companyEquipment, int companyId) throws PWCGException
    {
        int totalNewPlaness = calculatePersonnelToReplaceForCompany(companyEquipment);
        replaceEquipmentForCompany(companyEquipment, companyId, totalNewPlaness);
        CampaignEquipmentIOJson.writeEquipmentForCompany(campaign, companyId);
    }

    private int calculatePersonnelToReplaceForCompany(Equipment companyEquipment)
    {
        int activeCompanyEquipment = companyEquipment.getActiveEquippedTanks().size();
        int equipmentNeededForFull = Company.COMPANY_EQUIPMENT_SIZE -  activeCompanyEquipment;
        int equipmentNeededForViable = (Company.COMPANY_EQUIPMENT_SIZE / 2) -  activeCompanyEquipment;
        
        int equipmentNeededForViableWithCushion = equipmentNeededForViable + 2;
        int extraEquipment = RandomNumberGenerator.getRandom(equipmentNeededForFull - equipmentNeededForViableWithCushion);
        int totalEquipment = equipmentNeededForViableWithCushion + extraEquipment;
        return totalEquipment;
    }

    private void replaceEquipmentForCompany(Equipment companyEquipment, int companyId, int totalNewPlanes) throws PWCGException
    {
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
        String tankTypeName = determineTankTypeToAdd(companyId);
        EquippedTank equippedTank = TankEquipmentFactory.makeTankForDepot(campaign, tankTypeName, company.getCountry().getCountry());
        companyEquipment.addEquippedTankToCompany(campaign, companyId, equippedTank);
    }

    private String determineTankTypeToAdd(int companyId) throws PWCGException
    {
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
        List<String> activeArchTypes = company.getActiveArchTypes(campaign.getDate());
        int archTypeIndex = RandomNumberGenerator.getRandom(activeArchTypes.size());
        
        TankArchType tankArchType = PWCGContext.getInstance().getTankTypeFactory().getTankArchType(activeArchTypes.get(archTypeIndex));
        String tankTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), tankArchType);
        return tankTypeName;
    }
}
