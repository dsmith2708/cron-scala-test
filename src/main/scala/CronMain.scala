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
      val splitRecord = record.split(" ")
      outputWhenNextRun(splitRecord, curCalendar)
    })
  }

  def outputWhenNextRun(configRecord: Array[String], curCalendar: Calendar): Unit = {
    configRecord match {
      case x if x(2) == "/bin/run_me_daily" => {
        val timeToRunToday = Calendar.getInstance()
        timeToRunToday.set(Calendar.HOUR_OF_DAY, x(1).toInt)
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
        timeOfNextRun.set(Calendar.MINUTE, x(0).toInt)
        if(timeOfNextRun.before(curCalendar)) {
          timeOfNextRun.add(Calendar.HOUR_OF_DAY, 1)
          if(timeOfNextRun.get(Calendar.DAY_OF_MONTH) == curCalendar.get(Calendar.DAY_OF_MONTH)) {
            println(s"${timeOfNextRun.get(Calendar.HOUR_OF_DAY)}:${timeOfNextRun.get(Calendar.MINUTE)} Today - ${configRecord(2)}")
          } else {
            println(s"${timeOfNextRun.get(Calendar.HOUR_OF_DAY)}:${timeOfNextRun.get(Calendar.MINUTE)} Tomorrow - ${configRecord(2)}")
          }
        } else {
          println(s"${timeOfNextRun.get(Calendar.HOUR_OF_DAY)}:${timeOfNextRun.get(Calendar.MINUTE)} Today - ${configRecord(2)}")
        }

      }
      case x if x(2) == "/bin/run_every_minute" => {
        val timeOfNextRun: Calendar = curCalendar.clone().asInstanceOf[Calendar]
        timeOfNextRun.add(Calendar.MINUTE, 1)
        if(timeOfNextRun.get(Calendar.DAY_OF_MONTH) == curCalendar.get(Calendar.DAY_OF_MONTH)) {
          println(s"${timeOfNextRun.get(Calendar.HOUR_OF_DAY)}:${timeOfNextRun.get(Calendar.MINUTE)} Today - ${configRecord(2)}")
        } else {
          println(s"${timeOfNextRun.get(Calendar.HOUR_OF_DAY)}:${timeOfNextRun.get(Calendar.MINUTE)} Tomorrow - ${configRecord(2)}")
        }
      }
      case x if x(2) == "/bin/run_me_sixty_times" => {
        val timeToRunToday = Calendar.getInstance()
        timeToRunToday.set(Calendar.HOUR_OF_DAY, x(1).toInt)
        timeToRunToday.set(Calendar.MINUTE, 0)
        if(timeToRunToday.before(curCalendar)) {
          timeToRunToday.add(Calendar.DAY_OF_MONTH, 1)
          println(String.format("%02d:%02d", timeToRunToday.get(Calendar.HOUR_OF_DAY), timeToRunToday.get(Calendar.MINUTE)) + " Tomorrow - " + configRecord(2))
        } else {
          timeToRunToday.add(Calendar.DAY_OF_MONTH, 1)
          println(String.format("%02d:%02d", timeToRunToday.get(Calendar.HOUR_OF_DAY), timeToRunToday.get(Calendar.MINUTE)) + " Today - " + configRecord(2))
        }
      }
      case _ => println("error, specified script in bin not recognised")
    }
  }

  def main(args: Array[String]): Unit = {

    val curHourMinute = getHourMinuteFromConsole(args(0))
    val curCalendar = Calendar.getInstance()
    if(curHourMinute(0) != 100) curCalendar.set(Calendar.HOUR_OF_DAY, curHourMinute(0))
    if(curHourMinute(1) != 100) curCalendar.set(Calendar.MINUTE, curHourMinute(1))

    splitRecordsAndOutput(getFile(args(1)).getLines.toList, curCalendar)
  }

}
