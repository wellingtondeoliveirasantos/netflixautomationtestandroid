package br.com.netflix.automationteste.android.tests.home;

import br.com.netflix.automationtest.android.setup.Initializer;
import br.com.netflix.automationteste.android.testcases.home.HomeCases;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;

public class HomeTest extends Initializer {
    private HomeCases home;

    @BeforeEach
    public void Background(){
        home = new HomeCases();
    }

    @Tag("Regression")
    @Order(1)
    @Epic("Automation Test")
    @Feature("Home")
    @Story("Home")
    @DisplayName("Home Dragon Banguela")
    @Test
    public void homeBanquela() throws Exception {
            home.homeClicarDragon();
    }
}
