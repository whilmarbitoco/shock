package org.whilmarbitoco.core.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.sql.*;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RelationManager.
 * Uses simple fake JDBC classes instead of Mockito.
 */
class RelationManagerTest {

    // ── Test models ───────────────────────────────────────────────

    @Table(name = "users")
    static class TestUser {
        @Primary
        @Column(name = "id")
        private int id;
        @Column(name = "name")
        private String name;
        @HasMany(foreignKey = "user_id", referencedTable = "posts", referencedColumn = "id")
        private List<TestPost> posts;
        TestUser() {}
        TestUser(int id, String name) { this.id = id; this.name = name; }
        int getId() { return id; }
        String getName() { return name; }
        List<TestPost> getPosts() { return posts; }
        void setPosts(List<TestPost> posts) { this.posts = posts; }
    }

    @Table(name = "posts")
    static class TestPost {
        @Primary
        @Column(name = "id")
        private int id;
        @Column(name = "title")
        private String title;
        @Column(name = "user_id")
        private int userId;
        @BelongsTo(foreignKey = "user_id", referencedTable = "users", referencedColumn = "id")
        private TestUser user;
        TestPost() {}
        TestPost(int id, String title, int userId) { this.id = id; this.title = title; this.userId = userId; }
        int getId() { return id; }
        String getTitle() { return title; }
        int getUserId() { return userId; }
        TestUser getUser() { return user; }
        void setUser(TestUser user) { this.user = user; }
    }

    // ── Fake JDBC ─────────────────────────────────────────────────

    static class FakeResultSet implements ResultSet {
        private final List<Map<String, Object>> rows;
        private int cursor = -1;

        FakeResultSet(List<Map<String, Object>> rows) { this.rows = rows; }

        @Override public boolean next() { cursor++; return cursor < rows.size(); }
        @Override public Object getObject(String column) { return rows.get(cursor).get(column); }
        @Override public void close() {}

