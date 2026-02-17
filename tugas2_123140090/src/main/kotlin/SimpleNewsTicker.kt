import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Scanner

data class TickerItem(
    val id: Int,
    val headline: String,
    val category: String,
    val summary: String,
    val source: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)

class NewsTickerApp {
    private val processedItemsCount = MutableStateFlow(0)
    private val activeFilter = MutableStateFlow<String?>(null)
    private val timeDisplay = DateTimeFormatter.ofPattern("HH:mm:ss")

    private val contentPool = listOf(
        TickerItem(1, "New Solar Battery Tech Unveiled", "SciTech", "Efficiency jumps to 45% in lab tests.", "ScienceDaily"),
        TickerItem(2, "Lakers Secure Playoff Spot", "Sports", "LeBron leads with triple-double performance.", "ESPN"),
        TickerItem(3, "Crypto Market Volatility Returns", "Finance", "Bitcoin swings by 10% in single day.", "Bloomberg"),
        TickerItem(4, "Jakarta Traffic Policy Overhaul", "Local", "Governor proposes new odd-even expansion.", "Detik"),
        TickerItem(5, "New Marvel Movie Teaser Drops", "Ent", "Fans speculate on easter eggs in 2-minute clip.", "IGN"),
        TickerItem(6, "Global Rice Prices Stabilize", "Business", "Supply chains recover after harvest season.", "Reuters"),
        TickerItem(7, "SpaceX Starship Launch Success", "SciTech", "Booster landed perfectly on the pad.", "Space.com"),
        TickerItem(8, "Man Utd in Transfer Talks", "Sports", "Targeting young striker from Serie A.", "SkySports"),
        TickerItem(9, "Inflation Rate Hits 3-Year Low", "Finance", "Central bank hints at rate cuts.", "CNBC"),
        TickerItem(10, "Pop Star Concert Cancelled", "Ent", "Health issues cited for sudden cancellation.", "Billboard"),
        TickerItem(11, "Bandung Smart City Award", "Local", "City recognized for digital innovation.", "Antara"),
        TickerItem(12, "AI Act Passed in EU", "Politics", "Regulations set to start next year.", "Euractiv"),
        TickerItem(13, "Python 3.13 Features Leaked", "SciTech", "JIT compiler performance boosts discussed.", "RealPython"),
        TickerItem(14, "F1: Ferrari Pole Position", "Sports", "Leclerc dominates qualifying session.", "Autosport"),
        TickerItem(15, "Oil Prices Dip on Supply Data", "Business", "OPEC+ meeting outcome uncertain.", "OilPrice"),
        TickerItem(16, "Viral TikTok Trend Warnings", "Lifestyle", "Doctors advise against dangerous challenge.", "Healthline"),
        TickerItem(17, "New MRT Line Construction Begins", "Local", "Phase 3 to connect East and West.", "Kompas"),
        TickerItem(18, "Quantum Computing Breakthrough", "SciTech", "Researchers sustain qubit state for 5 seconds.", "Nature"),
        TickerItem(19, "NBA Finals Schedule Released", "Sports", "Game 1 starts next Thursday.", "NBA.com"),
        TickerItem(20, "Tech Giant Layoffs Continue", "Business", "Another 5,000 jobs cut in restructuring.", "TechCrunch"),
        TickerItem(21, "Indie Game Awards 2026", "Ent", "'Hollow Knight: Silksong' finally wins GOTY.", "Polygon"),
        TickerItem(22, "Climate Summit Conclusions", "Politics", "Nations agree to stricter carbon caps.", "UN News"),
        TickerItem(23, "New Cafe Hype in South Jakarta", "Lifestyle", "Queue lines reach 2 hours for coffee.", "Manual Jakarta"),
        TickerItem(24, "Electric Vehicle Sales Flatline", "Auto", "Market saturation or charging infrastructure issues?", "MotorTrend"),
        TickerItem(25, "5G Expansion in Rural Areas", "SciTech", "Government project reaches 50% milestone.", "Kominfo")
    )

    fun streamContent(): Flow<TickerItem> = flow {
        var index = 0
        while (true) {
            emit(contentPool[index % contentPool.size].copy(timestamp = LocalDateTime.now()))
            index++
            delay(1500)
        }
    }

    suspend fun fetchDetailedView(id: Int): String {
        delay(600)
        val item = contentPool.find { it.id == id } ?: return "Err: Item $id missing."
        return """
            #############################################
            # SOURCE: ${item.source} | CAT: ${item.category}
            # HEADLINE: ${item.headline}
            # -------------------------------------------
            # ${item.summary}
            #############################################
        """.trimIndent()
    }

    private suspend fun runStream(filter: String?) {
        streamContent()
            .filter { filter == null || it.category.equals(filter, true) }
            .map { 
                val time = it.timestamp.format(timeDisplay)
                "[$time] (${it.source}) ${it.headline}" 
            }
            .collect {
                processedItemsCount.value++
                println(it)
            }
    }

    suspend fun launchApp() = coroutineScope {
        println("\n*** DIGITAL NEWS TICKER v2.0 ***\n")
        printMenu()

        var job = launch { runStream(null) }

        // Background stats runner
        launch {
            processedItemsCount.collect { 
                if (it > 0 && it % 10 == 0) println("\n[System] Processed $it items...\n") 
            }
        }

        withContext(Dispatchers.IO) {
            val inputScanner = Scanner(System.`in`)
            while (isActive && inputScanner.hasNext()) {
                val cmd = inputScanner.next()
                when (cmd.lowercase()) {
                    "all" -> switchChannel(null).also { job.cancel(); job = it }
                    "tech" -> switchChannel("SciTech").also { job.cancel(); job = it }
                    "sport" -> switchChannel("Sports").also { job.cancel(); job = it }
                    "biz" -> switchChannel("Business").also { job.cancel(); job = it }
                    "local" -> switchChannel("Local").also { job.cancel(); job = it }
                    "ent" -> switchChannel("Ent").also { job.cancel(); job = it }
                    "read" -> {
                         println("\n[Fetching Top Stories...]")
                         val t1 = async { fetchDetailedView(1) }
                         val t2 = async { fetchDetailedView(3) }
                         val t3 = async { fetchDetailedView(13) }
                         println(t1.await())
                         println(t2.await())
                         println(t3.await())
                    }
                    "exit" -> {
                        job.cancel()
                        cancel()
                        println("Shutting down ticker...")
                        return@withContext
                    }
                    else -> println("Invalid command. Try: all, tech, sport, biz, local, ent, read, exit")
                }
            }
        }
    }

    private fun CoroutineScope.switchChannel(cat: String?): Job {
        println("\n>>> CHANNEL CHANGED: ${cat ?: "GLOBAL MIX"}")
        activeFilter.value = cat
        return launch { runStream(cat) }
    }

    private fun printMenu() {
        println("COMMANDS: [all] [tech] [sport] [biz] [local] [ent] | [read] [exit]")
    }
}

fun main() = runBlocking {
    NewsTickerApp().launchApp()
}