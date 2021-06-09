import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
class Kts {

    suspend fun suspendingGetImage(id: String) = withContext(Dispatchers.IO) {

    }

}