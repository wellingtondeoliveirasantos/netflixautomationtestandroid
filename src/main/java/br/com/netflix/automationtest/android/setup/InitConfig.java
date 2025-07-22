package br.com.netflix.automationtest.android.setup;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class InitConfig {

    private static AndroidDriver driver;
    private Properties properties;
    public static final Path ROOT = FileSystems.getDefault().getPath("").toAbsolutePath();

    public InitConfig() {
        properties = new Properties();
        String configFilePath = ROOT + "/src/main/resources/globalParameter.properties";
        try (InputStream input = new FileInputStream(configFilePath)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar o arquivo de configuração", e);
        }
    }

    public URL getUrl() {
        try {
            // URL padrão do Appium Server local
            return new URL("http://127.0.0.1:4723/");
        } catch (Exception e) {
            throw new RuntimeException("URL inválida para Appium Server local", e);
        }
    }

    public synchronized AndroidDriver createDriver(String testDisplayName) {
        if (driver != null) {
            return driver;
        }

        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Lê e seta as capabilities do properties
        capabilities.setCapability("platformName", properties.getProperty("platformName"));
        capabilities.setCapability("automationName", properties.getProperty("automationName"));
        capabilities.setCapability("deviceName", properties.getProperty("deviceName"));

        // Monta caminho absoluto para o app
        String appPath = properties.getProperty("app");
        if (appPath.startsWith("/")) {
            // absoluto
            capabilities.setCapability("app", appPath);
        } else {
            // relativo à raiz do projeto
            String absoluteAppPath = Paths.get(ROOT.toString(), appPath).toString();
            capabilities.setCapability("app", absoluteAppPath);
        }

        // Setar appPackage e appActivity, se existirem
        String appPackage = properties.getProperty("appPackage");
        if (appPackage != null && !appPackage.isEmpty()) {
            capabilities.setCapability("appPackage", appPackage);
        }

        String appActivity = properties.getProperty("appActivity");
        if (appActivity != null && !appActivity.isEmpty()) {
            capabilities.setCapability("appActivity", appActivity);
        }

        // Outros capabilities
        String noReset = properties.getProperty("noReset");
        if (noReset != null) {
            capabilities.setCapability("noReset", Boolean.parseBoolean(noReset));
        }

        String newCommandTimeout = properties.getProperty("newCommandTimeout");
        if (newCommandTimeout != null) {
            capabilities.setCapability("newCommandTimeout", Integer.parseInt(newCommandTimeout));
        }

        // Permissões automáticas
        capabilities.setCapability("autoGrantPermissions", true);

        try {
            driver = new AndroidDriver(getUrl(), capabilities);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao iniciar driver Android local", e);
        }

        return driver;
    }

    public static synchronized AndroidDriver getDriverInstance() {
        if (driver == null) {
            throw new IllegalStateException("Driver ainda não inicializado.");
        }
        return driver;
    }

    public static synchronized void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Erro ao finalizar driver: " + e.getMessage());
            } finally {
                driver = null;
            }
        }
    }
}