        // Stubs
        @Override public boolean wasNull() { return false; }
        @Override public String getString(int i) { throw new UnsupportedOperationException(); }
        @Override public boolean getBoolean(int i) { throw new UnsupportedOperationException(); }
        @Override public byte getByte(int i) { throw new UnsupportedOperationException(); }
        @Override public short getShort(int i) { throw new UnsupportedOperationException(); }
        @Override public int getInt(int i) { throw new UnsupportedOperationException(); }
        @Override public long getLong(int i) { throw new UnsupportedOperationException(); }
        @Override public float getFloat(int i) { throw new UnsupportedOperationException(); }
        @Override public double getDouble(int i) { throw new UnsupportedOperationException(); }
        @Override public java.math.BigDecimal getBigDecimal(int i, int s) { throw new UnsupportedOperationException(); }
        @Override public byte[] getBytes(int i) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Date getDate(int i) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Time getTime(int i) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Timestamp getTimestamp(int i) { throw new UnsupportedOperationException(); }
        @Override public java.io.InputStream getAsciiStream(int i) { throw new UnsupportedOperationException(); }
        @Override public java.io.InputStream getUnicodeStream(int i) { throw new UnsupportedOperationException(); }
        @Override public java.io.InputStream getBinaryStream(int i) { throw new UnsupportedOperationException(); }
        @Override public String getString(String c) { throw new UnsupportedOperationException(); }
        @Override public boolean getBoolean(String c) { throw new UnsupportedOperationException(); }
        @Override public byte getByte(String c) { throw new UnsupportedOperationException(); }
        @Override public short getShort(String c) { throw new UnsupportedOperationException(); }
        @Override public int getInt(String c) { throw new UnsupportedOperationException(); }
        @Override public long getLong(String c) { throw new UnsupportedOperationException(); }
        @Override public float getFloat(String c) { throw new UnsupportedOperationException(); }
        @Override public double getDouble(String c) { throw new UnsupportedOperationException(); }
        @Override public java.math.BigDecimal getBigDecimal(String c, int s) { throw new UnsupportedOperationException(); }
        @Override public byte[] getBytes(String c) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Date getDate(String c) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Time getTime(String c) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Timestamp getTimestamp(String c) { throw new UnsupportedOperationException(); }
        @Override public java.io.InputStream getAsciiStream(String c) { throw new UnsupportedOperationException(); }
        @Override public java.io.InputStream getUnicodeStream(String c) { throw new UnsupportedOperationException(); }
        @Override public java.io.InputStream getBinaryStream(String c) { throw new UnsupportedOperationException(); }
        @Override public java.sql.SQLWarning getWarnings() { return null; }
        @Override public void clearWarnings() {}
        @Override public String getCursorName() { return null; }
        @Override public java.sql.ResultSetMetaData getMetaData() { return null; }
        @Override public Object getObject(int i) { throw new UnsupportedOperationException(); }
        @Override public int findColumn(String c) { throw new UnsupportedOperationException(); }
        @Override public java.io.Reader getCharacterStream(int i) { throw new UnsupportedOperationException(); }
        @Override public java.io.Reader getCharacterStream(String c) { throw new UnsupportedOperationException(); }
        @Override public java.math.BigDecimal getBigDecimal(int i) { throw new UnsupportedOperationException(); }
        @Override public java.math.BigDecimal getBigDecimal(String c) { throw new UnsupportedOperationException(); }
        @Override public boolean isBeforeFirst() { return false; }
        @Override public boolean isAfterLast() { return false; }
        @Override public boolean isFirst() { return false; }
        @Override public boolean isLast() { return false; }
        @Override public void beforeFirst() {}
        @Override public void afterLast() {}
        @Override public boolean first() { return false; }
        @Override public boolean last() { return false; }
        @Override public int getRow() { return 0; }
        @Override public boolean absolute(int r) { return false; }
        @Override public boolean relative(int r) { return false; }
        @Override public boolean previous() { return false; }
        @Override public void setFetchDirection(int d) {}
        @Override public int getFetchDirection() { return 0; }
        @Override public void setFetchSize(int s) {}
        @Override public int getFetchSize() { return 0; }
        @Override public int getType() { return 0; }
        @Override public int getConcurrency() { return 0; }
        @Override public boolean rowUpdated() { return false; }
        @Override public boolean rowInserted() { return false; }
        @Override public boolean rowDeleted() { return false; }
        @Override public void updateNull(int i) {}
        @Override public void updateBoolean(int i, boolean x) {}
        @Override public void updateByte(int i, byte x) {}
        @Override public void updateShort(int i, short x) {}
        @Override public void updateInt(int i, int x) {}
        @Override public void updateLong(int i, long x) {}
        @Override public void updateFloat(int i, float x) {}
        @Override public void updateDouble(int i, double x) {}
        @Override public void updateBigDecimal(int i, java.math.BigDecimal x) {}
        @Override public void updateString(int i, String x) {}
        @Override public void updateBytes(int i, byte[] x) {}
        @Override public void updateDate(int i, java.sql.Date x) {}
        @Override public void updateTime(int i, java.sql.Time x) {}
        @Override public void updateTimestamp(int i, java.sql.Timestamp x) {}
        @Override public void updateAsciiStream(int i, java.io.InputStream x, int l) {}
        @Override public void updateBinaryStream(int i, java.io.InputStream x, int l) {}
        @Override public void updateCharacterStream(int i, java.io.Reader x, int l) {}
        @Override public void updateObject(int i, Object x, int s) {}
        @Override public void updateObject(int i, Object x) {}
        @Override public void updateNull(String c) {}
        @Override public void updateBoolean(String c, boolean x) {}
        @Override public void updateByte(String c, byte x) {}
        @Override public void updateShort(String c, short x) {}
        @Override public void updateInt(String c, int x) {}
        @Override public void updateLong(String c, long x) {}
        @Override public void updateFloat(String c, float x) {}
        @Override public void updateDouble(String c, double x) {}
        @Override public void updateBigDecimal(String c, java.math.BigDecimal x) {}
        @Override public void updateString(String c, String x) {}
        @Override public void updateBytes(String c, byte[] x) {}
        @Override public void updateDate(String c, java.sql.Date x) {}
        @Override public void updateTime(String c, java.sql.Time x) {}
        @Override public void updateTimestamp(String c, java.sql.Timestamp x) {}
        @Override public void updateAsciiStream(String c, java.io.InputStream x, int l) {}
        @Override public void updateBinaryStream(String c, java.io.InputStream x, int l) {}
        @Override public void updateCharacterStream(String c, java.io.Reader r, int l) {}
        @Override public void updateObject(String c, Object x, int s) {}
        @Override public void updateObject(String c, Object x) {}
        @Override public void insertRow() {}
        @Override public void updateRow() {}
        @Override public void deleteRow() {}
        @Override public void refreshRow() {}
        @Override public void cancelRowUpdates() {}
        @Override public void moveToInsertRow() {}
        @Override public void moveToCurrentRow() {}
        @Override public java.sql.Statement getStatement() { return null; }
        @Override public Object getObject(int i, Map<String, Class<?>> m) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Ref getRef(int i) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Blob getBlob(int i) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Clob getClob(int i) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Array getArray(int i) { throw new UnsupportedOperationException(); }
        @Override public Object getObject(String c, Map<String, Class<?>> m) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Ref getRef(String c) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Blob getBlob(String c) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Clob getClob(String c) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Array getArray(String c) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Date getDate(int i, java.util.Calendar cal) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Date getDate(String c, java.util.Calendar cal) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Time getTime(int i, java.util.Calendar cal) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Time getTime(String c, java.util.Calendar cal) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Timestamp getTimestamp(int i, java.util.Calendar cal) { throw new UnsupportedOperationException(); }
        @Override public java.sql.Timestamp getTimestamp(String c, java.util.Calendar cal) { throw new UnsupportedOperationException(); }
        @Override public java.net.URL getURL(int i) { throw new UnsupportedOperationException(); }
        @Override public java.net.URL getURL(String c) { throw new UnsupportedOperationException(); }
        @Override public void updateRef(int i, java.sql.Ref x) {}
        @Override public void updateRef(String c, java.sql.Ref x) {}
        @Override public void updateBlob(int i, java.sql.Blob x) {}
        @Override public void updateBlob(String c, java.sql.Blob x) {}
        @Override public void updateClob(int i, java.sql.Clob x) {}
        @Override public void updateClob(String c, java.sql.Clob x) {}
        @Override public void updateArray(int i, java.sql.Array x) {}
        @Override public void updateArray(String c, java.sql.Array x) {}
        @Override public java.sql.RowId getRowId(int i) { throw new UnsupportedOperationException(); }
        @Override public java.sql.RowId getRowId(String c) { throw new UnsupportedOperationException(); }
        @Override public void updateRowId(int i, java.sql.RowId x) {}
        @Override public void updateRowId(String c, java.sql.RowId x) {}
        @Override public int getHoldability() { return 0; }
        @Override public boolean isClosed() { return false; }
        @Override public void updateNString(int i, String s) {}
        @Override public void updateNString(String c, String s) {}
        @Override public void updateNClob(int i, java.sql.NClob nClob) {}
        @Override public void updateNClob(String columnLabel, java.sql.NClob nClob) {}
        @Override public java.sql.NClob getNClob(int i) { throw new UnsupportedOperationException(); }
        @Override public java.sql.NClob getNClob(String c) { throw new UnsupportedOperationException(); }
        @Override public java.sql.SQLXML getSQLXML(int i) { throw new UnsupportedOperationException(); }
        @Override public java.sql.SQLXML getSQLXML(String c) { throw new UnsupportedOperationException(); }
        @Override public void updateSQLXML(int i, java.sql.SQLXML x) {}
        @Override public void updateSQLXML(String c, java.sql.SQLXML x) {}
        @Override public String getNString(int i) { throw new UnsupportedOperationException(); }
        @Override public String getNString(String c) { throw new UnsupportedOperationException(); }
        @Override public java.io.Reader getNCharacterStream(int i) { throw new UnsupportedOperationException(); }
        @Override public java.io.Reader getNCharacterStream(String c) { throw new UnsupportedOperationException(); }
        @Override public void updateNCharacterStream(int i, java.io.Reader r, long l) {}
        @Override public void updateNCharacterStream(String c, java.io.Reader r, long l) {}
        @Override public void updateAsciiStream(int i, java.io.InputStream x, long l) {}
        @Override public void updateBinaryStream(int i, java.io.InputStream x, long l) {}
        @Override public void updateCharacterStream(int i, java.io.Reader x, long l) {}
        @Override public void updateAsciiStream(String c, java.io.InputStream x, long l) {}
        @Override public void updateBinaryStream(String c, java.io.InputStream x, long l) {}
        @Override public void updateCharacterStream(String c, java.io.Reader r, long l) {}
        @Override public void updateBlob(int i, java.io.InputStream x, long l) {}
        @Override public void updateBlob(String c, java.io.InputStream x, long l) {}
        @Override public void updateClob(int i, java.io.Reader r, long l) {}
        @Override public void updateClob(String c, java.io.Reader r, long l) {}
        @Override public void updateNClob(int i, java.io.Reader r, long l) {}
        @Override public void updateNClob(String c, java.io.Reader r, long l) {}
        @Override public void updateNCharacterStream(int i, java.io.Reader x) {}
        @Override public void updateNCharacterStream(String c, java.io.Reader x) {}
        @Override public void updateAsciiStream(int i, java.io.InputStream x) {}
        @Override public void updateBinaryStream(int i, java.io.InputStream x) {}
        @Override public void updateCharacterStream(int i, java.io.Reader x) {}
        @Override public void updateAsciiStream(String c, java.io.InputStream x) {}
        @Override public void updateBinaryStream(String c, java.io.InputStream x) {}
        @Override public void updateCharacterStream(String c, java.io.Reader r) {}
        @Override public void updateBlob(int i, java.io.InputStream x) {}
        @Override public void updateBlob(String c, java.io.InputStream x) {}
        @Override public void updateClob(int i, java.io.Reader r) {}
        @Override public void updateClob(String c, java.io.Reader r) {}
        @Override public void updateNClob(int i, java.io.Reader r) {}
        @Override public void updateNClob(String c, java.io.Reader r) {}
        @Override public <T> T getObject(int i, Class<T> t) { throw new UnsupportedOperationException(); }
        @Override public <T> T getObject(String c, Class<T> t) { throw new UnsupportedOperationException(); }
        @Override public <T> T unwrap(Class<T> t) { throw new UnsupportedOperationException(); }
        @Override public boolean isWrapperFor(Class<?> t) { return false; }
    }

