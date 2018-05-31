package actor;


import java.util.Optional;
import java.util.concurrent.TimeUnit;


import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;

public class TestActor extends AbstractActor {
	
	public static class Schedule{ }
	
	
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	
	Boolean isTimer=false;
	Cancellable scheduleCancellable;
	
	
	public TestActor()
	{
		this(new Integer(1));
	}
	
	
	public TestActor(Integer dummy)
	{
		log.info("Create TestActor");
	}
	
	 public static Props props(Integer magicNumber) {
	    // You need to specify the actual type of the returned actor
	    // since Java 8 lambdas have some runtime type information erased
	    return Props.create(TestActor.class, () -> new TestActor(magicNumber));
	  }
	
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(String.class, meg->{
					log.info("Received String message: {}", meg);
					
					if(meg.equalsIgnoreCase("PING"))
					{
						log.info("Send PONG");
						getSender().tell("PONG", this.getSelf());
					}else if(meg.equalsIgnoreCase("START_TIMER"))
					{
						isTimer=true;
						getContext().setReceiveTimeout(Duration.create(1, TimeUnit.SECONDS));
					}else if(meg.equalsIgnoreCase("STOP_TIMER"))
					{
						isTimer=false;
					}else if(meg.equalsIgnoreCase("START_SCHEDULE"))
					{
						scheduleCancellable = getContext().getSystem().scheduler().scheduleOnce(Duration.create(5, TimeUnit.SECONDS),
								new Runnable() {
									@Override
									public void run() {
										getSelf().tell(new Schedule(), ActorRef.noSender());
									}
								}, getContext().getSystem().dispatcher());
					}
					
					
				}).match(Exception.class, meg->{
					throw meg;
				}).match(ReceiveTimeout.class,meg->{
					
					log.info("Received ReceiveTimeout");
					if(isTimer==true)
					{
						getContext().setReceiveTimeout(Duration.create(1, TimeUnit.SECONDS));
					}
				
				}).match(Schedule.class,meg->{
					
					log.info("Received Schedule");
			
				}).matchAny( o -> log.info("received unknown message")
						)
				.build();
	}

	@Override
	public void postRestart(Throwable reason) throws Exception {
		log.info("lifecycle : {}", "postRestart()");
		super.postRestart(reason);
	}

	@Override
	public void postStop() throws Exception {
		log.info("lifecycle : {}", "postStop()");
		super.postStop();
	}

	@Override
	public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
		log.info("lifecycle : {}", "preRestart()");
		super.preRestart(reason, message);
	}

	@Override
	public void preStart() throws Exception {
		log.info("lifecycle : {}", "preStart()");
		super.preStart();
	}

}
