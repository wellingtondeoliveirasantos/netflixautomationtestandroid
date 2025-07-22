
# Android Automation Testing - Stream Player App

Automação de testes para o aplicativo Android **Stream Player App** utilizando **Java 21**, **Appium**, **JUnit 5** e **Allure** para relatórios.

---

## Índice

- [Descrição](#descrição)
- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Configuração](#configuração)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Execução dos Testes](#execução-dos-testes)
- [Configuração do Driver - `InitConfig`](#configuração-do-driver---initconfig)
- [Exemplo de Teste](#exemplo-de-teste)
- [Resolução de Problemas Comuns](#resolução-de-problemas-comuns)
- [Autor](#autor)
- [Licença](#licença)

---

## Descrição

Este projeto automatiza o teste funcional do aplicativo Android **Stream Player App** usando Appium, JUnit 5 e gera relatórios com Allure. O foco está na criação de testes robustos e reutilizáveis que podem ser executados localmente em emuladores Android ou dispositivos reais.

---

## Tecnologias

- Java 21
- Appium Java Client 9.0.0
- Selenium WebDriver 4.15.0
- JUnit 5
- Allure
- Gradle
- Android Emulator / Dispositivo físico
- Appium Server

---

## Pré-requisitos

- JDK 21 instalado
- Android SDK configurado com pelo menos um emulador criado
- Appium Server instalado e configurado ([Instruções](https://appium.io/docs/en/latest/))
- APK do aplicativo a ser testado (ex: `app-debug.apk`)
- Variáveis de ambiente configuradas para `ANDROID_HOME` e `PATH` com SDK tools

---

## Configuração

### 1. Arquivo `globalParameter.properties`

Coloque no caminho `src/main/resources/globalParameter.properties` com as configurações:

```properties
platformName=Android
automationName=UiAutomator2
deviceName=Android Emulator
app=src/test/resources/app/app-debug.apk
appPackage=com.codandotv.streamplayerapp
appActivity=.MainActivity
noReset=true
newCommandTimeout=300
```

### 2. Arquivo `build.gradle`

Configuração do Gradle com as principais dependências e plugins:

```groovy
plugins {
    id 'java'
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

configurations {
    agent {
        canBeResolved = true
        canBeConsumed = false
    }
}

tasks.withType(JavaCompile).configureEach {
    options.encoding = 'UTF-8'
    options.compilerArgs.add('-parameters')
}

dependencies {
    agent "org.aspectj:aspectjweaver:1.9.22"
    implementation 'io.qameta.allure:allure-java-commons:2.21.0'
    implementation 'org.seleniumhq.selenium:selenium-java:4.15.0'
    implementation 'org.junit.jupiter:junit-jupiter-api:5.9.2'
    implementation 'org.junit.platform:junit-platform-engine:1.9.2'
    implementation 'org.junit.platform:junit-platform-commons:1.9.2'
    implementation group: 'com.browserstack', name: 'browserstack-local-java', version: '1.1.4'
    implementation 'io.appium:java-client:9.0.0'
    testImplementation "org.slf4j:slf4j-simple:2.0.12"
    implementation 'commons-io:commons-io:2.15.1'
    compileOnly 'org.projectlombok:lombok:1.18.24'
    testImplementation 'org.junit.jupiter:junit-jupiter-engine:5.9.0'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.9.0'
    testImplementation 'com.google.inject:guice:7.0.0'
    testImplementation "io.qameta.allure:allure-junit5"
    testImplementation platform("io.qameta.allure:allure-bom:2.26.0")
}

tasks.withType(JavaCompile) {
    options.encoding = 'UTF-8'
}

test {
    ignoreFailures = true
    useJUnitPlatform {
        if (System.getProperty('includeTags') != null) {
            includeTags System.getProperty('includeTags')
        }
    }
    systemProperty "allure.results.directory", "target/allure-results"
    jvmArgs = [ "-javaagent:${configurations.agent.singleFile}" ]
    testLogging.showStandardStreams = true
    reports.junitXml.required = false
}
```

### 3. Iniciar Appium Server

```bash
appium
```

### 4. Iniciar Emulador ou conectar dispositivo

```bash
emulator -avd NomeDoSeuEmulador
```

ou

```bash
adb devices
```

---

## Estrutura do Projeto

```
.
├── src
│   ├── main
│   │   └── java
│   │       └── br/com/netflix/automationtest/android/setup
│   │           └── InitConfig.java
│   └── test
│       └── java
│           └── br/com/netflix/automationtest/android/tests
│               └── HomeTest.java
├── src/main/resources
│   └── globalParameter.properties
├── src/test/resources/app
│   └── app-debug.apk
├── build.gradle
└── README.md
```

---

## Execução dos Testes

```bash
./gradlew clean test
```

---

## Configuração do Driver - `InitConfig`

Classe responsável por carregar configurações, montar capabilities e criar a instância do driver Appium.

---

## Exemplo de Teste

```java
package br.com.netflix.automationtest.android.tests.home;

import br.com.netflix.automationtest.android.setup.InitConfig;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.*;
import io.qameta.allure.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HomeTest {

    private AndroidDriver driver;

    @BeforeEach
    public void setup() {
        driver = new InitConfig().createDriver("HomeTest");
    }

    @Test
    @Tag("Regression")
    @Order(1)
    @Epic("Automation Test")
    @Feature("Home")
    @Story("Home")
    @DisplayName("Home Dragon Banguela")
    public void homeBanquela() throws Exception {
        // Exemplo de chamada a método que executa ação no app
        home.homeClicarDragon();
    }

    @AfterEach
    public void tearDown() {
        InitConfig.quitDriver();
    }
}
```

---

## Resolução de Problemas Comuns

- **Application not found**  
  Verifique se o caminho do APK no arquivo de propriedades está correto e o arquivo está presente.

- **Must provide a selector when finding elements**  
  Confirme que os seletores usados (ID, XPath, etc.) não estão vazios ou incorretos.

- **Appium Server não conecta**  
  Verifique se o Appium Server está ativo na URL e porta corretas (`http://127.0.0.1:4723`).

- **Driver não inicializa**  
  Verifique versões do Appium, Android SDK e configurações das capabilities.

---

## Autor

Wellington Santos  
Contato: [https://www.linkedin.com/in/wellington-staff-qa-expert/]

---

## Licença

MIT License — uso livre, contribuições são bem-vindas!