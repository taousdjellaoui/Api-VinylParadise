package ApiVinylparadise.ApiVinylParadise.Domaine
import ApiVinylparadise.ApiVinylParadise.Domaine.Artist
import java.sql.Date


data class Album(
    val id: Int?,
    val titre: String,
    val artist: Artist,
    val genres: List<Genre> = listOf(), 
    val dateDeSortie: Date,
    val image_url: String? = "",
)
