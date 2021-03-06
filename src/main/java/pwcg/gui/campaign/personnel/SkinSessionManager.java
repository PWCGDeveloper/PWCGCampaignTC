package pwcg.gui.campaign.personnel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.skin.Skin;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

/**
 * Manages state for a skin editing session.
 * During the edit process an overall view of skin assignment must be maintained in memory.
 * By maintaining state in memory a cancel allows the use to revert to the previous state.
 * Because assignment to one crewMember impacts availability to another, assignments to all crewMembers must be maintained.
 * 
 * @author Patrick Wilson
 *
 */
public class SkinSessionManager
{
    private CrewMember crewMember = null;
    private Map <Integer, CrewMemberSkinInfo> crewMemberSkinSets = new HashMap <>();
    
    private boolean companySkinsSelected = false;
    private boolean nonCompanySkinsSelected = false;
    private boolean looseSkinsSelected = false;

    public SkinSessionManager()
    {
    }

    public List<Skin> getCompanySkins(String selectedPlane) throws PWCGException
    {
        List<Skin> skinNames = new ArrayList<>();

        Campaign campaign = PWCGContext.getInstance().getCampaign();
        Company company = crewMember.determineCompany();

        List<Skin> companySkins = PWCGContext.getInstance().getSkinManager().getSkinsByCompanyTankDate(selectedPlane, company.getCompanyId(), campaign.getDate());
        skinNames = getConfiguredSkins(companySkins);

        return skinNames;
    }

    public List<Skin> getNonCompanySkins(String selectedPlane) throws PWCGException
    {
        List<Skin> skinNames = new ArrayList<>();
        
        Campaign campaign = PWCGContext.getInstance().getCampaign();

        List<Skin> unaffiliatedSkins = PWCGContext.getInstance().getSkinManager().getSkinsByCompanyTankDate(selectedPlane, Skin.PERSONAL_SKIN, campaign.getDate());

        skinNames = getConfiguredSkins(unaffiliatedSkins);

        return skinNames;
    }

    public List<Skin> getLooseSkins(String selectedPlane) throws PWCGException
    {
        List<Skin> looseSkins = new ArrayList<>();

        // Get what we already have for the company or as nonCompany
        List<Skin> companySkins = getCompanySkins(selectedPlane);
        List<Skin> nonCompanySkins = getNonCompanySkins(selectedPlane);
        
        List<Skin> knownSkinsForPlane = new ArrayList<>();
        knownSkinsForPlane.addAll(companySkins);
        knownSkinsForPlane.addAll(nonCompanySkins);

        // Exclude known company and nonCompanys
        List<Skin> allLooseSkins = PWCGContext.getInstance().getSkinManager().getLooseSkinByTank(selectedPlane);
        for (Skin looseSkin : allLooseSkins)
        {
            if (!isKnownSkin(knownSkinsForPlane, looseSkin.getSkinName()))
            {
                looseSkins.add(looseSkin);
            }
        }

        return looseSkins;
    }

    private boolean isKnownSkin(List<Skin> knownSkinsForPlane, String skinName)
    {
        for (Skin knownSkin : knownSkinsForPlane)
        {
            if (knownSkin.getSkinName().equals(skinName))
            {
                return true;
            }
        }
        return false;
    }

    private List<Skin> getConfiguredSkins(List<Skin> skins) throws PWCGException
    {
        List<Skin> skinNames = new ArrayList<>();

        Campaign campaign = PWCGContext.getInstance().getCampaign();
        Date campaignDate = campaign.getDate();

        for (Skin skin : skins)
        {
            if (!(campaignDate.before(skin.getStartDate())) && !(campaignDate.after(skin.getEndDate())))
            {
                if (!isSkinInUseByAnotherCrewMember(skin))
                {
                    if (skin.getSkinName() != null)
                    {
                        skinNames.add(skin);
                    }
                    else
                    {
                        PWCGLogger.log(LogLevel.DEBUG, "Null skin in list");
                    }
                }
            }
        }

        return skinNames;
    }

