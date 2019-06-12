package actors;

import javax.inject.Named;
import javax.inject.Inject;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class NotificationActor {

	private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;

    @Inject
    public NotificationActor(ActorSystem actorSystem, ExecutionContext executionContext) {
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;

//        this.initialize();
    }

    private void initialize() {
//    	this.actorSystem.scheduler().schedule(
//            Duration.create(10, TimeUnit.SECONDS),
//            Duration.create(25, TimeUnit.SECONDS),
//            () -> System.out.println("Running block of code"),
//            this.executionContext
//        );
    }
    
    private void sendNotification() {
    	
    }

}
