# page-component
### Overview
In today’s web application technologies, webpages are composed from the JavaScript UI component libraries like AngularJS, react, jQuery UI, Ext JS, JSF PrimeFaces and others. All these libraries uses special templates composed by collection of primitive HTML elements that acts as a single UI component.

This project extends Selenium PageObject design pattern to support composite components. It also helps to create data-driven Web UI Automation tests more easily by generating XML data-sets from the PageObjects.

### Here Are the key features of this project:

* #### Support for page-components in PageObjcts
Currently Selenium PageObject design pattern supports the WebElement interface for fields of PageObject classes. This project allows to declare custom page-components as fields of the PageObject classes and use current Selenium @FindBy annotation to specify locator for those page-components. Page-components are initialized lazily using proxies.

* #### Support for data-driven testing
To support data-driven testing, "page-component" incorporates XStream open source project, which adds the option to serialize any PageObject to and from XML files. To support this feature, PageObject should be annotated with XStream annotations.

* #### Data Generation
"page-component" project supporting offline and on-the-fly data generation. This is a very exciting and time-saving feature which allows specific or random data generation during the test execution. Automation developers can specify data types for the specific fields of PageObjects by using @Data annotation. Currently UITAF supports 8 data generators: fixed value, custom list, date, decimal number, alphanumeric, word, human names and addresses. The generated data is saved as XML file with each test execution, and can be used for regression testing.

* #### PageObject Auto-Fill
This feature can automatically populate web page with supplied or generated data, this is done by automatic iteration through all your PageObject fields, and population with specific data using specific web element behaviour, so for text web element sendkeys method will be used and for check boxes click method will be used. Automation developer can exclude any field from automatic population by providing @SkipAutoFill annotation.

* #### PageObject Auto-Validation
This feature is similar to Auto-Fill feature but instead of population, assertion mechanism is used to validate all the PageObjct fields against predefined data. Automation developer can exclude any field from automatic validation by providing @SkipAutoValidate annotation.

* #### Data-sets aggregation
This feature allows to consolidate several PageObjects in one class or to create nested PageObject classes and automatically to generate data-set XML files with any data complexity.

* #### PageObject validation before and after data population
Each data-component field can be validated with different data values: initial data is validated when web page is just loaded and expected data is validated after field is populated.

* #### Validation of fields with mutated value
In some cases the entered field value is changing (mutating) a good example is Date Picker control where user is entering 031214 and final value is changing to 03-12-2014. Page-component project can validate this types of scenarios with correct data.

* #### Ajax handling capabilities
Page-component framework automatically handles Ajax affected page-components even during automatic field population.

* #### Maven Dependency
This open source project is distributed through maven central repository. Here is a Maven dependency for page-component project.
```
<dependency>
    <groupId>com.googlecode.page-component</groupId>
    <artifactId>page-component</artifactId>
    <version>1.3.7</version>
</dependency>
```
