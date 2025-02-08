package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FindNLargestTest {

	private static final String PATH_TO_TEST_FILE = "src/test/resources/test.xlsx";

	private final ApiController apiController = new ApiController();

	@Test
	void fileNotFoundTest() {
		RequestDto request = new RequestDto("unknown_path", -1);
		ResponseEntity<String> response = apiController.api(request);
		assertEquals(String.format(ApiController.FILE_NOT_FOUND_MESSAGE_TEMPLATE, "unknown_path"), response.getBody());
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
	}

	@Test
	void nSmallThanZero() {
		RequestDto request = new RequestDto(PATH_TO_TEST_FILE, -1);
		ResponseEntity<String> response = apiController.api(request);
		assertTrue(Objects.requireNonNull(response.getBody()).contains("Incorrect value"));
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
	}

	@Test
	void nBiggerThanSize() {
		RequestDto request = new RequestDto(PATH_TO_TEST_FILE, 100500);
		ResponseEntity<String> response = apiController.api(request);
		assertTrue(Objects.requireNonNull(response.getBody()).contains("Incorrect value"));
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
	}

	@Test
	void successResponseTest() {
		RequestDto request = new RequestDto(PATH_TO_TEST_FILE, 3);
		ResponseEntity<String> response = apiController.api(request);
		assertTrue(Objects.requireNonNull(response.getBody()).contains("88"));
		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
	}
}
