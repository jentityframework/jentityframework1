# JEntity Framework
With JEntity Framework, you can use Java 8 Lambda Expressions fetch the data from the underlying database. JEntity Framework supports different types of queries which in turn convert into SQL queries for the underlying database.


# System Requirements
- Java 8 or Above

# Required Libraries
- asm-all-5.2
- gson-2.8.5
- jaque-2.1.0
- mysql-connector-java-5.1.36
- persistence-api-1.0.2

# JPA Annotations
Current version only works with JPA Annotations as below:
- @Table
- @Id
- @GeneratedValue

# Examples 
### Create Database
Create a database named mydb. This database have Product table as below:

#### Structure of Product Table

![Data of Product](http://learningprogramming.net/wp-content/uploads/java/jentityframework/database-structure.png)

#### Data of Product Table

![Data of Product](http://learningprogramming.net/wp-content/uploads/java/jentityframework/database-data.png)

### Database Configuration
Create new json file named appsettings.json in the classpath of your project
```
{
  "connectionString": {
    "url": "jdbc:mysql://localhost:3306/mydb",
    "username": "root",
    "password": "123456"
  }
}
```

### Create Entity
Create new package named entities. In this package, create new java class named Product.java as below:
```
package entitites;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "product")
public class Product implements Serializable {
	
	@Id
	@GeneratedValue
	private int id;

	private String description;

	private boolean featured;

	private String name;

	private String photo;

	private BigDecimal price;

	private int quantity;

	private int categoryid;

	private LocalDate dateCreated;

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isFeatured() {
		return featured;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}

}
```

### Create Database Context
Create new package named models. In this package, create new java class named MyDemoContext.java as below:
```
package models;

import entitites.Product;
import jentityframework.DbSet;

public class MyDemoContext  {

	public DbSet<Product> Products;
	
	public MyDemoContext() {
		Products = new DbSet<Product>(Product.class);
	}
	
}
```

### Fetch Data From Database
- Get All Rows

- Get Rows with Multiple Conditions

- Get A Row

- Sum

- Count

- Min

- Max

- Avg

# Documents 

# License

[© LearningProgramming.NET](http://learningprogramming.net). All rights reserved.
