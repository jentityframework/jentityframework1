# JEntity Framework
**JEntity Framework** stands for **Java Entity Framework**. With **JEntity Framework**, you can use **Java 8 Lambda Expressions** fetch the data from the underlying database. **JEntity Framework** supports different types of queries which in turn convert into SQL queries for the underlying database.

# Documents and Examples
Download jars file of JEntity Framework at [http://learningprogramming.net/java/jentity-framework/getting-started-with-jentity-framework](http://learningprogramming.net/java/jentity-framework/getting-started-with-jentity-framework)

# Documents and Examples
See more examples with JEntity Framework at [http://learningprogramming.net/java/jentity-framework](http://learningprogramming.net/java/jentity-framework)

# System Requirements
- Java 8 or Above

# Required Libraries
- asm-all-5.2
- gson-2.8.5
- jaque-2.1.0
- mysql-connector-java-5.1.36
- persistence-api-1.0.2

# JPA Annotations
Current version only works with **JPA Annotations** as below:
- @Table
- @Id
- @GeneratedValue

# Examples 
### Create Database
Create a database named **mydb**. This database have **Product** table as below:

#### Structure of Product Table

![Data of Product](http://learningprogramming.net/wp-content/uploads/java/jentityframework/database-structure.png)

#### Data of Product Table

![Data of Product](http://learningprogramming.net/wp-content/uploads/java/jentityframework/database-data.png)

### Database Configuration
Create new json file named **appsettings.json** in the classpath of your project
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
Create new package named **entities**. In this package, create new java class named **Product.java** as below:
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

	private String name;

	private BigDecimal price;

	private int quantity;

	private String description;

	private boolean status;

	private LocalDate dateCreated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

}
```

### Create Database Context
Create new package named **models**. In this package, create new java class named **MyDemoContext.java** as below:
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
- **Get All Rows**:
```
package demo;

import java.util.List;
import entitites.Product;
import models.MyDemoContext;

public class Demo {

	public static void main(String[] args) {
		
		MyDemoContext db = new MyDemoContext();
		List<Product> products = db.Products.toList();
		System.out.println("Products: " + products.size());
		for(Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("quantity: " + product.getQuantity());
			System.out.println("====================");
		}

	}

}
```

- **Get Rows with Multiple Conditions**:
```
package demo;

import java.util.List;
import entitites.Product;
import models.MyDemoContext;

public class Demo {

	public static void main(String[] args) {
		
		MyDemoContext db = new MyDemoContext();
		List<Product> products = db.Products.Where(p -> p.getQuantity() >= 5 && p.getQuantity() <= 20 && p.isStatus()).toList();
		System.out.println("Products: " + products.size());
		for(Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("quantity: " + product.getQuantity());
			System.out.println("====================");
		}

	}

}
```

- **Like**:
```
package demo;

import java.util.List;
import entities.Product;
import models.MyDBContext;

public class Demo {

	public static void main(String[] args) {
		
		MyDBContext db = new MyDBContext();
		List<Product> products = db.Products.Where(p -> p.getName().endsWith("1")).toList();
		System.out.println("Products: " + products.size());
		for(Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("status: " + product.isStatus());
			System.out.println("date: " + product.getDateCreated());
			System.out.println("====================");
		}
		
		products = db.Products.Where(p -> p.getName().startsWith("lap")).toList();
		System.out.println("Products: " + products.size());
		for(Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("status: " + product.isStatus());
			System.out.println("date: " + product.getDateCreated());
			System.out.println("====================");
		}
		
		products = db.Products.Where(p -> p.getName().contains("lap")).toList();
		System.out.println("Products: " + products.size());
		for(Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("status: " + product.isStatus());
			System.out.println("date: " + product.getDateCreated());
			System.out.println("====================");
		}

	}

}
```

- **Order By**:
```
package demo;

import java.util.List;
import entitites.Product;
import jentityframework.OrderBy;
import models.MyDemoContext;

public class Demo {

	public static void main(String[] args) {
		
		MyDemoContext db = new MyDemoContext();
		List<Product> products = db.Products.orderBy("price", OrderBy.DESC).toList();
		System.out.println("Products: " + products.size());
		for(Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("quantity: " + product.getQuantity());
			System.out.println("====================");
		}
		
		products = db.Products.Where(p -> p.isStatus()).orderBy("price", OrderBy.DESC).toList();
		System.out.println("Products: " + products.size());
		for(Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("quantity: " + product.getQuantity());
			System.out.println("====================");
		}
		
	}

}
```

- **Limit**:
```
package demo;

import java.util.List;
import entitites.Product;
import jentityframework.OrderBy;
import models.MyDemoContext;

public class Demo {

	public static void main(String[] args) {
		
		MyDemoContext db = new MyDemoContext();
		List<Product> products = db.Products.skip(0).take(2).toList();
		System.out.println("Products: " + products.size());
		for(Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("quantity: " + product.getQuantity());
			System.out.println("====================");
		}
		
		products = db.Products.Where(p -> p.isStatus()).skip(0).take(2).toList();
		System.out.println("Products: " + products.size());
		for(Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("quantity: " + product.getQuantity());
			System.out.println("====================");
		}
		
		products = db.Products.orderBy("price", OrderBy.DESC).skip(0).take(2).toList();
		System.out.println("Products: " + products.size());
		for(Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("quantity: " + product.getQuantity());
			System.out.println("====================");
		}
		
	}

}
```

- **Get A Row**:
```
package demo;

import entitites.Product;
import models.MyDemoContext;

public class Demo {

	public static void main(String[] args) {

		MyDemoContext db = new MyDemoContext();
		Product product = db.Products.Where(p -> p.getId() == 6).singleOrDefault();
		System.out.println("id: " + product.getId());
		System.out.println("name: " + product.getName());
		System.out.println("price: " + product.getPrice());
		System.out.println("quantity: " + product.getQuantity());

	}

}
```

- **Get Rows with DateTime Conditions**:
```
package demo;

import java.util.List;
import entitites.Product;
import models.MyDemoContext;

public class Demo {

	public static void main(String[] args) {

		MyDemoContext db = new MyDemoContext();
		List<Product> products = db.Products.Where(p -> p.getDateCreated().getYear() == 2018
				&& p.getDateCreated().getMonthValue() == 8 && p.getDateCreated().getDayOfMonth() == 21).toList();
		for (Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("date: " + product.getDateCreated());
			System.out.println("==================");
		}
		
		products = db.Products.Where(p -> p.getDateCreated().isAfter(LocalDate.of(2018, 10, 20))).toList();
		System.out.println("Products: " + products.size());
		for(Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("status: " + product.isStatus());
			System.out.println("date: " + product.getDateCreated());
			System.out.println("====================");
		}
		
		products = db.Products.Where(p -> p.getDateCreated().isBefore(LocalDate.of(2018, 10, 20))).toList();
		System.out.println("Products: " + products.size());
		for(Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("status: " + product.isStatus());
			System.out.println("date: " + product.getDateCreated());
			System.out.println("====================");
		}
		
		products = db.Products.Where(p -> p.getDateCreated().equals(LocalDate.of(2018, 10, 20))).toList();
		System.out.println("Products: " + products.size());
		for(Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("status: " + product.isStatus());
			System.out.println("date: " + product.getDateCreated());
			System.out.println("====================");
		}
		
	}

}
```

- **Get Rows with BigDecimal Conditions**:
```
package demo;

import java.util.List;
import entities.Product;
import models.MyDBContext;

public class Demo {

	public static void main(String[] args) {
		
		MyDBContext db = new MyDBContext();
		List<Product> products = db.Products.Where(p -> p.getPrice().compareTo(null) >= 20 && p.getPrice().compareTo(null) <= 100).toList();
		System.out.println("Products: " + products.size());
		for(Product product : products) {
			System.out.println("id: " + product.getId());
			System.out.println("name: " + product.getName());
			System.out.println("price: " + product.getPrice());
			System.out.println("status: " + product.isStatus());
			System.out.println("date: " + product.getDateCreated());
			System.out.println("====================");
		}
		
	}

}
```

- **Sum**:
```
package demo;

import models.MyDemoContext;

public class Demo {

	public static void main(String[] args) {

		MyDemoContext db = new MyDemoContext();
		double result1 = db.Products.sum("quantity");
		System.out.println("Result 1: " + result1);
		double result2 = db.Products.sum("price* quantity");
		System.out.println("Result 2: " + result2);
		double result3 = db.Products.Where(p -> p.isStatus()).sum("price* quantity");
		System.out.println("Result 3: " + result3);

	}

}
```

- **Count**:
```
package demo;

import models.MyDemoContext;

public class Demo {

	public static void main(String[] args) {

		MyDemoContext db = new MyDemoContext();
		long result1 = db.Products.count();
		System.out.println("Result 1: " + result1);
		long result2 = db.Products.Where(p -> p.isStatus()).count();
		System.out.println("Result 2: " + result2);

	}

}
```

- **Min**:
```
package demo;

import models.MyDemoContext;

public class Demo {

	public static void main(String[] args) {

		MyDemoContext db = new MyDemoContext();
		double min1 = db.Products.min("price");
		System.out.println("Min 1: " + min1);
		double min2 = db.Products.Where(p -> p.isStatus()).min("price");
		System.out.println("Min 2: " + min2);

	}

}
```

- **Max**:
```
package demo;

import models.MyDemoContext;

public class Demo {

	public static void main(String[] args) {

		MyDemoContext db = new MyDemoContext();
		double max1 = db.Products.max("price");
		System.out.println("Max 1: " + max1);
		double max2 = db.Products.Where(p -> p.isStatus()).max("price");
		System.out.println("Max 2: " + max2);

	}

}
```

- **Avg**:
```
package demo;

import models.MyDemoContext;

public class Demo {

	public static void main(String[] args) {

		MyDemoContext db = new MyDemoContext();
		double avg1 = db.Products.avg("price");
		System.out.println("Avg 1: " + avg1);
		double avg2 = db.Products.Where(p -> p.isStatus()).avg("price");
		System.out.println("Avg 2: " + avg2);

	}

}
```

- **Insert**:
```
package demo;

import java.math.BigDecimal;
import java.time.LocalDate;
import entitites.Product;
import models.MyDemoContext;

public class Demo {

	public static void main(String[] args) {

		MyDemoContext db = new MyDemoContext();
		Product product = new Product();
		product.setName("Tablet 1");
		product.setPrice(BigDecimal.valueOf(120.5));
		product.setQuantity(5);
		product.setStatus(true);
		product.setDateCreated(LocalDate.now());
		product.setDescription("Description of Tablet 1");
		long id = (long) db.Products.insert(product);
		System.out.println("id: " + id);

	}

}
```

- **Update**:
```
package demo;

import java.math.BigDecimal;
import java.time.LocalDate;
import entitites.Product;
import models.MyDemoContext;

public class Demo {

	public static void main(String[] args) {

		MyDemoContext db = new MyDemoContext();
		Product product = db.Products.Where(p -> p.getId() == 6).singleOrDefault();
		product.setName("ABC");
		product.setPrice(BigDecimal.valueOf(45.5));
		product.setQuantity(5);
		product.setStatus(false);
		product.setDateCreated(LocalDate.of(2018, 11, 24));
		boolean result = db.Products.update(product) > 0;
		System.out.println("result: " + result);

	}

}
```

- **Delete**:
```
package demo;

import entitites.Product;
import models.MyDemoContext;

public class Demo {

	public static void main(String[] args) {

		MyDemoContext db = new MyDemoContext();
		Product product = db.Products.Where(p -> p.getId() == 6).singleOrDefault();
		boolean result = db.Products.delete(product) > 0;
		System.out.println("result: " + result);

	}

}
```

# License

[© LearningProgramming.NET](http://learningprogramming.net). All rights reserved.
