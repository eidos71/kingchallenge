package test.eidos.kingchanllenge.dto;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.eidos.kingchallenge.domain.dto.KingResponseDTO;
import org.eidos.kingchallenge.httpserver.utils.MediaContentTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RunWith(EasyMockRunner.class)
public class TestMeditaDTO  extends EasyMock{

	private static final Logger LOG = LoggerFactory
			.getLogger(TestMeditaDTO.class);
	
	@Test
	public void test() {
		KingResponseDTO result = new KingResponseDTO.Builder().putContentBody("scoreREsponse")
				.putContentType(MediaContentTypeEnum.TEXT_PLAIN).build();

	}

}
