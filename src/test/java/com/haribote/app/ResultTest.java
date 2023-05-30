package com.haribote.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintWriter;

import org.junit.jupiter.api.Test;

class ResultTest {
	@Test
	void testMakeOk() {
		@SuppressWarnings(value = { "unused" })
		Result<String, String> ok = new Ok<String, String>("ok");
	}

	@Test
	void testMakeErr() {
		@SuppressWarnings(value = { "unused" })
		Result<String, String> err = new Err<String, String>("err");
	}

	@Test
	void testOkIsOk() {
		Result<String, String> ok = new Ok<String, String>("ok");
		assertTrue(ok.isOk());
	}

	@Test
	void testErrIsNotOk() {
		Result<String, String> err = new Err<String, String>("err");
		assertFalse(err.isOk());
	}

	@Test
	void testOkIsNotErr() {
		Result<String, String> ok = new Ok<String, String>("ok");
		assertFalse(ok.isErr());
	}

	@Test
	void testErrIsErr() {
		Result<String, String> err = new Err<String, String>("err");
		assertTrue(err.isErr());
	}

	@Test
	void testExpectOkReturnsValue() {
		var value = "ok";
		Result<String, String> ok = new Ok<String, String>(value);
		String result = ok.expect("error message");
		assertEquals(value, result);
	}

	@Test
	void testExpectErrThrowsException() {
		Result<String, String> err = new Err<String, String>("err");
		RuntimeException e = assertThrows(UnsafeUnwrapException.class, () -> err.expect("error message"));
		assertEquals("error message: err", e.getMessage());
		assertEquals("com.haribote.app.UnsafeUnwrapException: error message: err", e.toString());
	}

	@Test
	void testLearnRTEGetMessage() {
		var e = new RuntimeException("message");
		assertEquals("message", e.getMessage());
		assertEquals("java.lang.RuntimeException: message", e.toString());
	}

	@Test
	void testUnwrapOk() {
		var value = "ok";
		Result<String, String> ok = new Ok<String, String>(value);

		String result = ok.unwrap();

		assertEquals(value, result);
	}

	@Test
	void testUnwrapStringErr() {
		var value = "err";
		Result<String, String> err = new Err<String, String>(value);

		RuntimeException e = assertThrows(UnsafeUnwrapException.class, () -> err.unwrap());
		assertEquals("err", e.getMessage());

	}

	@Test
	void testUnwrapErr() {
		var value = new Exception("err");
		Result<String, Exception> err = new Err<String, Exception>(value);

		RuntimeException e = assertThrows(UnsafeUnwrapException.class, () -> err.unwrap());
		RuntimeException expect = new RuntimeException(value);
		assertEquals(e.getMessage(), expect.getMessage());

	}
}
