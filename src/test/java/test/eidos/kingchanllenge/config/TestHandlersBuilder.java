package test.eidos.kingchanllenge.config;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.eidos.kingchallenge.KingdomConfManager;
import org.eidos.kingchallenge.KingdomHandlerConf;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.eidos.kingchanllenge.AbstractKingTest;

@RunWith(EasyMockRunner.class)
public class TestHandlersBuilder extends AbstractKingTest  {
	static final Logger LOG = Logger.getLogger(TestHandlersBuilder.class.getName());
	Map<AtomicInteger,Date> lastLogin=Collections.synchronizedMap(new HashMap<AtomicInteger,Date>() );



	@Test(expected =LogicKingChallengeException.class)
	public void testInvalidClass() {
		KingdomHandlerConf handler = new KingdomHandlerConf.Builder("wrong.eidos.kingchallenge.httpserver.handlers.HeraldHandler")
	.contextPath("/herald").build();
	}
	@Test
	public void testKingomDefaultConstructor() {
		String context="/";
		KingdomHandlerConf handler = new org.eidos.kingchallenge.KingdomHandlerConf.Builder("org.eidos.kingchallenge.httpserver.handlers.HeraldHandler").build();
		LOG.info(handler.toString() );
		 assertThat("", handler.getContext(), equalTo(context));
	}
	@Test
	public void testKindomHandler() {
		String context="/herald";
			KingdomHandlerConf handler = new KingdomHandlerConf.Builder("org.eidos.kingchallenge.httpserver.handlers.HeraldHandler")
		.contextPath(context).build();
			LOG.info(handler.toString() );
			 assertThat("", handler.getContext(), equalTo(context));
	}
	@Test
	public void testKindomHandlerWithNullContextPath() {
		String context="/";
			KingdomHandlerConf handler = new KingdomHandlerConf.Builder("org.eidos.kingchallenge.httpserver.handlers.HeraldHandler")
		.contextPath(null).build();
			LOG.info((handler.toString() ) );
			 assertThat("", handler.getContext(), equalTo(context));
	}
	@Test
	public void testInitHandler() {
		Set<KingdomHandlerConf> map = KingdomConfManager.getInstance().getHandlerConfList();
		 assertThat("", map.size(), equalTo(1));
	}


	
	
}
