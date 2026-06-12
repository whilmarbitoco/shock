package org.whilmarbitoco.core.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Mapper.
 * Uses simple fake classes instead of Mockito.
 */
class MapperTest {

    // Test model
    @Table(name = "users")
    static class User {
        @Primary
        private int id;

        @Column(name = "username")
        private String username;

        @Column(name = "email")
        private String email;

        User() {}

        int getId() { return id; }
        String getUsername() { return username; }
        String getEmail() { return email; }
    }

    // Fake ResultSet that returns values from a map
    static class FakeResultSet implements ResultSet {
        private final Map<String, Object> data;
        private boolean nextCalled = false;

        FakeResultSet(Map<String, Object> data) {
            this.data = data;
        }

        @Override
        public boolean next() {
            if (!nextCalled) {
                nextCalled = true;
                return true;
            }
            return false;
        }

        @Override
        public Object getObject(String columnLabel) {
            return data.get(columnLabel);
        }

        // All other methods throw UnsupportedOperationException
        @Override public void close() {}
        @Override public boolean wasNull() { return false; }
        @Override public String getString(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public boolean getBoolean(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public byte getByte(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public short getShort(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public int getInt(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public long getLong(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public float getFloat(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public double getDouble(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.math.BigDecimal getBigDecimal(int columnIndex, int scale) { throw new UnsupportedOperationException(); }
        @Override public byte[] getBytes(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Date getDate(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Time getTime(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Timestamp getTimestamp(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.io.InputStream getAsciiStream(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.io.InputStream getUnicodeStream(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.io.InputStream getBinaryStream(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public String getString(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public boolean getBoolean(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public byte getByte(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public short getShort(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public int getInt(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public long getLong(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public float getFloat(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public double getDouble(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.math.BigDecimal getBigDecimal(String columnLabel, int scale) { throw new UnsupportedOperationException(); }
        @Override public byte[] getBytes(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Date getDate(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Time getTime(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Timestamp getTimestamp(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.io.InputStream getAsciiStream(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.io.InputStream getUnicodeStream(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.io.InputStream getBinaryStream(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.sql.SQLWarning getWarnings() { return null; }
        @Override public void clearWarnings() {}
        @Override public String getCursorName() { return null; }
        @Override public java.sql.ResultSetMetaData getMetaData() { return null; }
        @Override public Object getObject(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public int findColumn(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.io.Reader getCharacterStream(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.io.Reader getCharacterStream(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.math.BigDecimal getBigDecimal(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.math.BigDecimal getBigDecimal(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public boolean isBeforeFirst() { return false; }
        @Override public boolean isAfterLast() { return false; }
        @Override public boolean isFirst() { return false; }
        @Override public boolean isLast() { return false; }
        @Override public void beforeFirst() {}
        @Override public void afterLast() {}
        @Override public boolean first() { return false; }
        @Override public boolean last() { return false; }
        @Override public int getRow() { return 0; }
        @Override public boolean absolute(int row) { return false; }
        @Override public boolean relative(int rows) { return false; }
        @Override public boolean previous() { return false; }
        @Override public void setFetchDirection(int direction) {}
        @Override public int getFetchDirection() { return 0; }
        @Override public void setFetchSize(int rows) {}
        @Override public int getFetchSize() { return 0; }
        @Override public int getType() { return 0; }
        @Override public int getConcurrency() { return 0; }
        @Override public boolean rowUpdated() { return false; }
        @Override public boolean rowInserted() { return false; }
        @Override public boolean rowDeleted() { return false; }
        @Override public void updateNull(int columnIndex) {}
        @Override public void updateBoolean(int columnIndex, boolean x) {}
        @Override public void updateByte(int columnIndex, byte x) {}
        @Override public void updateShort(int columnIndex, short x) {}
        @Override public void updateInt(int columnIndex, int x) {}
        @Override public void updateLong(int columnIndex, long x) {}
        @Override public void updateFloat(int columnIndex, float x) {}
        @Override public void updateDouble(int columnIndex, double x) {}
        @Override public void updateBigDecimal(int columnIndex, java.math.BigDecimal x) {}
        @Override public void updateString(int columnIndex, String x) {}
        @Override public void updateBytes(int columnIndex, byte[] x) {}
        @Override public void updateDate(int columnIndex, java.sql.Date x) {}
        @Override public void updateTime(int columnIndex, java.sql.Time x) {}
        @Override public void updateTimestamp(int columnIndex, java.sql.Timestamp x) {}
        @Override public void updateAsciiStream(int columnIndex, java.io.InputStream x, int length) {}
        @Override public void updateBinaryStream(int columnIndex, java.io.InputStream x, int length) {}
        @Override public void updateCharacterStream(int columnIndex, java.io.Reader x, int length) {}
        @Override public void updateObject(int columnIndex, Object x, int scaleOrLength) {}
        @Override public void updateObject(int columnIndex, Object x) {}
        @Override public void updateNull(String columnLabel) {}
        @Override public void updateBoolean(String columnLabel, boolean x) {}
        @Override public void updateByte(String columnLabel, byte x) {}
        @Override public void updateShort(String columnLabel, short x) {}
        @Override public void updateInt(String columnLabel, int x) {}
        @Override public void updateLong(String columnLabel, long x) {}
        @Override public void updateFloat(String columnLabel, float x) {}
        @Override public void updateDouble(String columnLabel, double x) {}
        @Override public void updateBigDecimal(String columnLabel, java.math.BigDecimal x) {}
        @Override public void updateString(String columnLabel, String x) {}
        @Override public void updateBytes(String columnLabel, byte[] x) {}
        @Override public void updateDate(String columnLabel, java.sql.Date x) {}
        @Override public void updateTime(String columnLabel, java.sql.Time x) {}
        @Override public void updateTimestamp(String columnLabel, java.sql.Timestamp x) {}
        @Override public void updateAsciiStream(String columnLabel, java.io.InputStream x, int length) {}
        @Override public void updateBinaryStream(String columnLabel, java.io.InputStream x, int length) {}
        @Override public void updateCharacterStream(String columnLabel, java.io.Reader reader, int length) {}
        @Override public void updateObject(String columnLabel, Object x, int scaleOrLength) {}
        @Override public void updateObject(String columnLabel, Object x) {}
        @Override public void insertRow() {}
        @Override public void updateRow() {}
        @Override public void deleteRow() {}
        @Override public void refreshRow() {}
        @Override public void cancelRowUpdates() {}
        @Override public void moveToInsertRow() {}
        @Override public void moveToCurrentRow() {}
        @Override public java.sql.Statement getStatement() { return null; }
        @Override public Object getObject(int columnIndex, java.util.Map<String, Class<?>> map) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Ref getRef(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Blob getBlob(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Clob getClob(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Array getArray(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public Object getObject(String columnLabel, java.util.Map<String, Class<?>> map) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Ref getRef(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Blob getBlob(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Clob getClob(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Array getArray(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Date getDate(int columnIndex, java.util.Calendar cal) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Date getDate(String columnLabel, java.util.Calendar cal) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Time getTime(int columnIndex, java.util.Calendar cal) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Time getTime(String columnLabel, java.util.Calendar cal) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Timestamp getTimestamp(int columnIndex, java.util.Calendar cal) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Timestamp getTimestamp(String columnLabel, java.util.Calendar cal) { throw new UnsupportedOperationException(); }
        @Override public java.net.URL getURL(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.net.URL getURL(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public void updateRef(int columnIndex, java.sql.Ref x) {}
        @Override public void updateRef(String columnLabel, java.sql.Ref x) {}
        @Override public void updateBlob(int columnIndex, java.sql.Blob x) {}
        @Override public void updateBlob(String columnLabel, java.sql.Blob x) {}
        @Override public void updateClob(int columnIndex, java.sql.Clob x) {}
        @Override public void updateClob(String columnLabel, java.sql.Clob x) {}
        @Override public void updateArray(int columnIndex, java.sql.Array x) {}
        @Override public void updateArray(String columnLabel, java.sql.Array x) {}
        @Override public java.sql.RowId getRowId(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.sql.RowId getRowId(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public void updateRowId(int columnIndex, java.sql.RowId x) {}
        @Override public void updateRowId(String columnLabel, java.sql.RowId x) {}
        @Override public int getHoldability() { return 0; }
        @Override public boolean isClosed() { return false; }
        @Override public void updateNString(int columnIndex, String nString) {}
        @Override public void updateNString(String columnLabel, String nString) {}
        @Override public void updateNClob(int columnIndex, java.sql.Clob nClob) {}
        @Override public void updateNClob(String columnLabel, java.sql.Clob nClob) {}
        @Override public java.sql.NClob getNClob(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.sql.NClob getNClob(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.sql.SQLXML getSQLXML(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.sql.SQLXML getSQLXML(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public void updateSQLXML(int columnIndex, java.sql.SQLXML xmlObject) {}
        @Override public void updateSQLXML(String columnLabel, java.sql.SQLXML xmlObject) {}
        @Override public String getNString(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public String getNString(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public java.io.Reader getNCharacterStream(int columnIndex) { throw new UnsupportedOperationException(); }
        @Override public java.io.Reader getNCharacterStream(String columnLabel) { throw new UnsupportedOperationException(); }
        @Override public void updateNCharacterStream(int columnIndex, java.io.Reader x, long length) {}
        @Override public void updateNCharacterStream(String columnLabel, java.io.Reader reader, long length) {}
        @Override public void updateAsciiStream(int columnIndex, java.io.InputStream x, long length) {}
        @Override public void updateBinaryStream(int columnIndex, java.io.InputStream x, long length) {}
        @Override public void updateCharacterStream(int columnIndex, java.io.Reader x, long length) {}
        @Override public void updateAsciiStream(String columnLabel, java.io.InputStream x, long length) {}
        @Override public void updateBinaryStream(String columnLabel, java.io.InputStream x, long length) {}
        @Override public void updateCharacterStream(String columnLabel, java.io.Reader reader, long length) {}
        @Override public void updateBlob(int columnIndex, java.io.InputStream inputStream, long length) {}
        @Override public void updateBlob(String columnLabel, java.io.InputStream inputStream, long length) {}
        @Override public void updateClob(int columnIndex, java.io.Reader reader, long length) {}
        @Override public void updateClob(String columnLabel, java.io.Reader reader, long length) {}
        @Override public void updateNClob(int columnIndex, java.io.Reader reader, long length) {}
        @Override public void updateNClob(String columnLabel, java.io.Reader reader, long length) {}
        @Override public void updateNCharacterStream(int columnIndex, java.io.Reader x) {}
        @Override public void updateNCharacterStream(String columnLabel, java.io.Reader reader) {}
        @Override public void updateAsciiStream(int columnIndex, java.io.InputStream x) {}
        @Override public void updateBinaryStream(int columnIndex, java.io.InputStream x) {}
        @Override public void updateCharacterStream(int columnIndex, java.io.Reader x) {}
        @Override public void updateAsciiStream(String columnLabel, java.io.InputStream x) {}
        @Override public void updateBinaryStream(String columnLabel, java.io.InputStream x) {}
        @Override public void updateCharacterStream(String columnLabel, java.io.Reader reader) {}
        @Override public void updateBlob(int columnIndex, java.io.InputStream inputStream) {}
        @Override public void updateBlob(String columnLabel, java.io.InputStream inputStream) {}
        @Override public void updateClob(int columnIndex, java.io.Reader reader) {}
        @Override public void updateClob(String columnLabel, java.io.Reader reader) {}
        @Override public void updateNClob(int columnIndex, java.io.Reader reader) {}
        @Override public void updateNClob(String columnLabel, java.io.Reader reader) {}
        @Override public <T> T getObject(int columnIndex, Class<T> type) { throw new UnsupportedOperationException(); }
        @Override public <T> T getObject(String columnLabel, Class<T> type) { throw new UnsupportedOperationException(); }
        @Override public <T> T unwrap(Class<T> iface) { throw new UnsupportedOperationException(); }
        @Override public boolean isWrapperFor(Class<T> iface) { return false; }
    }

    // Fake PreparedStatement that records setObject calls
    static class FakePreparedStatement implements PreparedStatement {
        private final Map<Integer, Object> params = new HashMap<>();

        Map<Integer, Object> getParams() { return params; }

        @Override
        public void setObject(int parameterIndex, Object x) {
            params.put(parameterIndex, x);
        }

        // All other methods throw UnsupportedOperationException or return defaults
        @Override public java.sql.ResultSet executeQuery() { return null; }
        @Override public int executeUpdate() { return 0; }
        @Override public void close() {}
        @Override public int getMaxFieldSize() { return 0; }
        @Override public void setMaxFieldSize(int max) {}
        @Override public int getMaxRows() { return 0; }
        @Override public void setMaxRows(int max) {}
        @Override public void setEscapeProcessing(boolean enable) {}
        @Override public int getQueryTimeout() { return 0; }
        @Override public void setQueryTimeout(int seconds) {}
        @Override public void cancel() {}
        @Override public java.sql.SQLWarning getWarnings() { return null; }
        @Override public void clearWarnings() {}
        @Override public void setCursorName(String name) {}
        @Override public boolean execute() { return false; }
        @Override public java.sql.ResultSet getResultSet() { return null; }
        @Override public int getUpdateCount() { return 0; }
        @Override public boolean getMoreResults() { return false; }
        @Override public void setFetchDirection(int direction) {}
        @Override public int getFetchDirection() { return 0; }
        @Override public void setFetchSize(int rows) {}
        @Override public int getFetchSize() { return 0; }
        @Override public int getResultSetConcurrency() { return 0; }
        @Override public int getResultSetType() { return 0; }
        @Override public void addBatch() {}
        @Override public void clearBatch() {}
        @Override public int[] executeBatch() { return new int[0]; }
        @Override public java.sql.Connection getConnection() { return null; }
        @Override public boolean getMoreResults(int current) { return false; }
        @Override public java.sql.ResultSet getGeneratedKeys() { return null; }
        @Override public int executeUpdate(String sql) { return 0; }
        @Override public boolean execute(String sql) { return false; }
        @Override public int executeUpdate(String sql, int autoGeneratedKeys) { return 0; }
        @Override public int executeUpdate(String sql, int[] columnIndexes) { return 0; }
        @Override public int executeUpdate(String sql, String[] columnNames) { return 0; }
        @Override public boolean execute(String sql, int autoGeneratedKeys) { return false; }
        @Override public boolean execute(String sql, int[] columnIndexes) { return false; }
        @Override public boolean execute(String sql, String[] columnNames) { return false; }
        @Override public int getResultSetHoldability() { return 0; }
        @Override public boolean isClosed() { return false; }
        @Override public void setPoolable(boolean poolable) {}
        @Override public boolean isPoolable() { return false; }
        @Override public void closeOnCompletion() {}
        @Override public boolean isCloseOnCompletion() { return false; }
        @Override public <T> T unwrap(Class<T> iface) { throw new UnsupportedOperationException(); }
        @Override public boolean isWrapperFor(Class<T> iface) { return false; }
        @Override public void setNull(int parameterIndex, int sqlType) {}
        @Override public void setBoolean(int parameterIndex, boolean x) {}
        @Override public void setByte(int parameterIndex, byte x) {}
        @Override public void setShort(int parameterIndex, short x) {}
        @Override public void setInt(int parameterIndex, int x) {}
        @Override public void setLong(int parameterIndex, long x) {}
        @Override public void setFloat(int parameterIndex, float x) {}
        @Override public void setDouble(int parameterIndex, double x) {}
        @Override public void setBigDecimal(int parameterIndex, java.math.BigDecimal x) {}
        @Override public void setString(int parameterIndex, String x) {}
        @Override public void setBytes(int parameterIndex, byte[] x) {}
        @Override public void setDate(int parameterIndex, java.sql.Date x) {}
        @Override public void setTime(int parameterIndex, java.sql.Time x) {}
        @Override public void setTimestamp(int parameterIndex, java.sql.Timestamp x) {}
        @Override public void setAsciiStream(int parameterIndex, java.io.InputStream x, int length) {}
        @Override public void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) {}
        @Override public void setCharacterStream(int parameterIndex, java.io.Reader reader, int length) {}
        @Override public void setObject(int parameterIndex, Object x, int targetSqlType) {}
        @Override public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) {}
        @Override public void addBatch(String sql) {}
        @Override public void setCharacterStream(int parameterIndex, java.io.Reader reader) {}
        @Override public void setRef(int parameterIndex, java.sql.Ref x) {}
        @Override public void setBlob(int parameterIndex, java.sql.Blob x) {}
        @Override public void setClob(int parameterIndex, java.sql.Clob x) {}
        @Override public void setArray(int parameterIndex, java.sql.Array x) {}
        @Override public java.sql.ResultSetMetaData getMetaData() { return null; }
        @Override public void setDate(int parameterIndex, java.sql.Date x, java.util.Calendar cal) {}
        @Override public void setTime(int parameterIndex, java.sql.Time x, java.util.Calendar cal) {}
        @Override public void setTimestamp(int parameterIndex, java.sql.Timestamp x, java.util.Calendar cal) {}
        @Override public void setNull(int parameterIndex, int sqlType, String typeName) {}
        @Override public void setURL(int parameterIndex, java.net.URL x) {}
        @Override public java.sql.ParameterMetaData getParameterMetaData() { return null; }
        @Override public void setRowId(int parameterIndex, java.sql.RowId x) {}
        @Override public void setNString(int parameterIndex, String value) {}
        @Override public void setNCharacterStream(int parameterIndex, java.io.Reader value, long length) {}
        @Override public void setNClob(int parameterIndex, java.sql.NClob value) {}
        @Override public void setClob(int parameterIndex, java.io.Reader reader, long length) {}
        @Override public void setBlob(int parameterIndex, java.io.InputStream inputStream, long length) {}
        @Override public void setNClob(int parameterIndex, java.io.Reader reader, long length) {}
        @Override public void setSQLXML(int parameterIndex, java.sql.SQLXML xmlObject) {}
        @Override public void setObject(int parameterIndex, Object x, java.sql.SQLType targetSqlType, int scaleOrLength) {}
        @Override public void setObject(int parameterIndex, Object x, java.sql.SQLType targetSqlType) {}
        @Override public long executeLargeUpdate() { return 0; }
        @Override public void setAsciiStream(int parameterIndex, java.io.InputStream x) {}
        @Override public void setBinaryStream(int parameterIndex, java.io.InputStream x) {}
        @Override public void setBlob(int parameterIndex, java.io.InputStream inputStream) {}
        @Override public void setClob(int parameterIndex, java.io.Reader reader) {}
        @Override public void setNClob(int parameterIndex, java.io.Reader reader) {}
        @Override public void setAsciiStream(int parameterIndex, java.io.InputStream x, long length) {}
        @Override public void setBinaryStream(int parameterIndex, java.io.InputStream x, long length) {}
        @Override public void setCharacterStream(int parameterIndex, java.io.Reader reader, long length) {}
        @Override public void setNCharacterStream(int parameterIndex, java.io.Reader value) {}
        @Override public void setNCharacterStream(int parameterIndex, java.io.Reader value, long length) {}
    }

    @Test
    @DisplayName("toEntity maps ResultSet columns to fields by annotation name")
    void toEntityMapsByColumnName() throws SQLException {
        Mapper<User> mapper = new Mapper<>(User.class);
        Map<String, Object> data = Map.of("username", "alice", "email", "alice@test.com");
        ResultSet rs = new FakeResultSet(data);

        User user = mapper.toEntity(rs);

        assertEquals("alice", user.getUsername());
        assertEquals("alice@test.com", user.getEmail());
    }

    @Test
    @DisplayName("fromEntity sets PreparedStatement parameters in mapped field order")
    void fromEntitySetsParametersInOrder() throws SQLException {
        Mapper<User> mapper = new Mapper<>(User.class);
        FakePreparedStatement stmt = new FakePreparedStatement();
        User user = new User(0, "bob", "bob@test.com");

        mapper.fromEntity(user, stmt);

        assertEquals("bob", stmt.getParams().get(1));
        assertEquals("bob@test.com", stmt.getParams().get(2));
    }

    @Test
    @DisplayName("Mapper ignores fields without @Column annotation")
    void mapperIgnoresUnannotatedFields() throws SQLException {
        @Table(name = "items")
        class ItemWithExtra {
            @Primary
            private int id;
            @Column(name = "name")
            private String name;
            private String ignored;
        }

        Mapper<ItemWithExtra> mapper = new Mapper<>(ItemWithExtra.class);
        Map<String, Object> data = Map.of("name", "widget");
        ResultSet rs = new FakeResultSet(data);

        ItemWithExtra item = mapper.toEntity(rs);
        assertEquals("widget", item.name);
        assertNull(item.ignored);
    }
}
