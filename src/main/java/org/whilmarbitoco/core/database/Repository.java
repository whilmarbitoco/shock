package org.whilmarbitoco.core.database;

import org.whilmarbitoco.core.utils.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public abstract class Repository<T> {

    protected final Connection connection = DBConnection.getConnection();


    protected EntityManager<T> entityManager;
    protected Builder builder;
    protected Mapper<T> mapper;
    protected String tableName;
    protected List<String> columns;

    public Repository(Class<T> type) {
        this.entityManager = new EntityManager<>(type);
        this.builder = new Builder();
        this.mapper = new Mapper<>(type);
        this.tableName = entityManager.getTable();
        this.columns = entityManager.getColumns();
    }


    public final void create(T entity) {
        entityManager.validate(entity);
        String query = builder.insert(tableName, columns).build();
        execute(entity, query);
    }

    public List<T> findAll() {
        String query = builder.select(tableName).build();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            return executeQuery(stmt).list();
        } catch (SQLException err) {
            throw new RuntimeException("[Repository] SQL Error:: " + err.getMessage());
        }
    }

    public Optional<T> findByID(int id) {
        Optional<String> primaryKey = entityManager.getPrimaryKey();

        if (primaryKey.isEmpty())
            throw new RuntimeException("[Repository] Empty primary key for " + entityManager.getTable());

        String query = builder.select(tableName).where(primaryKey.get() + " = ?").build();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, id);
            return Optional.ofNullable(executeQuery(stmt).firstResult());
        } catch (SQLException err) {
            throw new RuntimeException("[Repository] SQL Error:: " + err.getMessage());
        }
    }

    public List<T> findWhere(String column, String condition, Object value) {
        entityManager.isValidColumn(column);
        entityManager.isValidCondition(condition);

        String query = builder.select(tableName).where(column + " " + condition + " ?").build();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, value);
            return executeQuery(stmt).list();
        } catch (SQLException err) {
            throw new RuntimeException("[Repository] SQL Error:: " + err.getMessage());
        }
    }

    public List<T> rawWhere(String condition, Object... value) {
        String query = builder.select(tableName).where(condition).build();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            for (int i = 0; i < value.length; i++) {
                stmt.setObject(i + 1, value[i]);
            }

            return executeQuery(stmt).list();

        } catch (SQLException err) {
            throw new RuntimeException("[Repository] SQL Error:: " + err.getMessage());
        }
    }

    public List<T> Raw(String query, Object... value) {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            for (int i = 0; i < value.length; i++) {
                stmt.setObject(i + 1, value[i]);
            }

            return executeQuery(stmt).list();

        } catch (SQLException err) {
            throw new RuntimeException("[Repository] SQL Error:: " + err.getMessage());
        }
    }

    public List<T> findLike(String column, Object value) {
        entityManager.isValidColumn(column);

        String query = builder.select(tableName).where(column).like("?").build();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, "%" + value + "%");
            return executeQuery(stmt).list();
        } catch (SQLException err) {
            throw new RuntimeException("[Repository] SQL Error:: " + err.getMessage());
        }
    }

    public void update(T entity) {
        Optional<String> primaryKey = entityManager.getPrimaryKey();

        if (primaryKey.isEmpty())
            throw new RuntimeException("[Repository] Empty primary key for " + entityManager.getTable());

        Object pkValue = entityManager.getPrimaryKeyValue(entity);

        String query = builder.update(tableName, columns)
                .where(primaryKey.get() + " = " + pkValue).build();

        execute(entity, query);
    }

    public void delete(int id) {
        Optional<String> primaryKey = entityManager.getPrimaryKey();

        if (primaryKey.isEmpty())
            throw new RuntimeException("[Repository] Empty primary key for " + entityManager.getTable());

        String query = builder.delete(tableName).where(primaryKey.get() + " = ?").build();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, id);
            System.out.println("QUERY:: " + stmt.toString());
            stmt.execute();

        } catch (SQLException err) {
            throw new RuntimeException("[Repository] SQL Error:: " + err.getMessage());
        }
    }

    public void deleteWhere(String condition, Object... values) {
        String query = builder.delete(tableName).where(condition).build();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }
            System.out.println("QUERY:: " + stmt.toString());
            stmt.execute();

        } catch (SQLException err) {
            throw new RuntimeException("[Repository] SQL Error:: " + err.getMessage());
        }
    }

    public void deleteAll() {
        String query = builder.delete(tableName).build();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            System.out.println("QUERY:: " + stmt.toString());
            stmt.execute();

        } catch (SQLException err) {
            throw new RuntimeException("[Repository] SQL Error:: " + err.getMessage());
        }
    }

    public int max() {
        Optional<String> key = entityManager.getPrimaryKey();
        if (key.isEmpty())
            throw new RuntimeException("[Repository] Empty primary key for " + entityManager.getTable());

        String query = builder.max(key.get(), tableName).build();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }

            return 0;
        } catch (SQLException err) {
            throw new RuntimeException("[Repository] SQL Error:: " + err.getMessage());
        }
    }

    public int count() {
        String query = builder.count(tableName).build();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }

            return 0;
        } catch (SQLException err) {
            throw new RuntimeException("[Repository] SQL Error:: " + err.getMessage());
        }
    }

    protected QueryResult<T> executeQuery(PreparedStatement stmt) {
        try {
            List<T> result = new ArrayList<>();

            if (Config.debug()) {
                System.out.println("QUERY:: " + stmt.toString());
            }
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                result.add(mapper.toEntity(res));
            }

            return new QueryResult<>(result);
        } catch (SQLException err) {
            throw new RuntimeException("[Repository] Failed to execute on table["+tableName+ "] due to -> " + err.getMessage());
        }
    }

    protected void execute(T entity, String query) {
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            PreparedStatement pstmt = mapper.fromEntity(entity, stmt);
            if (Config.debug()) {
                System.out.println("QUERY:: " + pstmt.toString());
            }
            pstmt.execute();
        } catch (SQLException err) {
            throw new RuntimeException("[Repository] SQL Error:: " + err.getMessage());
        }
    }

}