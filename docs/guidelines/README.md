<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Code guidelines](#code-guidelines)
  - [Architecture](#architecture)
    - [Build and run](#build-and-run)
  - [Code Quality](#code-quality)
  - [Test Strategy](#test-strategy)

# Code guidelines

## Architecture

[![](https://mermaid.ink/img/pako:eNptkMFqwzAMhl8l6LRB-wI5FDpy6aEQaE_FF81WE7PEDrJMKSHvPi2ha6DVQej__h9hawQbHUEJ1y7ebIssxbkyodDa14ftdndUu1uDE9nMXu5r9pWTD5TSwh5KjZop-SQULL148-Zx7tNirtLqVyj4jYk-HkNRxySNZj7fxVcPhQ30xD16px8b_5gBaaknA6WODvnHgAmT5jBLPN2DhVI40wY45qaF8opdUpUHh0KVx4ax_6cDhkuMT03OS-Tjcsf5nNMv9iNxrA?type=png)](https://mermaid.live/edit#pako:eNptkMFqwzAMhl8l6LRB-wI5FDpy6aEQaE_FF81WE7PEDrJMKSHvPi2ha6DVQej__h9hawQbHUEJ1y7ebIssxbkyodDa14ftdndUu1uDE9nMXu5r9pWTD5TSwh5KjZop-SQULL148-Zx7tNirtLqVyj4jYk-HkNRxySNZj7fxVcPhQ30xD16px8b_5gBaaknA6WODvnHgAmT5jBLPN2DhVI40wY45qaF8opdUpUHh0KVx4ax_6cDhkuMT03OS-Tjcsf5nNMv9iNxrA)

## Build and Run

    #/tools> docker-compose up -d 
    #/api> gradle clean build
    #/api> gradle bootRun

or startup the application with make (for any problem, check the logs in `/tools/.log`) :

    #/tools>make bootstrap        (for the first launch)
    #/tools>make ide

## Code Quality

* We must ensure that our code quality respects Quality Gate "Sonar way" :

It won't be respected if any of these conditions is true:

* Coverage is less than 80.0%
* Duplicated Lines (%)    is greater than 3.0%
* Maintainability Rating is worse than A
* Reliability Rating is worse than A
* Security Hotspots Reviewed is less than 100%
* Security Rating is worse than A

## Test Strategy

* Test Domain objects without SpringContext by simply using Mockito  
  ex:
  ```java        
   @ExtendWith(MockitoExtension.class)
  ```        

  * Test Controller using WebTestClient available with Spring @WebTestClient
    ex:
  ```java    
   @WebMvcTest(CustomerController.class)
  ```

  * Integration Test using SpringBootTest available with Spring @SpringBootTest
    ex:
  ```java    
    @WebMvcTest(CustomerController.class)
  ```

  * Integration Test with database using @Testcontainers available with @Testcontainers and
    ex:
  ```java   
    @SpringBootTest 
    @Testcontainers
    class YourTestClass {
      @Container 
      static PostgresSQLContainer postgresSQLContainer = new PostgresSQLContainer("postgres:17.2-alpine")
        .withDatabaseName("test")
        .withUsername("admin")
        .withPassword("admin");
  
      @DynamicPropertySource
      static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresSQLContainer.getJdbcUrl);
        registry.add("spring.datasource.username", postgresSQLContainer.getUsername);
        registry.add("spring.datasource.password", postgresSQLContainer.getPassword);
      } 
  
      @Autowired
      private Repository repository;
  
      @BeforeAll
      static void beforeAll() {
        postgresSQLContainer.start();
      } 
  
      @AfterAll
      static void afterAll() {
        postgresSQLContainer.stop();
      } 
    }
  ```
