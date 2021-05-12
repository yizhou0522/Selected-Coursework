

package application;

/**
 * This is the Choice class which contains one of the several choices in a question
 *
 */
public class Choice {
	private boolean isCorrect;
	private String choice;

	/**
	 * This is the public constructor which initialize private variable
	 * 
	 * @param isCorrect whether the choice is correct
	 * @param choice Content of choice
	 */
	public Choice(boolean isCorrect, String choice) {
		this.isCorrect = isCorrect;
		this.choice = choice;
	}

	/**
	 * This is the public method which get the choices of the question
	 * 
	 * @return the choices of the question
	 */
	public String getChoice() {
		return choice;
	}

	/**
	 * This is the public method which return whether the choice that user choiced is correct or not
	 * 
	 * @return the result of the choice
	 */
	public boolean getIsCorrect() {
		return isCorrect;
	}
}
