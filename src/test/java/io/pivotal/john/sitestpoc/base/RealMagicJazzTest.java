package io.pivotal.john.sitestpoc.base;

import io.pivotal.john.sitestpoc.Groove;
import io.pivotal.john.sitestpoc.MagicMaker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.Lifecycle;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class RealMagicJazzTest {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private IntegrationFlow jazz;

	public void startupIntegration() {
		Lifecycle hiphop = (Lifecycle) jazz;
		if(!hiphop.isRunning()) {
			hiphop.start();
		}
	}
	@Before
	public void resetInvocationCount() {
		MagicMaker.clearInvocationCount();
	}

	public void shutdownIntegration() {
		Lifecycle hiphop = (Lifecycle) jazz;
		if(hiphop.isRunning()) {
			hiphop.stop();
		}
	}

	@Test
	public void jazz_flows() throws Exception {
		startupIntegration();
		List<Groove> theSoul = new ArrayList<>();
		theSoul.add(new Groove("Court and Spark", 1));
		theSoul.add(new Groove("Hardrock", 2));
		theSoul.add(new Groove("Junku", 3));
		theSoul.add(new Groove("Little One", 4));
		theSoul.add(new Groove("Rockit", 5));
		theSoul.add(new Groove("Sweet Bird",6));

		theSoul.forEach(g -> jmsTemplate.convertAndSend("the.vibe", g));
		try {
			waitUntil(() -> MagicMaker.getInvocationCount() == 6, 1_000);
			assertThat(MagicMaker.getInvocationCount()).isEqualTo(6);
		} finally {
			shutdownIntegration();
		}
	}

	private void waitUntil(BooleanSupplier desiredCondition, int timeoutInMillis) throws InterruptedException {
		long deadline = System.currentTimeMillis() + timeoutInMillis;
		while(System.currentTimeMillis() < deadline && !desiredCondition.getAsBoolean()) {
			Thread.sleep(10);
		}
	}
}
