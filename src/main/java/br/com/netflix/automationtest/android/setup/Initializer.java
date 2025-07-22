package br.com.netflix.automationtest.android.setup;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class Initializer {

    private InitConfig config;

    @BeforeEach
    public void initDriver() {
        config = new InitConfig();
        config.createDriver(getClass().getSimpleName()); // Usa o nome da classe como referência
    }

    @AfterEach
    public void tearDownDriver() {
        InitConfig.quitDriver();
    }

    @Step("{stepDescription}")
    public void step(String stepDescription) {
        Allure.step(stepDescription);
    }

    public static AndroidDriver getDriver() {
        return InitConfig.getDriverInstance();
    }
}
