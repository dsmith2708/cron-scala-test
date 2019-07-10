import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

object CronMain {


  def getHourMinuteFromConsole(firstArg: String): Array[Int] = {
      firstArg.split(":").map( stringVal => if(stringVal== "*") 100 else stringVal.toInt)
  }

  def main(args: Array[String]): Unit = {

    args.foreach(arg => println(arg))

    val curHourMinute = getHourMinuteFromConsole(args(0))
    curHourMinute.foreach(value => println(value))
    val curCalendar = Calendar.getInstance()
    if(curHourMinute(0) != 100) curCalendar.set(Calendar.HOUR, curHourMinute(0))
    if(curHourMinute(1) != 100) curCalendar.set(Calendar.MINUTE, curHourMinute(1))






  }

}
