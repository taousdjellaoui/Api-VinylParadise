package ApiVinylparadise.ApiVinylParadise.Domaine.Services

import ApiVinylparadise.ApiVinylParadise.AccesAuxDonnees.BD.UtilisateurDAOImpl
import ApiVinylparadise.ApiVinylParadise.Domaine.Utilisateur
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class UtilisateurServiceTests {

    @Mock lateinit var mockDAO: UtilisateurDAOImpl

    val utilisateur1 = Utilisateur(nom = "Doe", prenom = "John", email = "john.doe@example.com")

    val utilisateur2 = Utilisateur(nom = "Smith", prenom = "Jane", email = "jane.smith@example.com")

    @Test
    fun `Étant donné une banque d'utilisateurs lorsqu'on cherche un utilisateur avec un email inexistant on obtient null`() {
        Mockito.`when`(mockDAO.chercherParEmail("nonexistent@example.com")).thenReturn(null)

        val serviceTest = UtilisateurService(mockDAO)

        val resultatObserve = serviceTest.chercherUtilisateurParEmail("nonexistent@example.com")

        assertEquals(null, resultatObserve)
    }

    @Test
    fun `Étant donné plusieurs utilisateurs inscrits lorsqu'on récupère tous les utilisateurs on obtient la liste correspondante`() {
        val resultatAttendu = listOf(utilisateur1, utilisateur2)
        Mockito.`when`(mockDAO.chercherTous()).thenReturn(resultatAttendu)

        val serviceTest = UtilisateurService(utilisateurDAO = mockDAO)

        val resultatObserve = serviceTest.obtenirUtilisateurs()

        assertEquals(resultatAttendu, resultatObserve)
    }

    @Test
    fun `Étant donné un utilisateur avec un email spécifique lorsqu'on le cherche par email on obtient l'utilisateur correspondant`() {
        Mockito.doReturn(utilisateur1).`when`(mockDAO).chercherParEmail("john.doe@example.com")

        val cobaye = UtilisateurService(mockDAO)

        val resultatObtenu = cobaye.chercherUtilisateurParEmail("john.doe@example.com")

        assertEquals(utilisateur1, resultatObtenu)
    }

    @Test
    fun `Étant donné un nouvel utilisateur lorsqu'on le crée on obtient l'utilisateur ajouté`() {
        val nouvelUtilisateur =
                Utilisateur(nom = "Brown", prenom = "Charlie", email = "charlie.brown@example.com")
        Mockito.`when`(mockDAO.ajouter(nouvelUtilisateur)).thenReturn(nouvelUtilisateur)

        val serviceTest = UtilisateurService(mockDAO)

        val utilisateurAjoute = serviceTest.creerUtilisateur(nouvelUtilisateur)

        assertEquals(nouvelUtilisateur, utilisateurAjoute)
    }

    @Test
    fun `Étant donné un utilisateur existant lorsqu'on modifie ses informations par email on obtient l'utilisateur mis à jour`() {
        val email = "john.doe@example.com"
        val utilisateurMisAJour = utilisateur1.copy(prenom = "Johnny")
        Mockito.`when`(mockDAO.modifierParEmail(email, utilisateurMisAJour))
                .thenReturn(utilisateurMisAJour)

        val serviceTest = UtilisateurService(mockDAO)

        val utilisateurResultat =
                serviceTest.modifierUtilisateurParEmail(email, utilisateurMisAJour)

        assertEquals(utilisateurMisAJour, utilisateurResultat)
    }

    @Test
    fun `Étant donné un utilisateur existant lorsqu'on l'efface par email la méthode effacer est appelée`() {
        val email = "jane.smith@example.com"
        Mockito.doNothing().`when`(mockDAO).effacer(email)

        val serviceTest = UtilisateurService(mockDAO)

        serviceTest.effacerUtilisateurParEmail(email)

        Mockito.verify(mockDAO).effacer(email)
    }

    @Test
    fun `Étant donné un nom existant lorsqu'on cherche par nom on obtient la liste des utilisateurs correspondants`() {
        val nom = "Doe"
        val resultatAttendu = listOf(utilisateur1)
        Mockito.`when`(mockDAO.chercherUtilisateurParNom(nom)).thenReturn(resultatAttendu)

        val serviceTest = UtilisateurService(mockDAO)

        val resultatObserve = serviceTest.chercherUtilisateurParNom(nom)

        assertEquals(resultatAttendu, resultatObserve)
    }

    @Test
    fun `Étant donné une banque d'utilisateurs lorsqu'on cherche par un nom inexistant on obtient une liste vide`() {
        val nom = "NonExistentName"
        Mockito.`when`(mockDAO.chercherUtilisateurParNom(nom)).thenReturn(emptyList())

        val serviceTest = UtilisateurService(mockDAO)

        val resultatObserve = serviceTest.chercherUtilisateurParNom(nom)

        assertEquals(emptyList<Utilisateur>(), resultatObserve)
    }

    @Test
    fun `Étant donné un prénom existant lorsqu'on cherche par prénom on obtient la liste des utilisateurs correspondants`() {
        val prenom = "Jane"
        val resultatAttendu = listOf(utilisateur2)
        Mockito.`when`(mockDAO.chercherUtilisateurParPrenom(prenom)).thenReturn(resultatAttendu)

        val serviceTest = UtilisateurService(mockDAO)

        val resultatObserve = serviceTest.chercherUtilisateurParPrenom(prenom)

        assertEquals(resultatAttendu, resultatObserve)
    }

    @Test
    fun `Étant donné une banque d'utilisateurs lorsqu'on cherche par un prénom inexistant on obtient une liste vide`() {
        val prenom = "NonExistentFirstName"
        Mockito.`when`(mockDAO.chercherUtilisateurParPrenom(prenom)).thenReturn(emptyList())

        val serviceTest = UtilisateurService(mockDAO)

        val resultatObserve = serviceTest.chercherUtilisateurParPrenom(prenom)

        assertEquals(emptyList<Utilisateur>(), resultatObserve)
    }
}
