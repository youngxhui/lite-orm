package util

import com.mchange.v2.c3p0.ComboPooledDataSource
import java.sql.Connection
import javax.sql.DataSource

/**
 * @author 杨晓辉 2018-06-29 16:52
 */
object JDBCUtils {
    var ds: DataSource = ComboPooledDataSource()

    /**
     * 获取链接
     */
    fun getConnection(): Connection {
        return ds.connection
    }

    fun closeConnection() {
        val connection = getConnection()
        connection.close()
    }
}

fun main(args: Array<String>) {
    println("jdbc is ${JDBCUtils.getConnection()}")
}