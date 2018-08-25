package com.credolabs.genericsvuploader.csv;

import com.credolabs.genericsvuploader.exception.AppCheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.credolabs.genericsvuploader.utils.FileUtil.multipartToFile;

public interface GenericCsvDataProcessor {

    String UPDATE_QUERY = "update %s set %s where id = ?";
    String INSERT_QUERY = "INSERT INTO %s (%s) VALUES (%s)";
    String COLUMN_NAME_CANNOT_BE_NULL = "Column name cannot be null.";
    String COLUMN_NAME_CANNOT_BE_EMPTY = "Column name cannot be empty.";

    Logger LOG = LoggerFactory.getLogger(GenericCsvDataProcessor.class);

    default ResponseEntity<?> processUploaded(GenericFileUploadRequest genericFileUploadRequest) throws AppCheckedException {
        try {
            MultipartFile multipartFile = genericFileUploadRequest.getFile();
            if(multipartFile == null){
                return new ResponseEntity<>("No file provided", HttpStatus.BAD_REQUEST);
            }
            File file = multipartToFile(multipartFile);

            return genericUpload(
                    file,
                    genericFileUploadRequest
            );

        }
        catch (IOException e) {
            LOG.info("Exception while reading file ",e);
            return new ResponseEntity<>("Unable to read file", HttpStatus.BAD_REQUEST);
        }
    }

    default ResponseEntity<?> validateCell(String s) {
        if (s == null){
            return new ResponseEntity<Object>(
                    GenericCsvDataProcessor.COLUMN_NAME_CANNOT_BE_NULL,
                    HttpStatus.BAD_REQUEST
            );
        }
        else if (s.trim().isEmpty()){
            return new ResponseEntity<Object>(
                    GenericCsvDataProcessor.COLUMN_NAME_CANNOT_BE_EMPTY,
                    HttpStatus.BAD_REQUEST
            );
        }
        return null;
    }

    default ResponseEntity validateFileUploadRequest(GenericFileUploadRequest fileUploadRequest) {
        if (fileUploadRequest.getFile() == null){
            return new ResponseEntity<>("No file provided.", HttpStatus.INTERNAL_SERVER_ERROR);
        }else if (fileUploadRequest.getDriver() == null){
            return new ResponseEntity<>("Database driver cannot be null.", HttpStatus.INTERNAL_SERVER_ERROR);
        }else if (fileUploadRequest.getUrl() == null){
            return new ResponseEntity<>("Database URL cannot be null.", HttpStatus.INTERNAL_SERVER_ERROR);
        }else if (fileUploadRequest.getUser() == null){
            return new ResponseEntity<>("Database user cannot be null.", HttpStatus.INTERNAL_SERVER_ERROR);
        }else if (fileUploadRequest.getPassword() == null){
            return new ResponseEntity<>("Database password cannot be null.", HttpStatus.INTERNAL_SERVER_ERROR);
        }else if (fileUploadRequest.getProcess() == null){
            return new ResponseEntity<>("Process can either be UPDATE/INSERT.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    ResponseEntity<?> genericUpload(File file, GenericFileUploadRequest genericFileUploadRequest) throws AppCheckedException;
}
