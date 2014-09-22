package test.eidos.kingchanllenge.dto;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.eidos.kingchallenge.domain.dto.KingResponseDTO;
import org.eidos.kingchallenge.httpserver.utils.MediaContentTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.eidos.kingchanllenge.AbstractKingTest;



@RunWith(EasyMockRunner.class)
public class TestMeditaDTO  extends AbstractKingTest{

	
	@Test
	public void test() {
		KingResponseDTO result = new KingResponseDTO.Builder().putContentBody("scoreREsponse")
				.putContentType(MediaContentTypeEnum.TEXT_PLAIN).build();

	}

}
