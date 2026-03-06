package io.github.anjoismysign.blobwm.director.manager;

import io.github.anjoismysign.blobwm.BlobWM;
import io.github.anjoismysign.blobwm.configuration.WMConfiguration;
import io.github.anjoismysign.blobwm.director.WMManager;
import io.github.anjoismysign.blobwm.director.WMManagerDirector;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ConfigurationManager extends WMManager {
    private static WMConfiguration configuration;

    public ConfigurationManager(WMManagerDirector managerDirector) {
        super(managerDirector);
        reload();
    }

    @Override
    public void reload() {
        BlobWM plugin = getPlugin();
        plugin.saveResource("config.yml", false);
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        Constructor constructor = new Constructor(WMConfiguration.class, new LoaderOptions());
        Yaml yaml = new Yaml(constructor);
        try (FileInputStream inputStream = new FileInputStream(configFile)) {
            configuration = yaml.load(inputStream);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @NotNull
    public static WMConfiguration getConfiguration() {
        return configuration;
    }
}