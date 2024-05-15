package edu.ub.pis2324.projecte;

import android.app.Application;

public class App extends Application {
    private AppContainer appContainer;

    @Override
    public void onCreate(){
        super.onCreate();
        appContainer = new AppContainer();
    }

    public AppContainer getAppContainer(){
        return appContainer;
    }
}
