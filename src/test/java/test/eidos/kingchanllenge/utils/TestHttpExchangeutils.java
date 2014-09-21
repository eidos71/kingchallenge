package test.eidos.kingchanllenge.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.eidos.kingchallenge.httpserver.handlers.HttpKingExchangeHelper;
import org.eidos.kingchallenge.utils.CollectionsChallengeUtils;
import org.eidos.kingchallenge.utils.HttpExchangeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(EasyMockRunner.class)
public class TestHttpExchangeutils   extends EasyMock{
	private static final Logger LOG = LoggerFactory
			.getLogger(TestHttpExchangeutils.class);
	@Test
	public void testTestHttpExchangeutils() throws UnsupportedEncodingException {
		Map<String,Object> map= new HashMap<String,Object>();
		String expectedparam="BabWcCKxRmnzPqyvZVKMfxWPuICmnBzEXIx";
		HttpExchangeUtils.parseQuery("score?sessionkey=BabWcCKxRmnzPqyvZVKMfxWPuICmnBzEXIx", map);
		for( Entry<String, Object> elem :map.entrySet()) {
			assertThat("",  (String)elem.getValue() , equalTo(expectedparam));
		}
	}
	@Test
	public void testHttpKingExchangeHelper() {
		Map<String,Object> requestParamMap= new HashMap<String,Object>();
		requestParamMap.put("3","3");
		requestParamMap.put("sessionkey", "BabWcCKxRmnzPqyvZVKMfxWPuICmnBzEXIx");		
		assertThat("", HttpKingExchangeHelper.getKey(requestParamMap, "sessionkey"),  equalTo("BabWcCKxRmnzPqyvZVKMfxWPuICmnBzEXIx"));
		assertThat("", HttpKingExchangeHelper.getLongValue(requestParamMap, "3"),  equalTo(3L));
	}
}
