import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import scala.io.{BufferedSource, Source}

object CronMain {


  def getHourMinuteFromConsole(firstArg: String): Array[Int] = {
      firstArg.split(":").map( stringVal => if(stringVal== "*") 100 else stringVal.toInt)
  }

  def getFile(fileName: String): BufferedSource = {
    Source.fromFile(fileName)
  }

  def splitRecordsAndOutput(configFileRecords: List[String], curCalendar: Calendar): Unit = {
    configFileRecords.foreach(record => {
      println("ITEMM")
      val splitRecord = record.split(" ")
      outputWhenNextRun(splitRecord, curCalendar)
    })
  }

  def outputWhenNextRun(configRecord: Array[String], curCalendar: Calendar): Unit = {
      configRecord match {
        case x if x(2) == "/bin/run_me_daily" => {
          val timeToRunToday = Calendar.getInstance()

          timeToRunToday.set(Calendar.HOUR, x(1).toInt)
          timeToRunToday.set(Calendar.MINUTE, x(0).toInt)
          if(timeToRunToday.before(curCalendar)) {
            timeToRunToday.add(Calendar.DAY_OF_MONTH, 1)
            println(s"${timeToRunToday.get(Calendar.HOUR_OF_DAY)}:${timeToRunToday.get(Calendar.MINUTE)} Tomorrow - ${configRecord(2)}")
          } else {
            timeToRunToday.add(Calendar.DAY_OF_MONTH, 1)
            println(s"${timeToRunToday.get(Calendar.HOUR_OF_DAY)}:${timeToRunToday.get(Calendar.MINUTE)} Today - ${configRecord(2)}")
          }
        }
        case x if x(2) == "/bin/run_me_hourly" => {
          val timeOfNextRun = Calendar.getInstance()
        }
        case x if x(2) == "/bin/run_every_minute" => {
          println("run every minute")
        }
        case x if x(2) == "/bin/run_me_sixty_times" => {
          println("run 60 times at specified time")
        }
        case _ => println("error")
      }
  }


  def main(args: Array[String]): Unit = {

    val curHourMinute = getHourMinuteFromConsole(args(0))
    val curCalendar = Calendar.getInstance()
    if(curHourMinute(0) != 100) curCalendar.set(Calendar.HOUR_OF_DAY, curHourMinute(0))
    if(curHourMinute(1) != 100) curCalendar.set(Calendar.MINUTE, curHourMinute(1))


    println(curCalendar.getTime)


    splitRecordsAndOutput(getFile(args(1)).getLines.toList, curCalendar)



  }

}
