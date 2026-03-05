package poshtar.tests.injection;

import org.springframework.stereotype.Service;

@Service
public class DummyLoggingService {
    public String log(String message){
        return "Logged: " + message;
    }
}
