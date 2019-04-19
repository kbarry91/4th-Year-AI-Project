package ie.gmit.sw.ai.nn;


/**
 * Trainator Interface .
 * 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 */
public interface Trainator {
	public void train(double[][] data, double[][] desired, double alpha, int epochs);
}
