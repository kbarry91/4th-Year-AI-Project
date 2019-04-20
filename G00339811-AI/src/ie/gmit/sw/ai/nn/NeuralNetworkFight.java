package ie.gmit.sw.ai.nn;

import ie.gmit.sw.ai.nn.activator.*;
import ie.gmit.sw.ai.utils.*;
//Class contains the training data, expected output and actions 

/**
 * Nn Fight contains the training data for the neural Network.
 * Also holds the expected output and corresponding actions. 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 */
public class NeuralNetworkFight {
	
	// Load the training data and expected data from a file.
	private final double[][] data = DataExtractor.extractDataFromFile("resources/neural/trainingData");
	private final double[][] expected = DataExtractor.extractDataFromFile("resources/neural/expectedData");


	private NeuralNetwork neuralNet = null;
	
	public NeuralNetworkFight() {
	//	this.data=NnTrainingData.getData();
	}

	/**
	 * Trains the neural network using the Sigmoid activation function.
	 */
	public void train() {
		// iniilize it(input middel ,hidden , output)
		neuralNet = new NeuralNetwork(Activator.ActivationFunction.Sigmoid, 3, 3, 3);
		Trainator netTrainer = new BackpropagationTrainer(neuralNet);
		
		// train the network with the data, expected output, alpha and amount of epochs.
		netTrainer.train(data, expected, 0.2, 10000);// can change 10000 to smaller for faster performance
	}

	/**
	 * Process the result of the neural network and assign it to an action.
	 * 
	 * @param health the current health of the player.
	 * @param weapon the weapon health of the player.
	 * @param angerLevel the current angerLevel of the player.
	 * @return
	 * @throws Exception
	 */
	public int action(double health, double weapon, double angerLevel) throws Exception{
		
		double[] params = {health, weapon, angerLevel};
        double[] result = neuralNet.process(params);

        // Print the 
        for(double val : result){
            System.out.println("[NeuralNetworkFight] action() : val = "+val);
        }

        // Return the action to perform
		return Utils.getMaxIndex(result) + 1;
	}

}