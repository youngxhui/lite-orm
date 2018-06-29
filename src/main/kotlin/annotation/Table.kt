package annotation

/**
 * @author 杨晓辉 2018-06-29 21:23
 */

/**
 * 表名注解
 */
@Repeatable
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Entity(val name: String)

/**
 * 主键注解
 */
@Repeatable
@Target(AnnotationTarget.FIELD)
annotation class PrimaryKey


