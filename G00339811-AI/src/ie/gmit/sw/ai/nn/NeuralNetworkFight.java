package ie.gmit.sw.ai.nn;

import ie.gmit.sw.ai.nn.activator.*;

//Class contains the training data, expected output and actions 

/**
 * Nn Fight contains the training data for the neural Network.
 * Also holds the expected output and corresponding actions. 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 */
public class NeuralNetworkFight {
	// need to load data from fi;le
//	private final double[][] data ;
//	private final double[][] expected ;
	
	private final double[][] data = { //Health, Weapon, Anger level
			{ 2, 0, 0 }, { 2, 0, 0 }, { 2, 0, 1 }, { 2, 0, 1 }, { 2, 1, 0 },
			{ 2, 1, 0 }, { 2, 1, 2}, { 1, 0, 0 }, { 1, 0, 0 }, { 1, 0, 1 }, { 1, 0, 1 }, 
			{ 1, 1, 0 }, { 1, 1, 0 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 1 }, 
			{ 0, 0, 1 }, { 0, 1, 0 }, { 0, 1, 0 },{ 0, 1, 1 },{ 0, 1, 2 } };

	private final double[][] expected = { //Panic, Attack, Hide
			{ 0.0, 0.0, 1.0}, { 0.0, 0.0, 1.0 }, { 1.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0 }, 
			{ 0.0, 0.0, 0.0}, { 1.0, 0.0, 0.0},{ 0.0, 1.0, 0.0}, { 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0 }, 
			{ 1.0, 0.0, 0.0}, { 0.0, 0.0, 0.0}, { 0.0, 0.0, 0.0}, { 0.0, 0.0, 0.0 }, 
			{ 0.0, 0.0, 1.0}, { 0.0, 0.0, 0.0}, { 0.0, 0.0, 0.0}, { 0.0, 1.0, 0.0 }, 
			{ 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0}, { 0.0, 0.0, 1.0}, { 0.0, 0.0, 1.0} };
	
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