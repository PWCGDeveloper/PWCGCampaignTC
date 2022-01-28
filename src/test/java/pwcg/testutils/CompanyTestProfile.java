package pwcg.testutils;

import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.FrontMapIdentifier;

public enum CompanyTestProfile
{
    
    GROSS_DEUTSCHLAND_PZIV_PROFILE("Gross Deutschland PzIV", 201001001, "19420701", CampaignMode.CAMPAIGN_MODE_SINGLE, FrontMapIdentifier.KUBAN_MAP, false),
    GROSS_DEUTSCHLAND_PROFILE("Gross Deutschland", 201001001, "19420801", CampaignMode.CAMPAIGN_MODE_SINGLE, FrontMapIdentifier.STALINGRAD_MAP, false),
    PZ14_PROFILE("14th Pz Div", 201014001, "19430401", CampaignMode.CAMPAIGN_MODE_SINGLE, FrontMapIdentifier.STALINGRAD_MAP, false),
    PZ16_PROFILE("16th Pz Div", 201016003, "19430401", CampaignMode.CAMPAIGN_MODE_SINGLE, FrontMapIdentifier.STALINGRAD_MAP, false),
    GROSS_DEUTSCHLAND_KUBAN_PROFILE("Gross Deutschland Kuban", 201001001, "19431101", CampaignMode.CAMPAIGN_MODE_SINGLE, FrontMapIdentifier.KUBAN_MAP, false),
    PANZER_LEHR_PROFILE("Panzer Lehr", 201011003, "19440901", CampaignMode.CAMPAIGN_MODE_SINGLE, FrontMapIdentifier.BODENPLATTE_MAP, false),
    
    TANK_DIVISION_146_PROFILE("146th Tank Division", 101146002, "19420901", CampaignMode.CAMPAIGN_MODE_SINGLE, FrontMapIdentifier.STALINGRAD_MAP, false),
    TANK_DIVISION_147_PROFILE("147th Tank Division", 101147001, "19420901", CampaignMode.CAMPAIGN_MODE_SINGLE, FrontMapIdentifier.STALINGRAD_MAP, false),
    TANK_DIVISION_147_PROFILE_KUBAN("147th Tank Division Late", 101147001, "19431001", CampaignMode.CAMPAIGN_MODE_SINGLE, FrontMapIdentifier.KUBAN_MAP, false),
    TANK_DIVISION_147_PROFILE_LATE("147th Tank Division Late", 101147001, "19440901", CampaignMode.CAMPAIGN_MODE_SINGLE, FrontMapIdentifier.EAST1944_MAP, false),
    TANK_DIVISION_147_PROFILE_END("147th Tank Division Late", 101147001, "19450201", CampaignMode.CAMPAIGN_MODE_SINGLE, FrontMapIdentifier.EAST1945_MAP, false),

    THIRD_DIVISION_PROFILE("3rd Armored Division", 102003005, "19440901", CampaignMode.CAMPAIGN_MODE_SINGLE, FrontMapIdentifier.BODENPLATTE_MAP, false),
    SEVENTH_DIVISION_PROFILE("Seventh Armored Division", 103007001, "19440901", CampaignMode.CAMPAIGN_MODE_SINGLE, FrontMapIdentifier.BODENPLATTE_MAP, false),

    COOP_COMPETITIVE_PROFILE("Gross Deutschland Coop",CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId(), "19420801", CampaignMode.CAMPAIGN_MODE_COOP, FrontMapIdentifier.STALINGRAD_MAP, true),
    COOP_COOPERATIVE_PROFILE("Gross Deutschland Competitive",CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId(), "19420801", CampaignMode.CAMPAIGN_MODE_COOP, FrontMapIdentifier.STALINGRAD_MAP, false);

   private int companyId;
   private String dateString;
   private String key;
   private CampaignMode campaignMode;
   private boolean competitive = false;
   private FrontMapIdentifier mapidentifier = FrontMapIdentifier.NO_MAP;
    
   private CompanyTestProfile(String key, int companyId, String dateString, CampaignMode campaignMode, FrontMapIdentifier mapidentifier, boolean iscompetitive)
   {
       this.key = key;
       this.companyId = companyId;
       this.dateString = dateString;
       this.campaignMode = campaignMode;
       this.competitive = iscompetitive;
       this.mapidentifier = mapidentifier;
   }

    public String getKey()
    {
        return key;
    }

    public int getCompanyId()
    {
        return companyId;
    }

    public String getDateString()
    {
        return dateString;
    }

	public CampaignMode getCampaignMode() 
	{
		return campaignMode;
	}

    public boolean isCompetitive()
    {
        return competitive;
    }

    FrontMapIdentifier getMapIdentifier()
    {
        return mapidentifier;
    }
}
