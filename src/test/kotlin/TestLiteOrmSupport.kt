import entity.Person
import org.junit.Test

/**
 * @author 杨晓辉 2018-06-29 17:08
 */


class TestLiteOrmSupport {

    private var person = Person()

    init {

        person.name = "zs"
        person.age = "18"
        person.sex = "n"
        person.ids = 1
    }

    @Test
    fun testSave() {
        val save = person.save()
        println(save)
    }


    @Test
    fun testFindOne() {
        val one = person.findOne()
        println(one)
    }

    @Test
    fun testFind() {
        val person = LiteOrmSupport.find(Person::class.java, 1)
        println(person.toString())
    }

    @Test
    fun testUpdate() {

        person.age="20"
        person.update()
    }
}


