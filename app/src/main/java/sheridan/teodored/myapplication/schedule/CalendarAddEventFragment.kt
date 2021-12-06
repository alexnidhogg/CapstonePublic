package sheridan.teodored.myapplication.schedule

import DataObjects.Obligation
import Database.AppDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_calendar_add_event.*
import sheridan.teodored.myapplication.databinding.FragmentCalendarAddEventBinding


class CalendarAddEventFragment : Fragment() {

    private lateinit var binding : FragmentCalendarAddEventBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : AppDatabase

    private var doOnce : Boolean = true;

    private var year : Int = 0;
    private var month : Int = 0;
    private var date : Int = 0;

    private var repeat : String = "Never";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarAddEventBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "ScheduleAddEvent").fallbackToDestructiveMigration().allowMainThreadQueries().build()

        if(requireArguments().get("New") == true){
            binding.ScheduleNewActivity.isChecked = true
            binding.ScheduleNewNeverRadio.isChecked = true

            binding.ScheduleNewExpectedTime.visibility = View.INVISIBLE
            binding.ScheduleNewExpectedDurationInput.visibility = View.INVISIBLE
            binding.ScheduleNewInstructions.visibility = View.INVISIBLE

            binding.ScheduleNewTimeLabel.text = "Start"

            year = requireArguments().getInt("Year")
            month = requireArguments().getInt("Month")
            date = requireArguments().getInt("Date")
        }

        if(auth!!.currentUser == null){
            LogOut()
        }

        binding.ScheduleNewActivity.setOnClickListener {
            binding.ScheduleNewExpectedTime.visibility = View.INVISIBLE
            binding.ScheduleNewExpectedDurationInput.visibility = View.INVISIBLE
            binding.ScheduleNewInstructions.visibility = View.INVISIBLE

            binding.ScheduleNewTimeLabel.text = "Start"

            binding.ScheduleNewEndLabel.visibility = View.VISIBLE
            binding.ScheduleNewEndInput.visibility = View.VISIBLE
            binding.ScheduleNewDurationInput.visibility = View.VISIBLE
            binding.ScheduleNewDurationLabel.visibility = View.VISIBLE
            binding.ScheduleNewRepeatRadio.visibility = View.VISIBLE
            binding.ScheduleNewRepeatLabel.visibility = View.VISIBLE
        }

        binding.ScheduleNewAssignment.setOnClickListener {
            binding.ScheduleNewExpectedTime.visibility = View.VISIBLE
            binding.ScheduleNewExpectedDurationInput.visibility = View.VISIBLE
            binding.ScheduleNewInstructions.visibility = View.VISIBLE

            binding.ScheduleNewTimeLabel.text = "Time Due"

            binding.ScheduleNewEndLabel.visibility = View.INVISIBLE
            binding.ScheduleNewEndInput.visibility = View.INVISIBLE
            binding.ScheduleNewDurationInput.visibility = View.INVISIBLE
            binding.ScheduleNewDurationLabel.visibility = View.INVISIBLE
            binding.ScheduleNewRepeatRadio.visibility = View.INVISIBLE
            binding.ScheduleNewRepeatLabel.visibility = View.INVISIBLE
        }

        binding.ScheduleNewDailyRadio.setOnClickListener {
            binding.ScheduleNewDailyRadio.isChecked = true
            binding.ScheduleNewMonthlyRadio.isChecked = false
            binding.ScheduleNewWeeklyRadio.isChecked = false
            binding.ScheduleNewYearlyRadio.isChecked = false
            binding.ScheduleNewBiWeeklyRadio.isChecked = false
            binding.ScheduleNewNeverRadio.isChecked = false

            repeat = "Daily"
        }

        binding.ScheduleNewMonthlyRadio.setOnClickListener {
            binding.ScheduleNewDailyRadio.isChecked = false
            binding.ScheduleNewMonthlyRadio.isChecked = true
            binding.ScheduleNewWeeklyRadio.isChecked = false
            binding.ScheduleNewYearlyRadio.isChecked = false
            binding.ScheduleNewBiWeeklyRadio.isChecked = false
            binding.ScheduleNewNeverRadio.isChecked = false

            repeat = "Monthly"
        }

        binding.ScheduleNewWeeklyRadio.setOnClickListener {
            binding.ScheduleNewDailyRadio.isChecked = false
            binding.ScheduleNewMonthlyRadio.isChecked = false
            binding.ScheduleNewWeeklyRadio.isChecked = true
            binding.ScheduleNewYearlyRadio.isChecked = false
            binding.ScheduleNewBiWeeklyRadio.isChecked = false
            binding.ScheduleNewNeverRadio.isChecked = false

            repeat = "Weekly"
        }

        binding.ScheduleNewYearlyRadio.setOnClickListener {
            binding.ScheduleNewDailyRadio.isChecked = false
            binding.ScheduleNewMonthlyRadio.isChecked = false
            binding.ScheduleNewWeeklyRadio.isChecked = false
            binding.ScheduleNewYearlyRadio.isChecked = true
            binding.ScheduleNewBiWeeklyRadio.isChecked = false
            binding.ScheduleNewNeverRadio.isChecked = false

            repeat = "Yearly"
        }

        binding.ScheduleNewBiWeeklyRadio.setOnClickListener {
            binding.ScheduleNewDailyRadio.isChecked = false
            binding.ScheduleNewMonthlyRadio.isChecked = false
            binding.ScheduleNewWeeklyRadio.isChecked = false
            binding.ScheduleNewYearlyRadio.isChecked = false
            binding.ScheduleNewBiWeeklyRadio.isChecked = true
            binding.ScheduleNewNeverRadio.isChecked = false

            repeat = "Bi-Weekly"
        }

        binding.ScheduleNewNeverRadio.setOnClickListener {
            binding.ScheduleNewDailyRadio.isChecked = false
            binding.ScheduleNewMonthlyRadio.isChecked = false
            binding.ScheduleNewWeeklyRadio.isChecked = false
            binding.ScheduleNewYearlyRadio.isChecked = false
            binding.ScheduleNewBiWeeklyRadio.isChecked = false
            binding.ScheduleNewNeverRadio.isChecked = true

            repeat = "Never"
        }

        binding.ScheduleNewSaveButton.setOnClickListener {
            if(binding.ScheduleNewActivity.isChecked){
                //ACTIVITY SAVE
                db.obligationDao().insertAll(
                    Obligation(
                        uid = 0,
                        name = binding.ScheduleNewNameInput.text.toString(),
                        description = binding.ScheduleNewDescriptionInput.text.toString(),
                        dueYear = year,
                        dueMonth = month,
                        dueDay = date,
                        done = false,
                        timeEstimate = (binding.ScheduleNewExpectedDurationInput.text.toString().toFloat() * 60).toInt(),
                        repeat = repeat,
                        days = 0,
                        movable = false
                    )
                )

            } else {
                //ASSIGNMENT SAVE
            }
        }

        binding.ScheduleNewCancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    fun LogOut() {
        if(auth!!.currentUser != null){
            auth.signOut()
        }
        findNavController().navigateUp()
        findNavController().navigateUp()
    }

}