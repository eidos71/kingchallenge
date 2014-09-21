package test.eidos.kingchanllenge.config;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.eidos.kingchallenge.domain.KingdomHandlerConf;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.persistance.KingdomConfManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RunWith(EasyMockRunner.class)
public class TestHandlersBuilder extends EasyMockSupport {
	static final Logger LOG = LoggerFactory.getLogger(TestHandlersBuilder.class);
	Map<AtomicInteger,Date> lastLogin=Collections.synchronizedMap(new HashMap<AtomicInteger,Date>() );



	@Test(expected =LogicKingChallengeException.class)
	public void testInvalidClass() {
		KingdomHandlerConf handler = new KingdomHandlerConf.Builder("wrong.eidos.kingchallenge.httpserver.handlers.HeraldHandler")
	.contextPath("/herald").build();
	}
	@Test
	public void testKingomDefaultConstructor() {
		String context="/";
		KingdomHandlerConf handler = new KingdomHandlerConf.Builder("org.eidos.kingchallenge.httpserver.handlers.HeraldHandler").build();
		LOG.debug(handler.toString() );
		 assertThat("", handler.getContext(), equalTo(context));
	}
	@Test
	public void testKindomHandler() {
		String context="/herald";
			KingdomHandlerConf handler = new KingdomHandlerConf.Builder("org.eidos.kingchallenge.httpserver.handlers.HeraldHandler")
		.contextPath(context).build();
			LOG.debug(handler.toString() );
			 assertThat("", handler.getContext(), equalTo(context));
	}
	@Test
	public void testKindomHandlerWithNullContextPath() {
		String context="/";
			KingdomHandlerConf handler = new KingdomHandlerConf.Builder("org.eidos.kingchallenge.httpserver.handlers.HeraldHandler")
		.contextPath(null).build();
			LOG.debug(handler.toString() );
			 assertThat("", handler.getContext(), equalTo(context));
	}
	@Test
	public void testInitHandler() {
		Set<KingdomHandlerConf> map = KingdomConfManager.getInstance().getHandlerConfList();
		 assertThat("", map.size(), equalTo(1));
	}


	
	
}
