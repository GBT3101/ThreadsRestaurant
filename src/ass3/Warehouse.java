package ass3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Warehouse {
	/********* Fields *********/
	private ArrayList<Ingredient> ingredients;
	private ArrayList<KitchenTool> kitchenTools;
	
	/********* Constructors *********/
	
	public Warehouse(ArrayList<Ingredient> ingredients, ArrayList<KitchenTool> tools) {
		this.ingredients = ingredients;
		this.kitchenTools = tools;
	}
	
	/********* Methods *********/
	public synchronized void takeIngredient(Ingredient ing){
		// Take an ingredient, update it's quantity and consumed fields.
		for(int i=0; i<ingredients.size(); i++){
			if( ingredients.get(i).getName().equals(ing.getName())){
				ingredients.get(i).consume(ing.getQuantity());
				return;
			}
		}
	}
	
	public void takeKitchenTools(List<KitchenTool> tools){ // ReSource Ordering, Fixing the DeadLock issue.
		Collections.sort(tools); // sort the tools.
		for(KitchenTool tool : tools){ // take them one by one.
			takeKitchenTool(tool);
		}
	}
	
	public void takeKitchenTool(KitchenTool tool){
		// take a kitchen tool, update it's quantity field.
		for(int i=0; i<kitchenTools.size(); i++){
			if( kitchenTools.get(i).getName().equals(tool.getName())){
				kitchenTools.get(i).takeTool(tool.getQuantity());
				return;
			}
		}
	}
	
	public void returnKitchenTool(KitchenTool tool){
		// return a kitchen tool, update it's quantity field.
		for(int i=0; i<kitchenTools.size(); i++){
			if( kitchenTools.get(i).getName().equals(tool.getName())){
				kitchenTools.get(i).returnTool(tool.getQuantity());
				return;
			}
		}
	}
	
	public void giveCosumedIngredientToStatistics(){
		//update the statistics with all the consumed ingredients.
		for(Ingredient ing : ingredients){
			if(ing.getConsumed()>0){
				Statistics.addConsumedIngredient(ing);
			}
		}
	}
	
	
}
