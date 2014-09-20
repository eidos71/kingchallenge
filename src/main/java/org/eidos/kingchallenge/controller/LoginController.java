package org.eidos.kingchallenge.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.eidos.kingchallenge.domain.dto.KingResponseDTO;

/**
 * 
 * @author eidos71
 *
 */
public interface LoginController {
	public KingResponseDTO loginService (Long token) throws RuntimeException;
}
