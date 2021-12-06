package sheridan.teodored.myapplication.schedule

import DataObjects.Obligation
import DataObjects.TimeChunk
import Database.AppDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import sheridan.teodored.myapplication.R
import sheridan.teodored.myapplication.databinding.FragmentCalendarBinding
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var binding : FragmentCalendarBinding;
    private lateinit var auth : FirebaseAuth;

    private var currentDate : Int = 0;
    private var currentMonth : Int = 0;
    private var currentYear : Int = 0;
    private var currentDay : Int = 0;

    private var numbersToDisplay : ArrayList<String> = arrayListOf<String>()
    private var statuses : ArrayList<Status> = arrayListOf<Status>()

    private var selectedDate : Int = 0;
    private var selectedMonth : Int = 0;
    private var selectedYear : Int = 0;

    private var startOfMonth : Int = 0

    private var obligations : List<Obligation> = listOf()
    private var timeChunks : List<TimeChunk> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        auth = FirebaseAuth.getInstance()
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        if(auth.currentUser == null){
            LogOut()
        }

        val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "Schedule").fallbackToDestructiveMigration().allowMainThreadQueries().build()

        obligations = db.obligationDao().getAll()
        timeChunks = db.timechunkDao().getAll()

        val currentDateRaw = Date()

        currentDate = currentDateRaw.date
        currentMonth = currentDateRaw.month
        currentYear = currentDateRaw.year
        currentDay = currentDateRaw.day

        selectedDate = currentDate
        selectedMonth = currentMonth
        selectedYear = currentYear

        binding.ScheduleCalendarRecyclerView.adapter = CalendarViewAdapter(requireContext(), numbersToDisplay)
        binding.ScheduleCalendarRecyclerView.layoutManager = GridLayoutManager(requireContext(),7)

        (binding.ScheduleCalendarRecyclerView.adapter!! as CalendarViewAdapter).setInitialDate(currentDate, currentMonth, currentYear, ::UpdateDate)

        RenderCalendar(currentDate, currentMonth, currentYear, forward = false, init = true);

        binding.ScheduleBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ScheduleLogOutButton.setOnClickListener {
            LogOut()
        }

        binding.ScheduleMonthBackButton.setOnClickListener {
            if(selectedMonth == 0) {
                selectedYear -= 1
                selectedMonth = 11
            }
            else selectedMonth -= 1
            RenderCalendar(selectedDate, selectedMonth, selectedYear, forward = false)
        }

        binding.ScheduleMonthForwardButton.setOnClickListener {
            if(selectedMonth == 11) {
                selectedYear += 1
                selectedMonth = 0
            }
            else selectedMonth += 1
            RenderCalendar(selectedDate, selectedMonth, selectedYear, forward = true)
        }

        binding.ScheduleAddEventButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("New", true)
            bundle.putInt("Year", selectedYear)
            bundle.putInt("Month", selectedMonth)
            bundle.putInt("Date", selectedDate)
            findNavController().navigate(R.id.action_calendar_to_calendar_add_event, bundle)
        }

        return binding.root
    }

    fun UpdateDate(date : Int){
        if (date - startOfMonth + 1 > 0) selectedDate = date
    }

    fun RenderCalendar(day : Int, month : Int, year: Int, forward: Boolean, init : Boolean = false){
        val helper = DateHelper()

        startOfMonth = 0

        var numberOfDays =
            if(arrayListOf(0,2,4,6,8,9,11).contains(month)) 31
            else if(arrayListOf(3,5,7,10).contains(month)) 30
            else if(month == 1 && year % 4 == 0 && (year - 100) % 400 == 0 && (year - 100) % 100 == 0) 29
            else 28

        startOfMonth =
            if (init) (currentDay - currentDate + 14) % 7 + 1
            else {
                if(forward) (numbersToDisplay.count()) % 7
                else (numbersToDisplay.indexOf("1") - numberOfDays + 35) % 7
            }

        val count = numbersToDisplay.count()
        while (numbersToDisplay.isNotEmpty()) numbersToDisplay.removeLast()

        binding.ScheduleCalendarRecyclerView.adapter!!.notifyItemRangeRemoved(0,count)

        while (statuses.isNotEmpty()) statuses.removeLast()

        for(i in 1..startOfMonth) numbersToDisplay.add("")
        for(i in 1..numberOfDays) numbersToDisplay.add(i.toString())
        for(i in 0..(numbersToDisplay.count()-1)) {
            val status = Status()
            status.Current = day + startOfMonth - 1 == i && month == currentMonth && year == currentYear
            status.Selected = day + startOfMonth -1 == i
            status.Done = false
            status.Schedule = false
            status.Warning = false
            statuses.add(status)
        }

        (binding.ScheduleCalendarRecyclerView.adapter!! as CalendarViewAdapter).updateStatuses(statuses)

        binding.ScheduleCalendarRecyclerView.adapter!!.notifyItemRangeInserted(0, numbersToDisplay.count())

        binding.ScheduleMonthTitle.text = helper.IntMonthToString(selectedMonth) + " " + helper.IntYearToReadable(selectedYear).toString()
    }

    private fun LogOut() {
        if(auth.currentUser != null){
            auth.signOut()
        }
        findNavController().navigateUp()
        findNavController().navigateUp()
    }
}

class Status {
    var Current : Boolean = false
    var Warning : Boolean = false
    var Schedule : Boolean = false
    var Done : Boolean = false
    var Selected : Boolean = false

    override fun toString(): String {
        return "Current : " + Current.toString() + ", Warning : " + Warning.toString() + ", Schedule : " + Schedule.toString() + ", Done : " + Done.toString()
    }
}