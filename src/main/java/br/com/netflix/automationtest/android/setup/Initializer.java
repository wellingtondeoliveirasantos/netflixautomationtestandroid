package br.com.netflix.automationtest.android.setup;

import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class Initializer {

    private static AndroidDriver driver;

    @BeforeEach
    public void initDriver() {
        String executionMode = System.getProperty("execution.mode", "local").trim().toLowerCase();
        System.out.println("🔍 execution.mode = " + executionMode);

        switch (executionMode) {
            case "sauce":
            case "saucelabs":
                System.out.println("☁️ Iniciando testes no Sauce Labs...");
                driver = new InitConfigSaucelabs().createDriver(getClass().getSimpleName());
                break;

            default:
                System.out.println("💻 Iniciando testes localmente...");
                driver = new InitConfig().createDriver(getClass().getSimpleName());
                break;
        }
    }

    @AfterEach
    public void tearDownDriver() {
        if (driver != null) {
            driver.quit();
            System.out.println("🛑 Driver finalizado.");
        }
    }

    public static AndroidDriver getDriver() {
        return driver;
    }
}
