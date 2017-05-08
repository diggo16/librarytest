package com.cybercom.librarytest;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.cybercom.librarytest.PasswordStorage.CannotPerformOperationException;
import com.cybercom.librarytest.model.Author;
import com.cybercom.librarytest.model.Authors;
import com.cybercom.librarytest.model.Book;
import com.cybercom.librarytest.model.Books;
import com.cybercom.librarytest.model.User;
import com.cybercom.librarytest.model.Users;

public class TestData {
	public static void initTestData(EntityManager em) {
		Author margaretAtwood = new Author(
				1L, "Margaret", "Atwood", "Canada", 
				"Margaret Eleanor Atwood, CC OOnt FRSC (born November 18, 1939) is a "
				+ "Canadian poet, novelist, literary critic, essayist, and environmental "
				+ "activist. She is a winner of the Arthur C. Clarke Award and Prince of "
				+ "Asturias Award for Literature, has been shortlisted for the Booker "
				+ "Prize five times, winning once, and has been a finalist for the Governor "
				+ "General's Award several times, winning twice. In 2001, she was inducted "
				+ "into Canada's Walk of Fame."
		);
		Author neilGaiman = new Author(
				2L, "Neil", "Gaiman", "Great Britain", 
				"Neil Richard MacKinnon Gaiman (/ˈɡeɪmən/; born Neil Richard Gaiman; "
				+ "10 November 1960) is an English author of short fiction, novels, "
				+ "comic books, graphic novels, audio theatre and films. His notable "
				+ "works include the comic book series The Sandman and novels Stardust, "
				+ "American Gods, Coraline, and The Graveyard Book. He has won numerous "
				+ "awards, including the Hugo, Nebula, and Bram Stoker awards, as well "
				+ "as the Newbery and Carnegie medals."
		);
		Author terryPratchett = new Author(
				3L, "Terry", "Pratchett", "Great Britain", 
				"Sir Terence David John \"Terry\" Pratchett, OBE (28 April 1948 – 12 "
				+ "March 2015) was an English author of fantasy novels, especially comical "
				+ "works. He is best known for his Discworld series of 41 novels. Pratchett's "
				+ "first novel, The Carpet People, was published in 1971; after the first "
				+ "Discworld novel, The Colour of Magic, was published in 1983, he wrote two "
				+ "books a year on average."
		);
		Author arthurCClarke = new Author(
				4L, "Arthur C.", "Clarke", "Great Britain", 
				"Sri Lankabhimanya Sir Arthur Charles Clarke, CBE, FRAS (16 December 1917 – "
				+ "19 March 2008) was a British science fiction writer, science writer and "
				+ "futurist, inventor, undersea explorer, and television series host.He is "
				+ "perhaps most famous for being co-writer of the screenplay for the movie "
				+ "2001: A Space Odyssey, widely considered to be one of the most influential "
				+ "films of all time. His other science fiction writings earned him a number "
				+ "of Hugo and Nebula awards, which along with a large readership made him "
				+ "one of the towering figures of science fiction."
		);
		Author stephenBaxter = new Author(
				5L, "Stephen", "Baxter", "Great Britain", 
				"Stephen Baxter (born 13 November 1957) is a prolific British hard science"
				+ " fiction author. He has degrees in mathematics and engineering. Strongly "
				+ "influenced by SF pioneer H. G. Wells, Baxter has been a distinguished "
				+ "Vice-President of the international H. G. Wells Society since 2006."
		);
		Author robertAHeinlein = new Author(
				6L, "Robert A.", "Heinlein", "United States of America", 
				"Robert Anson Heinlein (/ˈhaɪnlaɪn/; July 7, 1907 – May 8, 1988) was an "
				+ "American science fiction writer. Often called the \"dean of science "
				+ "fiction writers\", he was an influential and controversial author of "
				+ "the genre in his time. He was one of the first science fiction writers "
				+ "to break into mainstream magazines such as The Saturday Evening Post in "
				+ "the late 1940s."
		);

		Book oryxAndCrake = new Book(
				"Oryx and Crake", 
				"The novel focuses on a post-apocalyptic character named Snowman, living "
				+ "near a group of primitive human-like creatures whom he calls Crakers.",
				"0-7710-0868-6", 
				411, 
				"2003-04-01", 
				1
		);
		oryxAndCrake.getAuthors().add(margaretAtwood);
		Book goodOmens = new Book(
				"Good Omens", 
				"It is the coming of the End Times: the Apocalypse is near, and Final "
				+ "Judgement will soon descend upon the human species. This comes as a "
				+ "bit of bad news to the angel Aziraphale (who was the guardian of the "
				+ "Eastern Gate of Eden) and the demon Crowley (who, when he was "
				+ "originally named Crawly, was the serpent who tempted Eve to eat the "
				+ "apple), respectively the representatives of Heaven and Hell on Earth, "
				+ "as they have become used to living their cozy, comfortable lives and "
				+ "have, in a perverse way, taken a liking to humanity.",
				"0-575-04800-X", 
				288,
				"1990-05-01", 
				5
		);
		goodOmens.getAuthors().add(neilGaiman);
		goodOmens.getAuthors().add(terryPratchett);
		Book guardsGuards = new Book(
				"Guards! Guards!", 
				"The story follows a plot by a secret brotherhood, the Unique and Supreme "
				+ "Lodge of the Elucidated Brethren of the Ebon Night, to overthrow the "
				+ "Patrician of Ankh-Morpork and install a puppet king, under the control "
				+ "of the Supreme Grand Master. Using a stolen magic book, they summon a "
				+ "dragon to strike fear into the people of Ankh-Morpork.",
				"0-575-04606-6", 
				288, 
				"1989-12-01", 
				1
		);
		guardsGuards.getAuthors().add(terryPratchett);
		Book timesEye = new Book(
				"Time's Eye", 
				"The story opens with two hominids, probably Homo erectus, known as "
				+ "'Seeker', a mother, and her infant daughter 'Grasper'. As they walked on "
				+ "the tranquil Earth two million years ago, they were suddenly captured by "
				+ "some blood-red beings, who turn out to be nineteenth-century British redcoat "
				+ "soldiers.",
				"0-00-713846-6", 
				392, 
				"2013-03-03", 
				1
		);
		timesEye.getAuthors().add(arthurCClarke);
		timesEye.getAuthors().add(stephenBaxter);
		Book _2001ASpaceOdyssey = new Book(
				"2001: A Space Odyssey", 
				"In the background to the story in the book, an ancient and unseen alien race "
				+ "uses a device with the appearance of a large crystalline monolith to "
				+ "investigate worlds all across the galaxy and, if possible, to encourage "
				+ "the development of intelligent life.",
				"0-453-00269-2", 
				221, 
				"1968-04-01", 
				1
		);
		_2001ASpaceOdyssey.getAuthors().add(arthurCClarke);
		Book rendezvousWithRama = new Book(
				"Rendezvous with Rama", 
				"After a meteorite falls in Northeast Italy in 2077, creating a major "
				+ "disaster, the government of Earth sets up the Spaceguard system as an "
				+ "early warning of arrivals from deep space.",
				"0-575-01587-X", 
				256, 
				"1973-06-28", 
				4
		);
		rendezvousWithRama.getAuthors().add(arthurCClarke);
		Book neverwhere = new Book(
				"Neverwhere", 
				"Neverwhere is the story of Richard Mayhew and his trials and tribulations "
				+ "in London. At the start of the story, he is a young businessman, recently "
				+ "moved from Scotland and with a normal life ahead. This breaks, however, "
				+ "when he stops to help a mysterious young girl who appears before him, "
				+ "bleeding and weakened, as he walks with his fiancée to dinner to meet "
				+ "her influential boss.",
				"0-7472-6668-9", 
				387, 
				"2000-11-02", 
				1
		);
		neverwhere.getAuthors().add(neilGaiman);
		Book americanGods = new Book(
				"American Gods", 
				"The central premise of the novel is that gods and mythological creatures "
				+ "exist because people believe in them (a form of thoughtform). Immigrants "
				+ "to the United States brought with them spirits and gods. However, the "
				+ "power of these mythological beings has diminished as people's beliefs wane. "
				+ "New gods have arisen, reflecting America's obsessions with media, celebrity, "
				+ "technology, and drugs, among others.",
				"0-380-97365-0", 
				465, 
				"2001-07-01", 
				3
		);
		americanGods.getAuthors().add(neilGaiman);
		Book coraline = new Book(
				"Coraline", 
				"Coraline Jones and her parents move into an old house that has been divided "
				+ "into flats. The other tenants include Miss Spink and Miss Forcible, two "
				+ "elderly women retired from the stage, and Mr. Bobinsky, initially referred "
				+ "to as \"the crazy old man upstairs\", who claims to be training a mouse "
				+ "circus. The flat beside Coraline's is unoccupied.",
				"0-06-113937-8", 
				163, 
				"2006-11-01", 
				1
		);
		coraline.getAuthors().add(neilGaiman);
		Book anansiBoys = new Book(
				"Anansi Boys", 
				"Anansi Boys is the story of Charles \"Fat Charlie\" Nancy, a timid Londoner "
				+ "devoid of ambition, whose unenthusiastic wedding preparations are disrupted "
				+ "when he learns of his father's death in Florida. The flamboyant Mr. Nancy, "
				+ "in whose shadow Fat Charlie has always lived, died in a typically embarrassing "
				+ "manner by suffering a fatal heart attack while singing to a young woman on "
				+ "stage in a karaoke bar.",
				"0-06-051518-X", 
				400, 
				"2005-11-20", 
				1
		);
		anansiBoys.getAuthors().add(neilGaiman);
		Book starshipTroopers = new Book(
				"Starship Troopers", 
				"The first-person narrative is about a young soldier named Juan \"Johnnie\" Rico "
				+ "and his exploits in the Mobile Infantry, a futuristic military service branch "
				+ "equipped with powered armor. Rico's military career progresses from recruit "
				+ "to non-commissioned officer and finally to officer against the backdrop of "
				+ "an interstellar war between mankind and an arachnoid species known as "
				+ "\"the Bugs\".",
				"0-450-02576-4", 
				263, 
				"1959-12-01", 
				2
		);
		starshipTroopers.getAuthors().add(robertAHeinlein);
		
		User admin, lennart, karl;
		try {
			admin = new User(
					"admin", 
					PasswordStorage.createHash("1234567890"), 
					User.Role.LIBRARIAN,
					"Admin first name",
					"Admin last name",
					"000-111222",
					"admin@example.com"
			);	
			lennart = new User(
					"lennart", 
					PasswordStorage.createHash("0123456789"), 
					User.Role.LOANER,
					"Lennart",
					"Moraeus",
					"+46000333444",
					"lennart@example.com"
			);
			karl = new User(
					"karl", 
					PasswordStorage.createHash("9876543210"), 
					User.Role.LOANER,
					"Karl",
					"Karlsson",
					"+46000555666",
					"karl@example.com"
			);
		} catch (CannotPerformOperationException e) {
			e.printStackTrace();
			return;
		}
		
		Books books = new Books();
		books.add(oryxAndCrake);
		books.add(goodOmens);
		books.add(guardsGuards);
		books.add(timesEye);
		books.add(_2001ASpaceOdyssey);
		books.add(rendezvousWithRama);
		books.add(neverwhere);
		books.add(americanGods);
		books.add(coraline);
		books.add(anansiBoys);
		books.add(starshipTroopers);

		Authors authors = new Authors();
		authors.add(margaretAtwood);
		authors.add(neilGaiman);
		authors.add(terryPratchett);
		authors.add(arthurCClarke);
		authors.add(stephenBaxter);
		authors.add(robertAHeinlein);
		
		Users users = new Users();
		users.add(admin);
		users.add(lennart);
		users.add(karl);
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		for (Author author : authors) {
			em.persist(author);
		}
		for (Book book : books) {
			em.persist(book);
		}
		for (User user : users) {
			em.persist(user);
		}
		tx.commit();
		em.close();
	}
}
