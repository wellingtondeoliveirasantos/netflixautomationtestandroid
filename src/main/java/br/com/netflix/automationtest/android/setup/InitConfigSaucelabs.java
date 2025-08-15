package br.com.netflix.automationtest.android.setup;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InitConfigSaucelabs {

    private static AndroidDriver driver;

    public synchronized AndroidDriver createDriver(String testDisplayName) {
        if (driver != null) {
            return driver;
        }

        try {
            DesiredCapabilities caps = new DesiredCapabilities();

            // Configurações obrigatórias para Android no Sauce Labs
            caps.setCapability("platformName", "Android");
            caps.setCapability("appium:deviceName", "Android GoogleAPI Emulator"); // ou um dispositivo real disponível
            caps.setCapability("appium:platformVersion", "12.0");
            caps.setCapability("appium:automationName", "UiAutomator2");

            // Caminho do app enviado para o Sauce Labs usando seu fileId
            caps.setCapability("appium:app", "storage:7739029d-47eb-48ce-8b81-07a23d24ca20");

            // Opções específicas do Sauce Labs (hardcoded)
            Map<String, Object> sauceOptions = new HashMap<>();
            sauceOptions.put("username", "oauth-wellingtondeoliveirasanto-a1a86");
            sauceOptions.put("accessKey", "ef9c8d6d-7326-4468-b076-03c04dc1bb6f");
            sauceOptions.put("build", "automation-tests-260318");
            sauceOptions.put("name", testDisplayName);
            caps.setCapability("sauce:options", sauceOptions);

            // URL do Sauce Labs
            URL sauceUrl = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");

            driver = new AndroidDriver(sauceUrl, caps);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao iniciar driver Android no Sauce Labs", e);
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
