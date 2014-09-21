package org.eidos.kingchallenge.httpserver.enums;



public enum KingControllerEnum {
	LOGIN("login"),SCORE("score"),HIGHSCORELIST("highscorelist"),UNKNOWN("");
	private final String controllerContext;
	KingControllerEnum(String value) {
		this.controllerContext =  value;
	}

	public String controller() {
		return controllerContext;
	}

	/**
	 * Returns the appropriate {@code HttpServerEnum} enum
	 * 
	 * @param controllerContext   context of controller
	 * @return the appropriate HttpServerEnum
	 */
	public static KingControllerEnum byControllercontext(String context) {
		for (KingControllerEnum controller : KingControllerEnum.values()) {
			if (controller.controllerContext.equals(context) )
				return controller;
		}
		return UNKNOWN;
	}
}
