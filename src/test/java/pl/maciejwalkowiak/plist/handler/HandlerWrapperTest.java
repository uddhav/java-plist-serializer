package pl.maciejwalkowiak.plist.handler;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class HandlerWrapperTest {
	private HandlerWrapper handlerWrapper;

	@Before
	public void setup() {
		handlerWrapper = new HandlerWrapper(null);
	}

	@Test
	public void testGetHandlerForObjectWhenHandlerFound() throws Exception {
		//given
		//handler supporting boolean and integer

		//when
		Handler handler = handlerWrapper.getHandlerForObject(Boolean.TRUE);

		//then
		assertThat(handler).isInstanceOf(BooleanHandler.class);
	}

	@Test
	public void testGetHandlerForObjectWhenHandlerNotFound() throws Exception {
		//given
		//handler supporting boolean and integer
		UUID uuid = UUID.randomUUID();

		//when
		try {
			handlerWrapper.getHandlerForObject(uuid);

			//then
			fail();
		} catch (HandlerNotFoundException e) {

		}
	}

	@Test
	public void testIsSupported() throws Exception {
		assertThat(handlerWrapper.isSupported(UUID.randomUUID())).isFalse();
		assertThat(handlerWrapper.isSupported(Boolean.FALSE)).isTrue();
	}
}
