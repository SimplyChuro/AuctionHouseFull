package actors;

import javax.inject.Named;
import javax.inject.Inject;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import java.util.List;

import models.Users;


import java.util.concurrent.TimeUnit;

public class CleanerActor {

	private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;

    @Inject
    public CleanerActor(ActorSystem actorSystem, ExecutionContext executionContext) {
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;

        this.initialize();
    }

    private void initialize() {
    	this.actorSystem.scheduler().schedule(
            Duration.create(1, TimeUnit.MINUTES),
            Duration.create(2, TimeUnit.DAYS),
            () -> cleanUsers(),
            this.executionContext
        );
    }
    
    private void cleanUsers() {
    	List<Users> users = Users.find.all();
    	for(Users user : users) {
    		if(!user.emailVerified) {
				user.address.delete();
				user.delete();
    		}
    	}
    }

}
