package org.nikola.velemir.poshtar.guice.adapter.request.deps.injection;


import com.google.inject.Singleton;

@Singleton
public class DummyLoggingService {
    public String log(String message){
        return "Logged: " + message;
    }
}
