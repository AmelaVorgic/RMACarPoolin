
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.rma_carpoolin.R
import com.example.rma_carpoolin.databinding.RideCellBinding
import com.example.rma_carpoolin.model.Ride
import java.util.*

class DisplayRideAdapter : RecyclerView.Adapter<DisplayRideAdapter.RideViewHolder>(), Filterable {
    private var rides: MutableList<Ride> = mutableListOf()
    private var filteredRides: MutableList<Ride> = mutableListOf()
    private var searchByArrival: Boolean = true


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ride_cell, parent, false)
        return RideViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RideViewHolder, position: Int) {
        val ride = filteredRides[position]
        holder.bind(ride)
    }

    override fun getItemCount(): Int {
        return filteredRides.size
    }

    fun setRides(rides: List<Ride>) {
        this.rides.clear()
        this.rides.addAll(rides)
        this.filteredRides.clear()
        this.filteredRides.addAll(rides)
        notifyDataSetChanged()
    }

    inner class RideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: RideCellBinding = RideCellBinding.bind(itemView)

        fun bind(ride: Ride) {
            binding.dateHolder.text = ride.date
            binding.arrivalHolder.text = ride.arrival
            binding.departureHolder.text = ride.departure
            binding.seatsHolder.text = ride.seats
            binding.rideNumberHolder.text = ride.number
        }
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Ride>()

                val query = constraint?.toString()?.trim()
                if (query.isNullOrEmpty()) {
                    filteredList.addAll(rides)
                } else {
                    for (ride in rides) {
                        if (searchByArrival && ride.arrival.contains(query, ignoreCase = true)) {
                            filteredList.add(ride)
                        } else if (!searchByArrival && ride.departure.contains(query, ignoreCase = true)) {
                            filteredList.add(ride)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredRides.clear()
                filteredRides.addAll(results?.values as List<Ride>)
                notifyDataSetChanged()
            }
        }
    }

    fun setSearchByArrival(searchByArrival: Boolean) {
        this.searchByArrival = searchByArrival
        filter.filter(null)
    }
}

