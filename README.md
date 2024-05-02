# Hibernate Connection Library with GUI Generation

This library streamlines Java application development by effortlessly generating graphical interfaces from defined entity classes. It seamlessly integrates with the Hibernate framework to provide database connectivity, with a primary focus on creating intuitive interfaces for managing database entities.

## 1. Definition of Entity Classes

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

    public Customer() {
        // Set default values for fields
        registrationDate = new Date();
        active = true;
    }

    // Getters and setters omitted for brevity
}
```
### 2. Explanation of Annotations

#### `@EntityCRUD`
- `title`: Specifies the title of the CRUD (Create, Read, Update, Delete) interface generated for the entity. It typically appears at the top of the interface, providing a clear indication of what kind of records the interface deals with.
- `formTitle`: Sets the title of the form or dialog used for adding or editing records. It appears as the title of the window or section where users input or modify data.
- `columnCount`: Determines the number of columns used for displaying records in the table view of the CRUD interface. It helps in organizing and presenting data more efficiently, especially when dealing with a large number of fields.
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
### 3. Usage of GUI Generation

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
