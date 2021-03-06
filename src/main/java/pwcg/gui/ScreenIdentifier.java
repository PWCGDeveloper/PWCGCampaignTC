package pwcg.gui;

public enum ScreenIdentifier
{
    PwcgMainScreen("PwcgMainScreen", "PWCG Main Screen", "MainFullScreen.jpg"),
    CampaignGeneratorScreen("CampaignGeneratorScreen", "Campaign Generation Screen", "CampaignGenFullScreen.jpg"),
    CampaignDeleteScreen("CampaignDeleteScreen", "Campaign Delete Screen", "CampaignGenFullScreen.jpg"),
    CampaignNewCrewMemberScreen("CampaignNewCrewMemberScreen", "Campaign Add New CrewMember Screen", "CampaignGenFullScreen.jpg"),
    
    PwcgSkinConfigurationAnalysisScreen("PwcgSkinConfigurationAnalysisScreen", "PWCG Skin Analysis Screen", "BrickWall.jpg"),
    PwcgTanksOwnedConfigurationScreen("PwcgTanksOwnedConfigurationScreen", "PWCG Planes Owned Config Screen", "BrickWall.jpg"),
    PwcgMusicConfigScreen("PwcgMusicConfigScreen", "PWCG Music Config Screen", "BrickWall.jpg"),
    CampaignHomeScreen("CampaignHomeScreen", "Campaign Home Screen", "BrickWall.jpg"),
    BriefingCrewMemberSelectionScreen("BriefingCrewMemberSelectionScreen", "Briefing CrewMember Selection Screen", "BrickWall.jpg"),
    BriefingDescriptionScreen("BriefingDescriptionScreen", "Briefing Description Screen", "BrickWall.jpg"),
    MapScreens("MapScreens", "All Map Screens", "BrickWall.jpg"),
    DebriefMissionDescriptionScreen("DebriefMissionDescriptionScreen", "Debrief Mission Description Screen", "BrickWall.jpg"),

    CampaignCrewMemberScreen("CampaignCrewMemberScreen", "Campaign CrewMember Screen", "CrewDeskTop.png"),

    PwcgCoopGlobalAdminScreen("PwcgCoopGlobalAdminScreen", "PWCG Global Coop Admin Screen", "TableTop.jpg"),
    PwcgSkinConfigurationAnalysisDisplayScreen("PwcgSkinConfigurationAnalysisDisplayScreen", "PWCG Skin Analysis Display Screen", "TableTop.jpg"),
    PwcgGlobalConfigurationScreen("PwcgGlobalConfigurationScreen", "PWCG Global Configuration Screen", "TableTop.jpg"),
    CampaignAdvancedConfigurationScreen("CampaignAdvancedConfigurationScreen", "Campaign Advanced Configuration Screen", "TableTop.jpg"),
    CampaignSimpleConfigurationScreen("CampaignSimpleConfigurationScreen", "Campaign Simple Configuration Screen", "TableTop.jpg"),
    CampaignEquipmentDepotScreen("CampaignEquipmentDepotScreen", "Campaign Equipment Depo Screen", "TableTop.jpg"),
    CampaignCoopAdminScreen("CampaignCoopAdminScreen", "Campaign Coop Admin Screen", "TableTop.jpg"),
    CampaignReferenceCrewMemberSelectorScreen("CampaignReferenceCrewMemberSelectorScreen", "Campaign Reference CrewMember Selector Screen", "TableTop.jpg"),
    CampaignIntelligenceReportScreen("CampaignIntelligenceReportScreen", "Campaign Intelligence Report Screen", "TableTop.jpg"),
    CampaignJournalScreen("CampaignJournalScreen", "Campaign Journal Screen", "TableTop.jpg"),
    CampaignCompanyLogScreen("CampaignCompanyLogScreen", "Campaign Company Log Screen", "TableTop.jpg"),
    CampaignNewsScreen("CampaignNewsScreen", "Campaign News Screen", "TableTop.jpg"),
    Newspaper("Newspaper", "Newspaper", "Newspaper.jpg"),
    CampaignMedalScreen("CampaignMedalScreen", "Campaign Medal Screen", "TableTop.jpg"),    
    CampaignCrewMemberLogScreen("CampaignCrewMemberLogScreen", "Campaign CrewMember Log Screen", "TableTop.jpg"),    
    CampaignSkinConfigurationScreen("CampaignSkinConfigurationScreen", "Campaign Skin Configuration Screen", "TableTop.jpg"),    
    CampaignLeaveScreen("CampaignLeaveScreen", "Campaign Leave Screen", "TableTop.jpg"),    
    CampaignTransferScreen("CampaignTransferScreen", "Campaign Transfer Screen", "TableTop.jpg"),  
    BriefingCoopPersonaChooser("BriefingCoopPersonaChooser", "Briefing Coop Persona Chooser Screen", "TableTop.jpg"),    
    BriefingRoleChooser("BriefingRoleChooser", "Briefing Role Chooser Screen", "TableTop.jpg"),    
    AARInitiationScreen("AARInitiationScreen", "AAR Initiation Screen", "TableTop.jpg"),    
    AARReportMainPanel("AARReportMainPanel", "AAR Report Tab Screen", "TableTop.jpg"),    
    MissingSkinScreen("MissingSkinScreen", "Missing Skin Screen", "TableTop.jpg"),    
    BriefingEditorEditorScreen("BriefingEditorEditorScreen", "Briefing Waypoint Editor Screen", "TableTop.jpg"),
        
    CampaignCrewMemberChalkboard("CampaignCrewMemberChalkboard", "Campaign CrewMember Chalkboard", "chalkboard.png"),
    CampaignEquipmentChalkboard("CampaignEquipmentChalkboard", "Campaign Equipment Chalkboard", "chalkboard.png"),
    PlaqueBronzeBackground("PlaqueBronzeBackground", "Campaign Home CrewMember List Plaque", "PlaqueBronzeBackground.png"),
    CampaignHomeCompanyPlaque("CampaignHomeCompanyPlaque", "Campaign Home Company Plaque", "PlaqueBronze.png"),
    Document("Document", "Document", "document.png"),
    BlankDocument("BlankDocument", "Blank Document", "BlankDocument.png"),
    DocumentBag("DocumentBag", "DocumentBag", "DocumentBag.png"),
    DocumentFolder("DocumentFolder", "DocumentFolder", "DocumentFolder.png"),
    OpenMedalBox("OpenMedalBox", "Open Medal Box", "OpenMedalBox.png"),
    OpenCrewMemberLog("OpenCrewMemberLog", "Open Crew Member Log", "OpenCrewLog.jpg"),
    OpenJournal("OpenJournal", "Open CrewMember Journal", "OpenJournal.png"),
    OpenCompanyLog("OpenCompanyLog", "Open Company Log Book", "OpenJournal.png");
    
    private String screenKey;
    private String screenDescription;
    private String defaultImageName;

    ScreenIdentifier(String screenKey, String screenDescription, String defaultImageName)
    {
        this.screenKey = screenKey;
        this.screenDescription = screenDescription;
        this.defaultImageName = defaultImageName;
    }

    public String getScreenKey()
    {
        return screenKey;
    }

    public String getScreenDescription()
    {
        return screenDescription;
    }

    public String getDefaultImageName()
    {
        return defaultImageName;
    }
}
