package br.com.netflix.automationtest.android.util;

import br.com.netflix.automationtest.android.setup.Initializer;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import lombok.Setter;
import org.openqa.selenium.*;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Random;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Actions extends Initializer {

    @Setter
    protected static AndroidDriver driver;
    private LocalCommandLine runCommand;
    public static final Path ROOT = FileSystems.getDefault().getPath("").toAbsolutePath();

    public static AndroidDriver getDriver() {
        if (Initializer.getDriver() == null) {
            throw new IllegalStateException("O driver não foi inicializado. Certifique-se de que o método setup foi chamado.");
        }
        return Initializer.getDriver();
    }

    public WebElement aguardaElementoEstarVisivel(By element) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        } catch (TimeoutException e) {
            throw new RuntimeException("O elemento não ficou visível após 30 segundos: " + element, e);
        }
    }

    public String retornarTexto(By element) {
        return aguardaElementoEstarVisivel(element).getText().trim();
    }

    public void clicarElemento(By element, String nomeElemento) {
        try {
            WebElement mobileElement = aguardaElementoEstarVisivel(element);
            mobileElement.click();
            System.out.println(nomeElemento + " clicado com sucesso.");
        } catch (WebDriverException e) {
            System.out.println("Elemento '" + nomeElemento + "' não encontrado ou não clicável. Ignorando.");
        }
    }


    public void digitarElemento(By element, String valor) throws InterruptedException {
        try {
            WebElement mobileElement = aguardaElementoEstarVisivel(element);
            for (char c : valor.toCharArray()) {
                mobileElement.sendKeys(String.valueOf(c));
                Thread.sleep(10); // Simula digitação humana
            }
            mobileElement.sendKeys(Keys.RETURN);
            System.out.println("Valor '" + valor + "' digitado com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao tentar digitar no elemento. Ignorando.");
        }
    }

    protected String gerarNomeAleatorio() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int tamanho = 8;
        Random random = new Random();
        StringBuilder nome = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            nome.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return "Categoria-" + nome;
    }

    protected String gerarNomes() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int tamanho = 8;
        Random random = new Random();
        StringBuilder nome = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            nome.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return "Nome" + nome;
    }

    protected StringBuilder gerarCodigoAleatorio() {
        String caracteres = "01234";
        int tamanho = 5;
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return codigo;
    }

    public void arrastarElemento(By elementoLocalizador, int offsetX, int offsetY) {
        try {
            WebElement elemento = getDriver().findElement(elementoLocalizador);
            int startX = elemento.getLocation().getX() + (elemento.getSize().getWidth() / 2);
            int startY = elemento.getLocation().getY() + (elemento.getSize().getHeight() / 2);
            int endX = startX + offsetX;
            int endY = startY + offsetY;

            System.out.println("Iniciando swipe de (" + startX + ", " + startY + ") para (" + endX + ", " + endY + ")");

            new TouchAction<>(getDriver())
                    .press(point(startX, startY))
                    .waitAction(waitOptions(ofMillis(500)))
                    .moveTo(point(endX, endY))
                    .release()
                    .perform();
        } catch (Exception e) {
            System.err.println("Erro ao arrastar o elemento: " + e.getMessage());
            throw e;
        }
    }

    public void limparElemento(By element) {
        aguardaElementoEstarVisivel(element).clear();
    }

    public void comparaTextosEsperadoElement(String textoEsperado, By element) {
        assertEquals(textoEsperado, retornarTexto(element));
    }

    public void comparaTextosEsperadoFixos(String textoEsperado, String textoObtido) {
        assertEquals(textoEsperado, textoObtido);
    }

    public void esperarElementoVisivel(By localizador, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.visibilityOfElementLocated(localizador));
    }

    public void sendKeysCommandLine(String text) throws IOException {
        runCommand = new LocalCommandLine();
        runCommand.executeCommand("adb shell input text " + text);
    }

    public void getFileCommandLine() throws IOException {
        runCommand = new LocalCommandLine();
        runCommand.executeCommand("adb pull /sdcard/Download/downloadfile.pdf " + ROOT + "/output");
    }

    public void removeFileCommandLine() throws IOException {
        runCommand = new LocalCommandLine();
        runCommand.executeCommand("adb shell rm -f -rR -v /sdcard/Download/downloadfile.pdf");
    }
}
