package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.List;

// add the annotations to make this a REST controller
@RestController
// add the annotation to make this controller the endpoint for the following url
@RequestMapping("/categories")
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
@CrossOrigin
public class CategoriesController
{
    private CategoryService categoryService;
    private ProductService productService;


    // create an Autowired constructor to inject the categoryService and productService
    public CategoriesController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    // add the appropriate annotation for a get action
    @GetMapping
    @PreAuthorize("permitAll()")
    public List<Category> getAll()
    {
        // find and return all categories
        return categoryService.getAllCategories();
    }

    // add the appropriate annotation for a get action
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public Category getById(@PathVariable int id)
    {
        // get the category by id
        Category category = categoryService.getById(id);
        if (category == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        return category;
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return productService.listByCategoryId(categoryId);
    }

    // add annotation to call this method for a POST action
    @PostMapping
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category)
    {
        // insert the category and return it with status 201 Created
        Category newCategory = categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    @PutMapping("/{id}")
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ADMIN')")
    public Category updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        // update the category by id and return the updated category (200 OK)
        Category updatedCategory = categoryService.update(id, category);
        if (updatedCategory == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        return updatedCategory;
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    @DeleteMapping("/{id}")
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id)
    {
        // delete the category by id and return status 204 No Content
        if (categoryService.getById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
