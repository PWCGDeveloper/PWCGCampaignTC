package pwcg.gui.maingui;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.UIManager;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;

public class PwcgMain
{
	public static void main(String[] args) 
	{
        PwcgMain pwcg = new PwcgMain();
        pwcg.startPWCGTC();
	}

	public PwcgMain() 
	{
	}
	

	private void startPWCGTC()
	{
        try
        {
            validatetestDriverNotEnabled();            
            setProduct();
            initializePWCGStaticData();
            setupUIManager();
            
            PwcgMainScreen campaignMainScreen = new PwcgMainScreen();
            campaignMainScreen.makePanels();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
	}

    private void validatetestDriverNotEnabled()
    {
        TestDriver testDriver = TestDriver.getInstance();
        if (testDriver.isEnabled())
        {
            ErrorDialog.userError("PWCG test driver is enabled - PWCG will not function normally");
        }
    }

    private void setProduct() throws PWCGException
    {
        
    }

    private void initializePWCGStaticData()
    {
        PWCGContext.getInstance();
    }

    private void setupUIManager() throws PWCGException
    {
        Color tabSelectedColor = ColorMap.PAPER_BACKGROUND;
        UIManager.put("TabbedPane.selected", tabSelectedColor);
        UIManager.put("TabbedPane.contentOpaque", false);
        
        Insets insets = UIManager.getInsets("TabbedPane.contentBorderInsets");
        insets.top = -1;
        UIManager.put("TabbedPane.contentBorderInsets", insets);
        
        UIManager.put("OptionPane.background", ColorMap.NEWSPAPER_BACKGROUND);
        UIManager.getLookAndFeelDefaults().put("Panel.background", ColorMap.NEWSPAPER_BACKGROUND);
    }
}
