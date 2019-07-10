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

  def splitRecordsAndOutput(configFileRecords: List[String]): Unit = {
    configFileRecords.foreach(record => {
      println("ITEMM")
      val splitRecord = record.split(" ")
      outputWhenNextRun(splitRecord)
    })
  }

  def outputWhenNextRun(configRecord: Array[String]): Unit = {
      configRecord match {
        case x if x(2) == "/bin/run_me_daily" => {
          println("run daily")
        }
        case x if x(2) == "/bin/run_me_hourly" => {
          println("run hourly")
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
    if(curHourMinute(0) != 100) curCalendar.set(Calendar.HOUR, curHourMinute(0))
    if(curHourMinute(1) != 100) curCalendar.set(Calendar.MINUTE, curHourMinute(1))

    splitRecordsAndOutput(getFile(args(1)).getLines.toList)



  }

}
