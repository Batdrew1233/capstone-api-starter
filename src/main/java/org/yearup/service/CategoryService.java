package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories()
    {
        // get all categories
        return categoryRepository.findAll();
    }

    /*
    Gets a category by its ID
    Returns null if the category does not exist.
     */
    public Category getById(int categoryId)
    {
        // get category by id
        return categoryRepository.findById(categoryId).orElse(null);
    }

    /*
    Creates and saves a new category to the databases.
     */
    public Category create(Category category)
    {
        // create a new category
        return categoryRepository.save(category);
    }

    /*
    Updates an existing category's name and description
    Returns the updated category, or null if the category doesn't exist.
     */
    public Category update(int categoryId, Category category)
    {
        // update category and return the updated category
        Category existing = categoryRepository.findById(categoryId).orElse(null);
        if (existing == null){
            return null;
        }
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        return categoryRepository.save(existing);
    }

    public void delete(int categoryId)
    {
        categoryRepository.deleteById(categoryId);
    }
}
