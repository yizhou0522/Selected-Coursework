

package application;
import java.io.File;
import java.util.List;
import javafx.collections.ObservableList;

/**
 * This is the interface 
 */
public interface QuestionDatabaseADT {

	/**
	 *This is the public method which add question manually by user to the database
	 *
	 * @param s the topic of the question 
	 * @param q the question
	 */
	public void addQuestion(String s, Question q);

	/**
	 * This is the public method which save question as JSON File
	 * 
	 * @param f the destination file
	 */
	public void saveQuestionsToJSON(File f);

	/**
	 * This is the public method which get the question from the database
	 * 
	 * @param s The topic which used to find the question
	 * @return return the question list belongs to one topic
	 */
	public List<Question> getQuestions(String s);

	/**
	 * This is the public method which get the question from the JSON file
	 *  
	 * @param f the source file
	 */
	public void loadQuestionsFromJSON(File f);

	/**
	 * This is the public method which get the list of the topic from the database
	 * 
	 * @return the list of the topic 
	 */
	public ObservableList<String> getTopics();

}
