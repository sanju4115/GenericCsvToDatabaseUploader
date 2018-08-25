package com.credolabs.genericsvuploader.csv;

import com.credolabs.genericsvuploader.exception.AppCheckedException;
import com.opencsv.CSVReader;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service("csvBaseTableUpdateDataProcessor")
public class CSVTableUpdateDataProcessor implements GenericCsvDataProcessor {

    @Override
    public ResponseEntity<?> genericUpload(File file, GenericFileUploadRequest genericFileUploadRequest) throws AppCheckedException {
        Connection connection = null;
            try (
                    Reader reader = Files.newBufferedReader(Paths.get(file.getPath()));
                    CSVReader csvReader = new CSVReader(reader);
            ) {
                String tableName = FilenameUtils.removeExtension(file.getName());
                String[] nextRecord;
                ArrayList<String> columnNameAndValue = new ArrayList<>();
                try {
                    connection = DriverManager.getConnection(
                            genericFileUploadRequest.getUrl(),
                            genericFileUploadRequest.getUser(),
                            genericFileUploadRequest.getPassword()
                    );
                    Class.forName(genericFileUploadRequest.getDriver());
                    connection.setAutoCommit(false);
                    String[] strings = csvReader.readNext();
                    List<String> memberFieldsToBindTo = new ArrayList<>();
                    for (String column : strings){
                        ResponseEntity<?> error = validateCell(column);
                        if (error != null) return error;
                        memberFieldsToBindTo.add(column.trim());
                    }
                    memberFieldsToBindTo.remove("id");
                    for (String aMemberFieldsToBindTo : memberFieldsToBindTo) {
                        columnNameAndValue.add(aMemberFieldsToBindTo + " = ? ");
                    }
                    String finalQuery = String.format(
                            GenericCsvDataProcessor.UPDATE_QUERY,
                            tableName,
                            String.join(",", columnNameAndValue)
                    );
                    while ((nextRecord = csvReader.readNext()) != null) {
                        PreparedStatement preparedStmt = connection.prepareStatement(finalQuery);
                        for (int i = 0; i < memberFieldsToBindTo.size(); i++) {
                            preparedStmt.setObject(i+1, nextRecord[i + 1].trim());
                        }
                        preparedStmt.setLong(memberFieldsToBindTo.size()+1, Long.parseLong(nextRecord[0]));
                        preparedStmt.executeUpdate();
                    }
                    connection.commit();
                }catch (Exception e) {
                    try {
                        if (connection != null) connection.rollback();
                    } catch (SQLException e1) {
                        throw new AppCheckedException(e1.getMessage());
                    }
                    throw new AppCheckedException(e.getMessage());
                }finally {
                    try {
                        if (connection != null)
                            connection.close();
                    } catch (SQLException e) {
                        throw new AppCheckedException(e.getMessage());
                    }
                }
            }catch (IOException e) {
                throw new AppCheckedException(e.getMessage());
            }
            return new ResponseEntity<Object>("Updated Successfully.", HttpStatus.OK);
    }
}