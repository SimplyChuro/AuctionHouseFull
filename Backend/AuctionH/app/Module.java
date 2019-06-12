import com.google.inject.AbstractModule;

import actors.CleanerActor;
import actors.NotificationActor;
import actors.SaleNotificationActor;

import java.time.Clock;

import services.ApplicationTimer;
import services.AtomicCounter;
import services.Counter;

public class Module extends AbstractModule {

    @Override
    public void configure() {
    	
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
        bind(ApplicationTimer.class).asEagerSingleton();
        bind(Counter.class).to(AtomicCounter.class);

//        bind(NotificationActor.class).asEagerSingleton();
        bind(CleanerActor.class).asEagerSingleton();
        bind(SaleNotificationActor.class).asEagerSingleton();
    }

}
