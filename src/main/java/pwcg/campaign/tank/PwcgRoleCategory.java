package pwcg.campaign.tank;

public enum PwcgRoleCategory
{
    FIGHTER("Fighter", PwcgRole.ROLE_FIGHTER),
    ATTACK("Attack", PwcgRole.ROLE_ATTACK),
    BOMBER("Bomber", PwcgRole.ROLE_BOMBER),
    TRANSPORT("Transport", PwcgRole.ROLE_TRANSPORT),
    GROUND_UNIT("Ground Unit", PwcgRole.ROLE_NONE),
    
    MAIN_BATTLE_TANK("Tank Unit", PwcgRole.ROLE_MAIN_BATTLE_TANK),
    TANK_DESTROYER("Tank Destroyer Unit", PwcgRole.ROLE_TANK_DESTROYER),
    SELF_PROPELLED_GUN("Self Propelled Gun Unit", PwcgRole.ROLE_SELF_PROPELLED_GUN),
    SELF_PROPELLED_AAA("Self Propelled AAA Unit", PwcgRole.ROLE_SELF_PROPELLED_AAA),
    ARMORED_CAR("Armored Car Unit", PwcgRole.ROLE_ARMORED_CAR),
    OTHER("other", PwcgRole.ROLE_NONE);

    private String roleCategoryDescription;
    private PwcgRole defaultRole;
    
    PwcgRoleCategory (String roleCategoryDescription, PwcgRole defaultRole) 
    {
        this.roleCategoryDescription = roleCategoryDescription;
        this.defaultRole = defaultRole;
    }
    
    public static PwcgRoleCategory getRoleCategoryFromDescription(String description)
    {
        for (PwcgRoleCategory roleCategory : PwcgRoleCategory.values())
        {
            if (roleCategory.getRoleCategoryDescription().equals(description))
            {
                return roleCategory;
            }
        }
        return PwcgRoleCategory.OTHER;
    }

    public String getRoleCategoryDescription()
    {
        return roleCategoryDescription;
    }

    public PwcgRole getDefaultRole()
    {
        return defaultRole;
    }

    public void setDefaultRole(PwcgRole defaultRole)
    {
        this.defaultRole = defaultRole;
    }

}
