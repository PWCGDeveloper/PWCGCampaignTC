package integration.campaign.io.json;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.io.json.ConfigurationIOJson;
import pwcg.core.config.ConfigSet;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class ConfigurationIOJsonTest
{
    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        String path = System.getProperty("user.dir") + "\\TCData\\input\\Configuration\\";
        Map<String, ConfigSet> configSet = ConfigurationIOJson.readJson(path);
        Assertions.assertTrue (configSet.size() > 0);
    }
}
