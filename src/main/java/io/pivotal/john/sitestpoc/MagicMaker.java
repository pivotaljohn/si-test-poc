package io.pivotal.john.sitestpoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class MagicMaker {
	private static final Logger log = LoggerFactory.getLogger(MagicMaker.class);

	private static AtomicInteger invocationCount = new AtomicInteger(0);

	public static int getInvocationCount() {
		return invocationCount.get();
	}

	public static void clearInvocationCount() {
		invocationCount.set(0);
	}

	public MagicMaker() throws InterruptedException {
		log.info("*** Magic Maker coming to life...");
		Thread.sleep(20_000);  // simulate "expensive" app ctx start-up; easier to notice re-instantiation in tests.
		log.info("*** Magic Maker ready to make the magic...");
	}

	public Music bibby(Groove groove) {
		return new Music(riff(groove));
	}

	private List<String> riff(Groove groove) {
		invocationCount.incrementAndGet();
		return IntStream.range(0, groove.getFunkiness())
			.mapToObj(funk -> groove.getName() + funk)
			.collect(Collectors.toList());
	}
}
