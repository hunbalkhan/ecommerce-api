package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        // get all categories
        List<Category> categories = new ArrayList<>();

        String query = """
                SELECT
                	category_id,
                    name,
                    description
                FROM categories""";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet results = statement.executeQuery()
        ) {
            while (results.next()) {

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
        }
        // get back full list
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        // get category by id
        String query = """
                SELECT * FROM categories
                WHERE category_id = ?""";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            // Set the ? parameter in SQL
            statement.setInt(1, categoryId);

            // execute query
            try (ResultSet results = statement.executeQuery()) {
                // while row is being returned, map it to Category object in helper
                while (results.next()
                ) {
                    return mapRow(results);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Category create(Category category) {
        // create a new category
        String query = """
                INSERT INTO categories (
                name,
                description)
                VALUES (?, ?);""";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            // executes update
            statement.executeUpdate();

            // get the generated category_id
            try (ResultSet keys = statement.getGeneratedKeys()
            ) {
                while (keys.next()) {
                    int newId = keys.getInt(1);
                    // returning newly created category
                    return getById(newId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // return null if creation is failed
        return null;
    }

    @Override
    public void update(int categoryId, Category category) {
        // update category
        String query = """
                UPDATE categories
                SET
                name = ?,
                description = ?,
                WHERE category_id = ?;""";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            // parameters
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId) {
        // delete category
        String query = "DELETE FROM categories WHERE category_id = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, categoryId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