    static class FakePreparedStatement implements PreparedStatement {
        final String sql;
        final Object[] params;
        private int paramCount = 0;

        FakePreparedStatement(String sql, int maxParams) {
            this.sql = sql;
            this.params = new Object[maxParams];
        }

        @Override public void setObject(int i, Object v) { params[i - 1] = v; paramCount++; }
        @Override public ResultSet executeQuery() { return null; }
        @Override public void close() {}
        @Override public void clearParameters() {}

        // Stubs
        @Override public java.sql.ResultSet executeQuery(String s) { return null; }
        @Override public int executeUpdate(String s) { return 0; }
        @Override public int getMaxFieldSize() { return 0; }
        @Override public void setMaxFieldSize(int m) {}
        @Override public int getMaxRows() { return 0; }
        @Override public void setMaxRows(int m) {}
        @Override public void setEscapeProcessing(boolean e) {}
        @Override public int getQueryTimeout() { return 0; }
        @Override public void setQueryTimeout(int s) {}
        @Override public void cancel() {}
        @Override public java.sql.SQLWarning getWarnings() { return null; }
        @Override public void clearWarnings() {}
        @Override public void setCursorName(String n) {}
        @Override public boolean execute() { return false; }
        @Override public java.sql.ResultSet getResultSet() { return null; }
        @Override public int getUpdateCount() { return 0; }
        @Override public boolean getMoreResults() { return false; }
        @Override public void setFetchDirection(int d) {}
        @Override public int getFetchDirection() { return 0; }
        @Override public void setFetchSize(int s) {}
        @Override public int getFetchSize() { return 0; }
        @Override public int getResultSetConcurrency() { return 0; }
        @Override public int getResultSetType() { return 0; }
        @Override public void addBatch() {}
        @Override public void clearBatch() {}
        @Override public int[] executeBatch() { return new int[0]; }
        @Override public java.sql.Connection getConnection() { return null; }
        @Override public boolean getMoreResults(int c) { return false; }
        @Override public java.sql.ResultSet getGeneratedKeys() { return null; }
        @Override public int executeUpdate() { return 0; }
        @Override public boolean execute(String s) { return false; }
        @Override public int executeUpdate(String s, int a) { return 0; }
        @Override public int executeUpdate(String s, int[] c) { return 0; }
        @Override public int executeUpdate(String s, String[] c) { return 0; }
        @Override public boolean execute(String s, int a) { return false; }
        @Override public boolean execute(String s, int[] c) { return false; }
        @Override public boolean execute(String s, String[] c) { return false; }
        @Override public int getResultSetHoldability() { return 0; }
        @Override public boolean isClosed() { return false; }
        @Override public void setPoolable(boolean p) {}
        @Override public boolean isPoolable() { return false; }
        @Override public void closeOnCompletion() {}
        @Override public boolean isCloseOnCompletion() { return false; }
        @Override public <T> T unwrap(Class<T> t) { throw new UnsupportedOperationException(); }
        @Override public boolean isWrapperFor(Class<?> t) { return false; }
        @Override public void setNull(int i, int s) {}
        @Override public void setBoolean(int i, boolean x) {}
        @Override public void setByte(int i, byte x) {}
        @Override public void setShort(int i, short x) {}
        @Override public void setInt(int i, int x) {}
        @Override public void setLong(int i, long x) {}
        @Override public void setFloat(int i, float x) {}
        @Override public void setDouble(int i, double x) {}
        @Override public void setBigDecimal(int i, java.math.BigDecimal x) {}
        @Override public void setString(int i, String x) {}
        @Override public void setBytes(int i, byte[] x) {}
        @Override public void setDate(int i, java.sql.Date x) {}
        @Override public void setTime(int i, java.sql.Time x) {}
        @Override public void setTimestamp(int i, java.sql.Timestamp x) {}
        @Override public void setAsciiStream(int i, java.io.InputStream x, int l) {}
        @Override public void setBinaryStream(int i, java.io.InputStream x, int l) {}
        @Override public void setCharacterStream(int i, java.io.Reader r, int l) {}
        @Override public void setObject(int i, Object x, int t) {}
        @Override public void setObject(int i, Object x, int t, int s) {}
        @Override public void addBatch(String s) {}
        @Override public void setCharacterStream(int i, java.io.Reader r) {}
        @Override public void setRef(int i, java.sql.Ref x) {}
        @Override public void setBlob(int i, java.sql.Blob x) {}
        @Override public void setClob(int i, java.sql.Clob x) {}
        @Override public void setArray(int i, java.sql.Array x) {}
        @Override public java.sql.ResultSetMetaData getMetaData() { return null; }
        @Override public void setDate(int i, java.sql.Date x, java.util.Calendar c) {}
        @Override public void setTime(int i, java.sql.Time x, java.util.Calendar c) {}
        @Override public void setTimestamp(int i, java.sql.Timestamp x, java.util.Calendar c) {}
        @Override public void setNull(int i, int s, String t) {}
        @Override public void setURL(int i, java.net.URL x) {}
        @Override public java.sql.ParameterMetaData getParameterMetaData() { return null; }
        @Override public void setRowId(int i, java.sql.RowId x) {}
        @Override public void setNString(int i, String v) {}
        @Override public void setNClob(int i, java.sql.NClob v) {}
        @Override public void setClob(int i, java.io.Reader r, long l) {}
        @Override public void setBlob(int i, java.io.InputStream x, long l) {}
        @Override public void setNClob(int i, java.io.Reader r, long l) {}
        @Override public void setSQLXML(int i, java.sql.SQLXML x) {}
        @Override public void setObject(int i, Object x, java.sql.SQLType t, int s) {}
        @Override public void setObject(int i, Object x, java.sql.SQLType t) {}
        @Override public long executeLargeUpdate() { return 0; }
        @Override public void setAsciiStream(int i, java.io.InputStream x) {}
        @Override public void setUnicodeStream(int i, java.io.InputStream x, int l) {}
        @Override public void setBinaryStream(int i, java.io.InputStream x) {}
        @Override public void setBlob(int i, java.io.InputStream x) {}
        @Override public void setClob(int i, java.io.Reader r) {}
        @Override public void setNClob(int i, java.io.Reader r) {}
        @Override public void setAsciiStream(int i, java.io.InputStream x, long l) {}
        @Override public void setBinaryStream(int i, java.io.InputStream x, long l) {}
        @Override public void setCharacterStream(int i, java.io.Reader r, long l) {}
        @Override public void setNCharacterStream(int i, java.io.Reader r) {}
        @Override public void setNCharacterStream(int i, java.io.Reader r, long l) {}
    }

