package org.nikola.velemir.poshtar.spring.adapter.request.deps.injection;

import org.springframework.stereotype.Service;

@Service
public class DummyLoggingService {
    public String log(String message){
        return "Logged: " + message;
    }
}
