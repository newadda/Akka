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
		System.out.println("=====================���� �����ϱ� Props=======================");
		
		// new �����ڰ� �ƴ� Props �� actor�� ��� ������ �ִ� ������ actor�� akka ������ �����Ǿ�� �ϱ� �����̴�. 
		// �Ϲ����� �׳� ��ü�� �ƴϴ�.
		
		// ����Ʈ �����ڷ� �����ϱ�.(���� ���� ������)
		Props props1 = Props.create(TestActor.class);
		
		// ���� new �����ڸ� ����ϱ� �����ϱ�
		// �� ���� �����ؾ��Ѵ�. actor�� �ٸ� actor�� �����ϱ� ���� �Ʒ��� ���� ����Ѵٸ� �����ؾ��Ѵ�.
		// ()->new TestActor()�� ���� actor�� ������ �� ȣ��ȴ�. �� ���������� ���� ��Ȳ�̴�.
		Props props2 = Props.create(TestActor.class,
		  () -> new TestActor(999)); // �����ؾ��Ѵ�.
		
		// ���ڰ� �ִ� �����ڷ� �����ϱ�
		Props props3 = Props.create(TestActor.class, 999);
		
		
		// ** ��õ���� ������δ� static factory method�� �����ϴ� ���̴�.
		Props props = TestActor.props(999);
		
		ActorRef actor;
		System.out.println("-- �̸� ���� Actor , �ڵ����� �̸��� �����ȴ�.");
	    actor = system.actorOf(props);
	    System.out.println(MessageFormat.format("path = {0}", actor.path()));
	    
	    System.out.println("-- �̸� �ִ� Actor");
	    actor = system.actorOf(props, "test");
	    System.out.println(MessageFormat.format("path = {0}", actor.path()));
	    
	    actor = system.actorOf(props, "parent.test");
	    System.out.println(MessageFormat.format("path = {0}", actor.path()));
		
	    
	    
	}

}
