package poshtar.tests.notification.injection;

import org.springframework.stereotype.Service;

@Service
public class DummyIncrementService {
    public int inc(int value){
        return value + 1;
    }
}
