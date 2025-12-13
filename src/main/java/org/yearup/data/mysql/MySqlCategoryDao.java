package org.yearup.data.mysql;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        // get all categories
        List<Category> categories = new ArrayList<>();

        String query = """
                USE easyshop;
                SELECT
                	category_id,
                    name,
                    description
                FROM categories""";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet results = statement.executeQuery()){
            while (results.next()){
//
//                int categoryId = results.getInt(1);
//                String categoryName = results.getString(2);
//                String description = results.getString(3);
//
//                Category c = new Category();
//                c.setCategoryId(categoryId);
//                c.setName(categoryName);
//                c.setDescription(description);
//      old code^^^
                // using the method helper already made line 111 Convert the current row into a Category object and add to list
                categories.add(mapRow(results));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // get back full list
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        // get category by id
        String query = """
                USE easyshop;
                
                SELECT * FROM categories
                WHERE category_id = ?""";

        try (   Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ){
            // Set the ? parameter in SQL
            statement.setInt(1, categoryId);

            // execute query
            try ( ResultSet results = statement.executeQuery()){
                // while row is being returned, map it to Category object in helper
                while (results.next()
                ){
                    return mapRow(results);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        String query = """
                USE easyshop;
                INSERT INTO categories (
                name,
                description)
                VALUES (?, ?);""";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
                ){
            statement.setString(1, category.getName());
            
        }





        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
