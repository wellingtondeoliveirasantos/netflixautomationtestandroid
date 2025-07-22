package br.com.netflix.automationtest.android.feature.home;

import br.com.netflix.automationtest.android.util.Actions;

public class HomeAct extends Actions {

    HomeMap homeMap = new HomeMap();

    public void clicarCardDragon() throws Exception{
        aguardaElementoEstarVisivel(homeMap.cardDragon);
        clicarElemento(homeMap.cardDragon,"Clicar no Card do Dragao");
    }

}
