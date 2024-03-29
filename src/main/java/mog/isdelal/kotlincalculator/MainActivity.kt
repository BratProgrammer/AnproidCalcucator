package mog.isdelal.kotlincalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mog.isdelal.kotlincalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Ввод строки примера
        binding.button0.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + "0" }
        binding.button1.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + "1" }
        binding.button2.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + "2" }
        binding.button3.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + "3" }
        binding.button4.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + "4" }
        binding.button5.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + "5" }
        binding.button6.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + "6" }
        binding.button7.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + "7" }
        binding.button8.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + "8" }
        binding.button9.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + "9" }
        binding.clearButton.setOnClickListener {
            binding.resultTextView.text = ""
            binding.taskTextView.text = ""
        }
        binding.backSpace.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString().dropLast(1) }
        binding.divisionButton.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + " / " }
        binding.multiplicationButton.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + " * " }
        binding.deductionButton.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + " - " }
        binding.additionButton.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + " + " }
        binding.dotButton.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + "." }
        binding.openBracket.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + "( " }
        binding.closeBacked.setOnClickListener { binding.taskTextView.text = binding.taskTextView.text.toString() + " )" }

        //обработка нажатия на =
        binding.resultButton.setOnClickListener {

            try {
                // переводим строку примера в ArrayList<String>, разделяя её по пробелам и удаляя их.
                val list: ArrayList<String> = arrayListOf()
                val string = binding.taskTextView.text.toString().trim()
                var lastIndex = 0
                for ((index, value) in string.withIndex()) {
                    var stringPath = ""
                    if (value == ' ') {
                        while (lastIndex < index) {
                            stringPath += string[lastIndex]
                            lastIndex++
                        }
                        list.add(stringPath)
                        lastIndex = index + 1

                    }
                }
                list.add(string.drop(lastIndex))
                binding.resultTextView.text = buildAllFun(list)
            } catch (e: Exception) {
                binding.taskTextView.text = ""
                binding.resultTextView.text = "Ошибка ввода"
            }

        }
    }
    // функция buildAllFun проверяет на наличие () в присере и передаёт его дальше в taskInTask, если () есть, или сразу в result, если их нет.
    private fun buildAllFun(arr: ArrayList<String>): String {
        if (binding.taskTextView.text.indexOf('(') != -1 || binding.taskTextView.text.indexOf(')') != -1) {
            var quantityOpen = 0
            var quantityClose = 0
            for (i in binding.taskTextView.text) {
                if (i == '(') {
                    quantityOpen++
                } else if (i == ')') {
                    quantityClose++
                }
            }
            return if (quantityClose != quantityOpen) {
                "Ошибка ввода"
            } else {
                taskInTask(arr)[0]
            }
        } else {
            return result(arr)[0]
        }
    }
    // функция taskInTask рекурсивно находит текст простого примера в (), который надо выполнит первым, и исходя из полученного результата, выполняет остальные действия, выходя из рекурсии.
    private fun taskInTask(arr: ArrayList<String>): ArrayList<String> {
        var indexFirst = 0
        var indexLast = 0
        for ((index, value) in arr.withIndex()) {
            if (value == "(") {
                indexFirst = index
            }
        }
        for ((index, value) in arr.withIndex()) {
            if (value == ")" && index > indexFirst) {
                indexLast = index
                break
            }
        }

        indexFirst++
        var taskInTaskList: ArrayList<String> = arrayListOf()
        while (indexFirst < indexLast) {
            taskInTaskList.add(arr[indexFirst])
            indexFirst++
        }

        val result: String = result(taskInTaskList)[0]
        taskInTaskList = arr

        for ((index, value) in arr.withIndex()) {
            if (value == "(") {
                indexFirst = index
            }
        }

        taskInTaskList.add(indexFirst++, result)
        indexLast++

        while (indexLast >= indexFirst) {
            taskInTaskList.removeAt(indexLast)
            indexLast--
        }

        return if (taskInTaskList.indexOf("(") != -1) {
            taskInTask(taskInTaskList)
        } else {
            result(taskInTaskList)
        }
    }
    // функция result выполняет счёт простых примеров без () например: "5 + 5 * 5". Возвращает список с одним элементом(ответом).
    private fun result(arr: ArrayList<String>): ArrayList<String> {
        var a: Double
        var b: Double
        while (arr.size != 1) {
            if (arr.indexOf("/") != -1 || arr.indexOf("*") != -1 || arr.indexOf("-") != -1 || arr.indexOf("+") != -1) {

                for ((index, value) in arr.withIndex()) {
                    if (value == "*") {
                        a = arr[index - 1].toDouble()
                        b = arr[index + 1].toDouble()
                        arr.add(index - 1, (a * b).toString())
                        arr.removeAt(index + 2)
                        arr.removeAt(index + 1)
                        arr.removeAt(index)
                        break
                    }
                    if (value == "/") {
                        a = arr[index - 1].toDouble()
                        b = arr[index + 1].toDouble()
                        arr.add(index - 1, (a / b).toString())
                        arr.removeAt(index + 2)
                        arr.removeAt(index + 1)
                        arr.removeAt(index)
                        break
                    }
                    if (arr.indexOf("/") == -1 && arr.indexOf("*") == -1 && (arr.indexOf("-") != -1 || arr.indexOf("+") != -1)) {

                        if (value == "+") {
                            a = arr[index - 1].toDouble()
                            b = arr[index + 1].toDouble()
                            arr.add(index - 1, (a + b).toString())
                            arr.removeAt(index + 2)
                            arr.removeAt(index + 1)
                            arr.removeAt(index)
                            break
                        }
                        if (value == "-") {
                            a = arr[index - 1].toDouble()
                            b = arr[index + 1].toDouble()
                            arr.add(index - 1, (a - b).toString())
                            arr.removeAt(index + 2)
                            arr.removeAt(index + 1)
                            arr.removeAt(index)
                            break
                        }
                    }
                }
            }
        }
        if (arr[0].toDouble() % 1 == 0.0) {
            arr[0] = arr[0].toDouble().toInt().toString()
        }
        return arr
    }
}