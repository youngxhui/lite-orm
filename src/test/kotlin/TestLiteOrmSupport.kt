import entity.Person
import org.junit.Test

/**
 * @author 杨晓辉 2018-06-29 17:08
 */


class TestLiteOrmSupport {

    private var person = Person()
    private var animal = Animal()

    init {

        person.name = "张三"
        person.age = "18"
        person.sex = "男"
        person.ids = 1
        animal.id = 1
        animal.type = "dog"

    }

    @Test
    fun testSave() {
        person.save()
    }


    @Test
    fun testFindOne(){
        person.findOne()
    }
}



class Animal() : LiteOrmSupport() {
    var id: Int = 0
    lateinit var type: String
    var age: Long = 1
    var float: Float = 0f
    var double: Double = 0.0
    var char: Char = '1'
    var byte: Byte = 2
    var num: Number = 9

}