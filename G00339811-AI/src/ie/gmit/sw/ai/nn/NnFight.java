package ie.gmit.sw.ai.nn;

import ie.gmit.sw.ai.nn.activator.*;

//Class contains the training data, expected output and actions 
public class NnFight {

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
	
	private NeuralNetwork nn = null;
	public NnFight() {
		
	}

	public void train() {
		// iniilize it(input middel ,hidden , output)
		nn = new NeuralNetwork(Activator.ActivationFunction.Sigmoid, 3, 3, 3);
		Trainator trainer = new BackpropagationTrainer(nn);
		trainer.train(data, expected, 0.2, 10000);// can change 10000 to smaller for faster performance
	}

	public int action(double health, double weapon, double angerLevel) throws Exception{

		
		double[] params = {health, weapon, angerLevel};

        double[] result = nn.process(params);

        for(double val : result){
            System.out.println(val);
        }

		int choice = (Utils.getMaxIndex(result) + 1);

		/*
		 * switch(choice){ case 1: System.out.println("Panic"); break; case 2:
		 * System.out.println("Attack"); break; case 3: System.out.println("Hide");
		 * break; default: System.out.println("Run Forest!! RUN...."); }
		 */

		return choice;
	}

}