    static class FakeConnection implements Connection {
        private final List<Map<String, Object>> resultSetData;
        private final String expectedSQL;

        FakeConnection(String expectedSQL, List<Map<String, Object>> resultSetData) {
            this.expectedSQL = expectedSQL;
            this.resultSetData = resultSetData;
        }

        @Override
        public PreparedStatement prepareStatement(String sql) {
            assertNotNull(sql);
            return new FakePreparedStatement(sql, 10) {
                @Override
                public ResultSet executeQuery() {
                    return new FakeResultSet(resultSetData);
                }
            };
        }

        @Override public void close() {}
        @Override public Statement createStatement() { return null; }
        @Override public boolean isClosed() { return false; }
        @Override public DatabaseMetaData getMetaData() { return null; }
        @Override public void setAutoCommit(boolean a) {}
        @Override public boolean getAutoCommit() { return true; }
        @Override public void commit() {}
        @Override public void rollback() {}
        @Override public void setReadOnly(boolean r) {}
        @Override public boolean isReadOnly() { return false; }
        @Override public void setCatalog(String c) {}
        @Override public String getCatalog() { return null; }
        @Override public void setTransactionIsolation(int l) {}
        @Override public int getTransactionIsolation() { return 0; }
        @Override public java.sql.SQLWarning getWarnings() { return null; }
        @Override public void clearWarnings() {}
        @Override public Statement createStatement(int r, int c) { return null; }
        @Override public PreparedStatement prepareStatement(String s, int r, int c) { return null; }
        @Override public java.sql.CallableStatement prepareCall(String s) { return null; }
        @Override public java.sql.CallableStatement prepareCall(String s, int r, int c) { return null; }
        @Override public Map<String, Class<?>> getTypeMap() { return null; }
        @Override public void setTypeMap(Map<String, Class<?>> m) {}
        @Override public void setHoldability(int h) {}
        @Override public int getHoldability() { return 0; }
        @Override public Savepoint setSavepoint() { return null; }
        @Override public Savepoint setSavepoint(String n) { return null; }
        @Override public void rollback(Savepoint s) {}
        @Override public void releaseSavepoint(Savepoint s) {}
        @Override public Statement createStatement(int r, int c, int h) { return null; }
        @Override public PreparedStatement prepareStatement(String s, int r, int c, int h) { return null; }
        @Override public java.sql.CallableStatement prepareCall(String s, int r, int c, int h) { return null; }
        @Override public PreparedStatement prepareStatement(String s, int a) { return null; }
        @Override public PreparedStatement prepareStatement(String s, int[] c) { return null; }
        @Override public PreparedStatement prepareStatement(String s, String[] c) { return null; }
        @Override public java.sql.Clob createClob() { return null; }
        @Override public java.sql.Blob createBlob() { return null; }
        @Override public java.sql.NClob createNClob() { return null; }
        @Override public java.sql.SQLXML createSQLXML() { return null; }
        @Override public boolean isValid(int t) { return true; }
        @Override public void setClientInfo(String n, String v) {}
        @Override public void setClientInfo(java.util.Properties p) {}
        @Override public String getClientInfo(String n) { return null; }
        @Override public java.util.Properties getClientInfo() { return null; }
        @Override public java.sql.Array createArrayOf(String t, Object[] e) { return null; }
        @Override public java.sql.Struct createStruct(String t, Object[] a) { return null; }
        @Override public void setSchema(String s) {}
        @Override public String getSchema() { return null; }
        @Override public void abort(java.util.concurrent.Executor e) {}
        @Override public void setNetworkTimeout(java.util.concurrent.Executor e, int m) {}
        @Override public int getNetworkTimeout() { return 0; }
        @Override public String nativeSQL(String sql) { return sql; }
        @Override public <T> T unwrap(Class<T> t) { throw new UnsupportedOperationException(); }
        @Override public boolean isWrapperFor(Class<?> t) { return false; }
    }

