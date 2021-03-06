package integration;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.config.ConfigSet;
import pwcg.core.config.ConfigSetKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class ConfigManagerGlobalTest
{

    public ConfigManagerGlobalTest() throws PWCGException
    {
        
    }
    

    @Test
    public void testGlobalConfigurationInitialize() throws PWCGException 
    {
    	ConfigManagerGlobal configManagerGlobal = ConfigManagerGlobal.getInstance();
    	Map<String, ConfigSet> globalConfigSets = configManagerGlobal.getDefaultCampaignConfigSets();
    	
        Assertions.assertTrue (globalConfigSets.containsKey(ConfigSetKeys.ConfigSetGUI));
        Assertions.assertTrue (globalConfigSets.containsKey(ConfigSetKeys.ConfigSetUserPref));
    }
    
    @Test
    public void testGlobalConfigurationChangeParam() throws PWCGException 
    {
        cleanUpUserDir();

    	ConfigManagerGlobal configManagerGlobal = ConfigManagerGlobal.getInstance();
    	configManagerGlobal.reset();
    	int originaMusicVolume = configManagerGlobal.getIntConfigParam(ConfigItemKeys.SoundVolumeKey);
        Assertions.assertTrue (originaMusicVolume == 10);
    	
        configManagerGlobal.setParam(ConfigItemKeys.SoundVolumeKey, "3");
        configManagerGlobal.write();
    	int newMusicVolume = configManagerGlobal.getIntConfigParam(ConfigItemKeys.SoundVolumeKey);
        Assertions.assertTrue (newMusicVolume == 3);

        configManagerGlobal.setParam(ConfigItemKeys.SoundVolumeKey, "3");
        configManagerGlobal.readConfig();;
    	int afterSaveMusicVolume = configManagerGlobal.getIntConfigParam(ConfigItemKeys.SoundVolumeKey);
        Assertions.assertTrue (afterSaveMusicVolume == 3);
    }

    
    @Test
    public void testGlobalConfigurationResetTest() throws PWCGException 
    {
        cleanUpUserDir();

    	ConfigManagerGlobal configManagerGlobal = ConfigManagerGlobal.getInstance();
    	
        configManagerGlobal.setParam(ConfigItemKeys.SoundVolumeKey, "3");
        configManagerGlobal.write();
        
    	int newMusicVolume = configManagerGlobal.getIntConfigParam(ConfigItemKeys.SoundVolumeKey);
        Assertions.assertTrue (newMusicVolume == 3);

    	configManagerGlobal.reset();
    	int originaMusicVolume = configManagerGlobal.getIntConfigParam(ConfigItemKeys.SoundVolumeKey);
        Assertions.assertTrue (originaMusicVolume == 10);

        configManagerGlobal.setParam(ConfigItemKeys.SoundVolumeKey, "3");
        configManagerGlobal.readConfig();;
    	int afterSaveMusicVolume = configManagerGlobal.getIntConfigParam(ConfigItemKeys.SoundVolumeKey);
        Assertions.assertTrue (afterSaveMusicVolume == 10);
    }

    private void cleanUpUserDir() throws PWCGException
    {
        String userConfigDirName = PWCGDirectoryUserManager.getInstance().getPwcgUserConfigDir();
        FileUtils.deleteFilesInDirectory(userConfigDirName);
    }

}
