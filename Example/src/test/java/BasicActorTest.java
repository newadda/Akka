import static org.junit.Assert.*;

import java.text.MessageFormat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import actor.TestActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BasicActorTest {

	static ActorSystem system;

	@BeforeClass
	public static void setup() {
		Config load = ConfigFactory.load("application.conf");
		system = ActorSystem.create("akka", load);
	}

	@AfterClass
	public static void teardown() {
		TestKit.shutdownActorSystem(system);
		system = null;
	}

	@After
	public void testAfter() {
		System.out.println("================================================");
		System.out.println();
		System.out.println();
	}

	@Test
	public void test() {
		System.out.println("=====================액터 생성하기 Props=======================");
		
		// new 연산자가 아닌 Props 가 actor를 대신 생성해 주는 이유는 actor는 akka 내에서 관리되어야 하기 때문이다. 
		// 일반적인 그냥 객체가 아니다.
		
		// 디폴트 생성자로 생성하기.(인자 없는 생성자)
		Props props1 = Props.create(TestActor.class);
		
		// 직접 new 연산자를 사용하기 생성하기
		// 이 경우는 조심해야한다. actor가 다른 actor를 생성하기 위해 아래와 같이 사용한다면 조심해야한다.
		// ()->new TestActor()는 실제 actor가 생성될 때 호출된다. 즉 순차적이지 않은 상황이다.
		Props props2 = Props.create(TestActor.class,
		  () -> new TestActor(999)); // 조심해야한다.
		
		// 인자가 있는 생성자로 생성하기
		Props props3 = Props.create(TestActor.class, 999);
		
		
		// ** 추천하즌 방법으로는 static factory method를 제공하는 것이다.
		Props props = TestActor.props(999);
		
		ActorRef actor;
		System.out.println("-- 이름 없는 Actor , 자동으로 이름이 생성된다.");
	    actor = system.actorOf(props);
	    System.out.println(MessageFormat.format("path = {0}", actor.path()));
	    
	    System.out.println("-- 이름 있는 Actor");
	    actor = system.actorOf(props, "test");
	    System.out.println(MessageFormat.format("path = {0}", actor.path()));
	    
	    actor = system.actorOf(props, "parent.test");
	    System.out.println(MessageFormat.format("path = {0}", actor.path()));
		
	    
	    
	}

}
