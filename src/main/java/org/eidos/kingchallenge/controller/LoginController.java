package org.eidos.kingchallenge.controller;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author eidos71
 *
 */
public interface LoginController {
	public String loginService (AtomicLong token) throws RuntimeException;
}
