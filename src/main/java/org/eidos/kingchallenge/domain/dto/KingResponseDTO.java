package org.eidos.kingchallenge.domain.dto;

import java.io.Serializable;

import org.eidos.kingchallenge.httpserver.utils.MediaContentTypeEnum;

public class KingResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5261474761826994048L;
	
	private final MediaContentTypeEnum contentType;

	private final String contentBody;
	
	private KingResponseDTO() {
		throw new UnsupportedOperationException();
	}
	private KingResponseDTO(Builder builder) {
		this.contentBody=builder.contentBody;
		this.contentType=builder.contentType;
	}
	
	
	
	public MediaContentTypeEnum getContentType() {
		return contentType;
	}
	public String getContentBody() {
		return contentBody;
	}



	@Override
	public String toString() {
		return "KingResponseDTO [contentType=" + contentType + ", contentBody="
				+ contentBody + "]";
	}



	public static class Builder{
		private MediaContentTypeEnum contentType;
		private String contentBody;
		public Builder() {
			
		}
		public Builder putContentType (MediaContentTypeEnum pContentType){
			if (pContentType==null) this.contentType=MediaContentTypeEnum.TEXT_PLAIN;
			this.contentType=pContentType;
			return this;
			
		}
		public Builder putContentBody(String pContentBody){
			if (pContentBody==null) this.contentBody="";
			this.contentBody=pContentBody;
			return this;
			
		}
		public KingResponseDTO build(){
			if (this.contentType==null) this.contentType=MediaContentTypeEnum.TEXT_PLAIN;
			if (this.contentBody==null) this.contentBody="";
			return new KingResponseDTO(this);
		}
	}
}
