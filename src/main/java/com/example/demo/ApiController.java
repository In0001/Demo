package com.example.demo;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@RestController
public class ApiController {

    public final static String FILE_NOT_FOUND_MESSAGE_TEMPLATE = "File %s is not found";

    private final Random random = new SecureRandom();

    @PostMapping("/api")
    public ResponseEntity<String> api(@RequestBody RequestDto requestDto) {
        String pathToFile = requestDto.getPath();
        int n = requestDto.getN();
        List<Integer> numbers = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(pathToFile)) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.rowIterator();
            while (iterator.hasNext()) {
                numbers.add((int) iterator.next().getCell(0).getNumericCellValue());
            }

            if (n <= 0 || n > numbers.size()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect value of n, min value is 1, max value is " + numbers.size());
            }
            return ResponseEntity.ok().body(String.valueOf(quickSelect(numbers.toArray(new Integer[0]), n)));
        } catch (FileNotFoundException fileNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(FILE_NOT_FOUND_MESSAGE_TEMPLATE, pathToFile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(String.format("Error: %s", e.getMessage()));
        }
    }

    public int quickSelect(Integer[] numbers, int n) {
        return quickSelect(numbers, 0, numbers.length - 1, n - 1);
    }

    private int quickSelect(Integer[] numbers, int first, int last, int n) {
        if (first <= last) {
            int pivot = partition(numbers, first, last);
            if (pivot == n) {
                return numbers[n];
            }
            if (pivot > n) {
                return quickSelect(numbers, first, pivot - 1, n);
            }
            return quickSelect(numbers, pivot + 1, last, n);
        }
        return Integer.MIN_VALUE;
    }

    private int partition(Integer[] numbers, int first, int last) {
        int pivot = first + random.nextInt(last - first + 1);
        swap(numbers, last, pivot);
        for (int i = first; i < last; i++) {
            if (numbers[i] > numbers[last]) {
                swap(numbers, i, first);
                first++;
            }
        }
        swap(numbers, first, last);
        return first;
    }

    private void swap(Integer[] numbers, int x, int y) {
        int tmp = numbers[x];
        numbers[x] = numbers[y];
        numbers[y] = tmp;
    }

}
