package org.eidos.kingchallenge.httpserver.utils;



/**
 * Basic enumeration with few defined EnumTypes
 * @author ernestpetit
 *
 */
public enum MediaContentTypeEnum{
	
	TEXT_PLAIN("text/plain; charset=UTF-8"), APPLICATION_JSON("application/json; charset=UTF-8"),APPLICATION_XML("application/xmlcharset=UTF-8");
	private String contentType;
	MediaContentTypeEnum(String media) {
		this.contentType = media;
	}
	public String code() {
		return contentType;
	}
	/**
	 * Returns the appropriate {@code LogicKingError} enum
	 * 
	 * @param code
	 *            status code
	 * @return the appropriate LogicKingError
	 */
	public static MediaContentTypeEnum byCode(String code) {
		for (MediaContentTypeEnum typeContent : MediaContentTypeEnum.values()) {
			if (typeContent.code() == code)
				return typeContent;
		}

		return TEXT_PLAIN;
	}
}
