package annotation

/**
 * @author 杨晓辉 2018-06-29 21:23
 */

/**
 *
 */
@Repeatable
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Entity(val name: String)

/**
 *
 */
@Repeatable
@Target(AnnotationTarget.FIELD)
annotation class PrimaryKey


