package com.proj.test.salarymanagement.utils;

import com.proj.test.salarymanagement.dto.UserRecordDto;
import com.proj.test.salarymanagement.exception.UserException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CSVHelper {
    public static boolean hasCSVFormat(MultipartFile file) {
        if (!"text/csv".equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public List<UserRecordDto> csvToUsers(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             final CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withCommentMarker('#').withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            final List<CSVRecord> csvRecords = csvParser.getRecords();
            final List<UserRecordDto> recordDtoList = csvRecords.stream().map(csvRecord -> {
                UserRecordDto recordDto = new UserRecordDto(
                        csvRecord.get("id"),
                        csvRecord.get("login"),
                        csvRecord.get("name"),
                        Double.parseDouble(csvRecord.get("salary")),
                        csvRecord.get("startDate"));
                return recordDto;
            }).collect(Collectors.toList());
            return recordDtoList;
        } catch (IOException e) {
            throw new UserException("fail to parse CSV file: ");
        }
    }
}
