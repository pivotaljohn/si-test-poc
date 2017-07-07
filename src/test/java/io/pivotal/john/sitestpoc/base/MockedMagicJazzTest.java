package io.pivotal.john.sitestpoc.base;

import io.pivotal.john.sitestpoc.Groove;
import io.pivotal.john.sitestpoc.MagicMaker;
import io.pivotal.john.sitestpoc.Music;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MockedMagicJazzTest {

	@Autowired
	private JmsTemplate jmsTemplate;

	@MockBean()
	private MagicMaker magicMaker;

	@Test
	public void jazz_flows() throws InterruptedException {
		CountDownLatch musicToBeMade = new CountDownLatch(6);
		when(magicMaker.bibby(any(Groove.class))).then(invocation -> {
			musicToBeMade.countDown();
			return new Music();
		});

		List<Groove> theSoul = new ArrayList<>();
		theSoul.add(new Groove("Court and Spark", 1));
		theSoul.add(new Groove("Hardrock", 2));
		theSoul.add(new Groove("Junku", 3));
		theSoul.add(new Groove("Little One", 4));
		theSoul.add(new Groove("Rockit", 5));
		theSoul.add(new Groove("Sweet Bird",6));

		theSoul.forEach(g -> jmsTemplate.convertAndSend("the.vibe", g));
		musicToBeMade.await(1_000, TimeUnit.MILLISECONDS);
		assertThat(musicToBeMade.getCount()).isEqualTo(0);
	}
}