    private boolean isSkinInUseByAnotherCrewMember(Skin skinToCheck) throws PWCGException
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(crewMember.getCompanyId());
        CrewMembers crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember squadMember : crewMembers.getCrewMemberList())
        {
            if (!(squadMember.getSerialNumber() == crewMember.getSerialNumber()))
            {
                List<Skin> skinsForCrewMember = squadMember.getSkins();
                for (Skin skin : skinsForCrewMember)
                {
                    if (skinToCheck.getSkinName().equalsIgnoreCase(skin.getSkinName()))
                    {
                        return true;
                    }
                }
            }
        }
        
        // Then look for skins assigned this session
        for (CrewMemberSkinInfo crewMemberSkinSet: crewMemberSkinSets.values())
        {
            // Exclude skins assigned to other crewMembers during this session
            if (!(crewMemberSkinSet.getCrewMember().getSerialNumber() == crewMember.getSerialNumber()))
            {
                for (Skin skinAssignedThisSession : crewMemberSkinSet.getAllSkins().values())
                {
                    if (skinAssignedThisSession != null)
                    {
                        if (skinAssignedThisSession.getSkinName().equalsIgnoreCase(skinToCheck.getSkinName()))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }

    private void addSkinSetForCrewMember (CrewMember crewMember) throws PWCGException
    {
        if (!crewMemberSkinSets.containsKey(crewMember.getSerialNumber()))
        {
            CrewMemberSkinInfo crewMemberSkinSet = new CrewMemberSkinInfo(crewMember);
            crewMemberSkinSet.initialize();
            crewMemberSkinSets.put(crewMember.getSerialNumber(), crewMemberSkinSet);
        }
    }

    public void finalizeSkinAssignments ()
    {
        for (CrewMemberSkinInfo crewMemberSkinSet : crewMemberSkinSets.values())
        {
            if (crewMemberSkinSet != null)
            {
                crewMemberSkinSet.setSkinAssignments();
            }
        }
    }

    public CrewMember getCrewMember()
    {
        return crewMember;
    }

    public void setCrewMember(CrewMember crewMember) throws PWCGException
    {
        this.crewMember = crewMember;
        addSkinSetForCrewMember (crewMember);
    }

    public Skin getSkinForCrewMemberAndPlane(String planeName)
    {
        Skin skin = null;
        
        if (crewMember != null)
        {
            CrewMemberSkinInfo crewMemberSkinSet = crewMemberSkinSets.get(crewMember.getSerialNumber());
            if (crewMemberSkinSet != null)
            {
                skin = crewMemberSkinSet.getSkinForPlane(planeName);
            }
        }
        
        return skin;
    }

     public void updateSkinForPlane(String plane, Skin skin)
     {
         if (crewMember != null)
         {
             CrewMemberSkinInfo crewMemberSkinSet = crewMemberSkinSets.get(crewMember.getSerialNumber());
             if (crewMemberSkinSet != null)
             {
                 crewMemberSkinSet.updateSkinForPlane(plane, skin);
             }
         }
     }

     public CrewMemberSkinInfo getCrewMemberSkinSet()
     {
         return crewMemberSkinSets.get(crewMember.getSerialNumber());
     }

    public boolean isCompanySkinsSelected()
    {
        return companySkinsSelected;
    }

    public void setCompanySkinsSelected(boolean companySkinsSelected)
    {
        this.companySkinsSelected = companySkinsSelected;
    }

    public boolean isNonCompanySkinsSelected()
    {
        return nonCompanySkinsSelected;
    }

    public void setNonCompanySkinsSelected(boolean nonCompanySkinsSelected)
    {
        this.nonCompanySkinsSelected = nonCompanySkinsSelected;
    }

    public boolean isLooseSkinsSelected()
    {
        return looseSkinsSelected;
    }

    public void setLooseSkinsSelected(boolean looseSkinsSelected)
    {
        this.looseSkinsSelected = looseSkinsSelected;
    }
    

    public void clearSkinCategorySelectedFlags()
    {
        this.looseSkinsSelected = false;
        this.companySkinsSelected = false;
        this.nonCompanySkinsSelected = false;
    }

    
}
