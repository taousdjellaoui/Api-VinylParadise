package ApiVinylparadise.ApiVinylParadise.Domaine.Services

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.extension.ExtendWith

import ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes.VinylsDAOImpl
import ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes.ArtistesDAOImpl
import ApiVinylparadise.ApiVinylParadise.AccesAuxDonnees.BD.UtilisateurDAOImpl
import ApiVinylparadise.ApiVinylParadise.Domaine.*
import ApiVinylparadise.ApiVinylParadise.Controleurs.Exception.InvalidVinylException
import ApiVinylparadise.ApiVinylParadise.Controleurs.Exception.ResourceNotFoundException
import java.sql.Date

@ExtendWith(MockitoExtension::class)
class VinylServiceTests {

    @Mock
    lateinit var mockVinylsDAO: VinylsDAOImpl

    @Mock
    lateinit var mockArtistesDAO: ArtistesDAOImpl

    @Mock
    lateinit var mockUtilisateursDAO: UtilisateurDAOImpl

    val utilisateur = Utilisateur(
        nom = "Doe",
        prenom = "John",
        email = "john.doe@example.com"
    )

    val artist = Artist(
        id = 1,
        nom = "The Beatles"
    )

    val genre = Genre(
        id = 1,
        nom = "Rock"
    )

    val album = Album(
        id = 1,
        titre = "Abbey Road",
        artist = artist,
        genres = listOf(genre),
        dateDeSortie = Date.valueOf("1969-09-26"),
        image_url = "https://example.com/abbeyroad.jpg"
    )

    val vinyl = Vinyl(
        id = 1,
        utilisateur = utilisateur,
        album = album,
        prix = 29.99,
        description = "..."
    )

    @Test
    fun `Étant donné plusieurs vinyles lorsqu'on récupère tous les vinyles on obtient la liste correspondante`() {
        val resultatAttendu = listOf(vinyl)
        Mockito.`when`(mockVinylsDAO.chercherTous()).thenReturn(resultatAttendu)

        val serviceTest = VinylService(mockVinylsDAO, mockArtistesDAO, mockUtilisateursDAO)

        val resultatObserve = serviceTest.rechercherToutVinyls()

        assertEquals(resultatAttendu, resultatObserve)
    }

    @Test
    fun `Étant donné une banque de vinyles vide lorsqu'on récupère tous les vinyles on obtient une ResourceNotFoundException`() {
        Mockito.`when`(mockVinylsDAO.chercherTous()).thenReturn(emptyList())

        val serviceTest = VinylService(mockVinylsDAO, mockArtistesDAO, mockUtilisateursDAO)

        assertFailsWith<ResourceNotFoundException> {
            serviceTest.rechercherToutVinyls()
        }
    }

   

    @Test
    fun `Étant donné un nom d'album inexistant lorsqu'on cherche des vinyles on obtient une ResourceNotFoundException`() {
        val titreAlbum = "NonExistentAlbum"
        Mockito.`when`(mockVinylsDAO.chercherParTitre(titreAlbum)).thenReturn(emptyList())

        val serviceTest = VinylService(mockVinylsDAO, mockArtistesDAO, mockUtilisateursDAO)

        assertFailsWith<ResourceNotFoundException> {
            serviceTest.trouverParTitreAlbum(titreAlbum)
        }
    }

    @Test
    fun `Étant donné un ID inexistant lorsqu'on cherche un vinyle par ID on obtient une ResourceNotFoundException`() {
        val idInexistant = 999
        Mockito.`when`(mockVinylsDAO.trouverVinylParId(idInexistant)).thenReturn(null)

        val serviceTest = VinylService(mockVinylsDAO, mockArtistesDAO, mockUtilisateursDAO)

        assertFailsWith<ResourceNotFoundException> {
            serviceTest.trouverVinylParId(idInexistant)
        }
    }
}
