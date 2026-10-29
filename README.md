# SIM Activation Portal (Spring Boot + JPA + MySQL)

Run: start MySQL, adjust credentials in `src/main/resources/application.properties`, then
`mvn spring-boot:run` (or import as Maven project in STS -> Run As -> Spring Boot App). Needs Java 17.
Schema `SimPortal`, tables and sample data are created automatically (ddl-auto + data.sql).

## Endpoints (all JSON, test in SoapUI)
1. POST /api/sims/validate                    {"simNumber":"1234567891235","serviceNumber":"1234567892"}
2. POST /api/customers/basic-validation       {"email":"bob@abc.com","dob":"1998-12-12"}
3. POST /api/customers/personal-validation    {"firstName":"Bob","lastName":"Sam","confirmEmail":"bob@abc.com"}
4. PUT  /api/customers/address                {"addressId":2,"uniqueIdNumber":"1234567891234568","address":"Vijaynagar","city":"Mysore","pincode":"570017","state":"Karnataka"}
5. POST /api/customers/id-validation          {"uniqueIdNumber":"1234567891234568","firstName":"Bob","lastName":"Sam","dateOfBirth":"1998-12-12"}
   -> SimDetails.simStatus for simId 2 changes inactive -> active
Errors: {"timestamp":..., "status":400, "error":"Bad Request", "message":"<custom message>"}