    // ── Tests ─────────────────────────────────────────────────────

    @Test
    @DisplayName("HasMany: loads children and hydrates list")
    void hasManyLoadsChildren() throws Exception {
        List<Map<String, Object>> rows = List.of(
                Map.of("id", 10, "title", "Post A", "user_id", 1),
                Map.of("id", 20, "title", "Post B", "user_id", 1)
        );
        FakeConnection conn = new FakeConnection("posts", rows);
        RelationManager rm = new RelationManager(conn);

        TestUser user = new TestUser(1, "Alice");
        rm.eagerLoad(user, "posts");

        assertNotNull(user.getPosts());
        assertEquals(2, user.getPosts().size());
        assertEquals("Post A", user.getPosts().get(0).getTitle());
        assertEquals("Post B", user.getPosts().get(1).getTitle());
    }

    @Test
    @DisplayName("HasMany: empty result produces empty list")
    void hasManyEmptyResult() throws Exception {
        FakeConnection conn = new FakeConnection("posts", List.of());
        RelationManager rm = new RelationManager(conn);

        TestUser user = new TestUser(1, "Alice");
        rm.eagerLoad(user, "posts");

        assertNotNull(user.getPosts());
        assertTrue(user.getPosts().isEmpty());
    }

    @Test
    @DisplayName("BelongsTo: loads parent and hydrates field")
    void belongsToLoadsParent() throws Exception {
        List<Map<String, Object>> rows = List.of(
                Map.of("id", 42, "name", "Bob")
        );
        FakeConnection conn = new FakeConnection("users", rows);
        RelationManager rm = new RelationManager(conn);

        TestPost post = new TestPost(1, "Hello", 42);
        rm.eagerLoad(post, "user");

        assertNotNull(post.getUser());
        assertEquals(42, post.getUser().getId());
        assertEquals("Bob", post.getUser().getName());
    }

