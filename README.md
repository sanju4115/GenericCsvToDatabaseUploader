# Generic CSV To Database Uploader 

![](https://firebasestorage.googleapis.com/v0/b/credo-f7d83.appspot.com/o/app%2Fic_launcher_rect.png?alt=media&token=1becc7dc-5682-4428-af30-21a5e3c60a38)

![](https://img.shields.io/github/release/qubyte/rubidium.svg)

### Features

- Exposes an api through which csv can be uploaded with ease to database
- Database update and insert both feature available

Details:

    Current project support for mysql database
	It can adjusted to different database just by adding database connector dependencies.
	For insert operation pass 'process' parameter value as 'INSERT' and 'UPDATE' for table rows update.
    


#### Endpoint Exposed

- Endpoint : /base/upload/genericUpdateOrInsert
- Method: POST
- With Body as form data with these parameters:

|   Param Name             |Type                          |Sample Data                        |
|----------------|-------------------------------|-----------------------------|
|file |`File`            |employee.csv            |
|driver           |`Text`            |com.mysql.jdbc.Driver            |
|url           |`Text`|jdbc:mysql://localhost:8080/db_name|
|user            |`Text`| pass
|password            |`Text`|pass
|process            |`Text`|INSERT/UPDATE

