# Hibernate Connection Library with GUI Generation

This library streamlines Java application development by effortlessly generating graphical interfaces from defined entity classes. It seamlessly integrates with the Hibernate framework to provide database connectivity, with a primary focus on creating intuitive interfaces for managing database entities.
## 1. Adding the Library with JitPack

To add the AutoCRUD library to your project using JitPack, follow these steps:

1. Add the JitPack repository to your `pom.xml`:

    ```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    ```

2. Add the AutoCRUD dependency to your `pom.xml`:

    ```xml
    <properties>
        <AutoCRUD-version>LATEST</AutoCRUD-version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>com.github.NazarioLuis</groupId>
            <artifactId>AutoCRUD</artifactId>
            <version>${AutoCRUD-version}</version>
        </dependency>
    </dependencies>
    ```

Replace `LATEST` with the latest version available if you want to specify a version, for example `v1.0.0`.


## 2. Hibernate configuration
To configure Hibernate for your project, create a `hibernate.properties` file and add the following settings:
```properties
hibernate.connection.url=jdbc:postgresql://localhost:5432/database
hibernate.connection.driver_class=org.postgresql.Driver
hibernate.connection.username=postgres
hibernate.connection.password=123
hibernate.current_session_context_class=thread
hibernate.show_sql=true
hibernate.hbm2ddl.auto=update
mapping_packages=package_name.entities
```
## 3. Definition of Entity Classes

To utilize the graphical interface generation functionality, define your entity classes representing tables in your database. Annotations can be applied to these classes and their fields to customize the behavior and appearance of the generated interfaces.

Here's an example using the "Customer" entity:

```java
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import py.nl.AutoCrud.annotations.HiddenInput;
import py.nl.AutoCrud.annotations.Input;
import py.nl.AutoCrud.annotations.RequiredInput;
import py.nl.AutoCrud.annotations.EntityCRUD;
import py.nl.AutoCrud.annotations.Relationship;

@Entity
@EntityCRUD(
    title = "Customer CRUD",
    formTitle = "Customer personal data",
    columnCount = 2,
    width = 80,
    height = 80
)
public class Customer {
    @Id
    private int id;
    
    @RequiredInput
    @Input(tableColumn = true)
    private String name;
    
    @RequiredInput
    @Input(tableColumn = true)
    private String lastName;
    
    @RequiredInput
    @Input(tableColumn = true)
    private String document;
    
    private String phone;
    
    private String address;
    
    @HiddenInput
    @Input(tableColumn = true)
    private Date registrationDate;
    
    @Input(tableColumn = true)
    private boolean active;

    @RequiredInput
    @ManyToOne
    @Relationship(displayInForm = ":name") // Add the relationship with City
    private City city; // Define the relationship with City

    public Customer() {
        // Set default values for fields
        registrationDate = new Date();
        active = true;
    }

    // Getters and setters omitted for brevity
}
```
Here we've added the `@ManyToOne` relationship with `City` in the `Customer` class, along with the `@Relationship` annotation to customize how this relationship is displayed in the generated form.

#### Definition of the `City` Entity:

```java
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import py.nl.AutoCrud.annotations.Input;
import py.nl.AutoCrud.annotations.RequiredInput;
import py.nl.AutoCrud.annotations.EntityCRUD;

@Entity
@EntityCRUD(
    title = "City CRUD",
    formTitle = "City information",
    width = 60,
    height = 60
)
public class City {
    
    @Id
    @GeneratedValue
    private int id;

    @RequiredInput
    @Input(tableColumn = true)
    private String name;

    public City() {
    }

    // Getters and setters omitted for brevity
}
```
### 4. Explanation of Annotations

#### `@EntityCRUD`
- `title`: Specifies the title of the CRUD (Create, Read, Update, Delete) interface generated for the entity. It typically appears at the top of the interface, providing a clear indication of what kind of records the interface deals with.
- `formTitle`: Sets the title of the form or dialog used for adding or editing records. It appears as the title of the window or section where users input or modify data.
- `columnCount`: Determines the number of columns used for displaying the form in the CRUD interface. It helps in organizing and presenting form fields more efficiently, especially when dealing with forms with many fields.
- `width`: Specifies the width of the CRUD interface window as a percentage of the screen width. It allows customization of the interface's size to fit different screen resolutions and user preferences.
- `height`: Defines the height of the CRUD interface window as a percentage of the screen height. It enables adjusting the interface's vertical size according to the content and usability requirements.

#### `@HiddenInput`
This annotation indicates that the annotated field should be hidden in the graphical interface. It's useful for fields that are not meant to be directly visible or editable by users, such as internal identifiers or sensitive information.

#### `@Input`
- `label`: Specifies the label or prompt displayed alongside the input field in the graphical interface. It provides users with context and guidance on what type of data to input.
- `data`: Used for specifying predefined options for the input field, typically for dropdown lists or combo boxes. It allows users to select from a predefined set of values rather than entering free-form text.
- `longText`: Indicates whether the input field should be displayed as a text area for entering longer text. It's useful for fields that may contain paragraphs or extended descriptions.
- `tableColumn`: Determines whether the annotated field should be displayed as a column in the table view of the graphical interface. It allows customization of which fields are visible in the table, optimizing screen space and focusing on relevant information.

#### `@RequiredInput`
This annotation marks the annotated field as required in the graphical interface. It ensures that users must provide a value for the field when interacting with the interface, helping to maintain data integrity and completeness.
#### `@Relationship`
This annotation is used to define relationships between entities in the graphical interface.

- `displayInForm`: Specifies how the relationship is displayed in the form. For example, `:lastname, :name` can be used to display the lastname and name attributes of the related entity as a string.

### 5. Usage of GUI Generation

Once you've defined your entity classes and annotated them appropriately, you can use the provided functionality to generate graphical interfaces for CRUD operations. Here's an example of how to create a view for the "Customer" entity:

```java
import py.nl.AutoCrud.AutoCRUD;

public class Main {
    public static void main(String[] args) {
        // Create an instance of AutoCRUD for the Customer class
        AutoCRUD<Customer> crud = new AutoCRUD<>(Customer.class);
        
        // Show the graphical interface
        crud.setVisible(true);
    }
}
```
Replace "Customer" with your entity class name, and adjust the package and import statements accordingly. This code snippet creates a GUI interface for performing CRUD operations on the specified entity class.