    @Test
    @DisplayName("BelongsTo: no result leaves field null")
    void belongsToNoResult() throws Exception {
        FakeConnection conn = new FakeConnection("users", List.of());
        RelationManager rm = new RelationManager(conn);

        TestPost post = new TestPost(1, "Hello", 99);
        rm.eagerLoad(post, "user");

        assertNull(post.getUser());
    }

    @Test
    @DisplayName("Batch HasMany: loads relations for multiple parents")
    void batchHasManyLoadsMultiple() throws Exception {
        List<Map<String, Object>> rows = List.of(
                Map.of("id", 10, "title", "A", "user_id", 1),
                Map.of("id", 20, "title", "B", "user_id", 2)
        );
        FakeConnection conn = new FakeConnection("posts", rows);
        RelationManager rm = new RelationManager(conn);

        TestUser u1 = new TestUser(1, "Alice");
        TestUser u2 = new TestUser(2, "Bob");
        rm.eagerLoadAll(List.of(u1, u2), "posts");

        assertEquals(1, u1.getPosts().size());
        assertEquals("A", u1.getPosts().get(0).getTitle());
        assertEquals(1, u2.getPosts().size());
        assertEquals("B", u2.getPosts().get(0).getTitle());
    }

    @Test
    @DisplayName("Batch BelongsTo: loads parents for multiple children")
    void batchBelongsToLoadsMultiple() throws Exception {
        List<Map<String, Object>> rows = List.of(
                Map.of("id", 10, "name", "Alice"),
                Map.of("id", 20, "name", "Bob")
        );
        FakeConnection conn = new FakeConnection("users", rows);
        RelationManager rm = new RelationManager(conn);

        TestPost p1 = new TestPost(1, "X", 10);
        TestPost p2 = new TestPost(2, "Y", 20);
        rm.eagerLoadAll(List.of(p1, p2), "user");

        assertNotNull(p1.getUser());
        assertEquals("Alice", p1.getUser().getName());
        assertNotNull(p2.getUser());
        assertEquals("Bob", p2.getUser().getName());
    }

    @Test
    @DisplayName("Null entity is handled gracefully")
    void nullEntityHandled() {
        FakeConnection conn = new FakeConnection("posts", List.of());
        RelationManager rm = new RelationManager(conn);
        assertDoesNotThrow(() -> rm.eagerLoad(null, "posts"));
    }

    @Test
    @DisplayName("Unknown relation field is handled gracefully")
    void unknownFieldHandled() {
        FakeConnection conn = new FakeConnection("posts", List.of());
        RelationManager rm = new RelationManager(conn);
        TestUser user = new TestUser(1, "Alice");
        assertDoesNotThrow(() -> rm.eagerLoad(user, "nonexistent"));
    }
}
