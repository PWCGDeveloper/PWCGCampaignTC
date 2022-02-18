package pwcg.gui.rofmap.brief;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingCrewMemberAssignmentData;
import pwcg.mission.ICompanyMission;
import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.mission.platoon.tank.TankMcu;

public class BriefingDataInitializer
{
    private Mission mission;

    public BriefingDataInitializer(Mission mission)
    {
        this.mission = mission;
    }

    public BriefingCrewMemberAssignmentData initializeFromMission(ICompanyMission company) throws PWCGException
    {
        BriefingCrewMemberAssignmentData briefingAssignmentData = new BriefingCrewMemberAssignmentData();
        briefingAssignmentData.setCompany(company);

        CompanyPersonnel playerPersonnel = mission.getCampaign().getPersonnelManager().getCompanyPersonnel(company.getCompanyId());
        CrewMembers companyMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAcesNoWounded(
                playerPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), (mission.getCampaign().getDate()));
        for (CrewMember crewMember : companyMembers.getCrewMemberCollection().values())
        {
            briefingAssignmentData.addCrewMember(crewMember);
        }

        Equipment companyTanks = mission.getCampaign().getEquipmentManager().getEquipmentForCompany(company.getCompanyId());
        for (EquippedTank companyPlane : companyTanks.getActiveEquippedTanks().values())
        {
            briefingAssignmentData.addPlane(companyPlane);
        }

        ITankPlatoon playerPlatoon = mission.getPlatoons().getPlayerPlatoonForCompany(company.getCompanyId());
        for (TankMcu tank : playerPlatoon.getPlatoonTanks().getTanks())
        {
            briefingAssignmentData.assignCrewMember(tank.getTankCommander().getSerialNumber(), tank.getSerialNumber());
        }

        return briefingAssignmentData;
    }
}
