package sheridan.teodored.myapplication.schedule

class DateHelper {

    fun IntYearToReadable(year : Int) : Int {
        return year + 1900;
    }

    fun IntMonthToString(month : Int, short : Boolean = false) : String {
        when (month) {
            0 -> {
                return if (short) "Jan"
                else "January"
            }
            1 -> {
                return if (short) "Feb"
                else "February"
            }
            2 -> {
                return if (short) "Mar"
                else "March"

            }
            3 -> {
                return if (short) "Apr"
                else "April"
            }
            4 -> return "May"
            5 -> {
                return if (short) "Jun"
                else "June"
            }
            6 -> {
                return if (short) "Jul"
                else "July"
            }
            7 -> {
                return if (short) "Aug"
                else "August"
            }
            8 -> {
                return if (short) "Sep"
                else "September"
            }
            9 -> {
                return if (short) "Oct"
                else "October"
            }
            10 -> {
                return if (short) "Nov"
                else "November"
            }
            else -> {
                return if (short) "Dec"
                else "December"
            }
        }
    }

}