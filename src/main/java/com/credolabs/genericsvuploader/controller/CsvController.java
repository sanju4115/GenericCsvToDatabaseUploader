package com.credolabs.genericsvuploader.controller;

import com.credolabs.genericsvuploader.csv.GenericCsvDataProcessor;
import com.credolabs.genericsvuploader.csv.GenericFileUploadRequest;
import com.credolabs.genericsvuploader.csv.ProcessEnum;
import com.credolabs.genericsvuploader.exception.AppCheckedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/base")
public class CsvController {

    private final GenericCsvDataProcessor csvTableUpdateDataProcessor;
    private final GenericCsvDataProcessor csvTableInsertDataProcessor;

    @Autowired
    public CsvController(
            @Qualifier("csvBaseTableUpdateDataProcessor")GenericCsvDataProcessor csvTableUpdateDataProcessor,
            @Qualifier("csvBaseTableUpdateDataProcessor")GenericCsvDataProcessor csvTableInsertDataProcessor) {
        this.csvTableUpdateDataProcessor = csvTableUpdateDataProcessor;
        this.csvTableInsertDataProcessor = csvTableInsertDataProcessor;
    }

    @RequestMapping(path = "/upload/genericUpdateOrInsert", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity genericUpdate(GenericFileUploadRequest fileUploadRequest) {
        try {
            ResponseEntity error = csvTableUpdateDataProcessor.validateFileUploadRequest(fileUploadRequest);
            if (error != null) return error;
            if (fileUploadRequest.getProcess() == ProcessEnum.INSERT)
                return csvTableInsertDataProcessor.processUploaded(fileUploadRequest);
            else if (fileUploadRequest.getProcess() == ProcessEnum.UPDATE)
                return csvTableUpdateDataProcessor.processUploaded(fileUploadRequest);
        }
        catch (AppCheckedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Some Exception occurred in our system", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }
}
